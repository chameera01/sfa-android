package com.example.ahmed.sfa.models;

/**
 * Created by Shani on 19/12/2017.
 */

public class CollectionChequeDetails {

    private int headerId;
    private String collectionNoteNo;
    private String repId;
    private String invoiceNo;
    private double chequeAmt;
    private String chequeNo;
    private String bank;
    private String branch;
    private String realizeDate;
    private int isUpload;

    public int getHeaderId() {
        return headerId;
    }

    public void setHeaderId(int headerId) {
        this.headerId = headerId;
    }

    public String getCollectionNoteNo() {
        return collectionNoteNo;
    }

    public void setCollectionNoteNo(String collectionNoteNo) {
        this.collectionNoteNo = collectionNoteNo;
    }

    public String getRepId() {
        return repId;
    }

    public void setRepId(String repId) {
        this.repId = repId;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public double getChequeAmt() {
        return chequeAmt;
    }

    public void setChequeAmt(double chequeAmt) {
        this.chequeAmt = chequeAmt;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getRealizeDate() {
        return realizeDate;
    }

    public void setRealizeDate(String realizeDate) {
        this.realizeDate = realizeDate;
    }

    public int getIsUpload() {
        return isUpload;
    }

    public void setIsUpload(int isUpload) {
        this.isUpload = isUpload;
    }
}
