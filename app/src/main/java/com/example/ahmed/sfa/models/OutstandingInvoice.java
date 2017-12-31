package com.example.ahmed.sfa.models;

/**
 * Created by Shani on 15/12/2017.
 */

public class OutstandingInvoice {

    private String InvoiceNo;
    private double CurrentCredit;

    public String getInvoiceNo() {
        return InvoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        InvoiceNo = invoiceNo;
    }

    public double getCurrentCredit() {
        return CurrentCredit;
    }

    public void setCurrentCredit(double currentCredit) {
        CurrentCredit = currentCredit;
    }
}


