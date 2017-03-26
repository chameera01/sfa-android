package com.example.ahmed.sfa.models;

/**
 * Created by Ahmed on 3/21/2017.
 */

public class Principle {
    private String id;
    private String principle;

    public Principle(String id, String principle) {
        this.id = id;
        this.principle = principle;
    }

    public String getPrinciple() {
        return principle;
    }

    public void setPrinciple(String principle) {
        this.principle = principle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;

    }

    @Override
    public String toString() {
        return getPrinciple();
    }
}
