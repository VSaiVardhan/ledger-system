package com.vardhan.ledger.repository;

import com.vardhan.ledger.model.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EntryRepository extends JpaRepository<Entry, Long> {
    List<Entry> findByAccountId(Long accountId);
    List<Entry> findByAccountIdAndTransaction_DateBetween(
        Long accountId,
        String from,
        String to
    );
    List<Entry> findByAccountIdAndTransaction_DateStartingWith(
        Long accountId,
        String yearMonth
    );
    List<Entry> findByTransactionId(Long transactionId);
}