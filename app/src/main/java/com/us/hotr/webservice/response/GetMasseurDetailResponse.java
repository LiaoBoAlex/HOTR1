package com.us.hotr.webservice.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liaobo on 2017/12/13.
 */

public class GetMasseurDetailResponse implements Serializable {
    private Masseur massagist;
    private List<Product> productList;
    private List<Type> typeList;
    private int job_time;
    private Spa massage;
    private int sumOrderNum;


    public int getJob_time() {
        return job_time;
    }

    public void setJob_time(int job_time) {
        this.job_time = job_time;
    }

    public Masseur getMassagist() {
        return massagist;
    }

    public void setMassagist(Masseur massagist) {
        this.massagist = massagist;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public List<Type> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<Type> typeList) {
        this.typeList = typeList;
    }

    public Spa getMassage() {
        return massage;
    }

    public void setMassage(Spa massage) {
        this.massage = massage;
    }

    public int getSumOrderNum() {
        return sumOrderNum;
    }

    public void setSumOrderNum(int sumOrderNum) {
        this.sumOrderNum = sumOrderNum;
    }

    public class Spa{
        private int key;
        private String landmark_name;
        private String provinceName;
        private String cityName;
        private String areaName;
        private String massageAddress;
        private String massageLogo;
        private String massageName;
        private String openTimeOver;
        private String openTimeStart;

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public String getLandmark_name() {
            return landmark_name;
        }

        public void setLandmark_name(String landmark_name) {
            this.landmark_name = landmark_name;
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

        public String getMassageLogo() {
            return massageLogo;
        }

        public void setMassageLogo(String massageLogo) {
            this.massageLogo = massageLogo;
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
    public class Product{
        private int service_time;
        private int id;
        private int online_price;
        private int shop_price;
        private String product_usp;
        private int order_num;
        private int activity_price;
        private String product_name;
        private String product_main_img;

        public int getService_time() {
            return service_time;
        }

        public void setService_time(int service_time) {
            this.service_time = service_time;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getOnline_price() {
            return online_price;
        }

        public void setOnline_price(int online_price) {
            this.online_price = online_price;
        }

        public int getShop_price() {
            return shop_price;
        }

        public void setShop_price(int shop_price) {
            this.shop_price = shop_price;
        }

        public String getProduct_usp() {
            return product_usp;
        }

        public void setProduct_usp(String product_usp) {
            this.product_usp = product_usp;
        }

        public int getOrder_num() {
            return order_num;
        }

        public void setOrder_num(int order_num) {
            this.order_num = order_num;
        }

        public int getActivity_price() {
            return activity_price;
        }

        public void setActivity_price(int activity_price) {
            this.activity_price = activity_price;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public String getProduct_main_img() {
            return product_main_img;
        }

        public void setProduct_main_img(String product_main_img) {
            this.product_main_img = product_main_img;
        }
    }
    public class Masseur{
        private int key;
        private int massagistHeight;
        private String massagistInfo;
        private String massagistName;
        private String provinceName;
        private String cityName;
        private String areaName;
        private String massagistPhotos;

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public int getMassagistHeight() {
            return massagistHeight;
        }

        public void setMassagistHeight(int massagistHeight) {
            this.massagistHeight = massagistHeight;
        }

        public String getMassagistInfo() {
            return massagistInfo;
        }

        public void setMassagistInfo(String massagistInfo) {
            this.massagistInfo = massagistInfo;
        }

        public String getMassagistName() {
            return massagistName;
        }

        public void setMassagistName(String massagistName) {
            this.massagistName = massagistName;
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

        public String getMassagistPhotos() {
            return massagistPhotos;
        }

        public void setMassagistPhotos(String massagistPhotos) {
            this.massagistPhotos = massagistPhotos;
        }
    }
}
