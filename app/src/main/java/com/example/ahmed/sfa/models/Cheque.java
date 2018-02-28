package com.example.ahmed.sfa.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ahmed on 3/26/2017.
 */

public class Cheque implements Parcelable{
    public static final Creator<Cheque> CREATOR = new Creator<Cheque>() {
        @Override
        public Cheque createFromParcel(Parcel in) {
            return new Cheque(in);
        }

        @Override
        public Cheque[] newArray(int size) {
            return new Cheque[size];
        }
    };
    private double chequeVal;
    private String chequeNum;
    private String bank;
    private String collectionDate;
    private String realizedDate;
    private String branch;

    public Cheque(double chequeVal, String chequeNum, String bank, String realizedDate, String branch) {
        this.chequeVal = chequeVal;
        this.chequeNum = chequeNum;
        this.bank = bank;
        this.realizedDate = realizedDate;
        this.branch = branch;
    }

    public Cheque(int chequeVal, String chequeNum, String bank, String collectionDate, String realizedDate) {
        this.chequeVal = chequeVal;
        this.chequeNum = chequeNum;
        this.bank = bank;
        this.collectionDate = collectionDate;
        this.realizedDate = realizedDate;
    }

    public Cheque() {
        this.chequeVal = 0;
        this.chequeNum = "";
        this.bank = "";
        this.collectionDate = "";
        this.realizedDate = "";
    }

    protected Cheque(Parcel in) {
        chequeVal = in.readDouble();
        chequeNum = in.readString();
        bank = in.readString();
        collectionDate = in.readString();
        realizedDate = in.readString();
        branch = in.readString();
    }

    public double getChequeVal() {
        return chequeVal;
    }

    public void setChequeVal(int chequeVal) {
        this.chequeVal = chequeVal;
    }

    public void setChequeVal(double chequeVal) {
        this.chequeVal = chequeVal;
    }

    public String getChequeNum() {
        return chequeNum;
    }

    public void setChequeNum(String chequeNum) {
        this.chequeNum = chequeNum;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(String collectionDate) {
        this.collectionDate = collectionDate;
    }

    public String getRealizedDate() {
        return realizedDate;
    }

    public void setRealizedDate(String realizedDate) {
        this.realizedDate = realizedDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(chequeVal);
        dest.writeString(chequeNum);
        dest.writeString(bank);
        dest.writeString(collectionDate);
        dest.writeString(realizedDate);
        dest.writeString(branch);
    }
}
