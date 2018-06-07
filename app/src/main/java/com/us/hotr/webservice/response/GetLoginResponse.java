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

