package com.example.ahmed.sfa.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Ahmed on 3/24/2017.**/

public class SalesInvoiceSummary implements Parcelable{

    private static final String TAG = "INVOICE";
    private double discount;
    private double subtotal;
    private double total;
    private double returnVal;
    private int returnQty;
    private int invoicedQty;
    private int freeQty;

    private int shelfQty;
    private int orderQty;




    public SalesInvoiceSummary() {
        discount = 0.0;
        subtotal = 0.0;
        total = 0.0;
        invoicedQty = 0;
        freeQty = 0;
        returnVal =0.0;
        returnQty=0;

        shelfQty = 0;
        orderQty = 0;
    }

    public SalesInvoiceSummary(double discount, double subtotal, double total, int invoicedQty, int freeQty) {
        this.discount = discount;
        this.subtotal = subtotal;
        this.total = total;
        this.invoicedQty = invoicedQty;
        this.freeQty = freeQty;
        returnVal =0.0;
        returnQty=0;
    }

    public SalesInvoiceSummary(double discount, double subtotal, double total, int invoicedQty, int shelfQty, int orderQty, int freeQty) {
        this.discount = discount;
        this.subtotal = subtotal;
        this.total = total;
        this.invoicedQty = invoicedQty;
        this.freeQty = freeQty;
        returnVal = 0.0;
        returnQty = 0;

        this.shelfQty = shelfQty;
        this.orderQty = orderQty;


    }

    public static SalesInvoiceSummary createSalesInvoiceSummary(List<SalesInvoiceModel> data){

        Log.d(TAG, "inside SalesInvoiceSummary createSalesInvoiceSummary method");
        Log.d(TAG, "data list size_" + data.size());
        double discount=0.0;
        double subtotal=0.0;
        double total=0.0;
        int invoicedQty=0;
        int freeQty=0;

        int shelfQty = 0;
        int orderQty = 0;

        for(int i=0;i<data.size();i++){
            Log.d(TAG, "inside for loop where cumulates the values");
            discount +=data.get(i).getDiscount();
            subtotal += data.get(i).getSubtotalVal();
            total += data.get(i).getLineValue();
            invoicedQty += data.get(i).getInvoiceQuantity();
            freeQty += data.get(i).getFree();

            shelfQty += data.get(i).getShelf();
            Log.d(TAG, "shelf added_" + String.valueOf(data.get(i).getShelf()));
            orderQty += data.get(i).getOrder();
            Log.d(TAG, "order added_" + String.valueOf(data.get(i).getOrder()));
        }

        Log.d(TAG, "shelf_" + String.valueOf(shelfQty));
        Log.d(TAG, "order_" + String.valueOf(orderQty));
        Log.d(TAG, "free_" + String.valueOf(freeQty));

        return new SalesInvoiceSummary(discount, subtotal, total, invoicedQty, shelfQty, orderQty, freeQty);
    }

    protected SalesInvoiceSummary(Parcel in) {
        discount = in.readDouble();
        subtotal = in.readDouble();
        total = in.readDouble();
        invoicedQty = in.readInt();
        freeQty = in.readInt();
        returnVal = in.readDouble();
        returnQty = in.readInt();

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

    public int getShelfQty() {
        return shelfQty;
    }

    public void setShelfQty(int shelfQty) {
        this.shelfQty = shelfQty;
    }

    public int getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(int orderQty) {
        this.orderQty = orderQty;
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
        dest.writeDouble(returnVal);
        dest.writeInt(returnQty);

    }
}
