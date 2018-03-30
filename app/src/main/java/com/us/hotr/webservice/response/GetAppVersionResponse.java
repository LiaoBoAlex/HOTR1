package com.us.hotr.webservice.response;

import java.io.Serializable;

/**
 * Created by liaobo on 2018/3/29.
 */

public class GetAppVersionResponse implements Serializable {
    private boolean update_force;
    private boolean result;
    private String app_path;

    public boolean isUpdate_force() {
        return update_force;
    }

    public void setUpdate_force(boolean update_force) {
        this.update_force = update_force;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getApp_path() {
        return app_path;
    }

    public void setApp_path(String app_path) {
        this.app_path = app_path;
    }
}
