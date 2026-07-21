package com.amine.urticare.resources.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecoveryConfirmRequest {

    @NotBlank
    private String transactionId;

    @NotBlank
    private String otp;
}
