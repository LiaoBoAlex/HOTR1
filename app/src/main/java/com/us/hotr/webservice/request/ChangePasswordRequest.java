package com.us.hotr.webservice.request;

/**
 * Created by liaobo on 2018/2/2.
 */

public class ChangePasswordRequest {
    private String telephone;
    private String new_password;
    private String verify_code;

    public ChangePasswordRequest(String telephone, String password, String validCode){
        this.telephone = telephone;
        this.new_password = password;
        this.verify_code = validCode;
    }
}
