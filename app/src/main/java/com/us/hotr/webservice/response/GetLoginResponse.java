package com.us.hotr.webservice.response;

import com.us.hotr.storage.bean.User;

import java.io.Serializable;

/**
 * Created by liaobo on 2017/12/18.
 */

public class GetLoginResponse implements Serializable {
    private String jsessionid;
    private User user;
    private boolean first_register;
    private WechatUser wechatUser;
    private String youzan_cookie_key;
    private String youzan_access_token;
    private String youzan_cookie_value;

    public String getYouzan_cookie_key() {
        return youzan_cookie_key;
    }

    public void setYouzan_cookie_key(String youzan_cookie_key) {
        this.youzan_cookie_key = youzan_cookie_key;
    }

    public String getYouzan_access_token() {
        return youzan_access_token;
    }

    public void setYouzan_access_token(String youzan_access_token) {
        this.youzan_access_token = youzan_access_token;
    }

    public String getYouzan_cookie_value() {
        return youzan_cookie_value;
    }

    public void setYouzan_cookie_value(String youzan_cookie_value) {
        this.youzan_cookie_value = youzan_cookie_value;
    }

    public WechatUser getWechatUser() {
        return wechatUser;
    }

    public void setWechatUser(WechatUser wechatUser) {
        this.wechatUser = wechatUser;
    }

    public boolean isFirst_register() {
        return first_register;
    }

    public void setFirst_register(boolean first_register) {
        this.first_register = first_register;
    }

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

    public static class WechatUser implements Serializable{
        private String openId;
        private String name;
        private String headImgUrl;
        private int sex;

        public WechatUser(String openId, String name, String headImgUrl, int sex){
            this.openId = openId;
            this.name = name;
            this.headImgUrl = headImgUrl;
            this.sex = sex;
        }

        public String getOpenId() {
            return openId;
        }

        public String getName() {
            return name;
        }

        public String getHeadImgUrl() {
            return headImgUrl;
        }

        public int getSex() {
            return sex;
        }
    }
}

