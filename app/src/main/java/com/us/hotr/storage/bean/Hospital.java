package com.us.hotr.storage.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mloong on 2017/9/5.
 */

public class Hospital implements Serializable {
    private String hospital_logo;
    private List<Subject> good_at_project_list;
    private String hospital_name;
    private int hospital_id;
    private String hospital_address;
    private int order_num;
    private String hospital_type;
    private String hospital_photos;
    private String hospital_info;
    private String hospital_style;

    public String getHospital_logo() {
        return hospital_logo;
    }

    public void setHospital_logo(String hospital_logo) {
        this.hospital_logo = hospital_logo;
    }

    public List<Subject> getGood_at_project_list() {
        return good_at_project_list;
    }

    public void setGood_at_project_list(List<Subject> good_at_project_list) {
        this.good_at_project_list = good_at_project_list;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public int getHospital_id() {
        return hospital_id;
    }

    public void setHospital_id(int hospital_id) {
        this.hospital_id = hospital_id;
    }

    public String getHospital_address() {
        return hospital_address;
    }

    public void setHospital_address(String hospital_address) {
        this.hospital_address = hospital_address;
    }

    public int getOrder_num() {
        return order_num;
    }

    public void setOrder_num(int order_num) {
        this.order_num = order_num;
    }

    public String getHospital_type() {
        return hospital_type;
    }

    public void setHospital_type(String hospital_type) {
        this.hospital_type = hospital_type;
    }

    public String getHospital_photos() {
        return hospital_photos;
    }

    public void setHospital_photos(String hospital_photos) {
        this.hospital_photos = hospital_photos;
    }

    public String getHospital_info() {
        return hospital_info;
    }

    public void setHospital_info(String hospital_info) {
        this.hospital_info = hospital_info;
    }

    public String getHospital_style() {
        return hospital_style;
    }

    public void setHospital_style(String hospital_style) {
        this.hospital_style = hospital_style;
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
