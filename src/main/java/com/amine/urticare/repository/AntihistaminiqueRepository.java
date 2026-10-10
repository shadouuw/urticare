package com.amine.urticare.repository;

import com.amine.urticare.model.entities.AntihistaminiqueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AntihistaminiqueRepository extends JpaRepository<AntihistaminiqueEntity, Long> {

    List<AntihistaminiqueEntity> findAllByOrderBySortOrderAsc();

    Optional<AntihistaminiqueEntity> findByName(String name);
}
