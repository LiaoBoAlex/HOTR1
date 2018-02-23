package com.us.hotr.storage.bean;

import java.io.Serializable;

/**
 * Created by liaobo on 2018/1/11.
 */
public class Reply implements Serializable {
    private String content;
    private String nick_name;
    private long id;
    private long user_id;
    private String reply_to_user_nick_name;
    private long reply_to_user_id;
    private long reply_to_id;
    private long comment_id;
    private long hot_topic_id;
    private String create_time;

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

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getReply_to_user_nick_name() {
        return reply_to_user_nick_name;
    }

    public void setReply_to_user_nick_name(String reply_to_user_nick_name) {
        this.reply_to_user_nick_name = reply_to_user_nick_name;
    }

    public long getReply_to_user_id() {
        return reply_to_user_id;
    }

    public void setReply_to_user_id(long reply_to_user_id) {
        this.reply_to_user_id = reply_to_user_id;
    }

    public long getReply_to_id() {
        return reply_to_id;
    }

    public void setReply_to_id(long reply_to_id) {
        this.reply_to_id = reply_to_id;
    }

    public long getComment_id() {
        return comment_id;
    }

    public void setComment_id(long comment_id) {
        this.comment_id = comment_id;
    }

    public long getHot_topic_id() {
        return hot_topic_id;
    }

    public void setHot_topic_id(long hot_topic_id) {
        this.hot_topic_id = hot_topic_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
