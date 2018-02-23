package com.us.hotr.storage.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liaobo on 2017/12/25.
 */

public class Party implements Serializable {
    @SerializedName(value = "travel_start_time", alternate = {"travelStartTime"})
    private String travel_start_time;
    @SerializedName(value = "id", alternate = {"key"})
    private long id;
    @SerializedName(value = "party_list_img", alternate = {"main_img"})
    private String party_list_img;
    private int order_num;
    @SerializedName(value = "travel_name", alternate = {"travelName"})
    private String travel_name;
    @SerializedName(value = "travel_end_time", alternate = {"travelEndTime"})
    private String travel_end_time;
    @SerializedName(value = "sale_ticket_status", alternate = {"saleTicketStatus"})
    private int sale_ticket_status;
    private int access_count;
    @SerializedName(value = "sale_ticket_time", alternate = {"saleTicketTime"})
    private String sale_ticket_time;
    private String buyTicketNotice;
    private String cityStrategyDetail;
    private String cityStrategyImg;
    private int idType;
    private double priceRangeHigh;
    private double priceRangeLow;
    private String partyDetailImg;
    private String takeTicketNotice;
    private String travelAddress;
    private String travelInfo;
    private String videoUrl;

    public String getBuyTicketNotice() {
        return buyTicketNotice;
    }

    public void setBuyTicketNotice(String buyTicketNotice) {
        this.buyTicketNotice = buyTicketNotice;
    }

    public String getCityStrategyDetail() {
        return cityStrategyDetail;
    }

    public void setCityStrategyDetail(String cityStrategyDetail) {
        this.cityStrategyDetail = cityStrategyDetail;
    }

    public String getCityStrategyImg() {
        return cityStrategyImg;
    }

    public void setCityStrategyImg(String cityStrategyImg) {
        this.cityStrategyImg = cityStrategyImg;
    }

    public int getIdType() {
        return idType;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }

    public double getPriceRangeHigh() {
        return priceRangeHigh;
    }

    public void setPriceRangeHigh(double priceRangeHigh) {
        this.priceRangeHigh = priceRangeHigh;
    }

    public double getPriceRangeLow() {
        return priceRangeLow;
    }

    public void setPriceRangeLow(double priceRangeLow) {
        this.priceRangeLow = priceRangeLow;
    }

    public String getPartyDetailImg() {
        return partyDetailImg;
    }

    public void setPartyDetailImg(String partyDetailImg) {
        this.partyDetailImg = partyDetailImg;
    }

    public String getTakeTicketNotice() {
        return takeTicketNotice;
    }

    public void setTakeTicketNotice(String takeTicketNotice) {
        this.takeTicketNotice = takeTicketNotice;
    }

    public String getTravelAddress() {
        return travelAddress;
    }

    public void setTravelAddress(String travelAddress) {
        this.travelAddress = travelAddress;
    }

    public String getTravelInfo() {
        return travelInfo;
    }

    public void setTravelInfo(String travelInfo) {
        this.travelInfo = travelInfo;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getSale_ticket_time() {
        return sale_ticket_time;
    }

    public void setSale_ticket_time(String sale_ticket_time) {
        this.sale_ticket_time = sale_ticket_time;
    }

    public int getAccess_count() {
        return access_count;
    }

    public void setAccess_count(int access_count) {
        this.access_count = access_count;
    }

    public String getTravel_start_time() {
        return travel_start_time;
    }

    public void setTravel_start_time(String travel_start_time) {
        this.travel_start_time = travel_start_time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getParty_list_img() {
        return party_list_img;
    }

    public void setParty_list_img(String party_list_img) {
        this.party_list_img = party_list_img;
    }

    public int getOrder_num() {
        return order_num;
    }

    public void setOrder_num(int order_num) {
        this.order_num = order_num;
    }

    public String getTravel_name() {
        return travel_name;
    }

    public void setTravel_name(String travel_name) {
        this.travel_name = travel_name;
    }

    public String getTravel_end_time() {
        return travel_end_time;
    }

    public void setTravel_end_time(String travel_end_time) {
        this.travel_end_time = travel_end_time;
    }

    public int getSale_ticket_status() {
        return sale_ticket_status;
    }

    public void setSale_ticket_status(int sale_ticket_status) {
        this.sale_ticket_status = sale_ticket_status;
    }
}
