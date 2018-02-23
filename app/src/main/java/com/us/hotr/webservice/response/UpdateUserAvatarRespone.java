package com.us.hotr.webservice.response;

import java.io.Serializable;

/**
 * Created by liaobo on 2018/1/18.
 */

public class UpdateUserAvatarRespone implements Serializable {
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
