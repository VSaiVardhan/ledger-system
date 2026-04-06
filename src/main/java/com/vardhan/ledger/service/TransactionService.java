package com.vardhan.ledger.service;

import com.vardhan.ledger.dto.EntryRequest;
import com.vardhan.ledger.dto.TransactionRequest;
import com.vardhan.ledger.exception.ResourceNotFoundException;
import com.vardhan.ledger.model.Account;
import com.vardhan.ledger.model.Entry;
import com.vardhan.ledger.model.EntryType;
import com.vardhan.ledger.model.Transaction;
import com.vardhan.ledger.repository.AccountRepository;
import com.vardhan.ledger.repository.EntryRepository;
import com.vardhan.ledger.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final EntryRepository entryRepository;

    // FIXED constructor injection
    public TransactionService(TransactionRepository transactionRepository,
                              AccountRepository accountRepository,
                              EntryRepository entryRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.entryRepository = entryRepository;
    }

    // ---------------- BASIC CRUD ----------------

    public String addTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
        return "Transaction saved to database!";
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction updateTransaction(Long id, Transaction updatedTransaction) {
        Transaction existing = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        existing.setTitle(updatedTransaction.getTitle());
        existing.setDate(updatedTransaction.getDate());

        return transactionRepository.save(existing);
    }

    public String deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Transaction not found");
        }

        transactionRepository.deleteById(id);
        return "Transaction deleted successfully!";
    }


    // ---------------- DOUBLE ENTRY LOGIC ----------------

    public String createDoubleEntryTransaction(TransactionRequest request) {

        if (request.getEntries() == null || request.getEntries().isEmpty()) {
            throw new RuntimeException("Entries cannot be empty");
        }

        double totalDebit = 0;
        double totalCredit = 0;

        // cleaner enum comparison
        for (EntryRequest e : request.getEntries()) {
            if (e.getType() == EntryType.DEBIT) {
                totalDebit += e.getAmount();
            } else if (e.getType() == EntryType.CREDIT) {
                totalCredit += e.getAmount();
            }
        }

        if (totalDebit != totalCredit) {
            throw new RuntimeException("Debit and Credit must be equal");
        }

        // Create transaction
        Transaction transaction = new Transaction();
        transaction.setTitle(request.getDescription());
        transaction.setDate(request.getDate());

        transaction = transactionRepository.save(transaction);

        // Create entries
        for (EntryRequest e : request.getEntries()) {

        Account account = accountRepository.findById(e.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        Entry entry = new Entry();
        entry.setTransaction(transaction);
        entry.setAccount(account);
        entry.setType(e.getType());
        entry.setAmount(e.getAmount());

        entryRepository.save(entry);

        //6 BALANCE UPDATE LOGIC
        double amount = e.getAmount();

        if (account.getBalance() == null) {
            account.setBalance(0.0);
        }

        switch (account.getType()) {

            case ASSET:
            case EXPENSE:
                if (e.getType() == EntryType.DEBIT) {
                    account.setBalance(account.getBalance() + amount);
                } else {
                    account.setBalance(account.getBalance() - amount);
                }
                break;

            case LIABILITY:
            case INCOME:
                if (e.getType() == EntryType.DEBIT) {
                    account.setBalance(account.getBalance() - amount);
                } else {
                    account.setBalance(account.getBalance() + amount);
                }
                break;
        }

        accountRepository.save(account);
    }

        return "Double-entry transaction created!";
    }

    public String reverseTransaction(Long transactionId) {

        Transaction original = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        List<Entry> originalEntries = entryRepository.findByTransactionId(transactionId);

        if (originalEntries.isEmpty()) {
            throw new RuntimeException("No entries found for transaction");
        }

        // Create new reversed transaction
        Transaction reversal = new Transaction();
        reversal.setTitle("Reversal of: " + original.getTitle());
        reversal.setDate(original.getDate());

        reversal = transactionRepository.save(reversal);

        for (Entry e : originalEntries) {

            Account account = e.getAccount();

            Entry reversedEntry = new Entry();
            reversedEntry.setTransaction(reversal);
            reversedEntry.setAccount(account);

            // Reverse type
            if (e.getType().name().equals("DEBIT")) {
                reversedEntry.setType(EntryType.CREDIT);
            } else {
                reversedEntry.setType(EntryType.DEBIT);
            }

            reversedEntry.setAmount(e.getAmount());

            entryRepository.save(reversedEntry);

            // ALSO reverse balance impact
            double amount = e.getAmount();

            if (account.getBalance() == null) {
                account.setBalance(0.0);
            }

            switch (account.getType()) {

                case ASSET:
                case EXPENSE:
                    if (reversedEntry.getType() == EntryType.DEBIT) {
                        account.setBalance(account.getBalance() + amount);
                    } else {
                        account.setBalance(account.getBalance() - amount);
                    }
                    break;

                case LIABILITY:
                case INCOME:
                    if (reversedEntry.getType() == EntryType.DEBIT) {
                        account.setBalance(account.getBalance() - amount);
                    } else {
                        account.setBalance(account.getBalance() + amount);
                    }
                    break;
            }

            accountRepository.save(account);
        }

        return "Transaction reversed successfully!";
    }

    public List<Entry> getEntriesByTransactionId(Long transactionId) {

        // Check transaction exists
        if (!transactionRepository.existsById(transactionId)) {
            throw new ResourceNotFoundException("Transaction not found");
        }

        return entryRepository.findByTransactionId(transactionId);
    }
}