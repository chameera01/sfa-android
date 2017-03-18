package com.example.ahmed.sfa.models;

/**
 * Created by DELL on 3/12/2017.
 */

public class Mst_Customermaster {
    private  String CustomerNo;
    private  String CustomerName;
    private  String  Address;
    private  String Town;
    private  String Telephone;
    private  String RouteName;

    public String getCustomerNo() {
        return CustomerNo;
    }

    public void setCustomerNo(String customerNo) {
        CustomerNo = customerNo;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getTown() {
        return Town;
    }

    public void setTown(String town) {
        Town = town;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    public String getRouteName() {
        return RouteName;
    }

    public void setRouteName(String routeName) {
        RouteName = routeName;
    }
}
