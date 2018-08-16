package com.us.hotr.webservice.request;

import java.io.Serializable;

public class GetYouzanTokenByUserIdRequest implements Serializable{
    private long userId;

    public GetYouzanTokenByUserIdRequest(long userId){
        this.userId = userId;
    }
}
