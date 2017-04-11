package com.example.ahmed.sfa.models;

/**
 * Created by DELL on 4/10/2017.
 */
public class Mst_Territory {
    //ID | TerritoryID | Territory | IsActive | LastUpdateDate 	*/
    private String territory_id;
    private String territory;
    private int isActive;
    private String lastUpdateDate;


    public String getTerritory_id() {
        return territory_id;
    }

    public void setTerritory_id(String territory_id) {
        this.territory_id = territory_id;
    }

    public String getTerritory() {
        return territory;
    }

    public void setTerritory(String territory) {
        this.territory = territory;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}
