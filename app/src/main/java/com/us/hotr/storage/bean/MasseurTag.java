package com.us.hotr.storage.bean;

import java.io.Serializable;

public class MasseurTag implements Serializable{
    private int tab_level;
    private long id;
    private String tab_content;
    private int tab_num;

    public int getTab_num() {
        return tab_num;
    }

    public void setTab_num(int tab_num) {
        this.tab_num = tab_num;
    }

    public int getTab_level() {
        return tab_level;
    }

    public void setTab_level(int tab_level) {
        this.tab_level = tab_level;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTab_content() {
        return tab_content;
    }

    public void setTab_content(String tab_content) {
        this.tab_content = tab_content;
    }
}
