package com.us.hotr.storage.bean;

import java.io.Serializable;

/**
 * Created by liaobo on 2018/2/11.
 */

public class HotSearchTopic implements Serializable {
    private String project_name;

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }
}
