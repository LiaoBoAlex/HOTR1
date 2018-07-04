package com.us.hotr.webservice.response;

import com.us.hotr.storage.bean.Case;
import com.us.hotr.storage.bean.Group;
import com.us.hotr.storage.bean.Hospital;
import com.us.hotr.storage.bean.Massage;
import com.us.hotr.storage.bean.Masseur;
import com.us.hotr.storage.bean.Module;
import com.us.hotr.storage.bean.Post;
import com.us.hotr.storage.bean.Product;
import com.us.hotr.storage.bean.Spa;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liaobo on 2018/2/8.
 */

public class GetHomePageResponse implements Serializable {
    private List<Case> recommendContrastPhotoList;
    private List<Post> recommendHotTopicList;
    private List<Module> listHomePageModule;
    private List<Masseur> recommendMassagistList;
    private List<Spa> recommendMassageList;
    private List<Group>myGrouList;
    private List<Product> recommendYmProductList;
    private List<Massage> recommendAmProductList;
    private List<Hospital> recommendHospitalList;
    private int total;

    public List<Hospital> getRecommendHospitalList() {
        return recommendHospitalList;
    }

    public void setRecommendHospitalList(List<Hospital> recommendHospitalList) {
        this.recommendHospitalList = recommendHospitalList;
    }

    public List<Massage> getRecommendAmProductList() {
        return recommendAmProductList;
    }

    public void setRecommendAmProductList(List<Massage> recommendAmProductList) {
        this.recommendAmProductList = recommendAmProductList;
    }

    public List<Product> getRecommendYmProductList() {
        return recommendYmProductList;
    }

    public void setRecommendYmProductList(List<Product> recommendYmProductList) {
        this.recommendYmProductList = recommendYmProductList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Group> getMyGrouList() {
        return myGrouList;
    }

    public void setMyGrouList(List<Group> myGrouList) {
        this.myGrouList = myGrouList;
    }

    public List<Masseur> getRecommendMassagistList() {
        return recommendMassagistList;
    }

    public void setRecommendMassagistList(List<Masseur> recommendMassagistList) {
        this.recommendMassagistList = recommendMassagistList;
    }

    public List<Spa> getRecommendMassageList() {
        return recommendMassageList;
    }

    public void setRecommendMassageList(List<Spa> recommendMassageList) {
        this.recommendMassageList = recommendMassageList;
    }

    public List<Case> getRecommendContrastPhotoList() {
        return recommendContrastPhotoList;
    }

    public void setRecommendContrastPhotoList(List<Case> recommendContrastPhotoList) {
        this.recommendContrastPhotoList = recommendContrastPhotoList;
    }

    public List<Post> getRecommendHotTopicList() {
        return recommendHotTopicList;
    }

    public void setRecommendHotTopicList(List<Post> recommendHotTopicList) {
        this.recommendHotTopicList = recommendHotTopicList;
    }

    public List<Module> getListHomePageModule() {
        return listHomePageModule;
    }

    public void setListHomePageModule(List<Module> listHomePageModule) {
        this.listHomePageModule = listHomePageModule;
    }
}
