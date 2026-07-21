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
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a Role in the system.
 * Maps to the database table INNORP002T_ROLES.
 * Each role can be assigned to multiple users and have multiple permissions.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "INNORP002T_ROLES")
public class Role implements Serializable {

	/** Primary key of the role. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "INNORP002_ID")
	private Long id;

	/** Name of the role (unique, not null). */
	@Column(name = "INNORP002_NAME", unique = true, nullable = false, length = 100)
	private String name;

	/** Description of the role. */
	@Column(name = "INNORP002_DESCRIPTION")
	private String description;

	/** Set of users assigned to this role. */
	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
	private Set<User> users = new HashSet<>();

	/** Set of permissions assigned to this role. */
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "INNORP005T_ROLE_PERMISSIONS_MAPPING",
			joinColumns = @JoinColumn(name = "INNORP002_ROLE_ID"),
			inverseJoinColumns = @JoinColumn(name = "INNORP004_PERMISSION_ID")
	)
	private Set<Permission> permissions;
}
