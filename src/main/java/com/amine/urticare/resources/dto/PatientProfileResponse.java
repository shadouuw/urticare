package com.amine.urticare.resources.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientProfileResponse {

    private Long id;
    private String email;
    private String firstname;
    private String lastname;
    private String phone;
    private String region;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
