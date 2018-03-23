package com.us.hotr.storage.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liaobo on 2018/2/8.
 */

public class PartyOrder implements Serializable{
    private String take_ticket_notice;
    private String purchaser_credentials_numb;
    private int order_state;
    private int purchaser_sex;
    private String addressee_name;
    @SerializedName(value = "total_number", alternate = {"totalNumber"})
    private int total_number;
    private String order_update_time;
    private String term_of_validity;
    private double order_price;
    @SerializedName(value = "order_code", alternate = {"orderCode"})
    private String order_code;
    private long id;
    private String addressee_phone;
    private int purchaser_credentials;
    @SerializedName(value = "travel_name", alternate = {"travelName"})
    private String travel_name;
    private long user_id;
    private String travel_start_time;
    private String addressee_email;
    private long travel_id;
    private String purchaser_phone;
    private String travel_img;
    private String travel_end_time;
    private String cancel_time;
    private String purchaser_name;
    @SerializedName(value = "rel_order_price", alternate = {"relOrderPrice"})
    private double rel_order_price;
    private String address;
    private String create_time;
    private String cancel_detail;
    private String pay_time;

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTake_ticket_notice() {
        return take_ticket_notice;
    }

    public void setTake_ticket_notice(String take_ticket_notice) {
        this.take_ticket_notice = take_ticket_notice;
    }

    public String getPurchaser_credentials_numb() {
        return purchaser_credentials_numb;
    }

    public void setPurchaser_credentials_numb(String purchaser_credentials_numb) {
        this.purchaser_credentials_numb = purchaser_credentials_numb;
    }

    public int getOrder_state() {
        return order_state;
    }

    public void setOrder_state(int order_state) {
        this.order_state = order_state;
    }

    public int getPurchaser_sex() {
        return purchaser_sex;
    }

    public void setPurchaser_sex(int purchaser_sex) {
        this.purchaser_sex = purchaser_sex;
    }

    public String getAddressee_name() {
        return addressee_name;
    }

    public void setAddressee_name(String addressee_name) {
        this.addressee_name = addressee_name;
    }

    public int getTotal_number() {
        return total_number;
    }

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }

    public String getOrder_update_time() {
        return order_update_time;
    }

    public void setOrder_update_time(String order_update_time) {
        this.order_update_time = order_update_time;
    }

    public String getTerm_of_validity() {
        return term_of_validity;
    }

    public void setTerm_of_validity(String term_of_validity) {
        this.term_of_validity = term_of_validity;
    }

    public double getOrder_price() {
        return order_price;
    }

    public void setOrder_price(double order_price) {
        this.order_price = order_price;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getAddressee_phone() {
        return addressee_phone;
    }

    public void setAddressee_phone(String addressee_phone) {
        this.addressee_phone = addressee_phone;
    }

    public int getPurchaser_credentials() {
        return purchaser_credentials;
    }

    public void setPurchaser_credentials(int purchaser_credentials) {
        this.purchaser_credentials = purchaser_credentials;
    }

    public String getTravel_name() {
        return travel_name;
    }

    public void setTravel_name(String travel_name) {
        this.travel_name = travel_name;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getTravel_start_time() {
        return travel_start_time;
    }

    public void setTravel_start_time(String travel_start_time) {
        this.travel_start_time = travel_start_time;
    }

    public String getAddressee_email() {
        return addressee_email;
    }

    public void setAddressee_email(String addressee_email) {
        this.addressee_email = addressee_email;
    }

    public long getTravel_id() {
        return travel_id;
    }

    public void setTravel_id(long travel_id) {
        this.travel_id = travel_id;
    }

    public String getPurchaser_phone() {
        return purchaser_phone;
    }

    public void setPurchaser_phone(String purchaser_phone) {
        this.purchaser_phone = purchaser_phone;
    }

    public String getTravel_img() {
        return travel_img;
    }

    public void setTravel_img(String travel_img) {
        this.travel_img = travel_img;
    }

    public String getTravel_end_time() {
        return travel_end_time;
    }

    public void setTravel_end_time(String travel_end_time) {
        this.travel_end_time = travel_end_time;
    }

    public String getCancel_time() {
        return cancel_time;
    }

    public void setCancel_time(String cancel_time) {
        this.cancel_time = cancel_time;
    }

    public String getPurchaser_name() {
        return purchaser_name;
    }

    public void setPurchaser_name(String purchaser_name) {
        this.purchaser_name = purchaser_name;
    }

    public double getRel_order_price() {
        return rel_order_price;
    }

    public void setRel_order_price(double rel_order_price) {
        this.rel_order_price = rel_order_price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getCancel_detail() {
        return cancel_detail;
    }

    public void setCancel_detail(String cancel_detail) {
        this.cancel_detail = cancel_detail;
    }
}
