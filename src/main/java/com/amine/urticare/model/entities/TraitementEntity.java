package com.amine.urticare.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// A patient can have one ACTIVE oral prescription and one ACTIVE injectable
// prescription at the same time (e.g. an antihistamine alongside
// omalizumab) — they are not mutually exclusive. Uniqueness is therefore
// per (user_id, type), not per user_id alone.
@Entity
@Table(name = "traitements", schema = "public",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "type"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TraitementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(nullable = false)
    private String type; // oral | injectable

    @Column(nullable = false)
    private String rubrique; // antihistaminique | ciclosporine | omalizumab | dupilumab | autre

    private String medicament;

    @Column(name = "precision_text")
    private String precision;

    @Column(nullable = false)
    private String frequence; // jour | semaine | toutes_les_2_semaines | toutes_les_4_semaines | toutes_les_n_semaines

    // Set only when frequence == 'toutes_les_n_semaines' (injectable,
    // rubrique 'autre') — the configurable interval in weeks.
    @Column(name = "frequence_semaines")
    private Integer frequenceSemaines;

    // Prescribed strength per unit (mg per tablet/capsule) and prescribed
    // quantity (tablets/capsules) — puissanceMg * nombreComprimes = the
    // total prescribed daily dose in mg, shown in the PDF report. For
    // ciclosporine, puissanceMg is one of 10/25/50/100 (capsule strengths).
    @Column(name = "puissance_mg")
    private Double puissanceMg;

    @Column(name = "nombre_comprimes")
    private Double nombreComprimes;

    private Double score = 0.0;

    @Column(name = "reminder_enabled")
    private Boolean reminderEnabled = false;

    @Column(name = "reminder_hour")
    private Integer reminderHour;

    @Column(name = "reminder_minute")
    private Integer reminderMinute;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
