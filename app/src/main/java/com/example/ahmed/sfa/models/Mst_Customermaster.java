package com.example.ahmed.sfa.models;

/**
 * Created by DELL on 3/12/2017.
 */

public class Mst_Customermaster {

    private  String CustomerNo="default";
    private  String CustomerName="default";
    private  String  Address="default";
    private  String Town="default";
    private  String Telephone="default";
    private  String RouteName="default";
    ///*newly added when webservice is being done*/
    private  String districtID="test";
    private  String district="test";
    private  String areaID="test";
    private  String area="test";
    private  String fax="default";
    private  String email="default";
    private  String brNo="default";
    private  String ownerContactNo="default";
    private  String ownerName="default";
    private  String phamacyRegNo="default";
    private  double creditLimit=0.0;
    private  double currentCreditAmount=0.0;
    private  String customerStatusID="default";
    private  String customerStatus="default";
    private  String insertDate="default";
    private  String routeID="default";
    private  String imageID="default";
    private  double latitude=0.0;
    private  double longitude=0.0;
    private  String companyCode="default";
    private  int isActive=0;
    private  String lastUpdateDate;
    private int isCashCustomer = 0;

    public String getCustomerNo() {
        return CustomerNo;
    }

    public void setCustomerNo(String customerNo) {
        CustomerNo = customerNo;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getTown() {
        return Town;
    }

    public void setTown(String town) {
        Town = town;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    public String getRouteName() {
        return RouteName;
    }

    public void setRouteName(String routeName) {
        RouteName = routeName;
    }

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

    public String getAreaID() {
        return areaID;
    }

    public void setAreaID(String areaID) {
        this.areaID = areaID;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBrNo() {
        return brNo;
    }

    public void setBrNo(String brNo) {
        this.brNo = brNo;
    }

    public String getOwnerContactNo() {
        return ownerContactNo;
    }

    public void setOwnerContactNo(String ownerContactNo) {
        this.ownerContactNo = ownerContactNo;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPhamacyRegNo() {
        return phamacyRegNo;
    }

    public void setPhamacyRegNo(String phamacyRegNo) {
        this.phamacyRegNo = phamacyRegNo;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public double getCurrentCreditAmount() {
        return currentCreditAmount;
    }

    public void setCurrentCreditAmount(double currentCreditAmount) {
        this.currentCreditAmount = currentCreditAmount;
    }

    public String getCustomerStatusID() {
        return customerStatusID;
    }

    public void setCustomerStatusID(String customerStatusID) {
        this.customerStatusID = customerStatusID;
    }

    public String getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(String customerStatus) {
        this.customerStatus = customerStatus;
    }

    public String getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }

    public String getRouteID() {
        return routeID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
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

    public int getIsCashCustomer() {
        return isCashCustomer;
    }

    public void setIsCashCustomer(int isCashCustomer) {
        this.isCashCustomer = isCashCustomer;
    }
}
