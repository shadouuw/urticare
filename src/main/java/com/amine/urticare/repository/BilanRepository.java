package com.amine.urticare.repository;

import com.amine.urticare.model.entities.BilanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BilanRepository extends JpaRepository<BilanEntity, Long> {

    Optional<BilanEntity> findByPatientEmailAndDate(String patientEmail, LocalDate date);

    List<BilanEntity> findByPatientEmailOrderByDateDesc(String patientEmail);
}
