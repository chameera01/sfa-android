package com.example.ahmed.sfa.models;

/**
 * Created by DELL on 3/10/2017.
 */

public class Tr_TabStock {
    private String serverId;
    private String id;
    private String principle;
    private String principleID;
    private String brand;
    private String brandId;
    private String itemCode;
    private String description;
    private String batchNumber;
    private String expireyDate;
    private double sellingPrice;
    private double retailPrice;
    private  int quantity;
    private String lastupadateDate;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrinciple() {
        return principle;
    }

    public void setPrinciple(String principle) {
        this.principle = principle;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getExpireyDate() {
        return expireyDate;
    }

    public void setExpireyDate(String expireyDate) {
        this.expireyDate = expireyDate;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getLastupadateDate() {
        return lastupadateDate;
    }

    public void setLastupadateDate(String lastupadateDate) {
        this.lastupadateDate = lastupadateDate;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getPrincipleID() {
        return principleID;
    }

    public void setPrincipleID(String principleID) {
        this.principleID = principleID;
    }
}
