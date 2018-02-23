package com.us.hotr.webservice.response;

import com.us.hotr.storage.bean.User;

import java.io.Serializable;

/**
 * Created by liaobo on 2017/12/18.
 */

public class GetLoginResponse implements Serializable {
    private String jsessionid;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getJsessionid() {
        return jsessionid;
    }

    public void setJsessionid(String jsessionid) {
        this.jsessionid = jsessionid;
    }
}

