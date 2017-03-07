package com.example.ahmed.sfa.models;

/**
 * Created by Ahmed on 3/6/2017.
 */

public class CustomerStatus {
    private String customerStatusID;
    private String customerStatus;

    public String getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(String customerStatus) {
        this.customerStatus = customerStatus;
    }

    public String getCustomerStatusID() {
        return customerStatusID;
    }

    public void setCustomerStatusID(String customerStatusID) {
        this.customerStatusID = customerStatusID;
    }

    public CustomerStatus(String customerStatusID, String customerStatus) {

        this.customerStatusID = customerStatusID;
        this.customerStatus = customerStatus;
    }

    @Override
    public String toString(){
        return customerStatus;
    }
}
