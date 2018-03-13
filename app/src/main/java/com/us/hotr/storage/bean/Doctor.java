package com.us.hotr.storage.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mloong on 2017/9/5.
 */

public class Doctor implements Serializable {
    @SerializedName(value = "doctor_name", alternate = {"docName"})
    private String doctor_name;
    private String hospital_name;
    private String doctor_job;
    @SerializedName(value = "doctor_id", alternate = {"id"})
    private long doctor_id;
    @SerializedName(value = "doctor_main_img", alternate = {"docPhoto", "main_img"})
    private String doctor_main_img;
    private List<String> doctor_img;
    private List<Subject> good_at_project_list;
    private String doctor_info;
    private long hospital_id;
    private int order_num;
    private String typeName;
    private int case_num;

    public int getCase_num() {
        return case_num;
    }

    public void setCase_num(int case_num) {
        this.case_num = case_num;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getDoctor_job() {
        return doctor_job;
    }

    public void setDoctor_job(String doctor_job) {
        this.doctor_job = doctor_job;
    }

    public long getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(long doctor_id) {
        this.doctor_id = doctor_id;
    }

    public List<Subject> getGood_at_project_list() {
        return good_at_project_list;
    }

    public void setGood_at_project_list(List<Subject> good_at_project_list) {
        this.good_at_project_list = good_at_project_list;
    }

    public String getDoctor_info() {
        return doctor_info;
    }

    public void setDoctor_info(String doctor_info) {
        this.doctor_info = doctor_info;
    }

    public long getHospital_id() {
        return hospital_id;
    }

    public void setHospital_id(long hospital_id) {
        this.hospital_id = hospital_id;
    }

    public int getOrder_num() {
        return order_num;
    }

    public void setOrder_num(int order_num) {
        this.order_num = order_num;
    }

    public String getDoctor_main_img() {
        return doctor_main_img;
    }

    public void setDoctor_main_img(String doctor_main_img) {
        this.doctor_main_img = doctor_main_img;
    }

    public List<String> getDoctor_img() {
        return doctor_img;
    }

    public void setDoctor_img(List<String> doctor_img) {
        this.doctor_img = doctor_img;
    }

    public class Subject implements Serializable{
        private String type_name;

        public String getType_name() {
            return type_name;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
        }
    }
}
