package com.us.hotr.webservice.request;

import com.us.hotr.storage.bean.Ticket;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liaobo on 2018/2/7.
 */

public class CreatePartyOrderRequest implements Serializable {
    private long travel_id;
    private String term_of_validity;
    private String address;
    private String addressee_email;
    private String addressee_name;
    private String addressee_phone;
    private double price_without_coupon;
    private int ticket_count;
    private double actual_payable;
    private String purchaser_name;
    private int purchaser_credentials;
    private String purchaser_credentials_numb;
    private int gender;
    private String purchaser_phone;
    private String ticketStr;

    public long getTravel_id() {
        return travel_id;
    }

    public void setTravel_id(long travel_id) {
        this.travel_id = travel_id;
    }

    public String getTerm_of_validity() {
        return term_of_validity;
    }

    public void setTerm_of_validity(String term_of_validity) {
        this.term_of_validity = term_of_validity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressee_email() {
        return addressee_email;
    }

    public void setAddressee_email(String addressee_email) {
        this.addressee_email = addressee_email;
    }

    public String getAddressee_name() {
        return addressee_name;
    }

    public void setAddressee_name(String addressee_name) {
        this.addressee_name = addressee_name;
    }

    public String getAddressee_phone() {
        return addressee_phone;
    }

    public void setAddressee_phone(String addressee_phone) {
        this.addressee_phone = addressee_phone;
    }

    public double getPrice_without_coupon() {
        return price_without_coupon;
    }

    public void setPrice_without_coupon(double price_without_coupon) {
        this.price_without_coupon = price_without_coupon;
    }

    public int getTicket_count() {
        return ticket_count;
    }

    public void setTicket_count(int ticket_count) {
        this.ticket_count = ticket_count;
    }

    public double getActual_payable() {
        return actual_payable;
    }

    public void setActual_payable(double actual_payable) {
        this.actual_payable = actual_payable;
    }

    public String getPurchaser_name() {
        return purchaser_name;
    }

    public void setPurchaser_name(String purchaser_name) {
        this.purchaser_name = purchaser_name;
    }

    public int getPurchaser_credentials() {
        return purchaser_credentials;
    }

    public void setPurchaser_credentials(int purchaser_credentials) {
        this.purchaser_credentials = purchaser_credentials;
    }

    public String getPurchaser_credentials_numb() {
        return purchaser_credentials_numb;
    }

    public void setPurchaser_credentials_numb(String purchaser_credentials_numb) {
        this.purchaser_credentials_numb = purchaser_credentials_numb;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getPurchaser_phone() {
        return purchaser_phone;
    }

    public void setPurchaser_phone(String purchaser_phone) {
        this.purchaser_phone = purchaser_phone;
    }

    public String getTicketStr() {
        return ticketStr;
    }

    public void setTicketStr(String ticketStr) {
        this.ticketStr = ticketStr;
    }
}
