package com.us.hotr.webservice.response;

import com.us.hotr.storage.bean.Case;
import com.us.hotr.storage.bean.Doctor;
import com.us.hotr.storage.bean.Product;
import com.us.hotr.storage.bean.Type;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liaobo on 2017/12/11.
 */

public class GetDoctorDetailResponse implements Serializable {
    private Doctor detail;
    private List<Product> productList;
    private List<Case> caseList;
    private List<Type> typeList;
    private List<Type> caseTypeList;
    private int totalProduct;
    private int totalCase;
    private int is_collected;

    public List<Type> getCaseTypeList() {
        return caseTypeList;
    }

    public void setCaseTypeList(List<Type> caseTypeList) {
        this.caseTypeList = caseTypeList;
    }

    public int getIs_collected() {
        return is_collected;
    }

    public void setIs_collected(int is_collected) {
        this.is_collected = is_collected;
    }

    public Doctor getDetail() {
        return detail;
    }

    public void setDetail(Doctor detail) {
        this.detail = detail;
    }

    public List<Type> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<Type> typeList) {
        this.typeList = typeList;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public List<Case> getCaseList() {
        return caseList;
    }

    public void setCaseList(List<Case> caseList) {
        this.caseList = caseList;
    }

    public int getTotalProduct() {
        return totalProduct;
    }

    public void setTotalProduct(int totalProduct) {
        this.totalProduct = totalProduct;
    }

    public int getTotalCase() {
        return totalCase;
    }

    public void setTotalCase(int totalCase) {
        this.totalCase = totalCase;
    }
}
