package com.example.ahmed.sfa.models;

/**
 * Created by Shani on 18/12/2017.
 */

public class CollectionNoteHeader {

    private String collectionNoteNo;
    private String collectedDate;
    private String type;
    private String customerNo;
    private int isUpload;

    public String getCollectionNoteNo() {
        return collectionNoteNo;
    }

    public void setCollectionNoteNo(String collectionNoteNo) {
        this.collectionNoteNo = collectionNoteNo;
    }

    public String getCollectedDate() {
        return collectedDate;
    }

    public void setCollectedDate(String collectedDate) {
        this.collectedDate = collectedDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public int getIsUpload() {
        return isUpload;
    }

    public void setIsUpload(int isUpload) {
        this.isUpload = isUpload;
    }
}
