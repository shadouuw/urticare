package com.amine.urticare.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Shared reference catalog (Annexe 8 — dénomination commune internationale
// des antihistaminiques), not user-owned data. Seeded once on startup by
// AntihistaminiqueSeeder.
@Entity
@Table(name = "antihistaminiques", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AntihistaminiqueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    // Principal (default) adult tablet/capsule strength in mg — used as the
    // default "puissance" in the prescription's puissance x nombreComprimes =
    // total daily dose computation (see TraitementEntity).
    @Column(name = "strength_mg")
    private Double strengthMg;

    // Alternative capsule/tablet strength available for the same molecule
    // (e.g. a lower-dose formulation) — offered as a second choice next to
    // strengthMg when a patient's prescription uses that strength instead.
    @Column(name = "alternative_mg")
    private Double alternativeMg;

    // Whether this catalog entry is the principal (first-line) formulation
    // for its molecule, as opposed to a secondary/alternative entry.
    @Column(name = "is_principale")
    private Boolean isPrincipale;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;
}
