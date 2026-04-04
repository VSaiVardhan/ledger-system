package com.vardhan.ledger.model;

import jakarta.persistence.*;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private AccountType type;

    private Double balance = 0.0;

    public Account() {}

    public Account(Long id, String name, AccountType type, Double balance) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.balance = balance;
    }

    // Getters & Setters

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public AccountType getType() { return type; }

    public void setType(AccountType type) { this.type = type; }

    public Double getBalance() { return balance; }

    public void setBalance(Double balance) { this.balance = balance; }
}