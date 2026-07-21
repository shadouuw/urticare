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
        schema = "app",
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

    @Column(name = "symptoms_json", columnDefinition = "TEXT")
    private String symptomsJson;

    @Column(name = "include_selfies", nullable = false)
    private Boolean includeSelfies;

    @Column(name = "selfie_count")
    private Integer selfieCount;

    @Column(name = "include_qol", nullable = false)
    private Boolean includeQol;

    @Column(name = "qol_answers_json", columnDefinition = "TEXT")
    private String qolAnswersJson;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
