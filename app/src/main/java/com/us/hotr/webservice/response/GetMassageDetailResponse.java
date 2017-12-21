package com.us.hotr.webservice.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liaobo on 2017/12/13.
 */

public class GetMassageDetailResponse implements Serializable {
    private Spa massage;
    private Product product;
    private List<com.us.hotr.storage.bean.Masseur> MassagistList;
    private List<Masseur> proposeMassagist;
    private int productOrderNum;
    private int totalMasseurCount;

    public int getTotalMasseurCount() {
        return totalMasseurCount;
    }

    public void setTotalMasseurCount(int totalMasseurCount) {
        this.totalMasseurCount = totalMasseurCount;
    }

    public int getProductOrderNum() {
        return productOrderNum;
    }

    public void setProductOrderNum(int productOrderNum) {
        this.productOrderNum = productOrderNum;
    }

    public Spa getMassage() {
        return massage;
    }

    public void setMassage(Spa massage) {
        this.massage = massage;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<com.us.hotr.storage.bean.Masseur> getMassageistList() {
        return MassagistList;
    }

    public void setMassageistList(List<com.us.hotr.storage.bean.Masseur> massageistList) {
        MassagistList = massageistList;
    }

    public List<Masseur> getProposeMassagist() {
        return proposeMassagist;
    }

    public void setProposeMassagist(List<Masseur> proposeMassagist) {
        this.proposeMassagist = proposeMassagist;
    }

    public class Spa implements Serializable{
        private int key;
        private String provinceName;
        private String cityName;
        private String areaName;
        private String massageAddress;
        private String massageLogo;
        private String massageName;
        private String openTimeOver;
        private String openTimeStart;
        private String landmarkName;

        public String getLandmark_name() {
            return landmarkName;
        }

        public void setLandmark_name(String landmark_name) {
            this.landmarkName = landmark_name;
        }

        public int getId() {
            return key;
        }

        public void setId(int id) {
            this.key = id;
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
    public class Masseur implements Serializable{
        private int id;
        private String massagist_main_img;
        private int order_num;
        private String job_start_time;
        private String massagist_name;
        private int massagist_height;
        private String massage_address;
        private int job_time;

        public int getJob_time() {
            return job_time;
        }

        public void setJob_time(int job_time) {
            this.job_time = job_time;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMassagist_main_img() {
            return massagist_main_img;
        }

        public void setMassagist_main_img(String massagist_main_img) {
            this.massagist_main_img = massagist_main_img;
        }

        public int getOrder_num() {
            return order_num;
        }

        public void setOrder_num(int order_num) {
            this.order_num = order_num;
        }

        public String getJob_start_time() {
            return job_start_time;
        }

        public void setJob_start_time(String job_start_time) {
            this.job_start_time = job_start_time;
        }

        public String getMassagist_name() {
            return massagist_name;
        }

        public void setMassagist_name(String massagist_name) {
            this.massagist_name = massagist_name;
        }

        public int getMassagist_height() {
            return massagist_height;
        }

        public void setMassagist_height(int massagist_height) {
            this.massagist_height = massagist_height;
        }

        public String getMassage_address() {
            return massage_address;
        }

        public void setMassage_address(String massage_address) {
            this.massage_address = massage_address;
        }
    }
    public class Product implements Serializable{
        private int key;
        private int activityCount;
        private int activityPrice;
        private String beginTime;
        private String endTime;
        private int massageId;
        private int onlinePrice;
        private String productCode;
        private String productDetail;
        private String productImg;
        private String productName;
        private int productType;
        private String productUsp;
        private int serviceTime;
        private int shopPrice;
        private int singleCount;
        private int typeId;
        private int indate;
        private String usableTime;

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public int getActivityCount() {
            return activityCount;
        }

        public void setActivityCount(int activityCount) {
            this.activityCount = activityCount;
        }

        public int getActivityPrice() {
            return activityPrice;
        }

        public void setActivityPrice(int activityPrice) {
            this.activityPrice = activityPrice;
        }

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getMassageId() {
            return massageId;
        }

        public void setMassageId(int massageId) {
            this.massageId = massageId;
        }

        public int getOnlinePrice() {
            return onlinePrice;
        }

        public void setOnlinePrice(int onlinePrice) {
            this.onlinePrice = onlinePrice;
        }

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public String getProductDetail() {
            return productDetail;
        }

        public void setProductDetail(String productDetail) {
            this.productDetail = productDetail;
        }

        public String getProductImg() {
            return productImg;
        }

        public void setProductImg(String productImg) {
            this.productImg = productImg;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public int getProductType() {
            return productType;
        }

        public void setProductType(int productType) {
            this.productType = productType;
        }

        public String getProductUsp() {
            return productUsp;
        }

        public void setProductUsp(String productUsp) {
            this.productUsp = productUsp;
        }

        public int getServiceTime() {
            return serviceTime;
        }

        public void setServiceTime(int serviceTime) {
            this.serviceTime = serviceTime;
        }

        public int getShopPrice() {
            return shopPrice;
        }

        public void setShopPrice(int shopPrice) {
            this.shopPrice = shopPrice;
        }

        public int getSingleCount() {
            return singleCount;
        }

        public void setSingleCount(int singleCount) {
            this.singleCount = singleCount;
        }

        public int getTypeId() {
            return typeId;
        }

        public void setTypeId(int typeId) {
            this.typeId = typeId;
        }

        public int getIndate() {
            return indate;
        }

        public void setIndate(int indate) {
            this.indate = indate;
        }

        public String getUsableTime() {
            return usableTime;
        }

        public void setUsableTime(String usableTime) {
            this.usableTime = usableTime;
        }
    }
}
