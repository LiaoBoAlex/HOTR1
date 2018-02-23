package com.us.hotr.storage.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liaobo on 2017/12/12.
 */

public class Massage implements Serializable {
    @SerializedName(value = "key", alternate = {"id"})
    private long key;
    private int activityCount;
    @SerializedName(value = "activityPrice", alternate = {"activity_price"})
    private double activityPrice;
    private String beginTime;
    private String endTime;
    //        private int massageId;
    @SerializedName(value = "onlinePrice", alternate = {"online_price"})
    private double onlinePrice;
    private String productCode;
    private String productDetail;
    @SerializedName(value = "productImg", alternate = {"product_img"})
    private String productImg;
    @SerializedName(value = "productName", alternate = {"product_name"})
    private String productName;
    @SerializedName(value = "productType", alternate = {"product_type"})
    private int productType;
    @SerializedName(value = "productUsp", alternate = {"product_usp"})
    private String productUsp;
    @SerializedName(value = "serviceTime", alternate = {"service_time"})
    private int serviceTime;
    @SerializedName(value = "shopPrice", alternate = {"shop_price"})
    private double shopPrice;
    private int singleCount;
    private int typeId;
    private int indate;
    private String usableTime;
    private int order_num;
    @SerializedName(value = "product_main_img", alternate = {"main_img"})
    private String product_main_img;
    private String massage_name;

    public int getOrder_num() {
        return order_num;
    }

    public void setOrder_num(int order_num) {
        this.order_num = order_num;
    }

    public String getProduct_main_img() {
        return product_main_img;
    }

    public void setProduct_main_img(String product_main_img) {
        this.product_main_img = product_main_img;
    }

    public String getMassage_name() {
        return massage_name;
    }

    public void setMassage_name(String massage_name) {
        this.massage_name = massage_name;
    }

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public int getActivityCount() {
        return activityCount;
    }

    public void setActivityCount(int activityCount) {
        this.activityCount = activityCount;
    }

    public double getActivityPrice() {
        return activityPrice;
    }

    public void setActivityPrice(double activityPrice) {
        this.activityPrice = activityPrice;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

//        public int getMassageId() {
//            return massageId;
//        }
//
//        public void setMassageId(int massageId) {
//            this.massageId = massageId;
//        }

    public double getOnlinePrice() {
        return onlinePrice;
    }

    public void setOnlinePrice(double onlinePrice) {
        this.onlinePrice = onlinePrice;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(String productDetail) {
        this.productDetail = productDetail;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public String getProductUsp() {
        return productUsp;
    }

    public void setProductUsp(String productUsp) {
        this.productUsp = productUsp;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public double getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(double shopPrice) {
        this.shopPrice = shopPrice;
    }

    public int getSingleCount() {
        return singleCount;
    }

    public void setSingleCount(int singleCount) {
        this.singleCount = singleCount;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getIndate() {
        return indate;
    }

    public void setIndate(int indate) {
        this.indate = indate;
    }

    public String getUsableTime() {
        return usableTime;
    }

    public void setUsableTime(String usableTime) {
        this.usableTime = usableTime;
    }
}
