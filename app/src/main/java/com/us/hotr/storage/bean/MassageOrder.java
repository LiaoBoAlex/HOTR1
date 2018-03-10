package com.us.hotr.storage.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liaobo on 2018/2/1.
 */

public class MassageOrder implements Serializable {

    private String city_name;
    private String order_cancel_detail;
    @SerializedName(value = "product_img", alternate = {"product_main_img"})
    private String product_img;
    private double coupon_price;
    private String order_code;
    private long id;
    @SerializedName(value = "order_product_sum", alternate = {"orderProductSum"})
    private int order_product_sum;
    private String numerical_order;
    private String area_name;
    private String order_payment_time;
    private int order_payment_state;
    private String massage_name;
    private String order_cancel_time;
    private int order_account_type;
    @SerializedName(value = "sum_price", alternate = {"sumPrice"})
    private double sum_price;
    private String order_create_time;
    private long massage_id;
    private String massagist_name;
    private String massage_address;
    private String product_name_usp;
    private double lat;
    private double lon;
    private long product_id;
    private double online_price;

    public double getOnline_price() {
        return online_price;
    }

    public void setOnline_price(double online_price) {
        this.online_price = online_price;
    }

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getProduct_name_usp() {
        return product_name_usp;
    }

    public void setProduct_name_usp(String product_name_usp) {
        this.product_name_usp = product_name_usp;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getOrder_cancel_detail() {
        return order_cancel_detail;
    }

    public void setOrder_cancel_detail(String order_cancel_detail) {
        this.order_cancel_detail = order_cancel_detail;
    }

    public String getProduct_img() {
        return product_img;
    }

    public void setProduct_img(String product_img) {
        this.product_img = product_img;
    }

    public double getCoupon_price() {
        return coupon_price;
    }

    public void setCoupon_price(double coupon_price) {
        this.coupon_price = coupon_price;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getOrder_product_sum() {
        return order_product_sum;
    }

    public void setOrder_product_sum(int order_product_sum) {
        this.order_product_sum = order_product_sum;
    }

    public String getNumerical_order() {
        return numerical_order;
    }

    public void setNumerical_order(String numerical_order) {
        this.numerical_order = numerical_order;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public String getOrder_payment_time() {
        return order_payment_time;
    }

    public void setOrder_payment_time(String order_payment_time) {
        this.order_payment_time = order_payment_time;
    }

    public int getOrder_payment_state() {
        return order_payment_state;
    }

    public void setOrder_payment_state(int order_payment_state) {
        this.order_payment_state = order_payment_state;
    }

    public String getMassage_name() {
        return massage_name;
    }

    public void setMassage_name(String massage_name) {
        this.massage_name = massage_name;
    }

    public String getOrder_cancel_time() {
        return order_cancel_time;
    }

    public void setOrder_cancel_time(String order_cancel_time) {
        this.order_cancel_time = order_cancel_time;
    }

    public int getOrder_account_type() {
        return order_account_type;
    }

    public void setOrder_account_type(int order_account_type) {
        this.order_account_type = order_account_type;
    }

    public double getSum_price() {
        return sum_price;
    }

    public void setSum_price(double sum_price) {
        this.sum_price = sum_price;
    }

    public String getOrder_create_time() {
        return order_create_time;
    }

    public void setOrder_create_time(String order_create_time) {
        this.order_create_time = order_create_time;
    }

    public long getMassage_id() {
        return massage_id;
    }

    public void setMassage_id(long massage_id) {
        this.massage_id = massage_id;
    }

    public String getMassagist_name() {
        return massagist_name;
    }

    public void setMassagist_name(String massagist_name) {
        this.massagist_name = massagist_name;
    }

    public String getMassage_address() {
        return massage_address;
    }

    public void setMassage_address(String massage_address) {
        this.massage_address = massage_address;
    }
}
