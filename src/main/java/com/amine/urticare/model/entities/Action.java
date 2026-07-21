package com.amine.urticare.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Entity representing an Action in the system.
 * Maps to the database table INNORP007T_ACTIONS.
 */
@Entity
@Table(name = "INNORP007T_ACTIONS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Action implements Serializable {

    /** Primary key of the action. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INNORP007_ID")
    private Long id;

    /** Name of the action (unique, not null). */
    @Column(name = "INNORP007_NAME", unique = true, nullable = false)
    private String name;

    /** Description of the action (unique, not null). */
    @Column(name = "INNORP007_DESCRIPTION", unique = true, nullable = false)
    private String description;
}
