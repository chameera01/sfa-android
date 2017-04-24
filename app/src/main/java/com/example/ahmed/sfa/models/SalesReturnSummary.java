package com.example.ahmed.sfa.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.ahmed.sfa.activities.SalesInvoicePayment;

/**
 * Created by Ahmed on 4/24/2017.
 */

public class SalesReturnSummary implements Parcelable{
    private double returnTot;
    private int returnQty;

    public SalesReturnSummary(SalesInvoiceSummary summary){
        returnTot = summary.getTotal();
        returnQty = summary.getInvoicedQty();

    }

    protected SalesReturnSummary(Parcel in) {
        returnTot = in.readDouble();
        returnQty = in.readInt();
    }

    public static final Creator<SalesReturnSummary> CREATOR = new Creator<SalesReturnSummary>() {
        @Override
        public SalesReturnSummary createFromParcel(Parcel in) {
            return new SalesReturnSummary(in);
        }

        @Override
        public SalesReturnSummary[] newArray(int size) {
            return new SalesReturnSummary[size];
        }
    };

    public double getReturnTot() {
        return returnTot;
    }

    public void setReturnTot(double returnTot) {
        this.returnTot = returnTot;
    }

    public int getReturnQty() {
        return returnQty;
    }

    public void setReturnQty(int returnQty) {
        this.returnQty = returnQty;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(returnTot);
        dest.writeInt(returnQty);
    }
}
