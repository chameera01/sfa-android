package com.example.ahmed.sfa.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.SyncStateContract;
import android.util.Log;

import com.example.ahmed.sfa.Constants;

/**
 * Created by Ahmed on 3/3/2017.
 */

public class Itinerary implements Parcelable {
    private String id;
    private String customerNo;
    private String customer;
    private String town;
    private boolean isInvoiced;
    private boolean isPlanned;
    private boolean isDismissed;


    protected Itinerary(Parcel in) {
        id = in.readString();
        customerNo = in.readString();
        customer = in.readString();
        town = in.readString();
        isInvoiced = in.readByte() != 0;
        isPlanned = in.readByte() != 0;
        isDismissed = in.readByte() != 0;
    }

    public static final Creator<Itinerary> CREATOR = new Creator<Itinerary>() {
        @Override
        public Itinerary createFromParcel(Parcel in) {
            return new Itinerary(in);
        }

        @Override
        public Itinerary[] newArray(int size) {
            return new Itinerary[size];
        }
    };

    public boolean isDismissed() {
        return isDismissed;
    }

    public void setDismissed(boolean dismissed) {
        isDismissed = dismissed;
    }

    public Itinerary(String id, String customerNo, String customer, String town, int isInvoiced, int isPlanned){
        this.id = id;
        this.customer = customer;
        this.customerNo = customerNo;
        this.town = town;
        this.isInvoiced = (isInvoiced== Constants.INVOICED)?true:false;
        this.isDismissed = (isInvoiced==Constants.DISMISSED)?true:false;
        Log.w("Test x",customerNo+"---"+this.isDismissed());
        this.isPlanned = (isPlanned==Constants.ACTIVE)?true:false;

    }

    public String getCustomerNo(){
        return customerNo;
    }

    public String getId(){
        return id;
    }

    public String getCustomer(){
        return customer;
    }

    public String getTown(){
        return town;
    }

    public boolean getIsInvoiced(){
        return isInvoiced;
    }

    public boolean isPlanned() {
        return isPlanned;
    }

    public void setPlanned(boolean planned) {
        isPlanned = planned;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(customerNo);
        dest.writeString(customer);
        dest.writeString(town);
        dest.writeByte((byte) (isInvoiced ? 1 : 0));
        dest.writeByte((byte) (isPlanned ? 1 : 0));
        dest.writeByte((byte) (isDismissed ? 1 : 0));
    }
}