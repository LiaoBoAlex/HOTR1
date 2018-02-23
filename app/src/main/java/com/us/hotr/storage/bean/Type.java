package com.us.hotr.storage.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liaobo on 2017/12/5.
 */

public class Type implements Serializable {

    public Type(int typeId, String typeName){
        this.typeName = typeName;
        this. typeId = typeId;
    }

    public Type(int typeId, int num, String typeName){
        this.typeName = typeName;
        this. typeId = typeId;
        this.product_num = num;
    }
    @SerializedName(value = "typeName", alternate = {"type_name"})
    private String typeName;
    @SerializedName(value = "typeId", alternate = {"type_id", "id"})
    private int typeId;
    @SerializedName(value = "product_num", alternate = {"productNum", "order_num"})
    private int product_num;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getProduct_num() {
        return product_num;
    }

    public void setProduct_num(int product_num) {
        this.product_num = product_num;
    }
}
