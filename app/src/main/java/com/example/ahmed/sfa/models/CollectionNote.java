package com.example.ahmed.sfa.models;

/**
 * Created by Shani on 19/12/2017.
 */

public class CollectionNote {

    private String InvoiceNo;
    private double creditAmt;
    private double cashAmt;
    private double chequeAmt;
    private double balance;
    private double remaining;

    public String getInvoiceNo() {
        return InvoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        InvoiceNo = invoiceNo;
    }

    public double getCreditAmt() {
        return creditAmt;
    }

    public void setCreditAmt(double creditAmt) {
        this.creditAmt = creditAmt;
    }

    public double getCashAmt() {
        return cashAmt;
    }

    public void setCashAmt(double cashAmt) {
        this.cashAmt = cashAmt;
    }

    public double getChequeAmt() {
        return chequeAmt;
    }

    public void setChequeAmt(double chequeAmt) {
        this.chequeAmt = chequeAmt;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getRemaining() {
        return remaining;
    }

    public void setRemaining(double remaining) {
        this.remaining = remaining;
    }
}
