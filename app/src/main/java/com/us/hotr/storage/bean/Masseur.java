package com.us.hotr.storage.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liaobo on 2017/12/12.
 */

public class Masseur implements Serializable {
    @SerializedName(value = "id", alternate = {"key"})
    private long id;
    @SerializedName(value = "massagist_main_img", alternate = {"main_img"})
    private String massagist_main_img;

    private int order_num;
    @SerializedName(value = "job_start_time", alternate = {"working_age"})
    private String job_start_time;
    @SerializedName(value = "massagist_name", alternate = {"massagistName"})
    private String massagist_name;
    @SerializedName(value = "massagist_height", alternate = {"massagistHeight"})
    private int massagist_height;
    @SerializedName(value = "massage_address", alternate = {"address"})
    private String massage_address;
    private int job_time;
    private String landmark_name;
    private String massagistInfo;
    private String provinceName;
    private String cityName;
    private String areaName;
    @SerializedName(value = "massagistPhotos", alternate = {"massagist_photos"})
    private String massagistPhotos;
    private int is_collected;

    public int getIs_collected() {
        return is_collected;
    }

    public void setIs_collected(int is_collected) {
        this.is_collected = is_collected;
    }

    public String getMassagistInfo() {
        return massagistInfo;
    }

    public void setMassagistInfo(String massagistInfo) {
        this.massagistInfo = massagistInfo;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getMassagistPhotos() {
        return massagistPhotos;
    }

    public void setMassagistPhotos(String massagistPhotos) {
        this.massagistPhotos = massagistPhotos;
    }

    public String getLandmark_name() {
        return landmark_name;
    }

    public void setLandmark_name(String landmark_name) {
        this.landmark_name = landmark_name;
    }

    public int getJob_time() {
        return job_time;
    }

    public void setJob_time(int job_time) {
        this.job_time = job_time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMassagist_main_img() {
        return massagist_main_img;
    }

    public void setMassagist_main_img(String massagist_main_img) {
        this.massagist_main_img = massagist_main_img;
    }

    public int getOrder_num() {
        return order_num;
    }

    public void setOrder_num(int order_num) {
        this.order_num = order_num;
    }

    public String getJob_start_time() {
        return job_start_time;
    }

    public void setJob_start_time(String job_start_time) {
        this.job_start_time = job_start_time;
    }

    public String getMassagist_name() {
        return massagist_name;
    }

    public void setMassagist_name(String massagist_name) {
        this.massagist_name = massagist_name;
    }

    public int getMassagist_height() {
        return massagist_height;
    }

    public void setMassagist_height(int massagist_height) {
        this.massagist_height = massagist_height;
    }

    public String getMassage_address() {
        return massage_address;
    }

    public void setMassage_address(String massage_address) {
        this.massage_address = massage_address;
    }
}
