package com.example.ahmed.sfa.models;

/**
 * Created by Shani on 24/01/2018.
 */

public class InvoiceType {

    private String invoice;
    private String type;

    public InvoiceType() {
        this.invoice = "";
        this.type = "";
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
