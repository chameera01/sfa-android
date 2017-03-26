package com.example.ahmed.sfa.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DecimalFormat;

/**
 * Created by Ahmed on 3/24/2017.
 */

public class SalesInvoiceSummary implements Parcelable{

    private double discount;
    private double subtotal;
    private double total;
    private int invoicedQty;
    private int freeQty;

    public SalesInvoiceSummary() {
        discount = 0.0;
        subtotal = 0.0;
        total = 0.0;
        invoicedQty = 0;
        freeQty = 0;
    }

    public SalesInvoiceSummary(double discount, double subtotal, double total, int invoicedQty,int freeQty) {
        this.discount = discount;
        this.subtotal = subtotal;
        this.total = total;
        this.invoicedQty = invoicedQty;
        this.freeQty = freeQty;
    }

    protected SalesInvoiceSummary(Parcel in) {
        discount = in.readDouble();
        subtotal = in.readDouble();
        total = in.readDouble();
        invoicedQty = in.readInt();
        freeQty = in.readInt();
    }

    public static final Creator<SalesInvoiceSummary> CREATOR = new Creator<SalesInvoiceSummary>() {
        @Override
        public SalesInvoiceSummary createFromParcel(Parcel in) {
            return new SalesInvoiceSummary(in);
        }

        @Override
        public SalesInvoiceSummary[] newArray(int size) {
            return new SalesInvoiceSummary[size];
        }
    };

    public double getDiscount() {
        DecimalFormat format = new DecimalFormat("0.2f");
        return Double.parseDouble(format.format(discount));
       // return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getSubtotal() {
        DecimalFormat format = new DecimalFormat("0.2f");
        return Double.parseDouble(format.format(subtotal));

    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getInvoicedQty() {
        return invoicedQty;
    }

    public void setInvoicedQty(int invoicedQty) {
        this.invoicedQty = invoicedQty;
    }

    public int getFreeQty() {
        return freeQty;
    }

    public void setFreeQty(int freeQty) {
        this.freeQty = freeQty;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(discount);
        dest.writeDouble(subtotal);
        dest.writeDouble(total);
        dest.writeInt(invoicedQty);
        dest.writeInt(freeQty);
    }
}
