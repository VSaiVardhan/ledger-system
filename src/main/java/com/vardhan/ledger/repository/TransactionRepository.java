package com.vardhan.ledger.repository;

import com.vardhan.ledger.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}