package com.us.hotr.storage.bean;

import java.io.Serializable;

public class PostOld implements Serializable {
    private int imageHeight;
    private int imageWidth;
    private String imageURL;
    private int status;
    private int type;
    private String editContent;

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getEditContent() {
        return editContent;
    }

    public void setEditContent(String editContent) {
        this.editContent = editContent;
    }
}
