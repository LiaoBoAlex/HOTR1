package com.us.hotr.webservice.response;

import com.us.hotr.storage.bean.Case;
import com.us.hotr.storage.bean.Doctor;
import com.us.hotr.storage.bean.Hospital;
import com.us.hotr.storage.bean.Product;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liaobo on 2017/12/7.
 */

public class GetHospitalDetailResponse implements Serializable {
    private Hospital detail;
    private List<SubjectCount> typeList;
    private List<Product> productList;
    private List<Doctor> doctorList;
    private List<Case> caseList;
    private int totalProduct;
    private int totalDoctor;
    private int totalCase;

    public Hospital getDetail() {
        return detail;
    }

    public void setDetail(Hospital detail) {
        this.detail = detail;
    }

    public List<SubjectCount> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<SubjectCount> typeList) {
        this.typeList = typeList;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public List<Doctor> getDoctorList() {
        return doctorList;
    }

    public void setDoctorList(List<Doctor> doctorList) {
        this.doctorList = doctorList;
    }

    public int getTotalProduct() {
        return totalProduct;
    }

    public void setTotalProduct(int totalProduct) {
        this.totalProduct = totalProduct;
    }

    public int getTotalDoctor() {
        return totalDoctor;
    }

    public void setTotalDoctor(int totalDoctor) {
        this.totalDoctor = totalDoctor;
    }

    public List<Case> getCaseList() {
        return caseList;
    }

    public void setCaseList(List<Case> caseList) {
        this.caseList = caseList;
    }

    public int getTotalCase() {
        return totalCase;
    }

    public void setTotalCase(int totalCase) {
        this.totalCase = totalCase;
    }

    public class SubjectCount implements Serializable {
        private String type_name;
        private int type_id;
        private int order_num;

        public String getType_name() {
            return type_name;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
        }

        public int getType_id() {
            return type_id;
        }

        public void setType_id(int type_id) {
            this.type_id = type_id;
        }

        public int getOrder_num() {
            return order_num;
        }

        public void setOrder_num(int order_num) {
            this.order_num = order_num;
        }
    }
}
