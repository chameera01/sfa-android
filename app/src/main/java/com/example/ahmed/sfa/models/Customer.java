package com.example.ahmed.sfa.models;

import android.database.Cursor;

/**
 * Created by Ahmed on 3/3/2017.
 */

public class Customer {
    private String customerNo;
    private String name;
    private String town;
    private String address;
    private String tel;
    private String lastInvoiceNo;
    private String lastInvoiceCredit;
    private String lastInvoiceTotal;
    private String lastInvoiceDate;
    private String imagePath;

    public Customer(String customerNo, String name, String town, String address, String tel, String lastInvoiceNo, String lastInvoiceCredit, String lastInvoiceTotal, String lastInvoiceDate, String imagePath) {
        this.customerNo = customerNo;
        this.name = name;
        this.town = town;
        this.address = address;
        this.tel = tel;
        this.lastInvoiceNo = lastInvoiceNo;
        this.lastInvoiceCredit = lastInvoiceCredit;
        this.lastInvoiceTotal = lastInvoiceTotal;
        this.lastInvoiceDate = lastInvoiceDate;
        this.imagePath = imagePath;
    }

    public Customer(Cursor cursor){
        if(cursor.moveToNext()){
            this.customerNo = cursor.getString(0);
            this.name = cursor.getString(1);
            this.town = cursor.getString(2);
            this.address = cursor.getString(3);
            this.tel = cursor.getString(4);
            this.lastInvoiceNo = cursor.getString(5);
            this.lastInvoiceCredit = cursor.getString(6);
            this.lastInvoiceTotal = cursor.getString(7);
            this.lastInvoiceDate = cursor.getString(8);
            this.imagePath = cursor.getString(9);
        }
    }


    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getLastInvoiceNo() {
        return lastInvoiceNo;
    }

    public void setLastInvoiceNo(String lastInvoiceNo) {
        this.lastInvoiceNo = lastInvoiceNo;
    }

    public String getLastInvoiceCredit() {
        return lastInvoiceCredit;
    }

    public void setLastInvoiceCredit(String lastInvoiceCredit) {
        this.lastInvoiceCredit = lastInvoiceCredit;
    }

    public String getLastInvoiceTotal() {
        return lastInvoiceTotal;
    }

    public void setLastInvoiceTotal(String lastInvoiceTotal) {
        this.lastInvoiceTotal = lastInvoiceTotal;
    }

    public String getLastInvoiceDate() {
        return lastInvoiceDate;
    }

    public void setLastInvoiceDate(String lastInvoiceDate) {
        this.lastInvoiceDate = lastInvoiceDate;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
