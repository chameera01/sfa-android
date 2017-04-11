package com.example.ahmed.sfa.models;

/**
 * Created by DELL on 4/3/2017.
 */
public class Mst_ProductBrandManagement {
    private String brandID;
    private String principleID;
    private String principle;
    private String mainBrand;
    private int  active;
    private String lastUpdateDate;


    public String getBrandID() {
        return brandID;
    }

    public void setBrandID(String brandID) {
        this.brandID = brandID;
    }

    public String getPrincipleID() {
        return principleID;
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

    public String getMainBrand() {
        return mainBrand;
    }

    public void setMainBrand(String mainBrand) {
        this.mainBrand = mainBrand;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}
