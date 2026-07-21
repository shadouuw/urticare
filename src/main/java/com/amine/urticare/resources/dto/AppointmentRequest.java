package com.amine.urticare.resources.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {

    @NotBlank(message = "Patient email is required")
    @Email(message = "Email must be valid")
    private String patientEmail;

    @NotNull(message = "Date/time is required")
    private LocalDateTime dateTime;

    private String doctorName;

    private String notes;
}
