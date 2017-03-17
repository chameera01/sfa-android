package com.example.ahmed.sfa.models;

/**
 * Created by Ahmed on 3/6/2017.
 */

public class Route {
    private String routeID;
    private String route;

    public Route(String routeID, String route) {
        this.routeID = routeID;
        this.route = route;
    }

    @Override
    public String toString() {
        return route;
    }

    public String getRouteID() {
        return routeID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }
}
