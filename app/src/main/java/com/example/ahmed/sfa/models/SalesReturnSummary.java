package com.example.ahmed.sfa.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by Ahmed on 4/24/2017.
 */

public class SalesReturnSummary implements Parcelable{
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
    private double returnTot;
    private int returnQty;

    public SalesReturnSummary(SalesInvoiceSummary summary){
        Log.d("SUM", "inside SalesReturnSummary_returnQty: " + summary.getSubtotal() + " retTot: " + summary.getInvoicedQty());
        returnTot = summary.getTotal();
        returnQty = summary.getInvoicedQty();

    }

    protected SalesReturnSummary(Parcel in) {
        returnTot = in.readDouble();
        returnQty = in.readInt();
    }

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
