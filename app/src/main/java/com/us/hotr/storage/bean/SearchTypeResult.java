package com.us.hotr.storage.bean;

import java.io.Serializable;

/**
 * Created by Mloong on 2017/8/30.
 */

public class SearchTypeResult implements Serializable {
    private String title;
    private String id;
    private int count;

    public SearchTypeResult(String title, String id, int count){
        this.title = title;
        this.id = id;
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
