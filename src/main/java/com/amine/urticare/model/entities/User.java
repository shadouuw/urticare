package com.amine.urticare.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.io.Serializable;
import java.util.Set;

/**
 * Represents a user account in the system.
 * Maps to the database table INNORP001T_ACCOUNT.
 * Each user can have multiple roles assigned.
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "INNORP001T_ACCOUNT")
public class User implements Serializable {

    /** Primary key of the user. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INNORP001_ID")
    private Long id;

    /** Username of the user (not null). */
    @Column(name = "INNORP001_USERNAME", nullable = false, length = 50)
    private String username;

    /** First name of the user (not null). */
    @Column(name = "INNORP001_FIRST_NAME", nullable = false, length = 50)
    private String firstname;

    /** Last name of the user (not null). */
    @Column(name = "INNORP001_LAST_NAME", nullable = false, length = 50)
    private String lastname;

    /** Email of the user (unique, not null). */
    @Column(name = "INNORP001_EMAIL", unique = true, nullable = false, length = 100)
    private String email;

    /** Flag indicating if the user is enabled. */
    @Column(name = "INNORP001_IS_ENABLED", nullable = false)
    private Boolean isEnabled;

    /** Password of the user (not null). */
    @Column(name = "INNORP001_PASSWORD", nullable = false)
    private String password;

    /** Set of roles assigned to the user. */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "INNORP003T_USER_ACCOUNT_MAPPING",
            joinColumns = @JoinColumn(name = "INNORP001_ACCOUNT_ID"),
            inverseJoinColumns = @JoinColumn(name = "INNORP002_ROLE_ID")
    )
    private Set<Role> roles;
}
