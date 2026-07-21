package com.amine.urticare.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "submissions", schema = "app")
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "pill_count", nullable = false)
    private Integer pillCount;

    @Column(nullable = false)
    private String frequency;

    @Column(name = "itch_level", nullable = false)
    private Integer itchLevel;

    @Column(nullable = false)
    private String severity;

    @ElementCollection
    @CollectionTable(
            name = "submission_affected_areas",
            schema = "app",
            joinColumns = @JoinColumn(name = "submission_id")
    )
    @Column(name = "area")
    private List<String> affectedAreas = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubmissionPhoto> photos = new ArrayList<>();

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