package com.amine.urticare.resources.dto;

/**
 * DTO carrying authenticated user information.
 *
 * @param email     Email of the user.
 * @param username  Username of the user.
 * @param isEnabled Whether the user account is enabled.
 */

public record UserInformation(String email, String username, Boolean isEnabled , String firstName, String lastName) { }
