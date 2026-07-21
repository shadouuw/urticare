package com.amine.urticare.service.impl;

import com.amine.urticare.common.Constants;
import com.amine.urticare.config.MfaAuthenticationCache;
import com.amine.urticare.model.entities.MfaTransaction;
import com.amine.urticare.model.entities.MyUserDetails;
import com.amine.urticare.model.entities.TokenEntity;
import com.amine.urticare.model.entities.User;
import com.amine.urticare.model.enumeration.TokenType;
import com.amine.urticare.repository.TokenRepository;
import com.amine.urticare.repository.UserRepository;
import com.amine.urticare.resources.dto.IntrospectionResponse;
import com.amine.urticare.resources.dto.JwtAuthenticationResponse;
import com.amine.urticare.resources.dto.LoginRequest;
import com.amine.urticare.resources.dto.MfaVerificationRequest;
import com.amine.urticare.resources.dto.RefreshToken;
import com.amine.urticare.resources.dto.RegisterRequest;
import com.amine.urticare.resources.dto.UserInformation;
import com.amine.urticare.service.IAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for user authentication and OAuth2 token management.
 * Handles login, MFA verification, token issuance, refresh, introspection, and revocation.
 * Integrates with {@link MfaService} for multiFactor authentication and
 * {@link MfaAuthenticationCache} to store temporary MFA sessions.
 */
