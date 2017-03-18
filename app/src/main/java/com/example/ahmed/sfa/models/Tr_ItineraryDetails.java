package com.example.ahmed.sfa.models;

/**
 * Created by DELL on 3/17/2017.
 */

public class Tr_ItineraryDetails {
    /*_ID integer primary key AUTOINCREMENT ,ItineraryID text,ItineraryDate text,CustomerNo text,IsPlaned text,IsInvoiced text," +
    "LastUpdateDate  text*/

    private  String ItineraryID;
    private  String ItineraryDate;
    private  String CustomerNo;
    private  int IsPlaned;
    private  int IsInvoiced;
    private  String  LastUpdateDate;


    public String getItineraryID() {
        return ItineraryID;
    }

    public void setItineraryID(String itineraryID) {
        ItineraryID = itineraryID;
    }

    public String getItineraryDate() {
        return ItineraryDate;
    }

    public void setItineraryDate(String itineraryDate) {
        ItineraryDate = itineraryDate;
    }

    public String getCustomerNo() {
        return CustomerNo;
    }

    public void setCustomerNo(String customerNo) {
        CustomerNo = customerNo;
    }

    public int getIsPlaned() {
        return IsPlaned;
    }

    public void setIsPlaned(int isPlaned) {
        IsPlaned = isPlaned;
    }

    public int getIsInvoiced() {
        return IsInvoiced;
    }

    public void setIsInvoiced(int isInvoiced) {
        IsInvoiced = isInvoiced;
    }

    public String getLastUpdateDate() {
        return LastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        LastUpdateDate = lastUpdateDate;
    }
}
