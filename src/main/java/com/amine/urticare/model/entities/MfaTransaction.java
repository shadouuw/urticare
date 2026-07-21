package com.amine.urticare.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "INNORP009T_MFA_TRANSACTIONS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MfaTransaction {

    @Id
    private String id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String otpHash;

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private int attempts;

    @Column(nullable = false)
    private boolean used;
}
