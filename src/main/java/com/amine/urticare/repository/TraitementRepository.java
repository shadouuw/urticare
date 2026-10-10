package com.amine.urticare.repository;

import com.amine.urticare.model.entities.TraitementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TraitementRepository extends JpaRepository<TraitementEntity, Long> {

    Optional<TraitementEntity> findByUserIdAndType(String userId, String type);

    List<TraitementEntity> findAllByUserId(String userId);

    void deleteByUserIdAndType(String userId, String type);
}
