package com.us.hotr.storage.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liaobo on 2017/12/26.
 */

public class Ticket implements Serializable {
    @SerializedName(value = "ticketId", alternate = {"id"})
    private long ticketId;
    @SerializedName(value = "ticketName", alternate = {"ticket_name"})
    private String ticketName;
    private String guideWord;
    private String ticketImg;
    private Integer onhandInventory;
    private Double ticketPrice;
    @SerializedName(value = "ticketNum", alternate = {"ticket_number"})
    private int count;
    private Double sum_price;

    public double getSum_price() {
        return sum_price;
    }

    public void setSum_price(double sum_price) {
        this.sum_price = sum_price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public long getId() {
        return ticketId;
    }

    public void setId(long id) {
        this.ticketId = id;
    }

    public String getGuideWord() {
        return guideWord;
    }

    public void setGuideWord(String guideWord) {
        this.guideWord = guideWord;
    }

    public String getTicketImg() {
        return ticketImg;
    }

    public void setTicketImg(String ticketImg) {
        this.ticketImg = ticketImg;
    }

    public int getOnhandInventory() {
        return onhandInventory;
    }

    public void setOnhandInventory(int onhandInventory) {
        this.onhandInventory = onhandInventory;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }
}
