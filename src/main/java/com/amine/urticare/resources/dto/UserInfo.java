package com.amine.urticare.resources.dto;


/**
 * DTO representing essential information about an authenticated user.
 *
 * <p>This is returned in the {@link JwtAuthenticationResponse} to provide
 * client-side apps with user identity and status details.</p>
 *
 * @param email     Email address used as the username.
 * @param isEnabled Account enabled status.
 */

public record UserInfo(String email, Boolean isEnabled) {}
