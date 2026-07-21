package com.amine.urticare.resources.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BilanRequest {

    @NotBlank(message = "Patient email is required")
    @Email(message = "Email must be valid")
    private String patientEmail;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotBlank(message = "Mood is required")
    private String mood;

    @NotNull(message = "includeSuivi is required")
    private Boolean includeSuivi;

    private Double itchScore;

    private Double papulesScore;

    private Map<String, Boolean> bodyParts;

    private Map<String, Integer> symptoms;

    @NotNull(message = "includeSelfies is required")
    private Boolean includeSelfies;

    private Integer selfieCount;

    @NotNull(message = "includeQol is required")
    private Boolean includeQol;

    private List<Integer> qolAnswers;
}
