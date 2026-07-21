package com.amine.urticare.resources.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuiviResponse {

    private Long id;
    private String treatmentName;
    private String dosage;
    private Integer pillsPerDay;
    private LocalDate startDate;
    private LocalDate endDate;
    private String doctorName;
    private String notes;
    private Boolean isActive;
    private Boolean isExpired;
    private Integer daysRemaining;
    private Integer totalDays;
    private Integer daysElapsed;
    private LocalDateTime createdAt;
}