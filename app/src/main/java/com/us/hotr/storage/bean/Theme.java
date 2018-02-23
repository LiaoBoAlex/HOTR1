package com.us.hotr.storage.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liaobo on 2018/1/5.
 */

public class Theme implements Serializable {
    private long key;
    private String themeName;
    private List<Group> coshowList;

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public List<Group> getCoshowList() {
        return coshowList;
    }

    public void setCoshowList(List<Group> coshowList) {
        this.coshowList = coshowList;
    }
}
