package com.us.hotr.webservice.request;

import java.io.Serializable;

/**
 * Created by liaobo on 2018/1/24.
 */

public class CreateProductOrderRequest implements Serializable {
    private long product_id;
    private long hospital_id;
    private int order_product_sum;
    private double actual_payable_price;
    private double pay_at_shop;
    private Long coupon_id;
    private Long coupon_user_id;

    public double getPay_at_shop() {
        return pay_at_shop;
    }

    public void setPay_at_shop(double pay_at_shop) {
        this.pay_at_shop = pay_at_shop;
    }

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }

    public long getHospital_id() {
        return hospital_id;
    }

    public void setHospital_id(long hospital_id) {
        this.hospital_id = hospital_id;
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

    public Long getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(Long coupon_id) {
        this.coupon_id = coupon_id;
    }

    public Long getCoupon_user_id() {
        return coupon_user_id;
    }

    public void setCoupon_user_id(Long coupon_user_id) {
        this.coupon_user_id = coupon_user_id;
    }
}
