package model;

/**
 * Created by DELL on 3/10/2017.
 */

public class Tr_TabStock {
    private String id;
    private String principle;
    private String brand;
    private String itemCode;
    private String description;
    private String batchNumber;
    private String expireyDate;
    private float sellingPrice;
    private float retailPrice;
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

    public float getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(float sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public float getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(float retailPrice) {
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
}
