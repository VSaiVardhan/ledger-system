package com.vardhan.ledger.dto;

import com.vardhan.ledger.model.EntryType;

public class StatementResponse {

    private EntryType type;
    private Double amount;
    private String title;
    private String date;
    private Double balance;

    public StatementResponse(EntryType type, Double amount, String title, String date, Double balance) {
        this.type = type;
        this.amount = amount;
        this.title = title;
        this.date = date;
        this.balance = balance;
    }

    public EntryType getType() { return type; }
    public Double getAmount() { return amount; }
    public String getTitle() { return title; }
    public String getDate() { return date; }
    public Double getBalance() { return balance; }
}