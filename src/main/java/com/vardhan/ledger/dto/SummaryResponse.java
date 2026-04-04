package com.vardhan.ledger.dto;

public class SummaryResponse {

    private double totalDebit;
    private double totalCredit;
    private double netBalanceChange;

    public SummaryResponse(double totalDebit, double totalCredit, double netBalanceChange) {
        this.totalDebit = totalDebit;
        this.totalCredit = totalCredit;
        this.netBalanceChange = netBalanceChange;
    }

    public double getTotalDebit() { return totalDebit; }
    public double getTotalCredit() { return totalCredit; }
    public double getNetBalanceChange() { return netBalanceChange; }
}