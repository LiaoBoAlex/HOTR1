package com.us.hotr.webservice.response;

import com.us.hotr.storage.bean.Massage;
import com.us.hotr.storage.bean.Masseur;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liaobo on 2017/12/14.
 */

public class GetSpaDetailResponse implements Serializable {
    private Spa massage;
    private List<Type> typeList;
    private List<Masseur> masseurList;
    private List<Massage> massageList;
    private int totalMassageCount;
    private int totalMasseurCount;

    public int getTotalMasseurCount() {
        return totalMasseurCount;
    }

    public void setTotalMasseurCount(int totalMasseurCount) {
        this.totalMasseurCount = totalMasseurCount;
    }

    public Spa getMassage() {
        return massage;
    }

    public void setMassage(Spa massage) {
        this.massage = massage;
    }

    public List<Type> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<Type> typeList) {
        this.typeList = typeList;
    }

    public List<Masseur> getMasseurList() {
        return masseurList;
    }

    public void setMasseurList(List<Masseur> masseurList) {
        this.masseurList = masseurList;
    }

    public List<Massage> getMassageList() {
        return massageList;
    }

    public void setMassageList(List<Massage> massageList) {
        this.massageList = massageList;
    }

    public int getTotalMassageCount() {
        return totalMassageCount;
    }

    public void setTotalMassageCount(int totalMassageCount) {
        this.totalMassageCount = totalMassageCount;
    }

    public class Spa implements Serializable {
        private int key;
        private String provinceName;
        private String cityName;
        private String areaName;
        private String massageAddress;
        private String massageInfo;
        private String massagePhotos;
        private String massageName;
        private String openTimeOver;
        private String openTimeStart;

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public String getMassageAddress() {
            return massageAddress;
        }

        public void setMassageAddress(String massageAddress) {
            this.massageAddress = massageAddress;
        }

        public String getMassageInfo() {
            return massageInfo;
        }

        public void setMassageInfo(String massageInfo) {
            this.massageInfo = massageInfo;
        }

        public String getMassagePhotos() {
            return massagePhotos;
        }

        public void setMassagePhotos(String massagePhotos) {
            this.massagePhotos = massagePhotos;
        }

        public String getMassageName() {
            return massageName;
        }

        public void setMassageName(String massageName) {
            this.massageName = massageName;
        }

        public String getOpenTimeOver() {
            return openTimeOver;
        }

        public void setOpenTimeOver(String openTimeOver) {
            this.openTimeOver = openTimeOver;
        }

        public String getOpenTimeStart() {
            return openTimeStart;
        }

        public void setOpenTimeStart(String openTimeStart) {
            this.openTimeStart = openTimeStart;
        }
    }

    public class Type{
        private int id;
        private int order_num;
        private String type_name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getOrder_num() {
            return order_num;
        }

        public void setOrder_num(int order_num) {
            this.order_num = order_num;
        }

        public String getType_name() {
            return type_name;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
        }
    }
}
