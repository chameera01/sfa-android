package com.example.ahmed.sfa.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.DecimalFormat;

/**
 * Created by Ahmed on 3/25/2017.
 */

public class SalesPayment implements Parcelable{
    private double fullInvDisc;//this is the total invoice descount rate
    private double discount;//this is the discount comes from the invoice
    private double totalDiscount;//this is the total of the discounts
    private double totalPrincipleDiscounts;//this is the discount comes from
    //discounts accounted for specific principle types

    private double credit;
    private double cheque;
    private double cash;

    private double subTotal;
    private int invQty;
    private int freeQty;

    private int creditDays;

    private double returnTot;
    private int returnQty;

    private  double total;

    public SalesPayment( double total, double subTotal, int invQty, int freeQty) {
        //this.fullInvDisc = fullInvDisc;
        this.total = total;
        this.credit = credit;
        this.subTotal = subTotal;
        this.invQty = invQty;
        this.freeQty = freeQty;
        cheque=0;
        cash=0;
        returnTot=0;
        returnQty=0;
        calculateFields();
    }

    public SalesPayment(SalesInvoiceSummary summary){
        this.fullInvDisc = 0.0;
        this.discount = summary.getDiscount();
        this.total = summary.getTotal();
        this.subTotal =summary.getSubtotal();
        this.invQty = summary.getInvoicedQty();
        this.freeQty = summary.getFreeQty();

        cheque=0;
        cash=0;
        returnTot=0;
        returnQty=0;
        creditDays=0;
        calculateFields();
    }

    protected SalesPayment(Parcel in) {
        fullInvDisc = in.readDouble();
        discount = in.readDouble();
        totalDiscount = in.readDouble();
        credit = in.readDouble();
        cheque = in.readDouble();
        cash = in.readDouble();
        subTotal = in.readDouble();
        invQty = in.readInt();
        freeQty = in.readInt();
        returnTot = in.readDouble();
        returnQty = in.readInt();
        total = in.readDouble();
        creditDays = in.readInt();
        totalPrincipleDiscounts  = in.readDouble();
    }

    public int getCreditDays() {
        return creditDays;
    }

    public void setCreditDays(int creditDays) {
        this.creditDays = creditDays;
    }

    public static final Creator<SalesPayment> CREATOR = new Creator<SalesPayment>() {
        @Override
        public SalesPayment createFromParcel(Parcel in) {
            return new SalesPayment(in);
        }

        @Override
        public SalesPayment[] newArray(int size) {
            return new SalesPayment[size];
        }
    };

    private void calculateFields(){

        totalDiscount = discount + (subTotal*fullInvDisc/100)+ totalPrincipleDiscounts;
        total = subTotal-totalDiscount-returnTot;
        credit = total-cash-cheque;
    }

    public double getFullInvDisc() {
        DecimalFormat format = new DecimalFormat("0.2f");
        return Double.parseDouble(format.format(fullInvDisc));
        //return fullInvDisc;
    }

    public void setFullInvDisc(double fullInvDisc) {
        this.fullInvDisc = fullInvDisc;
        calculateFields();
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;

    }

    public double getCheque() {
        return cheque;
    }

    public void setCheque(double cheque) {
        this.cheque = cheque;
        calculateFields();
    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
        calculateFields();
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
        calculateFields();
    }

    public int getInvQty() {
        return invQty;
    }

    public void setInvQty(int invQty) {
        this.invQty = invQty;
    }

    public int getFreeQty() {
        return freeQty;
    }

    public void setFreeQty(int freeQty) {
        this.freeQty = freeQty;
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

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public double getTotalPrincipleDiscounts() {
        return totalPrincipleDiscounts;
    }

    public void setTotalPrincipleDiscounts(double totalPrincipleDiscounts) {
        this.totalPrincipleDiscounts = totalPrincipleDiscounts;
        calculateFields();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(fullInvDisc);
        dest.writeDouble(discount);
        dest.writeDouble(totalDiscount);
        dest.writeDouble(credit);
        dest.writeDouble(cheque);
        dest.writeDouble(cash);
        dest.writeDouble(subTotal);
        dest.writeInt(invQty);
        dest.writeInt(freeQty);
        dest.writeDouble(returnTot);
        dest.writeInt(returnQty);
        dest.writeDouble(total);
        dest.writeInt(creditDays);
        dest.writeDouble(totalPrincipleDiscounts);
    }
}
