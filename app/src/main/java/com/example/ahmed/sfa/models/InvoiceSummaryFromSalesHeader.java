package com.example.ahmed.sfa.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Shani on 10/11/2017.
 */

public class InvoiceSummaryFromSalesHeader implements Parcelable {


    private String subTotal;
    private String invoiceTotal;      // total amount
    private String discountRate;
    private String discount;
    private String freeQty;         //total of free items per product
    private String returnTotal;     // where to find
    private String returnQty;       // where to find
    private String credit;          // ??
    private String cash;
    private String cheque;

    public InvoiceSummaryFromSalesHeader(String subTotal, String invoiceTotal, String discountRate, String discount, String freeQty, String credit, String cash, String cheque) {
        this.subTotal = subTotal;
        this.invoiceTotal = invoiceTotal;
        this.discountRate = discountRate;
        this.discount = discount;
        this.freeQty = freeQty;
        this.credit = credit;
        this.cash = cash;
        this.cheque = cheque;
    }

    protected InvoiceSummaryFromSalesHeader(Parcel in) {
        subTotal = in.readString();
        invoiceTotal = in.readString();
        discountRate = in.readString();
        discount = in.readString();
        freeQty = in.readString();
        returnTotal = in.readString();
        returnQty = in.readString();
        credit = in.readString();
        cash = in.readString();
        cheque = in.readString();
    }

    public static final Creator<InvoiceSummaryFromSalesHeader> CREATOR = new Creator<InvoiceSummaryFromSalesHeader>() {
        @Override
        public InvoiceSummaryFromSalesHeader createFromParcel(Parcel in) {
            return new InvoiceSummaryFromSalesHeader(in);
        }

        @Override
        public InvoiceSummaryFromSalesHeader[] newArray(int size) {
            return new InvoiceSummaryFromSalesHeader[size];
        }
    };

    public String getSubTotal() {
        return subTotal;
    }

    public String getInvoiceTotal() {
        return invoiceTotal;
    }

    public String getDiscountRate() {
        return discountRate;
    }

    public String getDiscount() {
        return discount;
    }

    public String getFreeQty() {
        return freeQty;
    }

    public String getCredit() {
        return credit;
    }

    public String getCash() {
        return cash;
    }

    public String getCheque() {
        return cheque;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(subTotal);
        dest.writeString(invoiceTotal);
        dest.writeString(discountRate);
        dest.writeString(discount);
        dest.writeString(freeQty);
        dest.writeString(returnTotal);
        dest.writeString(returnQty);
        dest.writeString(credit);
        dest.writeString(cash);
        dest.writeString(cheque);
    }
}
