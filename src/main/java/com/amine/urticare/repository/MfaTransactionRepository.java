package com.amine.urticare.repository;

import com.amine.urticare.model.entities.MfaTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MfaTransactionRepository extends JpaRepository<MfaTransaction, String> {
}