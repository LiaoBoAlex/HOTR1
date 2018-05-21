package com.us.hotr.storage.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liaobo on 2017/12/12.
 */

public class Spa implements Serializable {
    @SerializedName(value = "key", alternate = {"id"})
    private long key;
    private String provinceName;
    @SerializedName(value = "cityName", alternate = {"city_name"})
    private String cityName;
    @SerializedName(value = "areaName", alternate = {"area_name"})
    private String areaName;
    @SerializedName(value = "massageAddress", alternate = {"address"})
    private String massageAddress;
    @SerializedName(value = "massage_main_img", alternate = {"massageLogo", "main_img", "product_main_img"})
    private String massageLogo;
    @SerializedName(value = "massageName", alternate = {"massage_name"})
    private String massageName;
    @SerializedName(value = "openTimeOver", alternate = {"open_time_over"})
    private String openTimeOver;
    @SerializedName(value = "openTimeStart", alternate = {"open_time_start"})
    private String openTimeStart;
    @SerializedName(value = "landmarkName", alternate = {"landmark_name"})
    private String landmarkName;
    private int order_num;
    private String massageInfo;
    @SerializedName(value = "massagePhotos", alternate = {"massage_photos"})
    private String massagePhotos;
    private double lat;
    private double lon;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getMassageInfo() {
        return massageInfo;
    }

    public void setMassageInfo(String massageInfo) {
        this.massageInfo = massageInfo;
    }

    public String getMassagePhotos() {
        return massagePhotos;
    }

    public void setMassagePhotos(String massagePhotos) {
        this.massagePhotos = massagePhotos;
    }

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public String getLandmarkName() {
        return landmarkName;
    }

    public void setLandmarkName(String landmarkName) {
        this.landmarkName = landmarkName;
    }

    public int getOrder_num() {
        return order_num;
    }

    public void setOrder_num(int order_num) {
        this.order_num = order_num;
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

    public String getMassageAddress() {
        return massageAddress;
    }

    public void setMassageAddress(String massageAddress) {
        this.massageAddress = massageAddress;
    }

    public String getMassageLogo() {
        return massageLogo;
    }

    public void setMassageLogo(String massageLogo) {
        this.massageLogo = massageLogo;
    }

    public String getMassageName() {
        return massageName;
    }

    public void setMassageName(String massageName) {
        this.massageName = massageName;
    }

    public String getOpenTimeOver() {
        return openTimeOver;
    }

    public void setOpenTimeOver(String openTimeOver) {
        this.openTimeOver = openTimeOver;
    }

    public String getOpenTimeStart() {
        return openTimeStart;
    }

    public void setOpenTimeStart(String openTimeStart) {
        this.openTimeStart = openTimeStart;
    }
}
