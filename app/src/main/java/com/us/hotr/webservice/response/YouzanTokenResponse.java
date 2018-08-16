package com.us.hotr.webservice.response;

import java.io.Serializable;

public class YouzanTokenResponse implements Serializable {
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
