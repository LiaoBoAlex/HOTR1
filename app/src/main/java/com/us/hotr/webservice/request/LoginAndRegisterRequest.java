package com.us.hotr.webservice.request;

/**
 * Created by liaobo on 2018/2/2.
 */

public class LoginAndRegisterRequest {
    private String telephone;
    private String validCode;
    private int device_type;

    public LoginAndRegisterRequest(String telephone, String validCode){
        this.telephone = telephone;
        this.validCode = validCode;
        this.device_type = 0;
    }
}
