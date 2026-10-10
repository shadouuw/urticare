package com.amine.urticare.repository;

import com.amine.urticare.model.entities.GouvernoratEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GouvernoratRepository extends JpaRepository<GouvernoratEntity, Long> {

    List<GouvernoratEntity> findAllByOrderBySortOrderAsc();

    Optional<GouvernoratEntity> findByName(String name);
}
