package com.example.ahmed.sfa.models;

/**
 * Created by DELL on 10/22/2017.
 */
public class RepStock {

    private  String productCode;
    private  String batchCode;
    private  int  quantity;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

