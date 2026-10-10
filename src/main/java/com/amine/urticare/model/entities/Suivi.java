package com.amine.urticare.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "suivis", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Suivi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "treatment_name", nullable = false)
    private String treatmentName;

    @Column(name = "dosage")
    private String dosage;

    @Column(name = "pills_per_day")
    private Integer pillsPerDay;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "doctor_name")
    private String doctorName;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(endDate);
    }
}