@Slf4j
@Service
public class AuthService implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;
    private final MfaService mfaService;
    private final MfaAuthenticationCache mfaAuthenticationCache;
    private final PasswordEncoder passwordEncoder;

    @Value("${auth.server.issuer}")
    private String issuer;

    @Value("${auth.server.security.mfa.method}")
    private String method;

    @Value("${auth.server.security.jwt.expiration}")
    private Integer jwtExpiration;

    @Value("${auth.server.security.refresh-token.expiration}")
    private Integer refreshExpiration;

    @Value("${auth.server.security.mfa.otp-ttl-seconds}")
    private int mfaTtlSeconds;

    public AuthService(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            UserDetailsService userDetailsService,
            JwtEncoder jwtEncoder,
            JwtDecoder jwtDecoder,
            TokenRepository tokenRepository,
            MfaService mfaService,
            MfaAuthenticationCache mfaAuthenticationCache, PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.tokenRepository = tokenRepository;
        this.mfaService = mfaService;
        this.mfaAuthenticationCache = mfaAuthenticationCache;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public JwtAuthenticationResponse login(LoginRequest loginRequest) {

        MyUserDetails userDetails = (MyUserDetails) userDetailsService
                .loadUserByUsername(loginRequest.getEmail());

        if (userDetails == null || userDetails.getUserDetails() == null) {
            throw new UsernameNotFoundException("Invalid username or password");
        }

        // Authenticate credentials
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        User user = userDetails.getUserDetails();

        // If MFA not required, check the existing token
        if (!mfaService.isMfaRequired()) {
            UserInformation userInformation = new UserInformation(user.getEmail(), user.getUsername(), user.getIsEnabled(), user.getFirstname(), user.getLastname());
            Optional<TokenEntity> existingToken = tokenRepository.findFirstByUserId(user.getId());
            if (existingToken.isPresent() && isValidAccessToken(existingToken.get().getAccessToken())) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                return new JwtAuthenticationResponse(
                        existingToken.get().getAccessToken(),
                        existingToken.get().getRefreshToken(),
                        jwtExpiration,
                        TokenType.BEARER,
                        userInformation
                );
            }
        }

        // If MFA required, create MFA transaction
        if (mfaService.isMfaRequired()) {
            tokenRepository.deleteAllByUser(user);

            MfaTransaction txn = mfaService.createTransaction(user);
            mfaAuthenticationCache.store(txn.getId(), authentication);

            return JwtAuthenticationResponse.builder()
                    .mfaRequired(true)
                    .transactionId(txn.getId())
                    .mfaMethod(method)
                    .expiresIn(mfaTtlSeconds)
                    .build();
        }

        // Issue tokens if MFA not required
        return issueTokens(authentication, userDetails);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public JwtAuthenticationResponse verifyMfa(MfaVerificationRequest request) {

        // Validate OTP and get the user
        User user = mfaService.validateOtp(request.getTransactionId(), request.getOtp());

        // Retrieve cached authentication
        Authentication authentication = mfaAuthenticationCache.get(request.getTransactionId());
        if (authentication == null) {
            throw new IllegalStateException("MFA session expired or not found");
        }

        // Remove MFA session from cache
        mfaAuthenticationCache.remove(request.getTransactionId());

        // Set security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Issue JWT tokens
        return issueTokens(authentication, new MyUserDetails(user));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resendMfa(String transactionId) {
        mfaService.resendOtp(transactionId);
    }

    /**
     * Issues access and refresh tokens for the authenticated user.
     *
     * @param authentication the current authentication
     * @param userDetails    the user details
     * @return JWT authentication response
     */
    private JwtAuthenticationResponse issueTokens(Authentication authentication, MyUserDetails userDetails) {

        SecurityContextHolder.getContext().setAuthentication(authentication);

        List<String> authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        tokenRepository.deleteAllByUser(userDetails.getUserDetails());

        String accessToken = generateToken(userDetails.getUserDetails().getEmail(),
                authorities, jwtExpiration, Constants.ACCESS_TOKEN);

        String refreshToken = generateToken(userDetails.getUserDetails().getEmail(),
                authorities, refreshExpiration, Constants.REFRESH_TOKEN);

        tokenRepository.save(
                TokenEntity.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .expired(false)
                        .revoked(false)
                        .tokenType(TokenType.BEARER)
                        .user(userDetails.getUserDetails())
                        .build()
        );

        User user = userDetails.getUserDetails();

        UserInformation userInformation = new UserInformation(
                user.getEmail(),
                user.getUsername(),
                user.getIsEnabled(),
                user.getFirstname(),
                user.getLastname()
        );

        return new JwtAuthenticationResponse(accessToken, refreshToken, jwtExpiration, TokenType.BEARER , userInformation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JwtAuthenticationResponse refreshToken(RefreshToken refreshTokenRequest) {
        try {
            Jwt decodedRefreshToken = jwtDecoder.decode(refreshTokenRequest.getValue());

            if (!Constants.REFRESH_TOKEN.equals(decodedRefreshToken.getClaimAsString(Constants.TOKEN_TYPE))) {
                log.error("Invalid refresh token");
            }

            String email = decodedRefreshToken.getSubject();
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            List<String> authorities = userDetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            MyUserDetails myUserDetails = (MyUserDetails) userDetails;


            String newAccessToken = generateToken(email, authorities, jwtExpiration, Constants.ACCESS_TOKEN);
            String newRefreshToken = generateToken(email, authorities, refreshExpiration, Constants.REFRESH_TOKEN);

            User user = myUserDetails.getUserDetails();

            UserInformation userInformation = new UserInformation(
                    user.getEmail(),
                    user.getUsername(),
                    user.getIsEnabled(),
                    user.getFirstname(),
                    user.getLastname()
            );

            return new JwtAuthenticationResponse(newAccessToken, newRefreshToken, jwtExpiration, TokenType.BEARER,userInformation);

        } catch (JwtException e) {
            log.error("Failed to refresh token: {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserInformation getCurrentUser(Jwt jwt) {

        if (jwt == null) {
            throw new BadCredentialsException("Invalid or missing JWT");
        }
        User user = userRepository.findByEmailIgnoreCaseAndIsEnabled(jwt.getSubject(), true)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new UserInformation(
                user.getEmail(),
                user.getUsername(),
                user.getIsEnabled(),
                user.getFirstname(),
                user.getLastname()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void revokeTokens(Jwt jwt, HttpServletRequest request, HttpServletResponse response) {
        try {
            TokenEntity tokenEntity = tokenRepository.findByAccessToken(jwt.getTokenValue())
                    .orElseThrow(() -> new BadCredentialsException("User not found"));

            User user = tokenEntity.getUser();
            tokenRepository.deleteAllByUser(user);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) authentication.setAuthenticated(false);

            new SecurityContextLogoutHandler().logout(request, response, authentication);
            SecurityContextHolder.clearContext();
            request.logout();
            request.getSession().invalidate();

        } catch (Exception e) {
            log.error("Failed to revoke tokens: {}", e.getMessage(), e);
        }
    }

    /**
     * Generates a JWT token with given parameters.
     *
     * @param email             user email
     * @param authorities       user authorities
     * @param expirationSeconds token expiration in seconds
     * @param tokenType         access or refresh token type
     * @return encoded JWT token string
     */
    private String generateToken(String email, List<String> authorities, long expirationSeconds, String tokenType) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(email)
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expirationSeconds))
                .claim("authorities", authorities)
                .claim(Constants.TOKEN_TYPE, tokenType)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IntrospectionResponse introspectToken(String token) {
        try {
            TokenEntity tokenEntity = tokenRepository.findByAccessToken(token).orElse(null);
            if (tokenEntity == null) {
                log.warn("Token not found: {}", token);
                return IntrospectionResponse.builder().active(false).build();
            }

            Jwt decodedJwt = jwtDecoder.decode(token);
            boolean active = decodedJwt.getExpiresAt() != null && decodedJwt.getExpiresAt().isAfter(Instant.now());

            return IntrospectionResponse.builder()
                    .active(active)
                    .sub(decodedJwt.getSubject())
                    .username(decodedJwt.getSubject())
                    .clientId(decodedJwt.getClaimAsString("client_id"))
                    .tokenType(decodedJwt.getClaimAsString(Constants.TOKEN_TYPE))
                    .scope(decodedJwt.getClaimAsString("scope"))
                    .authorities(decodedJwt.getClaimAsStringList("authorities"))
                    .exp(decodedJwt.getExpiresAt() != null ? decodedJwt.getExpiresAt().getEpochSecond() : null)
                    .iat(decodedJwt.getIssuedAt() != null ? decodedJwt.getIssuedAt().getEpochSecond() : null)
                    .iss(decodedJwt.getIssuer() != null ? decodedJwt.getIssuer().toString() : null)
                    .build();

        } catch (JwtException e) {
            log.warn("Token introspection failed: {}", e.getMessage());
            return IntrospectionResponse.builder().active(false).build();
        }
    }

    /**
     * Validates if the given access token is still active.
     *
     * @param token the access token string
     * @return true if valid, false otherwise
     */
    private boolean isValidAccessToken(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            return jwt.getExpiresAt() != null && jwt.getExpiresAt().isAfter(Instant.now());
        } catch (JwtException e) {
            log.warn("Invalid token: {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    public JwtAuthenticationResponse register(RegisterRequest registerRequest) {

        // Check if email already exists
        if (userRepository.findByEmailIgnoreCaseAndIsEnabled(registerRequest.getEmail(), true).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = new User();
        user.setFirstname(registerRequest.getFirstname());
        user.setLastname(registerRequest.getLastname());
        user.setEmail(registerRequest.getEmail());
        user.setUsername(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setIsEnabled(true);

        userRepository.save(user);

        // Auto-login after registration
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        registerRequest.getEmail(),
                        registerRequest.getPassword()
                )
        );

        MyUserDetails userDetails = (MyUserDetails) userDetailsService
                .loadUserByUsername(registerRequest.getEmail());

        return issueTokens(authentication, userDetails);
    }
    }

