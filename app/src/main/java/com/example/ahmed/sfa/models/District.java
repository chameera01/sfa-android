package com.example.ahmed.sfa.models;

/**
 * Created by Ahmed on 3/6/2017.
 */

public class District {
    private String districtID;
    private String district;

    public String getDistrictID() {
        return districtID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public District(String districtID, String district) {

        this.districtID = districtID;
        this.district = district;
    }

    @Override
    public String toString(){
        return district;
    }
}
