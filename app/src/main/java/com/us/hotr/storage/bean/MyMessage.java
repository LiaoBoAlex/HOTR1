package com.us.hotr.storage.bean;

import java.io.Serializable;

/**
 * Created by liaobo on 2018/3/6.
 */

public class MyMessage implements Serializable {
    public static final int TYPE_SEND_TEXT = 1;
    public static final int TYPE_SEND_PHOTO = 2;
    public static final int TYPE_RECEIVE_TEXT = 3;
    public static final int TYPE_RECEIVE_PHOTO = 4;
    public static final int TYPE_DATE = 5;

    public static final int STATUS_SENDING = 1;
    public static final int STATUS_SENT = 2;
    public static final int STATUS_FAILED = 3;

    private int type;
    private String content;
    private int status;
    private int id;
    private long createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public MyMessage(int type, String content, int status){
        this.type = type;
        this.content = content;
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
