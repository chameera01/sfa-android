package com.example.ahmed.sfa.models;

/**
 * Created by Ahmed on 3/3/2017.
 */

public class Itinerary {
    private String id;
    private String customerNo;
    private String customer;
    private String town;
    private boolean isInvoiced;

    public Itinerary(String id,String customerNo, String customer, String town, int isInvoiced){
        this.id = id;
        this.customer = customer;
        this.customerNo = customerNo;
        this.town = town;
        this.isInvoiced = (isInvoiced==0)?false:true;
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
}