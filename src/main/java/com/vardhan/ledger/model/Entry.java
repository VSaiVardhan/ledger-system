package com.vardhan.ledger.model;

import jakarta.persistence.*;

@Entity
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Enumerated(EnumType.STRING)
    private EntryType type;

    @Column(nullable = false)
    private Double amount;

    public Entry() {}

    public Entry(Long id, Transaction transaction, Account account, EntryType type, Double amount) {
        this.id = id;
        this.transaction = transaction;
        this.account = account;
        this.type = type;
        this.amount = amount;
    }

    // Getters & Setters

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Transaction getTransaction() { return transaction; }

    public void setTransaction(Transaction transaction) { this.transaction = transaction; }

    public Account getAccount() { return account; }

    public void setAccount(Account account) { this.account = account; }

    public EntryType getType() { return type; }

    public void setType(EntryType type) { this.type = type; }

    public Double getAmount() { return amount; }

    public void setAmount(Double amount) { this.amount = amount; }
}
