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
import java.io.Serializable;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a Permission in the system.
 * Maps to the database table INNORP004T_PERMISSIONS.
 * Each permission can be linked to multiple actions.
 */
@Entity
@Table(name = "INNORP004T_PERMISSIONS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Permission implements Serializable {

	/** Primary key of the permission. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "INNORP004_ID")
	private Long id;

	/** Name of the permission (unique, not null). */
	@Column(name = "INNORP004_NAME", unique = true, nullable = false, length = 100)
	private String name;

	/** Description of the permission. */
	@Column(name = "INNORP004_DESCRIPTION")
	private String description;

	/** Type of resource this permission applies to. */
	@Column(name = "INNORP004_RESOURCE_TYPE", length = 50)
	private String resourceType;

	/** Set of actions associated with this permission. */
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "INNORP006T_PERMISSION_ACTIONS_MAPPING",
			joinColumns = @JoinColumn(name = "INNORP004_PERMISSION_ID"),
			inverseJoinColumns = @JoinColumn(name = "INNORP007_ACTION_ID")
	)
	private Set<Action> actions;
}
