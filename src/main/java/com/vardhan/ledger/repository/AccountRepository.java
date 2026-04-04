package com.vardhan.ledger.repository;

import com.vardhan.ledger.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}