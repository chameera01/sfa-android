package com.example.ahmed.sfa.models;

/**
 * Created by Ahmed on 3/3/2017.
 */

public class Customer {
    private String customerNo;
    private String name;
    private String town;
    private String address;
    private String tel;
    private String lastInvoice;
    private String lastVisit;
    private String imagePath;

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public Customer(String customerNo, String name, String town, String address, String tel, String lastInvoice, String lastVisit,String imagePath) {
        this.customerNo = customerNo;
        this.name = name;
        this.town = town;
        this.address = address;
        this.tel = tel;
        this.lastInvoice = lastInvoice;
        this.lastVisit = lastVisit;
        this.imagePath=imagePath;
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

    public String getLastInvoice() {
        return lastInvoice;
    }

    public void setLastInvoice(String lastInvoice) {
        this.lastInvoice = lastInvoice;
    }

    public String getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(String lastVisit) {
        this.lastVisit = lastVisit;
    }

    public String getImagePath(){
        return imagePath;
    }
}
