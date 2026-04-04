package com.vardhan.ledger.dto;

import com.vardhan.ledger.model.EntryType;

public class EntryRequest {

    private Long accountId;
    private EntryType type;
    private Double amount;

    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }

    public EntryType getType() { return type; }
    public void setType(EntryType type) { this.type = type; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
}