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
public class TraitementResponse {

    private Long id;
    private String type;
    private String rubrique;
    private String medicament;
    private String precision;
    private String frequence;
    private Integer frequenceSemaines;
    private Double puissanceMg;
    private Double nombreComprimes;
    private Double score;
    private Boolean reminderEnabled;
    private Integer reminderHour;
    private Integer reminderMinute;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
