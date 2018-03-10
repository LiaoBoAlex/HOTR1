package com.us.hotr.storage.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liaobo on 2018/1/26.
 */

public class ProductOrder implements Serializable {
    @SerializedName(value = "sum_price", alternate = {"sumPrice"})
    private double sum_price;
    private long id;
    @SerializedName(value = "order_create_time", alternate = {"createTime"})
    private String order_create_time;
    @SerializedName(value = "order_product_sum", alternate = {"productSum"})
    private int order_product_sum;
    private String order_cancel_detail;
    @SerializedName(value = "order_payment_state", alternate = {"paymentStatus"})
    private int order_payment_state;
    @SerializedName(value = "order_cancel_time", alternate = {"cancelTime"})
    private String order_cancel_time;
    private String order_payment_time;
    private String product_name_usp;
    private String city_name;
    private List<Promise> promiseList;
    private double coupon_price;
    @SerializedName(value = "order_code", alternate = {"orderCode"})
    private String order_code;
    private String actual_delivery;
    private String numerical_order;
    private String hospital_name;
    private String area_name;
    private String hospital_address;
    private int order_account_type;
    private double pay_at_shop;
    private String province_name;
    @SerializedName(value = "hospital_id", alternate = {"hospitalId"})
    private long hospital_id;
    @SerializedName(value = "product_main_img", alternate = {"product_img"})
    private String product_main_img;
    private long userId;
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    public String getProduct_main_img() {
        return product_main_img;
    }

    public void setProduct_main_img(String product_main_img) {
        this.product_main_img = product_main_img;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public List<Promise> getPromiseList() {
        return promiseList;
    }

    public void setPromiseList(List<Promise> promiseList) {
        this.promiseList = promiseList;
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

    public String getActual_delivery() {
        return actual_delivery;
    }

    public void setActual_delivery(String actual_delivery) {
        this.actual_delivery = actual_delivery;
    }

    public String getNumerical_order() {
        return numerical_order;
    }

    public void setNumerical_order(String numerical_order) {
        this.numerical_order = numerical_order;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public String getHospital_address() {
        return hospital_address;
    }

    public void setHospital_address(String hospital_address) {
        this.hospital_address = hospital_address;
    }

    public int getOrder_account_type() {
        return order_account_type;
    }

    public void setOrder_account_type(int order_account_type) {
        this.order_account_type = order_account_type;
    }

    public double getPay_at_shop() {
        return pay_at_shop;
    }

    public void setPay_at_shop(double pay_at_shop) {
        this.pay_at_shop = pay_at_shop;
    }

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    public long getHospital_id() {
        return hospital_id;
    }

    public void setHospital_id(long hospital_id) {
        this.hospital_id = hospital_id;
    }

    public double getSum_price() {
        return sum_price;
    }

    public void setSum_price(double sum_price) {
        this.sum_price = sum_price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrder_create_time() {
        return order_create_time;
    }

    public void setOrder_create_time(String order_create_time) {
        this.order_create_time = order_create_time;
    }

    public int getOrder_product_sum() {
        return order_product_sum;
    }

    public void setOrder_product_sum(int order_product_sum) {
        this.order_product_sum = order_product_sum;
    }

    public String getOrder_cancel_detail() {
        return order_cancel_detail;
    }

    public void setOrder_cancel_detail(String order_cancel_detail) {
        this.order_cancel_detail = order_cancel_detail;
    }

    public int getOrder_payment_state() {
        return order_payment_state;
    }

    public void setOrder_payment_state(int order_payment_state) {
        this.order_payment_state = order_payment_state;
    }

    public String getOrder_cancel_time() {
        return order_cancel_time;
    }

    public void setOrder_cancel_time(String order_cancel_time) {
        this.order_cancel_time = order_cancel_time;
    }

    public String getOrder_payment_time() {
        return order_payment_time;
    }

    public void setOrder_payment_time(String order_payment_time) {
        this.order_payment_time = order_payment_time;
    }

    public String getProduct_name_usp() {
        return product_name_usp;
    }

    public void setProduct_name_usp(String product_name_usp) {
        this.product_name_usp = product_name_usp;
    }

    public class Promise implements Serializable{
        private String promise_title;

        public String getPromise_title() {
            return promise_title;
        }

        public void setPromise_title(String promise_title) {
            this.promise_title = promise_title;
        }
    }
}
