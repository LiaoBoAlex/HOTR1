package com.us.hotr.webservice.response;

import com.us.hotr.storage.bean.Massage;
import com.us.hotr.storage.bean.Masseur;
import com.us.hotr.storage.bean.Spa;
import com.us.hotr.storage.bean.Type;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liaobo on 2017/12/13.
 */

public class GetMasseurDetailResponse implements Serializable {
    private Masseur massagist;
    private List<Massage> productList;
    private List<Type> typeList;
    private int job_time;
    private Spa massage;
    private int sumOrderNum;
    private int is_collected;

    public int getIs_collected() {
        return is_collected;
    }

    public void setIs_collected(int is_collected) {
        this.is_collected = is_collected;
    }

    public int getJob_time() {
        return job_time;
    }

    public void setJob_time(int job_time) {
        this.job_time = job_time;
    }

    public Masseur getMassagist() {
        return massagist;
    }

    public void setMassagist(Masseur massagist) {
        this.massagist = massagist;
    }

    public List<Massage> getProductList() {
        return productList;
    }

    public void setProductList(List<Massage> productList) {
        this.productList = productList;
    }

    public List<Type> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<Type> typeList) {
        this.typeList = typeList;
    }

    public Spa getMassage() {
        return massage;
    }

    public void setMassage(Spa massage) {
        this.massage = massage;
    }

    public int getSumOrderNum() {
        return sumOrderNum;
    }

    public void setSumOrderNum(int sumOrderNum) {
        this.sumOrderNum = sumOrderNum;
    }

}
