package com.amine.urticare.service;

import com.amine.urticare.resources.dto.IntrospectionResponse;
import com.amine.urticare.resources.dto.JwtAuthenticationResponse;
import com.amine.urticare.resources.dto.LoginRequest;
import com.amine.urticare.resources.dto.MfaVerificationRequest;
import com.amine.urticare.resources.dto.RefreshToken;
import com.amine.urticare.resources.dto.RegisterRequest;
import com.amine.urticare.resources.dto.UserInformation;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service interface for authentication operations, including login,
 * token refresh, MFA verification, token introspection, and logout.
 */
public interface IAuthService {

    /**
     * Authenticates a user with their login credentials and returns
     * access and refresh tokens.
     *
     * @param loginRequest the login request containing email/username and password
     * @return a {@link JwtAuthenticationResponse} containing tokens and user information
     */
    JwtAuthenticationResponse login(LoginRequest loginRequest);

    /**
     * Refreshes an access token using a valid refresh token.
     *
     * @param refreshTokenRequest the refresh token request
     * @return a {@link JwtAuthenticationResponse} containing new access and refresh tokens
     */
    JwtAuthenticationResponse refreshToken(RefreshToken refreshTokenRequest);

    /**
     * Verifies the provided MFA code for an MFA transaction and returns
     * access and refresh tokens upon successful verification.
     *
     * @param request the MFA verification request containing transaction ID and OTP
     * @return a {@link JwtAuthenticationResponse} containing tokens if verification succeeds
     */
    JwtAuthenticationResponse verifyMfa(MfaVerificationRequest request);

    /**
     * Resends a new MFA code for the given transaction.
     *
     * @param transactionId the MFA transaction ID
     */
    void resendMfa(String transactionId);

    /**
     * Introspects an access token and returns details about its validity,
     * expiration, and associated user information.
     *
     * @param token the access token to introspect
     * @return a {@link IntrospectionResponse} containing token details
     */
    IntrospectionResponse introspectToken(String token);

    /**
     * Retrieves information about the currently authenticated user
     * from the provided JWT.
     *
     * @param jwt the JWT representing the authenticated user
     * @return a {@link UserInformation} object containing user details
     */
    UserInformation getCurrentUser(Jwt jwt);

    /**
     * Revokes all tokens for the current user and clears the authentication context.
     *
     * @param jwt      the JWT representing the authenticated user
     * @param request  the current HTTP request
     * @param response the current HTTP response
     * @throws ServletException in case of logout failure
     */
    @Transactional
    void revokeTokens(Jwt jwt, HttpServletRequest request, HttpServletResponse response) throws ServletException;


    JwtAuthenticationResponse register(RegisterRequest registerRequest);
}
