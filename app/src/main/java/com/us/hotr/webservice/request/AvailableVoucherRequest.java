package com.us.hotr.webservice.request;

import java.io.Serializable;

/**
 * Created by liaobo on 2018/1/23.
 */

public class AvailableVoucherRequest implements Serializable {
    private int type;
    private double product_price;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getProduct_price() {
        return product_price;
    }

    public void setProduct_price(double product_price) {
        this.product_price = product_price;
    }
}
