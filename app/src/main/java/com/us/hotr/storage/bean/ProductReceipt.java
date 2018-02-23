package com.us.hotr.storage.bean;

import java.io.Serializable;

/**
 * Created by liaobo on 2018/2/23.
 */

public class ProductReceipt implements Serializable {
    private String verification_time;
    private String doctor_name;
    private long id;
    private String hospital_name;
    private String repeal_time;
    private String action_refund_time;
    private int numb;
    private double real_payment;
    private String refund_time;
    private int verification_state;
    private String order_code;
    private String product_name_usp;

    public String getVerification_time() {
        return verification_time;
    }

    public void setVerification_time(String verification_time) {
        this.verification_time = verification_time;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getRepeal_time() {
        return repeal_time;
    }

    public void setRepeal_time(String repeal_time) {
        this.repeal_time = repeal_time;
    }

    public String getAction_refund_time() {
        return action_refund_time;
    }

    public void setAction_refund_time(String action_refund_time) {
        this.action_refund_time = action_refund_time;
    }

    public int getNumb() {
        return numb;
    }

    public void setNumb(int numb) {
        this.numb = numb;
    }

    public double getReal_payment() {
        return real_payment;
    }

    public void setReal_payment(double real_payment) {
        this.real_payment = real_payment;
    }

    public String getRefund_time() {
        return refund_time;
    }

    public void setRefund_time(String refund_time) {
        this.refund_time = refund_time;
    }

    public int getVerification_state() {
        return verification_state;
    }

    public void setVerification_state(int verification_state) {
        this.verification_state = verification_state;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getProduct_name_usp() {
        return product_name_usp;
    }

    public void setProduct_name_usp(String product_name_usp) {
        this.product_name_usp = product_name_usp;
    }
}
