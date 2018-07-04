package com.us.hotr.storage.bean;

import java.io.Serializable;

public class VoucherSet implements Serializable {
    private long couponId;
    private int couponNum;

    public VoucherSet(long couponId, int couponNum){
        this.couponId = couponId;
        this.couponNum = couponNum;
    }

    public long getCouponId() {
        return couponId;
    }

    public void setCouponId(long couponId) {
        this.couponId = couponId;
    }

    public int getCouponNum() {
        return couponNum;
    }

    public void setCouponNum(int couponNum) {
        this.couponNum = couponNum;
    }
}
