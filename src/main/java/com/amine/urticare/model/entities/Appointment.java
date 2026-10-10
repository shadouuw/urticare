package com.amine.urticare.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_email", nullable = false)
    private String patientEmail;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "doctor_name")
    private String doctorName;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "reminder_sent", nullable = false)
    private Boolean reminderSent = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (reminderSent == null) {
            reminderSent = false;
        }
    }
}
