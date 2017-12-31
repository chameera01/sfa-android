package com.example.ahmed.sfa.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Shani on 04/11/2017.
 */

public class InvoiceSummary implements Parcelable {

    private String invoiceId;
    private String invoiceDate;
    private String invoiceValue;
    private String returnNo;
    private String returnValue;


    public InvoiceSummary(String invoiceId, String invoiceDate, String invoiceValue, String returnNo, String returnValue) {
        this.invoiceId = invoiceId;
        this.invoiceDate = invoiceDate;
        this.invoiceValue = invoiceValue;
        this.returnNo = returnNo;
        this.returnValue = returnValue;
    }

    protected InvoiceSummary(Parcel in) {
        invoiceId = in.readString();
        invoiceDate = in.readString();
        invoiceValue = in.readString();
        returnNo = in.readString();
        returnValue = in.readString();
    }

    public static final Creator<InvoiceSummary> CREATOR = new Creator<InvoiceSummary>() {
        @Override
        public InvoiceSummary createFromParcel(Parcel in) {
            return new InvoiceSummary(in);
        }

        @Override
        public InvoiceSummary[] newArray(int size) {
            return new InvoiceSummary[size];
        }
    };

    public String getInvoiceId() {
        return invoiceId;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public String getInvoiceValue() {
        return invoiceValue;
    }

    public String getReturnNo() {
        return returnNo;
    }

    public String getReturnValue() {
        return returnValue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(invoiceId);
        dest.writeString(invoiceDate);
        dest.writeString(invoiceValue);
        dest.writeString(returnNo);
        dest.writeString(returnValue);
    }

    public void readFromParcel(Parcel in) {
        invoiceId = in.readString();
        invoiceDate = in.readString();
        invoiceValue = in.readString();
        returnNo = in.readString();
        returnValue = in.readString();

    }
}
