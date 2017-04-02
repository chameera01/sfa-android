package com.example.ahmed.sfa.models;

/**
 * Created by DELL on 3/25/2017.
 */

public  class Mst_RepTable{

    private  String repId;
    private String deviceName;
    private  String repName;
    private  String address;
    private  String contactNo;
    private  String dealerName;
    private String dealerAdress;
    private String macAdress;
    private String agentId;
    private int isActive;
    private String lastUpdateDae;

    public String getRepId() {
        return repId;
    }

    public void setRepId(String repId) {
        this.repId = repId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getRepName() {
        return repName;
    }

    public void setRepName(String repName) {
        this.repName = repName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getDealerAdress() {
        return dealerAdress;
    }

    public void setDealerAdress(String dealerAdress) {
        this.dealerAdress = dealerAdress;
    }

    public String getMacAdress() {
        return macAdress;
    }

    public void setMacAdress(String macAdress) {
        this.macAdress = macAdress;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getLastUpdateDae() {
        return lastUpdateDae;
    }

    public void setLastUpdateDae(String lastUpdateDae) {
        this.lastUpdateDae = lastUpdateDae;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }
}
