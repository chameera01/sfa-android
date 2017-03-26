package com.example.ahmed.sfa.models;

/**
 * Created by Ahmed on 3/21/2017.
 */

public class Brand {
    private String principleID;
    private String brandID;
    private String brand;

    public Brand(String principleID, String brandID, String brand) {
        this.principleID = principleID;
        this.brandID = brandID;
        this.brand = brand;
    }

    public String getPrincipleID() {
        return principleID;
    }

    public void setPrincipleID(String principleID) {
        this.principleID = principleID;
    }

    public String getBrandID() {
        return brandID;
    }

    public void setBrandID(String brandID) {
        this.brandID = brandID;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public String toString() {
        return getBrand();
    }
}
