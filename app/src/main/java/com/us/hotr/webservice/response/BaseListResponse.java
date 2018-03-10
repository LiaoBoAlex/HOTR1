package com.us.hotr.webservice.response;

import com.google.gson.annotations.SerializedName;
import com.us.hotr.storage.bean.Type;

import java.util.List;

/**
 * Created by liaobo on 2017/12/6.
 */

public class BaseListResponse<T> {
    private int total;
    private List<Type> ymTypeList;
    @SerializedName(value = "rows", alternate = {"row"})
    private T rows;

    public List<Type> getYmTypeList() {
        return ymTypeList;
    }

    public void setYmTypeList(List<Type> ymTypeList) {
        this.ymTypeList = ymTypeList;
    }

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
