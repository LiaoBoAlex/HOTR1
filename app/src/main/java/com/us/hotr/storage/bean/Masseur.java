package com.us.hotr.storage.bean;

import java.io.Serializable;

/**
 * Created by liaobo on 2017/12/12.
 */

public class Masseur implements Serializable {
    private String massagist_main_img;
    private int id;
    private int order_num;
    private String massagist_name;
    private String landmark_name;

    public String getMassagist_main_img() {
        return massagist_main_img;
    }

    public void setMassagist_main_img(String massagist_main_img) {
        this.massagist_main_img = massagist_main_img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder_num() {
        return order_num;
    }

    public void setOrder_num(int order_num) {
        this.order_num = order_num;
    }

    public String getMassagist_name() {
        return massagist_name;
    }

    public void setMassagist_name(String massagist_name) {
        this.massagist_name = massagist_name;
    }

    public String getAddress() {
        return landmark_name;
    }

    public void setAddress(String address) {
        this.landmark_name = address;
    }
}
