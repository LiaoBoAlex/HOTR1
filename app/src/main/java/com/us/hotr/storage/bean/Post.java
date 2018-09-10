package com.us.hotr.storage.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liaobo on 2017/12/27.
 */

public class Post implements Serializable {
    @SerializedName(value = "show_img", alternate = {"showImg"})
    private String show_img;
    private Long id;
    private String link_ammerchant_id;
    private String link_hospital_id;
    private String link_amproduct_id;
    private String link_doctor_id;
    private String link_ymproduct_id;
    private String link_technician_id;
    private Integer read_cnt;
    private String nick_name;
    private String title;
    private String head_portrait;
    private Integer comment_cnt;
    private Integer like_cnt;
    private Integer is_new;
    //普通帖(文字)
    @SerializedName(value = "contentWord", alternate = {"content_word"})
    private String contentWord;
    private Long user_id;
    private Integer is_comment;
    private Integer user_type;
    @SerializedName(value = "isOfficial", alternate = {"is_official"})
    private int isOfficial;
    private Integer is_like;
    private Integer is_attention;
    //官方帖 内容
    private String content;
    private String create_time;
    @SerializedName(value = "contentImg", alternate = {"content_img"})
    private String contentImg;
    private List<Group> listCoshow;
    private String city_name;
    private String province_name;
    private Integer is_collect;
    private Integer age;
    private Integer gender;
    @SerializedName(value = "topicType", alternate = {"topic_type"})
    private int topicType;
    private Integer u_user_type;
    private Long massagist_id;

    public Integer getU_user_type() {
        return u_user_type;
    }

    public void setU_user_type(Integer u_user_type) {
        this.u_user_type = u_user_type;
    }

    public Long getMassagist_id() {
        return massagist_id;
    }

    public void setMassagist_id(Long massagist_id) {
        this.massagist_id = massagist_id;
    }

    public Integer getIs_new() {
        return is_new;
    }

    public void setIs_new(Integer is_new) {
        this.is_new = is_new;
    }

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    public String getShow_img() {
        return show_img;
    }

    public void setShow_img(String show_img) {
        this.show_img = show_img;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLink_ammerchant_id() {
        return link_ammerchant_id;
    }

    public void setLink_ammerchant_id(String link_ammerchant_id) {
        this.link_ammerchant_id = link_ammerchant_id;
    }

    public String getLink_hospital_id() {
        return link_hospital_id;
    }

    public void setLink_hospital_id(String link_hospital_id) {
        this.link_hospital_id = link_hospital_id;
    }

    public String getLink_amproduct_id() {
        return link_amproduct_id;
    }

    public void setLink_amproduct_id(String link_amproduct_id) {
        this.link_amproduct_id = link_amproduct_id;
    }

    public String getLink_doctor_id() {
        return link_doctor_id;
    }

    public void setLink_doctor_id(String link_doctor_id) {
        this.link_doctor_id = link_doctor_id;
    }

    public String getLink_ymproduct_id() {
        return link_ymproduct_id;
    }

    public void setLink_ymproduct_id(String link_ymproduct_id) {
        this.link_ymproduct_id = link_ymproduct_id;
    }

    public String getLink_technician_id() {
        return link_technician_id;
    }

    public void setLink_technician_id(String link_technician_id) {
        this.link_technician_id = link_technician_id;
    }

    public Integer getRead_cnt() {
        return read_cnt;
    }

    public void setRead_cnt(Integer read_cnt) {
        this.read_cnt = read_cnt;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHead_portrait() {
        return head_portrait;
    }

    public void setHead_portrait(String head_portrait) {
        this.head_portrait = head_portrait;
    }

    public Integer getComment_cnt() {
        return comment_cnt;
    }

    public void setComment_cnt(Integer comment_cnt) {
        this.comment_cnt = comment_cnt;
    }

    public Integer getLike_cnt() {
        return like_cnt;
    }

    public void setLike_cnt(Integer like_cnt) {
        this.like_cnt = like_cnt;
    }

    public String getContentWord() {
        return contentWord;
    }

    public void setContentWord(String contentWord) {
        this.contentWord = contentWord;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Integer getIs_comment() {
        return is_comment;
    }

    public void setIs_comment(Integer is_comment) {
        this.is_comment = is_comment;
    }

    public Integer getUser_type() {
        return user_type;
    }

    public void setUser_type(Integer user_type) {
        this.user_type = user_type;
    }

    public int getIsOfficial() {
        return isOfficial;
    }

    public void setIsOfficial(int isOfficial) {
        this.isOfficial = isOfficial;
    }

    public Integer getIs_like() {
        return is_like;
    }

    public void setIs_like(Integer is_like) {
        this.is_like = is_like;
    }

    public Integer getIs_attention() {
        return is_attention;
    }

    public void setIs_attention(Integer is_attention) {
        this.is_attention = is_attention;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getContentImg() {
        return contentImg;
    }

    public void setContentImg(String contentImg) {
        this.contentImg = contentImg;
    }

    public List<Group> getListCoshow() {
        return listCoshow;
    }

    public void setListCoshow(List<Group> listCoshow) {
        this.listCoshow = listCoshow;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public Integer getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(Integer is_collect) {
        this.is_collect = is_collect;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public int getTopicType() {
        return topicType;
    }

    public void setTopicType(int topicType) {
        this.topicType = topicType;
    }
}
