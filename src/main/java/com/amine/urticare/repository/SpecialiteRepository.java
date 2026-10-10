package com.amine.urticare.repository;

import com.amine.urticare.model.entities.SpecialiteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecialiteRepository extends JpaRepository<SpecialiteEntity, Long> {

    List<SpecialiteEntity> findAllByOrderBySortOrderAsc();

    Optional<SpecialiteEntity> findByName(String name);
}
