package com.example.ahmed.sfa.models;

/**
 * Created by DELL on 4/10/2017.
 */
public class Mst_Route {

    private String RouteID;
    private String TerritoryID;
    private String Territory;
    private String Route;
    private int isActive;
    private String lastUpdateDate;


    public String getRouteID() {
        return RouteID;
    }

    public void setRouteID(String routeID) {
        RouteID = routeID;
    }

    public String getTerritoryID() {
        return TerritoryID;
    }

    public void setTerritoryID(String territoryID) {
        TerritoryID = territoryID;
    }

    public String getTerritory() {
        return Territory;
    }

    public void setTerritory(String territory) {
        Territory = territory;
    }

    public String getRoute() {
        return Route;
    }

    public void setRoute(String route) {
        Route = route;
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
