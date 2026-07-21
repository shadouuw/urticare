package com.amine.urticare.resources.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO representing a request to refresh an expired or soon-to-expire access token.
 *
 * <p>The client sends the refresh token previously issued by the authentication
 * server in order to obtain a new access token.</p>
 */
@Data
public class RefreshToken {

    /**
     * The refresh token string provided by the authentication server
     * during the initial login.
     */
    @NotBlank
    private String value;
}
