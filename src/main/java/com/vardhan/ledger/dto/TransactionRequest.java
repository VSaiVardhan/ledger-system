package com.vardhan.ledger.dto;

import java.util.List;

public class TransactionRequest {

    private String description;
    private String date;
    private List<EntryRequest> entries;

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public List<EntryRequest> getEntries() { return entries; }
    public void setEntries(List<EntryRequest> entries) { this.entries = entries; }
}