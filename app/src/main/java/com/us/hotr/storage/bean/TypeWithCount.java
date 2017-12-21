package com.us.hotr.storage.bean;

import java.io.Serializable;

/**
 * Created by liaobo on 2017/12/15.
 */

public class TypeWithCount implements Serializable {
    private int type_id;
    private int product_num;
    private String type_name;

    public TypeWithCount(int type_id, int product_num, String type_name){
        this.type_id = type_id;
        this.product_num = product_num;
        this.type_name = type_name;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public int getProduct_num() {
        return product_num;
    }

    public void setProduct_num(int product_num) {
        this.product_num = product_num;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }
}
