package com.amine.urticare.resources.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request DTO for verifying a Multi-Factor Authentication (MFA) code.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MfaVerificationRequest {

    /** MFA transaction ID */
    @NotBlank
    private String transactionId;

    /** One-time password (OTP) */
    @NotBlank
    private String otp;
}
