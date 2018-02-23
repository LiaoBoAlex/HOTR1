package com.us.hotr.webservice.response;

import com.us.hotr.storage.bean.Comment;
import com.us.hotr.storage.bean.Doctor;
import com.us.hotr.storage.bean.Group;
import com.us.hotr.storage.bean.Hospital;
import com.us.hotr.storage.bean.Massage;
import com.us.hotr.storage.bean.Masseur;
import com.us.hotr.storage.bean.Post;
import com.us.hotr.storage.bean.Product;
import com.us.hotr.storage.bean.Spa;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liaobo on 2018/1/11.
 */

public class GetPostDetailResponse implements Serializable {
    private Post hotTopic;
    private List<Group> listCoshow;
    private List<Hospital> link_hospital_list;
    private List<Masseur> link_technician_list;
    private List<Spa> link_ammerchant_list;
    private List<Doctor> link_doctor_list;
    private List<Massage> link_amproduct_list;
    private List<Product> link_ymproduct_list;
    private List<Comment> listComment;

    public Post getHotTopic() {
        return hotTopic;
    }

    public void setHotTopic(Post hotTopic) {
        this.hotTopic = hotTopic;
    }

    public List<Group> getListCoshow() {
        return listCoshow;
    }

    public void setListCoshow(List<Group> listCoshow) {
        this.listCoshow = listCoshow;
    }

    public List<Hospital> getLink_hospital_list() {
        return link_hospital_list;
    }

    public void setLink_hospital_list(List<Hospital> link_hospital_list) {
        this.link_hospital_list = link_hospital_list;
    }

    public List<Masseur> getLink_technician_list() {
        return link_technician_list;
    }

    public void setLink_technician_list(List<Masseur> link_technician_list) {
        this.link_technician_list = link_technician_list;
    }

    public List<Spa> getLink_ammerchant_list() {
        return link_ammerchant_list;
    }

    public void setLink_ammerchant_list(List<Spa> link_ammerchant_list) {
        this.link_ammerchant_list = link_ammerchant_list;
    }

    public List<Doctor> getLink_doctor_list() {
        return link_doctor_list;
    }

    public void setLink_doctor_list(List<Doctor> link_doctor_list) {
        this.link_doctor_list = link_doctor_list;
    }

    public List<Massage> getLink_amproduct_list() {
        return link_amproduct_list;
    }

    public void setLink_amproduct_list(List<Massage> link_amproduct_list) {
        this.link_amproduct_list = link_amproduct_list;
    }

    public List<Product> getLink_ymproduct_list() {
        return link_ymproduct_list;
    }

    public void setLink_ymproduct_list(List<Product> link_ymproduct_list) {
        this.link_ymproduct_list = link_ymproduct_list;
    }

    public List<Comment> getListComment() {
        return listComment;
    }

    public void setListComment(List<Comment> listComment) {
        this.listComment = listComment;
    }
}
