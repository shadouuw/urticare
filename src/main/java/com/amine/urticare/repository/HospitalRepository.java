package com.amine.urticare.repository;

import com.amine.urticare.model.entities.HospitalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HospitalRepository extends JpaRepository<HospitalEntity, Long> {

    List<HospitalEntity> findAllByOrderBySortOrderAsc();

    List<HospitalEntity> findByTypeOrderBySortOrderAsc(String type);

    Optional<HospitalEntity> findByName(String name);
}
