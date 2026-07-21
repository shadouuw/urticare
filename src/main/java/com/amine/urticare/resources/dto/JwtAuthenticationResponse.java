package com.amine.urticare.resources.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.amine.urticare.model.enumeration.TokenType;
import lombok.Builder;

/**
 * Represents the response returned after user authentication.
 * Includes the JWT access token, refresh token, expiration time,
 * token type, and optional multiFactor authentication (MFA) details.
 */

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record JwtAuthenticationResponse(
        String accessToken,
        String refreshToken,
        Integer expiresIn,
        TokenType tokenType,
        UserInformation user,
        Boolean mfaRequired,
        String transactionId,
        String mfaMethod
) {

    /**
     * Constructs a response without MFA information.
     *
     * @param accessToken  the JWT access token
     * @param refreshToken the JWT refresh token
     * @param expiresIn    access token expiration time in seconds
     * @param tokenType    type of the token
     */
    public JwtAuthenticationResponse(String accessToken, String refreshToken, Integer expiresIn, TokenType tokenType, UserInformation user) {this(accessToken, refreshToken, expiresIn, tokenType,user, null, null, null);}
}

