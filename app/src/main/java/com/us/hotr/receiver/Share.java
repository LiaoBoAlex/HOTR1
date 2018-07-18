package com.us.hotr.receiver;

import java.io.Serializable;

/**
 * Created by liaobo on 2018/3/16.
 */

public class Share implements Serializable {
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_INVITE_FRIEND = 2;

    private String url;
    private String title;
    private String description;
    private String imageUrl;
    private String sinaContent;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSinaContent() {
        return sinaContent;
    }

    public void setSinaContent(String sinaContent) {
        this.sinaContent = sinaContent;
    }
}
