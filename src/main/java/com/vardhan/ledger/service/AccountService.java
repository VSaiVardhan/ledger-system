package com.vardhan.ledger.service;

import com.vardhan.ledger.dto.StatementResponse;
import com.vardhan.ledger.dto.SummaryResponse;
import com.vardhan.ledger.exception.ResourceNotFoundException;
import com.vardhan.ledger.model.Account;
import com.vardhan.ledger.model.Entry;
import com.vardhan.ledger.model.EntryType;
import com.vardhan.ledger.repository.AccountRepository;
import com.vardhan.ledger.repository.EntryRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final EntryRepository entryRepository;

    public AccountService(AccountRepository accountRepository, EntryRepository entryRepository) {
        this.accountRepository = accountRepository;
        this.entryRepository = entryRepository;
    }

    public Account createAccount(Account account) {

        // Validate required fields
        if (account.getName() == null || account.getName().isBlank()) {
            throw new IllegalArgumentException("Account name is required");
        }

        if (account.getType() == null) {
            throw new IllegalArgumentException("Account type is required");
        }

        // Ensure balance is never null
        if (account.getBalance() == null) {
            account.setBalance(0.0);
        }

        return accountRepository.save(account);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public List<StatementResponse> getAccountStatement(Long accountId, String from, String to) {

        if (!accountRepository.existsById(accountId)) {
            throw new ResourceNotFoundException("Account not found");
        }

        if (from != null && to != null && from.compareTo(to) > 0) {
            throw new IllegalArgumentException("Invalid date range");
        }

        List<Entry> entries;

        if (from != null && to != null) {
            entries = entryRepository.findByAccountIdAndTransaction_DateBetween(accountId, from, to);
        } else {
            entries = entryRepository.findByAccountId(accountId);
        }

        return entries.stream()
                .map(e -> new StatementResponse(
                        e.getType(),
                        e.getAmount(),
                        e.getTransaction().getTitle(),
                        e.getTransaction().getDate()
                ))
                .toList();
    }

    public SummaryResponse getMonthlySummary(Long accountId, String month, String year) {

        if (!accountRepository.existsById(accountId)) {
            throw new ResourceNotFoundException("Account not found");
        }

        // Format: YYYY-MM
        String yearMonth = year + "-" + month;

        List<Entry> entries = entryRepository
                .findByAccountIdAndTransaction_DateStartingWith(accountId, yearMonth);

        double totalDebit = 0;
        double totalCredit = 0;

        for (Entry e : entries) {
            if (e.getType() == EntryType.DEBIT) {
                totalDebit += e.getAmount();
            } else {
                totalCredit += e.getAmount();
            }
        }

        double net = totalDebit - totalCredit;

        return new SummaryResponse(totalDebit, totalCredit, net);
    }
}