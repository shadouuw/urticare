package com.amine.urticare.resources;

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
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * REST controller for OAuth2 authentication operations.
 * Handles user login, multiFactor authentication (MFA),
 * token refresh, token revocation, and retrieval of current user information.
 */
@RestController
@RequestMapping("/oauth")
public class OAuth2Controller {

    private final IAuthService authService;

    public OAuth2Controller(IAuthService authService) {
        this.authService = authService;
    }

    /**
     * Authenticates a user and issues access and refresh tokens.
     *
     * @param loginRequest the user's login credentials
     * @return {@link JwtAuthenticationResponse} containing access and refresh tokens,
     *         or MFA information if MFA is required
     */
    @PostMapping("/token")
    public JwtAuthenticationResponse getToken(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    /**
     * Verifies a multiFactor authentication (MFA) code.
     *
     * @param request MFA verification request containing transaction ID and OTP
     * @return {@link JwtAuthenticationResponse} with access and refresh tokens if successful
     */
    @PostMapping("/verify")
    public JwtAuthenticationResponse verifyMfa(
            @Valid @RequestBody MfaVerificationRequest request) {
        return authService.verifyMfa(request);
    }

    /**
     * Resends an MFA code for a given transaction.
     *
     * @param transactionId the MFA transaction ID
     * @return 200 OK if the MFA code was resent successfully
     */
    @PostMapping("/resend")
    public ResponseEntity<Void> resendMfa(@RequestParam String transactionId) {
        authService.resendMfa(transactionId);
        return ResponseEntity.ok().build();
    }

    /**
     * Refreshes the access token using a refresh token.
     *
     * @param refreshToken the refresh token request
     * @return {@link JwtAuthenticationResponse} with a new access token
     */
    @PostMapping("/refresh")
    public JwtAuthenticationResponse refreshToken(@RequestBody RefreshToken refreshToken) {
        return authService.refreshToken(refreshToken);
    }

    /**
     * Performs token introspection to check if a token is active.
     *
     * @param token the token to introspect
     * @return {@link IntrospectionResponse} with token status and claims;
     *         401 Unauthorized if the token is inactive
     */
    @PostMapping("/introspect")
    public ResponseEntity<IntrospectionResponse> introspect(@RequestParam("token") String token) {
        IntrospectionResponse result = authService.introspectToken(token);

        if (!result.isActive()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.ok(result);
    }

    /**
     * Revokes all tokens for the currently authenticated user.
     *
     * @param token    JWT from the security context
     * @param request  HTTP servlet request
     * @param response HTTP servlet response
     * @return 200 OK if tokens were revoked successfully,
     *         401 if the token is invalid,
     *         400 for other failures
     */
    @PostMapping("/revoke")
    public ResponseEntity<String> revokeTokensEndpoint(
            @AuthenticationPrincipal Jwt token,
            HttpServletRequest request,
            HttpServletResponse response) {

        try {
            authService.revokeTokens(token, request, response);
            return ResponseEntity.ok("Tokens revoked successfully");

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Retrieves information about the currently authenticated user.
     *
     * @param jwt JWT from the security context
     * @return {@link UserInformation} with the current user's details
     */
    @GetMapping("/me")
    public UserInformation getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        return authService.getCurrentUser(jwt);
    }


    /**
     * Registers a new patient and issues access and refresh tokens.
     *
     * @param registerRequest the registration details
     * @return {@link JwtAuthenticationResponse} containing tokens and user info
     */
    @PostMapping("/register")
    public JwtAuthenticationResponse register(
            @Valid @RequestBody RegisterRequest registerRequest) {
        try {
            return authService.register(registerRequest);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }



}
