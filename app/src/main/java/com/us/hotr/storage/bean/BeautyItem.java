package com.us.hotr.storage.bean;

import java.io.Serializable;

/**
 * Created by Mloong on 2017/8/31.
 */

public class BeautyItem implements Serializable {
    private String id;
    private String data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
