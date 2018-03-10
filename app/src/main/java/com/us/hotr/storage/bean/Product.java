package com.us.hotr.storage.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liaobo on 2017/12/5.
 */

public class Product implements Serializable {

    private String doctor_name;
    private String hospital_name;
    private double online_price;
    private double shop_price;
    private int order_num;
    private String product_name;
    @SerializedName(value = "productId", alternate = {"id"})
    private long productId;
    @SerializedName(value = "product_main_img", alternate = {"main_img"})
    private String product_main_img;
    private String product_img;
    private String product_usp;
    private int product_type;
    private int amount;
    private int payment_type;
    private long hospital_id;
    private long doctor_id;
    private List<Promise> promiseList;
    private long project_id;
    private String project_name;

    public long getProject_id() {
        return project_id;
    }

    public void setProject_id(long project_id) {
        this.project_id = project_id;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public List<Promise> getPromiseList() {
        return promiseList;
    }

    public void setPromiseList(List<Promise> promiseList) {
        this.promiseList = promiseList;
    }

    public long getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(long doctor_id) {
        this.doctor_id = doctor_id;
    }

    public long getHospital_id() {
        return hospital_id;
    }

    public void setHospital_id(long hospital_id) {
        this.hospital_id = hospital_id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(int payment_type) {
        this.payment_type = payment_type;
    }

    public int getProduct_type() {
        return product_type;
    }

    public void setProduct_type(int product_type) {
        this.product_type = product_type;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public double getOnline_price() {
        return online_price;
    }

    public void setOnline_price(double online_price) {
        this.online_price = online_price;
    }

    public double getShop_price() {
        return shop_price;
    }

    public void setShop_price(double shop_price) {
        this.shop_price = shop_price;
    }

    public int getOrder_num() {
        return order_num;
    }

    public void setOrder_num(int order_num) {
        this.order_num = order_num;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProduct_main_img() {
        return product_main_img;
    }

    public void setProduct_main_img(String product_main_img) {
        this.product_main_img = product_main_img;
    }

    public String getProduct_img() {
        return product_img;
    }

    public void setProduct_img(String product_img) {
        this.product_img = product_img;
    }

    public String getProduct_usp() {
        return product_usp;
    }

    public void setProduct_usp(String product_usp) {
        this.product_usp = product_usp;
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
