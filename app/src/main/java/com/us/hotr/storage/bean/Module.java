package com.us.hotr.storage.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liaobo on 2018/2/8.
 */

public class Module implements Serializable {
    private long moduleAreaId;
    private String moduleName;
    private int moduleHeight;
    private String moduleBackgroundColor;
    private long key;
    private int isShow;
    private int moduleTypeId;
    private List<ModuleContent> bannerList;

    public long getModuleAreaId() {
        return moduleAreaId;
    }

    public void setModuleAreaId(long moduleAreaId) {
        this.moduleAreaId = moduleAreaId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public int getModuleHeight() {
        return moduleHeight;
    }

    public void setModuleHeight(int moduleHeight) {
        this.moduleHeight = moduleHeight;
    }

    public String getModuleBackgroundColor() {
        return moduleBackgroundColor;
    }

    public void setModuleBackgroundColor(String moduleBackgroundColor) {
        this.moduleBackgroundColor = moduleBackgroundColor;
    }

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public int getModuleTypeId() {
        return moduleTypeId;
    }

    public void setModuleTypeId(int moduleTypeId) {
        this.moduleTypeId = moduleTypeId;
    }

    public List<ModuleContent> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<ModuleContent> bannerList) {
        this.bannerList = bannerList;
    }

    public class ModuleContent implements Serializable{
        private long moduleId;
        private String bannerName;
        private String bannerImg;
        private int linkTypeId;
        private String linkUrl;

        public long getModuleId() {
            return moduleId;
        }

        public void setModuleId(long moduleId) {
            this.moduleId = moduleId;
        }

        public String getBannerName() {
            return bannerName;
        }

        public void setBannerName(String bannerName) {
            this.bannerName = bannerName;
        }

        public String getBannerImg() {
            return bannerImg;
        }

        public void setBannerImg(String bannerImg) {
            this.bannerImg = bannerImg;
        }

        public int getLinkTypeId() {
            return linkTypeId;
        }

        public void setLinkTypeId(int linkTypeId) {
            this.linkTypeId = linkTypeId;
        }

        public String getLinkUrl() {
            return linkUrl;
        }

        public void setLinkUrl(String linkUrl) {
            this.linkUrl = linkUrl;
        }
    }
}
