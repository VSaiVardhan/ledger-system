package com.vardhan.ledger.repository;

import com.vardhan.ledger.model.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EntryRepository extends JpaRepository<Entry, Long> {

    // ORDERED (CRITICAL for running balance)
    List<Entry> findByAccountIdOrderByTransaction_DateAsc(Long accountId);

    // ORDERED FILTER
    List<Entry> findByAccountIdAndTransaction_DateBetweenOrderByTransaction_DateAsc(
        Long accountId,
        String from,
        String to
    );

    // ORDERED MONTH FILTER
    List<Entry> findByAccountIdAndTransaction_DateStartingWithOrderByTransaction_DateAsc(
        Long accountId,
        String yearMonth
    );

    List<Entry> findByTransactionId(Long transactionId);
}