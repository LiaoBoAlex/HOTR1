package com.us.hotr.webservice.response;

/**
 * Created by Mloong on 2017/11/29.
 */

public class BaseResponse<T> {
    private int status;
    private String memo;

    private T result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
