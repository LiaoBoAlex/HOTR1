package com.us.hotr.webservice.response;

import com.us.hotr.storage.bean.Doctor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liaobo on 2017/12/19.
 */

public class GetProductDetailResponse implements Serializable {
    private int productOrderNum;
    private Product product;
    private List<Promise> promiseList;
    private Hospital hospital;
    private Doctor doctor;
    private List<com.us.hotr.storage.bean.Product> proposedProduct;
    private int proposedProductCount;
    private int is_collected;

    public int getIs_collected() {
        return is_collected;
    }

    public void setIs_collected(int is_collected) {
        this.is_collected = is_collected;
    }

    public List<com.us.hotr.storage.bean.Product> getProposedProduct() {
        return proposedProduct;
    }

    public void setProposedProduct(List<com.us.hotr.storage.bean.Product> proposedProduct) {
        this.proposedProduct = proposedProduct;
    }

    public int getProposedProductCount() {
        return proposedProductCount;
    }

    public void setProposedProductCount(int proposedProductCount) {
        this.proposedProductCount = proposedProductCount;
    }

    public int getProductOrderNum() {
        return productOrderNum;
    }

    public void setProductOrderNum(int productOrderNum) {
        this.productOrderNum = productOrderNum;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Promise> getPromiseList() {
        return promiseList;
    }

    public void setPromiseList(List<Promise> promiseList) {
        this.promiseList = promiseList;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public class Promise implements Serializable{
        private long id;
        private String promise_title;
        private String promise_content;
        private String promise_info;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getPromise_title() {
            return promise_title;
        }

        public void setPromise_title(String promise_title) {
            this.promise_title = promise_title;
        }

        public String getPromise_content() {
            return promise_content;
        }

        public void setPromise_content(String promise_content) {
            this.promise_content = promise_content;
        }

        public String getPromise_info() {
            return promise_info;
        }

        public void setPromise_info(String promise_info) {
            this.promise_info = promise_info;
        }
    }
    public class Hospital implements Serializable{
        private long key;
        private String provinceName;
        private String cityName;
        private String areaName;
        private String hospAddress;
        private String hospName;
        private String hospType;
        private String hospLogo;
        private String hospPhotos;

        public String getHospPhotos() {
            return hospPhotos;
        }

        public void setHospPhotos(String hospPhotos) {
            this.hospPhotos = hospPhotos;
        }

        public String getHospLogo() {
            return hospLogo;
        }

        public void setHospLogo(String hospLogo) {
            this.hospLogo = hospLogo;
        }

        public long getKey() {
            return key;
        }

        public void setKey(long key) {
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

        public String getHospAddress() {
            return hospAddress;
        }

        public void setHospAddress(String hospAddress) {
            this.hospAddress = hospAddress;
        }

        public String getHospName() {
            return hospName;
        }

        public void setHospName(String hospName) {
            this.hospName = hospName;
        }

        public String getHospType() {
            return hospType;
        }

        public void setHospType(String hospType) {
            this.hospType = hospType;
        }
    }

    public class Product implements Serializable{
        private long key;
        private double activityOnlinePrice;
        private double activityPrepayment;
        private double onlinePrice;
        private int paymentType;
        private double prepayment;
        private String productDetail;
        private String productImg;
        private String productName;
        private int productType;
        private String productUsp;
        private double shopPrice;
        private int activityCount;
        private String usableTime;
        private int onsaleState;
        private int checkState;

        public boolean isProductVaiable(){
            if(onsaleState == 1 && checkState ==1)
                return  true;
            else
                return false;
        }

        public String getUsableTime() {
            return usableTime;
        }

        public void setUsableTime(String usableTime) {
            this.usableTime = usableTime;
        }

        public long getKey() {
            return key;
        }

        public void setKey(long key) {
            this.key = key;
        }

        public double getActivityOnlinePrice() {
            return activityOnlinePrice;
        }

        public void setActivityOnlinePrice(double activityOnlinePrice) {
            this.activityOnlinePrice = activityOnlinePrice;
        }

        public double getActivityPrepayment() {
            return activityPrepayment;
        }

        public void setActivityPrepayment(double activityPrepayment) {
            this.activityPrepayment = activityPrepayment;
        }

        public double getOnlinePrice() {
            return onlinePrice;
        }

        public void setOnlinePrice(double onlinePrice) {
            this.onlinePrice = onlinePrice;
        }

        public int getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(int paymentType) {
            this.paymentType = paymentType;
        }

        public double getPrepayment() {
            return prepayment;
        }

        public void setPrepayment(double prepayment) {
            this.prepayment = prepayment;
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

        public double getShopPrice() {
            return shopPrice;
        }

        public void setShopPrice(double shopPrice) {
            this.shopPrice = shopPrice;
        }

        public int getActivityCount() {
            return activityCount;
        }

        public void setActivityCount(int activityCount) {
            this.activityCount = activityCount;
        }
    }
}
