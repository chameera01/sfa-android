package com.example.ahmed.sfa.models;

import java.util.Date;

/**
 * Created by DELL on 3/4/2017.
 */
public class Mst_ProductMaster {
    private String id;
    private String itemCode;
    private String description;
    private String principleId;
    private String principle;
    private String brandId;
    private String brand;
    private String subBrandId;
    private String subBrand;
    private int unitSize;
    private String unitName;
    private double retailPrice;
    private double sellingPrice;
    private double buyingPrice;
    private int active;
    private Date lastupadateDate;
    private int targetAllow;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPrincipleId() {
        return principleId;
    }

    public void setPrincipleId(String principleId) {
        this.principleId = principleId;
    }

    public String getPrinciple() {
        return principle;
    }

    public void setPrinciple(String getPrinciple) {
        this.principle = getPrinciple;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSubBrandId() {
        return subBrandId;
    }

    public void setSubBrandId(String subBrandId) {
        this.subBrandId = subBrandId;
    }

    public String getSubBrand() {
        return subBrand;
    }

    public void setSubBrand(String SubBrand) {
        this.subBrand = SubBrand;
    }

    public int getUnitSize() {
        return unitSize;
    }

    public void setUnitSize(int unitSize) {
        this.unitSize = unitSize;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(float sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public double getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(double buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public Date getLastupadateDate() {
        return lastupadateDate;
    }

    public void setLastupadateDate(Date lastupadateDate) {
        this.lastupadateDate = lastupadateDate;
    }

    public int getTargetAllow() {
        return targetAllow;
    }

    public void setTargetAllow(int targetAllow) {
        this.targetAllow = targetAllow;
    }
}
