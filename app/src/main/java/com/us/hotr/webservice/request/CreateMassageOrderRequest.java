package com.us.hotr.webservice.request;

import java.io.Serializable;

/**
 * Created by liaobo on 2018/2/1.
 */

public class CreateMassageOrderRequest implements Serializable {
    private long massagist_id;
    private long massage_id;
    private long coupon_user_id;
    private long product_id;
    private long coupon_id;
    private int order_product_sum;
    private double actual_payable_price;

    public long getMassagist_id() {
        return massagist_id;
    }

    public void setMassagist_id(long massagist_id) {
        this.massagist_id = massagist_id;
    }

    public long getMassage_id() {
        return massage_id;
    }

    public void setMassage_id(long massage_id) {
        this.massage_id = massage_id;
    }

    public long getCoupon_user_id() {
        return coupon_user_id;
    }

    public void setCoupon_user_id(long coupon_user_id) {
        this.coupon_user_id = coupon_user_id;
    }

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }

    public long getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(long coupon_id) {
        this.coupon_id = coupon_id;
    }

    public int getOrder_product_sum() {
        return order_product_sum;
    }

    public void setOrder_product_sum(int order_product_sum) {
        this.order_product_sum = order_product_sum;
    }

    public double getActual_payable_price() {
        return actual_payable_price;
    }

    public void setActual_payable_price(double actual_payable_price) {
        this.actual_payable_price = actual_payable_price;
    }
}
