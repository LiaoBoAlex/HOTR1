package com.us.hotr.storage.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liaobo on 2018/1/5.
 */

public class Group implements Serializable {
    private String show_img;
    @SerializedName(value = "id", alternate = {"key"})
    private long id;
    private int attention_cnt;
    private String first_img;
    private String coshow_info;
    private String create_time;
    @SerializedName(value = "coshow_name", alternate = {"coshowName"})
    private String coshow_name;
    private String notice;
    private int is_attention;
    private int is_recommend;

    public int getIs_recommend() {
        return is_recommend;
    }

    public void setIs_recommend(int is_recommend) {
        this.is_recommend = is_recommend;
    }

    public int getIs_attention() {
        return is_attention;
    }

    public void setIs_attention(int is_attention) {
        this.is_attention = is_attention;
    }

    public String getBig_img() {
        return show_img;
    }

    public void setBig_img(String show_img) {
        this.show_img = show_img;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAttention_cnt() {
        return attention_cnt;
    }

    public void setAttention_cnt(int attention_cnt) {
        this.attention_cnt = attention_cnt;
    }

    public String getSmall_img() {
        return first_img;
    }

    public void setSmall_img(String first_img) {
        this.first_img = first_img;
    }

    public String getCoshow_info() {
        return coshow_info;
    }

    public void setCoshow_info(String coshow_info) {
        this.coshow_info = coshow_info;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getCoshow_name() {
        return coshow_name;
    }

    public void setCoshow_name(String coshow_name) {
        this.coshow_name = coshow_name;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
}
