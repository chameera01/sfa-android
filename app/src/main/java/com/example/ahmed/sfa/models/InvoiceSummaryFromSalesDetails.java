package com.example.ahmed.sfa.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Shani on 10/11/2017.
 */

public class InvoiceSummaryFromSalesDetails implements Parcelable {

    private String code;
    private String product;
    private String batchNo;
    private String unitPrice;
    private String stock;           //can't find where it is
    private String shelf;
    private String request;
    private String order;
    private String free;
    private String lineValue;


    public InvoiceSummaryFromSalesDetails(String code, String product, String batchNo, String unitPrice, String shelf, String request, String order, String free, String lineValue) {
        this.code = code;
        this.product = product;
        this.batchNo = batchNo;
        this.unitPrice = unitPrice;
        this.shelf = shelf;
        this.request = request;
        this.order = order;
        this.free = free;
        this.lineValue = lineValue;
    }

    protected InvoiceSummaryFromSalesDetails(Parcel in) {
        code = in.readString();
        product = in.readString();
        batchNo = in.readString();
        unitPrice = in.readString();
        stock = in.readString();
        shelf = in.readString();
        request = in.readString();
        order = in.readString();
        free = in.readString();
        lineValue = in.readString();
    }

    public static final Creator<InvoiceSummaryFromSalesDetails> CREATOR = new Creator<InvoiceSummaryFromSalesDetails>() {
        @Override
        public InvoiceSummaryFromSalesDetails createFromParcel(Parcel in) {
            return new InvoiceSummaryFromSalesDetails(in);
        }

        @Override
        public InvoiceSummaryFromSalesDetails[] newArray(int size) {
            return new InvoiceSummaryFromSalesDetails[size];
        }
    };

    public String getCode() {
        return code;
    }

    public String getProduct() {
        return product;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public String getShelf() {
        return shelf;
    }

    public String getRequest() {
        return request;
    }

    public String getOrder() {
        return order;
    }

    public String getFree() {
        return free;
    }

    public String getLineValue() {
        return lineValue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(product);
        dest.writeString(batchNo);
        dest.writeString(unitPrice);
        dest.writeString(stock);
        dest.writeString(shelf);
        dest.writeString(request);
        dest.writeString(order);
        dest.writeString(free);
        dest.writeString(lineValue);
    }
}
