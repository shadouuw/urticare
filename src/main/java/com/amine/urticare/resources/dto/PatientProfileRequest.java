package com.amine.urticare.resources.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientProfileRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    private String firstname;

    private String lastname;

    private String phone;

    private String region;

    private Integer age;

    private Double weight;

    private String gouvernorat;

    private String country;

    private String suiviType;

    private String hospital;

    private String doctorName;

    private String doctorSpecialty;
}
