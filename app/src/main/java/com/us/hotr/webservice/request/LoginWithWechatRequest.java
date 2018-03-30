package com.us.hotr.webservice.request;

/**
 * Created by liaobo on 2018/2/2.
 */

public class LoginWithWechatRequest {
    private String nickname;
    private String headimgurl;
    private String openid;
    private int sex;
    private int device_type;

    public LoginWithWechatRequest(String nickname, String headimgurl, String openid, int sex){
        this.nickname = nickname;
        this.headimgurl = headimgurl;
        this.openid = openid;
        this.sex = sex;
        this.device_type = 0;
    }
}
