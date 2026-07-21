package com.amine.urticare.resources.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class SuiviRequest {

    @NotBlank(message = "Treatment name is required")
    private String treatmentName;

    private String dosage;

    private Integer pillsPerDay;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    private String doctorName;

    private String notes;
}