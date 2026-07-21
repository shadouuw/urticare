package com.amine.urticare.resources.dto;

import lombok.Data;

/**
 * DTO representing the login request payload.
 *
 * <p>This object is sent by the client when attempting to
 * authenticate using email and password credentials.</p>
 */
@Data
public class LoginRequest {

	/**
	 * The user's email address used as the login identifier.
	 */
	private String email;

	/**
	 * The user's password used for authentication.
	 */
	private String password;
}
