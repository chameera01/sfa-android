package com.example.ahmed.sfa.models;

import java.util.ArrayList;

/**
 * Created by Shani on 20/12/2017.
 */

public class PrincipleQtySummary {

    private int Shelf;
    private int Order;
    private int Free;

    public PrincipleQtySummary(int shelf, int order, int free) {
        Shelf = shelf;
        Order = order;
        Free = free;
    }

    public int getShelf() {
        return Shelf;
    }

    public void setShelf(int shelf) {
        Shelf = shelf;
    }

    public int getOrder() {
        return Order;
    }

    public void setOrder(int order) {
        Order = order;
    }

    public int getFree() {
        return Free;
    }

    public void setFree(int free) {
        Free = free;
    }

    public static PrincipleQtySummary createPrincipleQtySummary(ArrayList<PrincipleQtySummary> data) {
        int freeQty = 0;

        int shelfQty = 0;
        int orderQty = 0;

        for (int i = 0; i < data.size(); i++) {
            freeQty += data.get(i).getFree();

            shelfQty += data.get(i).getShelf();
            orderQty += data.get(i).getOrder();
        }

        return new PrincipleQtySummary(shelfQty, orderQty, freeQty);
    }
}
