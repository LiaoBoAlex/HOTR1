package com.us.hotr.storage.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mloong on 2017/8/29.
 */

public class Provence implements Serializable {

    private String areaName;
    private List<City> children;

    public Provence(String areaName){
        this.areaName = areaName;
    }

    public Provence(String areaName, List<City> children){
        this.areaName = areaName;
        this.children = children;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public List<City> getCityList() {
        return children;
    }

    public void setCityList(List<City> children) {
        this.children = children;
    }

    public class City{
        private int key;
        private String areaName;
        private int isHotcity;

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public int getIsHotcity() {
            return isHotcity;
        }

        public void setIsHotcity(int isHotcity) {
            this.isHotcity = isHotcity;
        }
    }
}
