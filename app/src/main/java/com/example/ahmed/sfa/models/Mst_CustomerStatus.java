package com.example.ahmed.sfa.models;

/**
 * Created by DELL on 4/4/2017.
 */
public class Mst_CustomerStatus {
    //_id INTEGER PRIMARY KEY AUTOINCREMENT,StatusID TEXT,Status TEXT,isActive INTEGER,LastUpdateDate TEXT
    private String statusId;
    private String status;
    private String isActive;

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}
