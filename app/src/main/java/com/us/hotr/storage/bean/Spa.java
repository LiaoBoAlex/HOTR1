package com.us.hotr.storage.bean;

import java.io.Serializable;

/**
 * Created by liaobo on 2017/12/12.
 */

public class Spa implements Serializable {
    private String landmark_name;
    private int id;
    private String massage_main_img;
    private int order_num;
    private String massage_name;

    public String getAddress() {
        return landmark_name;
    }

    public void setAddress(String address) {
        this.landmark_name = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMassage_main_img() {
        return massage_main_img;
    }

    public void setMassage_main_img(String massage_main_img) {
        this.massage_main_img = massage_main_img;
    }

    public int getOrder_num() {
        return order_num;
    }

    public void setOrder_num(int order_num) {
        this.order_num = order_num;
    }

    public String getMassage_name() {
        return massage_name;
    }

    public void setMassage_name(String massage_name) {
        this.massage_name = massage_name;
    }
}
