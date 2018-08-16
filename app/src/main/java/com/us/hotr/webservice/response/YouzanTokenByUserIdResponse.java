package com.us.hotr.webservice.response;

import java.io.Serializable;

public class YouzanTokenByUserIdResponse implements Serializable {
    private String access_token;
    private String cookie_key;
    private String cookie_value;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getCookie_key() {
        return cookie_key;
    }

    public void setCookie_key(String cookie_key) {
        this.cookie_key = cookie_key;
    }

    public String getCookie_value() {
        return cookie_value;
    }

    public void setCookie_value(String cookie_value) {
        this.cookie_value = cookie_value;
    }
}
