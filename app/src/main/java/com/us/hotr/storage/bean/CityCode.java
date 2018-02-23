package com.us.hotr.storage.bean;

import java.io.Serializable;

/**
 * Created by liaobo on 2018/1/31.
 */

public class CityCode implements Serializable {
    private long baiduCityCode;
    private long productCityCode;
    private long massageCityCode;

    public CityCode(){
        baiduCityCode = -1;
        productCityCode = -1;
        massageCityCode = -1;
    }

    public long getBaiduCityCode() {
        return baiduCityCode;
    }

    public void setBaiduCityCode(long baiduCityCode) {
        this.baiduCityCode = baiduCityCode;
    }

    public long getProductCityCode() {
        return productCityCode;
    }

    public void setProductCityCode(long productCityCode) {
        this.productCityCode = productCityCode;
    }

    public long getMassageCityCode() {
        return massageCityCode;
    }

    public void setMassageCityCode(long massageCityCode) {
        this.massageCityCode = massageCityCode;
    }
}
