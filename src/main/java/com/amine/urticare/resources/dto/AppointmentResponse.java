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
public class AppointmentResponse {

    private Long id;
    private String patientEmail;
    private LocalDateTime dateTime;
    private String doctorName;
    private String notes;
    private Boolean reminderSent;
    private LocalDateTime createdAt;
}
