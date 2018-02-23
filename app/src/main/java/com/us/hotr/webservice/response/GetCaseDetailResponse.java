package com.us.hotr.webservice.response;

import com.us.hotr.storage.bean.Case;
import com.us.hotr.storage.bean.Comment;
import com.us.hotr.storage.bean.Doctor;
import com.us.hotr.storage.bean.Hospital;
import com.us.hotr.storage.bean.Product;
import com.us.hotr.storage.bean.User;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liaobo on 2018/2/12.
 */

public class GetCaseDetailResponse implements Serializable {
    private Product product;
    private List<Comment> ymContrastPhotoComment;
    private int is_like_contrast_photo;
    private int is_collect_contrast_photo;
    private int is_attention;
    private Case ymContrastPhoto;
    private User user;
//    private Hospital hospital;
//    private Doctor doctor;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Comment> getYmContrastPhotoComment() {
        return ymContrastPhotoComment;
    }

    public void setYmContrastPhotoComment(List<Comment> ymContrastPhotoComment) {
        this.ymContrastPhotoComment = ymContrastPhotoComment;
    }

    public int getIs_like_contrast_photo() {
        return is_like_contrast_photo;
    }

    public void setIs_like_contrast_photo(int is_like_contrast_photo) {
        this.is_like_contrast_photo = is_like_contrast_photo;
    }

    public int getIs_collect_contrast_photo() {
        return is_collect_contrast_photo;
    }

    public void setIs_collect_contrast_photo(int is_collect_contrast_photo) {
        this.is_collect_contrast_photo = is_collect_contrast_photo;
    }

    public int getIs_attention() {
        return is_attention;
    }

    public void setIs_attention(int is_attention) {
        this.is_attention = is_attention;
    }

    public Case getYmContrastPhoto() {
        return ymContrastPhoto;
    }

    public void setYmContrastPhoto(Case ymContrastPhoto) {
        this.ymContrastPhoto = ymContrastPhoto;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

//    public Hospital getHospital() {
//        return hospital;
//    }
//
//    public void setHospital(Hospital hospital) {
//        this.hospital = hospital;
//    }
//
//    public Doctor getDoctor() {
//        return doctor;
//    }
//
//    public void setDoctor(Doctor doctor) {
//        this.doctor = doctor;
//    }
}
