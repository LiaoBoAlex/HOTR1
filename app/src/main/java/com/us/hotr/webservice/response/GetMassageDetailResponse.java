package com.us.hotr.webservice.response;

import com.google.gson.annotations.SerializedName;
import com.us.hotr.storage.bean.Massage;
import com.us.hotr.storage.bean.Masseur;
import com.us.hotr.storage.bean.Product;
import com.us.hotr.storage.bean.Spa;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liaobo on 2017/12/13.
 */

public class GetMassageDetailResponse implements Serializable {
    private Spa massage;
    private Massage product;
    private List<Masseur> MassagistList;
    private List<Masseur> proposeMassagist;
    private int productOrderNum;
    private int totalMasseurCount;
    private int is_collected;

    public int getIs_collected() {
        return is_collected;
    }

    public void setIs_collected(int is_collected) {
        this.is_collected = is_collected;
    }

    public int getTotalMasseurCount() {
        return totalMasseurCount;
    }

    public void setTotalMasseurCount(int totalMasseurCount) {
        this.totalMasseurCount = totalMasseurCount;
    }

    public int getProductOrderNum() {
        return productOrderNum;
    }

    public void setProductOrderNum(int productOrderNum) {
        this.productOrderNum = productOrderNum;
    }

    public Spa getMassage() {
        return massage;
    }

    public void setMassage(Spa massage) {
        this.massage = massage;
    }

    public Massage getProduct() {
        return product;
    }

    public void setProduct(Massage product) {
        this.product = product;
    }

    public List<com.us.hotr.storage.bean.Masseur> getMassageistList() {
        return MassagistList;
    }

    public void setMassageistList(List<com.us.hotr.storage.bean.Masseur> massageistList) {
        MassagistList = massageistList;
    }

    public List<Masseur> getProposeMassagist() {
        return proposeMassagist;
    }

    public void setProposeMassagist(List<Masseur> proposeMassagist) {
        this.proposeMassagist = proposeMassagist;
    }
}
