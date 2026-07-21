package com.amine.urticare.repository;

import com.amine.urticare.model.entities.SubmissionPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionPhotoRepository extends JpaRepository<SubmissionPhoto, Long> {
}
