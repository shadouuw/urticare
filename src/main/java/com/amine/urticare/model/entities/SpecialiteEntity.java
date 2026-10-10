package com.amine.urticare.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Shared reference catalog of medical specialties, not user-owned data.
// Seeded once on startup by ReferentialSeeder.
@Entity
@Table(name = "specialites", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpecialiteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;
}
