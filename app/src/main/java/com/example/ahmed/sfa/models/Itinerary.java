package com.example.ahmed.sfa.models;

import android.util.Log;

/**
 * Created by Ahmed on 3/3/2017.
 */

public class Itinerary {
    private String id;
    private String customerNo;
    private String customer;
    private String town;
    private boolean isInvoiced;
    private boolean isPlanned;
    private boolean isDismissed;


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
        this.isInvoiced = (isInvoiced==0)?true:false;
        this.isDismissed = (isInvoiced==2)?true:false;
        Log.w("Test x",customerNo+"---"+this.isDismissed());
        this.isPlanned = (isPlanned==1)?false:true;

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
}