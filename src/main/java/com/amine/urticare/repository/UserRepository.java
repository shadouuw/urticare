package com.amine.urticare.repository;

import com.amine.urticare.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailIgnoreCaseAndIsEnabled(String email, boolean isEnabled);

    /**
     * Finds a user by email with eagerly loaded roles, permissions, and actions
     */
    @Query("SELECT u FROM User u " +
            "LEFT JOIN FETCH u.roles r " +
            "LEFT JOIN FETCH r.permissions p " +
            "LEFT JOIN FETCH p.actions " +
            "WHERE LOWER(u.email) = LOWER(:email) AND u.isEnabled = :isEnabled")
    Optional<User> findByEmailIgnoreCaseAndIsEnabledWithRolesAndPermissions(
            @Param("email") String email,
            @Param("isEnabled") boolean isEnabled
    );

    /**
     * Finds a user by ID with eagerly loaded roles, permissions, and actions
     */
    @Query("SELECT u FROM User u " +
            "LEFT JOIN  u.roles r " +
            "LEFT JOIN  r.permissions p " +
            "LEFT JOIN  p.actions " +
            "WHERE u.id = :id")
    Optional<User> findByIdWithRolesAndPermissions(@Param("id") Long id);

    /**
     * Finds a user by username (if you have username field)
     */
    @Query("SELECT u FROM User u " +
            "LEFT JOIN FETCH u.roles r " +
            "LEFT JOIN FETCH r.permissions p " +
            "LEFT JOIN FETCH p.actions " +
            "WHERE LOWER(u.username) = LOWER(:username) AND u.isEnabled = true")
    Optional<User> findByUsernameWithRolesAndPermissions(@Param("username") String username);

}