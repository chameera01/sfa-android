package com.example.ahmed.sfa.models;

/**
 * Created by Ahmed on 10/1/2017.
 */

public class PrincipleDiscountModel {
    private String principleID;
    private String principle;//Brand
    private double amount;
    private double discount;
    private double disountValue;

    public String getPrincipleID() {
        return principleID;
    }

    private void calculate(){
        disountValue = amount * discount/100;
    }

    public void setPrincipleID(String principleID) {
        this.principleID = principleID;
    }

    public String getPrinciple() {
        return principle;
    }

    public void setPrinciple(String principle) {
        this.principle = principle;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
        calculate();
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
        calculate();
    }

    public double getDisountValue() {
        return disountValue;
    }

    public void setDisountValue(double disountValue) {
        this.disountValue = disountValue;
    }
}
