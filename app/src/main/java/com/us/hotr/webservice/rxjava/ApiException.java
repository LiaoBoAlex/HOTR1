package com.us.hotr.webservice.rxjava;

/**
 * Created by Mloong on 2017/11/13.
 */

public class ApiException extends RuntimeException {

    private int errorCode;
    private String errorMsg;

    public ApiException(int resultCode, String erroMessage) {
        this.errorCode = resultCode;
        this.errorMsg = erroMessage;
    }

    public ApiException(String detailMessage) {
        super(detailMessage);
        errorMsg = detailMessage;
    }

    public ApiException(int resultCode){
        this.errorCode = resultCode;
        this.errorMsg = getApiExceptionMessage(resultCode);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    private static String getApiExceptionMessage(int code){
        String message = "";
        switch (code) {
            default:
                message = "网络未连接，请检查网络设置";
        }
        return message;
    }
}

