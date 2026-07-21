package com.amine.urticare.repository;

import com.amine.urticare.model.entities.Suivi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SuiviRepository extends JpaRepository<Suivi, Long> {

    Optional<Suivi> findByUserIdAndIsActiveTrue(String userId);

    List<Suivi> findByUserIdOrderByCreatedAtDesc(String userId);

    Page<Suivi> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

    boolean existsByUserIdAndIsActiveTrue(String userId);
}