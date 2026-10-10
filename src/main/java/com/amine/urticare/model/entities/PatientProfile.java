package com.amine.urticare.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "patient_profiles", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String firstname;

    private String lastname;

    private String phone;

    private String region;

    private Integer age;

    // Weight in kg — used to compute the ciclosporine weight-based target
    // dose (5mg/kg/day), see TraitementServiceImpl / the Flutter oral form.
    private Double weight;

    private String gouvernorat;

    private String country;

    // 'public' | 'private' — which hospital list to offer in the app.
    @Column(name = "suivi_type")
    private String suiviType;

    private String hospital;

    @Column(name = "doctor_name")
    private String doctorName;

    @Column(name = "doctor_specialty")
    private String doctorSpecialty;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
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
