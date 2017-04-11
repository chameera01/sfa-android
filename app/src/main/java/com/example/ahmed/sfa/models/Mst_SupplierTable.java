package com.example.ahmed.sfa.models;

/**
 * Created by DELL on 4/3/2017.
 */
public class Mst_SupplierTable {
    //ID | PrincipleID | Principle | Activate | LastUpdateDate
    private  String principleID;
    private  String principle;
    private  int active;
    private  String lastUpdateDate;


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
