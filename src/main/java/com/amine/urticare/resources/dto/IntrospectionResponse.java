package com.amine.urticare.resources.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 7662 OAuth2 Token Introspection Response DTO.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntrospectionResponse {

    /**
     * Whether or not the presented token is currently active.
     */
    private boolean active;

    /**
     * Subject of the token (typically the user email or ID).
     */
    private String sub;

    /**
     * The username associated with this token.
     */
    private String username;

    /**
     * The client_id of the OAuth2 client that requested this token.
     */
    private String clientId;

    /**
     * Token type, e.g. Bearer.
     */
    private String tokenType;

    /**
     * Token scopes.
     */
    private String scope;

    /**
     * Authorities or roles (optional, for internal debugging).
     */
    private List<String> authorities;

    /**
     * Token expiration (epoch seconds).
     */
    private Long exp;

    /**
     * Token issued at (epoch seconds).
     */
    private Long iat;

    /**
     * Token issuer.
     */
    private String iss;
}