package com.us.hotr.storage.bean;

import com.google.gson.annotations.SerializedName;

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
    private String encryption_QR_code;
    private String order_code;
    private String product_name_usp;
    private double pay_at_shop;
    private String verification_code;
    private double lat;
    private double lon;
    private int payment_type;
    private double prepayment;
    private double fixed_price;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getPrepayment() {
        return prepayment;
    }

    public void setPrepayment(double prepayment) {
        this.prepayment = prepayment;
    }

    public double getFixed_price() {
        return fixed_price;
    }

    public void setFixed_price(double fixed_price) {
        this.fixed_price = fixed_price;
    }

    public int getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(int payment_type) {
        this.payment_type = payment_type;
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

    public String getVerification_code() {
        return verification_code;
    }

    public void setVerification_code(String verification_code) {
        this.verification_code = verification_code;
    }

    public double getPay_at_shop() {
        return pay_at_shop;
    }

    public void setPay_at_shop(double pay_at_shop) {
        this.pay_at_shop = pay_at_shop;
    }

    public String getVerification_time() {
        return verification_time;
    }

    public void setVerification_time(String verification_time) {
        this.verification_time = verification_time;
    }

    public String getEncryption_QR_code() {
        return encryption_QR_code;
    }

    public void setEncryption_QR_code(String encryption_QR_code) {
        this.encryption_QR_code = encryption_QR_code;
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
