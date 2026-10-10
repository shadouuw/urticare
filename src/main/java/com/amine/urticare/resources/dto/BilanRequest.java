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

    private List<String> alertSymptoms;

    @NotNull(message = "includeSelfies is required")
    private Boolean includeSelfies;

    private Integer selfieCount;

    @NotNull(message = "includeQol is required")
    private Boolean includeQol;

    private List<Integer> qolAnswers;

    private Integer attachmentCount;

    private Boolean includeTraitement;

    private String traitementType;

    private String traitementRubrique;

    private String traitementMedicament;

    private String traitementPrecision;

    private String traitementFrequence;

    private Double traitementComprimes;

    private Double traitementPuissanceMg;

    private Double traitementDoseValue;

    private String traitementDoseUnit;

    private Boolean includeInjection;

    private String injectionRubrique;

    private String injectionPrecision;

    private String injectionFrequence;

    private Integer injectionFrequenceSemaines;

    private Double injectionDoseValue;

    private String injectionDoseUnit;
}
