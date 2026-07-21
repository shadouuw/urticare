package com.amine.urticare.repository;

import com.amine.urticare.model.entities.Submission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    Page<Submission> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

    long countByUserId(String userId);
}