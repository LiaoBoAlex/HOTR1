package com.us.hotr.webservice.request;

import java.io.Serializable;

/**
 * Created by liaobo on 2018/2/1.
 */

public class CancelOrderRequest implements Serializable {
    private long order_id;
    private String cancle_reason;

    public long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public String getCancle_reason() {
        return cancle_reason;
    }

    public void setCancle_reason(String cancle_reason) {
        this.cancle_reason = cancle_reason;
    }
}
