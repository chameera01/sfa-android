package com.example.ahmed.sfa.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ahmed on 10/1/2017.
 */

public class PrincipleDiscountModel implements Parcelable{
    private String principleID;
    private String principle;//Brand
    private double amount;
    private double discount;
    private double disountValue;



    public PrincipleDiscountModel(Parcel in) {
        principleID = in.readString();
        principle = in.readString();
        amount = in.readDouble();
        discount = in.readDouble();
        disountValue = in.readDouble();
    }
    public PrincipleDiscountModel() {

    }

    public static final Creator<PrincipleDiscountModel> CREATOR = new Creator<PrincipleDiscountModel>() {
        @Override
        public PrincipleDiscountModel createFromParcel(Parcel in) {
            return new PrincipleDiscountModel(in);
        }

        @Override
        public PrincipleDiscountModel[] newArray(int size) {
            return new PrincipleDiscountModel[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(principleID);
        dest.writeString(principle);
        dest.writeDouble(amount);
        dest.writeDouble(discount);
        dest.writeDouble(disountValue);
    }
}
