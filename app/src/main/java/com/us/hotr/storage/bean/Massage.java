package com.us.hotr.storage.bean;

import java.io.Serializable;

/**
 * Created by liaobo on 2017/12/12.
 */

public class Massage implements Serializable {
    private int service_time;
    private int id;
    private int online_price;
    private int shop_price;
    private int activity_price;
    private int order_num;
    private String product_main_img;
    private int product_type;
    private String massage_name;
    private String product_name;
    private String product_usp;

    public int getService_time() {
        return service_time;
    }

    public void setService_time(int service_time) {
        this.service_time = service_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOnline_price() {
        return online_price;
    }

    public void setOnline_price(int online_price) {
        this.online_price = online_price;
    }

    public int getShop_price() {
        return shop_price;
    }

    public void setShop_price(int shop_price) {
        this.shop_price = shop_price;
    }

    public int getActivity_price() {
        return activity_price;
    }

    public void setActivity_price(int activity_price) {
        this.activity_price = activity_price;
    }

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

    public int getProduct_type() {
        return product_type;
    }

    public void setProduct_type(int product_type) {
        this.product_type = product_type;
    }

    public String getMassage_name() {
        return massage_name;
    }

    public void setMassage_name(String massage_name) {
        this.massage_name = massage_name;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_usp() {
        return product_usp;
    }

    public void setProduct_usp(String product_usp) {
        this.product_usp = product_usp;
    }
}
