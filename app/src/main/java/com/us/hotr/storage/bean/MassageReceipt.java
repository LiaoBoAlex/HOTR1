package com.us.hotr.storage.bean;

import java.io.Serializable;

/**
 * Created by liaobo on 2018/2/23.
 */

public class MassageReceipt implements Serializable {
    private String verification_time;
    private long id;
    private String repeal_time;
    private String action_refund_time;
    private int numb;
    private String massagist_name;
    private double real_payment;
    private String refund_time;
    private String massage_name;
    private int verification_state;

    public String getVerification_time() {
        return verification_time;
    }

    public void setVerification_time(String verification_time) {
        this.verification_time = verification_time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getMassagist_name() {
        return massagist_name;
    }

    public void setMassagist_name(String massagist_name) {
        this.massagist_name = massagist_name;
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

    public String getMassage_name() {
        return massage_name;
    }

    public void setMassage_name(String massage_name) {
        this.massage_name = massage_name;
    }

    public int getVerification_state() {
        return verification_state;
    }

    public void setVerification_state(int verification_state) {
        this.verification_state = verification_state;
    }
}
