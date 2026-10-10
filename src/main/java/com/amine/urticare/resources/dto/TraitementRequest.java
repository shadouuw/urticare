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
public class TraitementRequest {

    @NotBlank(message = "Type is required")
    private String type;

    @NotBlank(message = "Rubrique is required")
    private String rubrique;

    private String medicament;

    private String precision;

    @NotBlank(message = "Frequence is required")
    private String frequence;

    private Integer frequenceSemaines;

    private Double puissanceMg;

    private Double nombreComprimes;

    private Double score;

    private Boolean reminderEnabled;

    private Integer reminderHour;

    private Integer reminderMinute;
}
