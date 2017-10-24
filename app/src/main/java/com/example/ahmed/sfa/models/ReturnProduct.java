package com.example.ahmed.sfa.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DELL on 10/25/2017.
 * For invoice gen
 */
public class ReturnProduct implements Parcelable {

    public static final Parcelable.Creator<ReturnProduct> CREATOR = new Parcelable.Creator<ReturnProduct>() {
        public ReturnProduct createFromParcel(Parcel in) {
            return new ReturnProduct(in);
        }

        public ReturnProduct[] newArray(int size) {
            return new ReturnProduct[size];
        }
    };
    private String description;
    private String batch;
    private String invoiceNumber;
    private int quantity;
    private int free;
    private double unitPrice;
    private double discount;
    private String issueMode;
    private double returnValue;
    private String productId;
    private String returnValidated;

    public ReturnProduct(Parcel source) {
        /*
		 * Reconstruct from the Parcel
		 */
        description = source.readString();
        batch = source.readString();
        invoiceNumber = source.readString();
        quantity = source.readInt();
        free = source.readInt();
        unitPrice = source.readDouble();
        discount = source.readDouble();
        issueMode = source.readString();
        returnValue = source.readDouble();
        productId = source.readString();
        returnValidated = source.readString();

    }

    public ReturnProduct() {
        // TODO Auto-generated constructor stub
    }

    public String getReturnValidated() {
        return returnValidated;
    }

    public void setReturnValidated(String returnValidated) {
        this.returnValidated = returnValidated;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(description);
        dest.writeString(batch);
        dest.writeString(invoiceNumber);
        dest.writeInt(quantity);
        dest.writeInt(free);
        dest.writeDouble(unitPrice);
        dest.writeDouble(discount);
        dest.writeString(issueMode);
        dest.writeDouble(returnValue);
        dest.writeString(productId);
        dest.writeString(returnValidated);

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getIssueMode() {
        return issueMode;
    }

    public void setIssueMode(String issueMode) {
        this.issueMode = issueMode;
    }

    public double getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(double returnValue) {
        this.returnValue = returnValue;
    }

}

