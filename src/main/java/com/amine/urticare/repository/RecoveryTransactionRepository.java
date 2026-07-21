package com.amine.urticare.repository;

import com.amine.urticare.model.entities.RecoveryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecoveryTransactionRepository extends JpaRepository<RecoveryTransaction, String> {
}
