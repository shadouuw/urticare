package com.amine.urticare.model.entities;

import com.amine.urticare.model.enumeration.TokenType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an authentication token in the system.
 * Maps to the database table INNORP008T_TOKEN.
 * Stores access and refresh tokens for a user, along with token status and type.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "INNORP008T_TOKEN")
public class TokenEntity {

    /** Primary key of the token. */
    @Id
    @GeneratedValue
    @Column(name = "INNORP008_ID")
    private Integer id;

    /** Access token string (unique). */
    @Column(name = "INNORP008_ACCESS_TOKEN", unique = true, length = 4000)
    private String accessToken;

    /** Refresh token string (unique). */
    @Column(name = "INNORP008_REFRESH_TOKEN", unique = true, length = 4000)
    private String refreshToken;

    /** Type of the token (e.g., BEARER). */
    @Enumerated(EnumType.STRING)
    @Column(name = "INNORP008_TOKEN_TYPE")
    private TokenType tokenType = TokenType.BEARER;

    /** Flag indicating if the token has been revoked. */
    @Column(name = "INNORP008_IS_REVOKED")
    private boolean revoked;

    /** Flag indicating if the token has expired. */
    @Column(name = "INNORP008_IS_EXPIRED")
    private boolean expired;

    /** User associated with this token. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INNORP001_ACCOUNT_ID")
    private User user;
}
