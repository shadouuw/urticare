package com.amine.urticare.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Shared reference catalog of hospitals (public/private), not user-owned
// data. Seeded once on startup by ReferentialSeeder — extend the seed list
// there as the official list grows.
@Entity
@Table(name = "hospitals", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HospitalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // 'public' | 'private'
    @Column(nullable = false)
    private String type;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;
}
