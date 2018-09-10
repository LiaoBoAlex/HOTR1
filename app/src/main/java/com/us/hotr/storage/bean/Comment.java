package com.us.hotr.storage.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liaobo on 2018/1/11.
 */

public class Comment implements Serializable {
    private String content;
    private String nick_name;
    private long id;
    private long hot_topic_id;
    private String head_portrait;
    private int like_cnt;
    private int is_like;
    @SerializedName(value = "listReply", alternate = {"contrastPhotoReplyList"})
    private List<Reply> listReply;
    private String create_time;
    private long user_id;
    private int u_user_type;
    private long massagist_id;

    public int getU_user_type() {
        return u_user_type;
    }

    public void setU_user_type(int u_user_type) {
        this.u_user_type = u_user_type;
    }

    public long getMassagist_id() {
        return massagist_id;
    }

    public void setMassagist_id(long massagist_id) {
        this.massagist_id = massagist_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getHot_topic_id() {
        return hot_topic_id;
    }

    public void setHot_topic_id(long hot_topic_id) {
        this.hot_topic_id = hot_topic_id;
    }

    public String getHead_portrait() {
        return head_portrait;
    }

    public void setHead_portrait(String head_portrait) {
        this.head_portrait = head_portrait;
    }

    public int getLike_cnt() {
        return like_cnt;
    }

    public void setLike_cnt(int like_cnt) {
        this.like_cnt = like_cnt;
    }

    public int getIs_like() {
        return is_like;
    }

    public void setIs_like(int is_like) {
        this.is_like = is_like;
    }

    public List<Reply> getListReply() {
        return listReply;
    }

    public void setListReply(List<Reply> listReply) {
        this.listReply = listReply;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }
}
