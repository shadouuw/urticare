package com.amine.urticare.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "bilans",
        schema = "public",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_bilans_patient_email_date",
                columnNames = {"patient_email", "date"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BilanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_email", nullable = false)
    private String patientEmail;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String mood;

    @Column(name = "include_suivi", nullable = false)
    private Boolean includeSuivi;

    @Column(name = "itch_score")
    private Double itchScore;

    @Column(name = "papules_score")
    private Double papulesScore;

    @Column(name = "body_parts_json", columnDefinition = "TEXT")
    private String bodyPartsJson;

    // Checked danger/warning signs that day (e.g. "breathingDifficulty",
    // "lipSwelling") — same clinical checklist as the app's "Signes
    // d'alerte" tracking card, stored as a JSON string array.
    @Column(name = "alert_symptoms_json", columnDefinition = "TEXT")
    private String alertSymptomsJson;

    @Column(name = "include_selfies", nullable = false)
    private Boolean includeSelfies;

    @Column(name = "selfie_count")
    private Integer selfieCount;

    @Column(name = "include_qol", nullable = false)
    private Boolean includeQol;

    @Column(name = "qol_answers_json", columnDefinition = "TEXT")
    private String qolAnswersJson;

    @Column(name = "attachment_count")
    private Integer attachmentCount;

    @Column(name = "include_traitement")
    private Boolean includeTraitement;

    @Column(name = "traitement_type")
    private String traitementType;

    @Column(name = "traitement_rubrique")
    private String traitementRubrique;

    @Column(name = "traitement_medicament")
    private String traitementMedicament;

    @Column(name = "traitement_precision")
    private String traitementPrecision;

    @Column(name = "traitement_frequence")
    private String traitementFrequence;

    @Column(name = "traitement_comprimes")
    private Double traitementComprimes;

    // Strength (mg per pill/capsule) chosen for that day's oral prescription
    // — combined with traitementComprimes to compute the actual daily dose
    // taken (puissance x comprimés), shown in the bilan detail and PDF report.
    @Column(name = "traitement_puissance_mg")
    private Double traitementPuissanceMg;

    @Column(name = "traitement_dose_value")
    private Double traitementDoseValue;

    @Column(name = "traitement_dose_unit")
    private String traitementDoseUnit;

    // Dedicated injection snapshot — independent of the traitementX fields
    // above (which describe the oral treatment when one exists). Only set
    // when an injectable dose was actually given that day.
    @Column(name = "include_injection")
    private Boolean includeInjection;

    @Column(name = "injection_rubrique")
    private String injectionRubrique;

    @Column(name = "injection_precision")
    private String injectionPrecision;

    @Column(name = "injection_frequence")
    private String injectionFrequence;

    @Column(name = "injection_frequence_semaines")
    private Integer injectionFrequenceSemaines;

    @Column(name = "injection_dose_value")
    private Double injectionDoseValue;

    @Column(name = "injection_dose_unit")
    private String injectionDoseUnit;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
