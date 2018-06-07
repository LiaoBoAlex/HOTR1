package com.us.hotr.storage.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liaobo on 2018/1/5.
 */

public class Voucher implements Serializable {
    private long id;
    private double coupon_money;
//    @SerializedName(value = "use_start_time", alternate = {"coupon_start_time"})
    private String use_start_time;
//    @SerializedName(value = "use_end_time", alternate = {"coupon_end_time"})
    private String use_end_time;
    private String coupon_name;
    private double full_money;
    private long couponUserId;

    public long getCouponUserId() {
        return couponUserId;
    }

    public void setCouponUserId(long couponUserId) {
        this.couponUserId = couponUserId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getCoupon_money() {
        return coupon_money;
    }

    public void setCoupon_money(double coupon_money) {
        this.coupon_money = coupon_money;
    }

    public String getUse_start_time() {
        return use_start_time;
    }

    public void setUse_start_time(String use_start_time) {
        this.use_start_time = use_start_time;
    }

    public String getUse_end_time() {
        return use_end_time;
    }

    public void setUse_end_time(String use_end_time) {
        this.use_end_time = use_end_time;
    }

    public String getCoupon_name() {
        return coupon_name;
    }

    public void setCoupon_name(String coupon_name) {
        this.coupon_name = coupon_name;
    }

    public double getFull_money() {
        return full_money;
    }

    public void setFull_money(double full_money) {
        this.full_money = full_money;
    }
}
