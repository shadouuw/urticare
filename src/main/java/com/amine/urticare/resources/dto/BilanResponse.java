package com.amine.urticare.resources.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BilanResponse {

    private Long id;
    private String patientEmail;
    private LocalDate date;
    private String mood;
    private Boolean includeSuivi;
    private Double itchScore;
    private Double papulesScore;
    private Map<String, Boolean> bodyParts;
    private Map<String, Integer> symptoms;
    private Boolean includeSelfies;
    private Integer selfieCount;
    private Boolean includeQol;
    private List<Integer> qolAnswers;
    private LocalDateTime createdAt;
}
