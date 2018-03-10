package com.us.hotr.storage.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liaobo on 2017/12/5.
 */

public class Type implements Serializable {

    public Type(long typeId, String typeName){
        this.typeName = typeName;
        this. typeId = typeId;
    }

    public Type(long typeId, int num, String typeName){
        this.typeName = typeName;
        this. typeId = typeId;
        this.product_num = num;
    }
    @SerializedName(value = "typeName", alternate = {"type_name"})
    private String typeName;
    @SerializedName(value = "typeId", alternate = {"type_id", "id"})
    private long typeId;
    @SerializedName(value = "product_num", alternate = {"productNum", "order_num", "amount"})
    private int product_num;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public int getProduct_num() {
        return product_num;
    }

    public void setProduct_num(int product_num) {
        this.product_num = product_num;
    }
}
