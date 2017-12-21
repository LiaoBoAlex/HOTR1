package com.us.hotr.webservice.response;

import com.us.hotr.storage.bean.Product;

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
        private int id;
        private String promise_title;
        private String promise_content;
        private String promise_info;

        public int getId() {
            return id;
        }

        public void setId(int id) {
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
        private int key;
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

    public class Doctor implements Serializable{
        private int id;
        private String docName;
        private String typeName;
        private String docPhoto;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDocName() {
            return docName;
        }

        public void setDocName(String docName) {
            this.docName = docName;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getDocPhoto() {
            return docPhoto;
        }

        public void setDocPhoto(String docPhoto) {
            this.docPhoto = docPhoto;
        }
    }

    public class Product implements Serializable{
        private int key;
        private int activityOnlinePrice;
        private int activityPrepayment;
        private int onlinePrice;
        private int paymentType;
        private int prepayment;
        private String productDetail;
        private String productImg;
        private String productName;
        private int productType;
        private String productUsp;
        private int shopPrice;
        private int activityCount;

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public int getActivityOnlinePrice() {
            return activityOnlinePrice;
        }

        public void setActivityOnlinePrice(int activityOnlinePrice) {
            this.activityOnlinePrice = activityOnlinePrice;
        }

        public int getActivityPrepayment() {
            return activityPrepayment;
        }

        public void setActivityPrepayment(int activityPrepayment) {
            this.activityPrepayment = activityPrepayment;
        }

        public int getOnlinePrice() {
            return onlinePrice;
        }

        public void setOnlinePrice(int onlinePrice) {
            this.onlinePrice = onlinePrice;
        }

        public int getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(int paymentType) {
            this.paymentType = paymentType;
        }

        public int getPrepayment() {
            return prepayment;
        }

        public void setPrepayment(int prepayment) {
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

        public int getShopPrice() {
            return shopPrice;
        }

        public void setShopPrice(int shopPrice) {
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
