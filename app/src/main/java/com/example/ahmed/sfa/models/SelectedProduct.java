package com.example.ahmed.sfa.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by DELL on 10/25/2017.
 *
 * for invoice gen
 */
public class SelectedProduct implements Parcelable {
    public static final Parcelable.Creator<SelectedProduct> CREATOR = new Parcelable.Creator<SelectedProduct>() {
        public SelectedProduct createFromParcel(Parcel in) {
            Log.d("", "createFromParcel()");
            return new SelectedProduct(in);
        }

        public SelectedProduct[] newArray(int size) {
            Log.d("", "createFromParcel() newArray ");
            return new SelectedProduct[size];
        }
    };
    private int rowId;
    private String productId;
    private String productCode;
    private String productBatch;
    private int quantity;
    private String expiryDate;
    private String timeStamp;
    private int requestedQuantity;
    private int free;
    private int normal;
    private double discount;
    private int shelfQuantity;
    private double price;
    private String productDescription;
    private double total;

    public SelectedProduct(Parcel source) {
        /*
		 * Reconstruct from the Parcel
		 */
        Log.v("", "ParcelData(Parcel source): time to put back parcel data");


        rowId = source.readInt();
        productId = source.readString();
        productCode = source.readString();
        productBatch = source.readString();
        quantity = source.readInt();
        expiryDate = source.readString();
        timeStamp = source.readString();

        requestedQuantity = source.readInt();
        free = source.readInt();
        normal = source.readInt();
        discount = source.readDouble();
        shelfQuantity = source.readInt();

        productDescription = source.readString();
        price = source.readDouble();


    }

    public SelectedProduct() {
        // TODO Auto-generated constructor stub
    }

    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        Log.v("Log", "writeToParcel..." + flags);

        dest.writeInt(rowId);
        dest.writeString(productId);
        dest.writeString(productCode);
        dest.writeString(productBatch);
        dest.writeInt(quantity);
        dest.writeString(expiryDate);
        dest.writeString(timeStamp);

        dest.writeInt(requestedQuantity);
        dest.writeInt(free);
        dest.writeInt(normal);
        dest.writeDouble(discount);
        dest.writeInt(shelfQuantity);

        dest.writeString(productDescription);
        dest.writeDouble(price);

    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductBatch() {
        return productBatch;
    }

    public void setProductBatch(String productBatch) {
        this.productBatch = productBatch;
    }

    public int getRequestedQuantity() {
        return requestedQuantity;
    }

    public void setRequestedQuantity(int quantity) {
        this.requestedQuantity = quantity;
    }

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
    }

    public int getNormal() {
        return normal;
    }

    public void setNormal(int normal) {
        this.normal = normal;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public int getShelfQuantity() {
        return shelfQuantity;
    }

    public void setShelfQuantity(int shelfQuantity) {
        this.shelfQuantity = shelfQuantity;
    }

    /**
     * @return the total
     */
    public double getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(double total) {
        this.total = total;
    }

}
