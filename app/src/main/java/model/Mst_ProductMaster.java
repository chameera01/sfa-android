package model;

import java.util.Date;

/**
 * Created by DELL on 3/4/2017.
 */
public class Mst_ProductMaster {
    private String id;
    private String itemCode;
    private String description;
    private String principleId;
    private String principle;
    private String brandId;
    private String brand;
    private String subBrandId;
    private String subBrand;
    private int unitSize;
    private String unitName;
    private float retailPrice;
    private float sellingPrice;
    private float buyingPrice;
    private Boolean active;
    private Date lastupadateDate;
    private Boolean targetAllow;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrincipleId() {
        return principleId;
    }

    public void setPrincipleId(String principleId) {
        this.principleId = principleId;
    }

    public String getGetPrinciple() {
        return principle;
    }

    public void setGetPrinciple(String getPrinciple) {
        this.principle = getPrinciple;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSubBrandId() {
        return subBrandId;
    }

    public void setSubBrandId(String subBrandId) {
        this.subBrandId = subBrandId;
    }

    public String getGetSubBrand() {
        return subBrand;
    }

    public void setGetSubBrand(String getSubBrand) {
        this.subBrand = getSubBrand;
    }

    public int getUnitSize() {
        return unitSize;
    }

    public void setUnitSize(int unitSize) {
        this.unitSize = unitSize;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public float getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(float retailPrice) {
        this.retailPrice = retailPrice;
    }

    public float getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(float sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public float getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(float buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Date getLastupadateDate() {
        return lastupadateDate;
    }

    public void setLastupadateDate(Date lastupadateDate) {
        this.lastupadateDate = lastupadateDate;
    }

    public Boolean getTargetAllow() {
        return targetAllow;
    }

    public void setTargetAllow(Boolean targetAllow) {
        this.targetAllow = targetAllow;
    }
}
