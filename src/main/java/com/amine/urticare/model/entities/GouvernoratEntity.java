package com.amine.urticare.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Shared reference catalog (Tunisia's 24 gouvernorats), not user-owned data.
// Seeded once on startup by ReferentialSeeder.
@Entity
@Table(name = "gouvernorats", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GouvernoratEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;
}
