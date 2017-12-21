package com.us.hotr.webservice.response;

/**
 * Created by liaobo on 2017/12/6.
 */

public class BaseListResponse<T> {
    private int total;
    private T rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public T getRows() {
        return rows;
    }

    public void setRows(T rows) {
        this.rows = rows;
    }
}
