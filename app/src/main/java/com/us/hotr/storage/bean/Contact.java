package com.us.hotr.storage.bean;

import java.io.Serializable;

/**
 * Created by liaobo on 2018/2/7.
 */

public class Contact implements Serializable {
    private String purchaser_name;
    private int purchaser_credentials;
    private String purchaser_credentials_numb;
    private int gender;
    private String purchaser_phone;
    private String term_of_validity;
    private String valideDate;

    public String getValideDate() {
        return valideDate;
    }

    public void setValideDate(String valideDate) {
        this.valideDate = valideDate;
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

    public String getTerm_of_validity() {
        return term_of_validity;
    }

    public void setTerm_of_validity(String term_of_validity) {
        this.term_of_validity = term_of_validity;
    }
}
