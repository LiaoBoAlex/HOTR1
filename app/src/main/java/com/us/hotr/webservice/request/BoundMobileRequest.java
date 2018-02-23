package com.us.hotr.webservice.request;

import java.io.Serializable;

/**
 * Created by liaobo on 2018/2/2.
 */

public class BoundMobileRequest implements Serializable {
    private String telephone;
    private String verify_code;

    public BoundMobileRequest(String telephone, String validCode){
        this.telephone = telephone;
        this.verify_code = validCode;
    }
}
