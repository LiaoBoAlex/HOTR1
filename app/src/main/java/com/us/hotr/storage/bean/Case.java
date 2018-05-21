package com.us.hotr.storage.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liaobo on 2017/12/7.
 */

public class Case implements Serializable {
    @SerializedName(value = "key", alternate = {"id"})
    private Long key;
    @SerializedName(value = "contrastPhotoTitle", alternate = {"contrast_photo_title"})
    private String contrastPhotoTitle;
    @SerializedName(value = "beforeOperationPhoto", alternate = {"before_operation_photo"})
    private String beforeOperationPhoto;
    @SerializedName(value = "afterOperationPhoto", alternate = {"after_operation_photo"})
    private String afterOperationPhoto;
    @SerializedName(value = "contrastPhotoContent", alternate = {"contrast_photo_content"})
    private String contrastPhotoContent;
    @SerializedName(value = "createTime", alternate = {"create_time"})
    private String createTime;
    @SerializedName(value = "hospitalId", alternate = {"hospital_id"})
    private long hospitalId;
    @SerializedName(value = "hospitalName", alternate = {"hospital_name"})
    private String hospitalName;
    @SerializedName(value = "doctorId", alternate = {"doctor_id"})
    private long doctorId;
    @SerializedName(value = "doctorName", alternate = {"doctor_name"})
    private String doctorName;
    @SerializedName(value = "projectId", alternate = {"project_id"})
    private long projectId;
    @SerializedName(value = "projectName", alternate = {"project_name"})
    private String projectName;
    @SerializedName(value = "productId", alternate = {"product_id"})
    private long productId;
    @SerializedName(value = "productName", alternate = {"product_name"})
    private String productName;
    @SerializedName(value = "userId", alternate = {"user_id"})
    private Long userId;
    @SerializedName(value = "commentCnt", alternate = {"comment_cnt"})
    private Integer commentCnt;
    @SerializedName(value = "readCnt", alternate = {"read_cnt"})
    private Integer readCnt;
    @SerializedName(value = "likeCnt", alternate = {"like_cnt"})
    private Integer likeCnt;
    @SerializedName(value = "isComment", alternate = {"is_comment"})
    private Integer isComment;
    private Integer is_like;
    private String nick_name;
    private String head_portrait;
    private Integer is_attention;
    private String filePathBefore;
    private String filePathAfter;
    private Product product;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getFilePathBefore() {
        return filePathBefore;
    }

    public void setFilePathBefore(String filePathBefore) {
        this.filePathBefore = filePathBefore;
    }

    public String getFilePathAfter() {
        return filePathAfter;
    }

    public void setFilePathAfter(String filePathAfter) {
        this.filePathAfter = filePathAfter;
    }

    public int getIs_like() {
        return is_like;
    }

    public void setIs_like(int is_like) {
        this.is_like = is_like;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getHead_portrait() {
        return head_portrait;
    }

    public void setHead_portrait(String head_portrait) {
        this.head_portrait = head_portrait;
    }

    public Integer getIs_attention() {
        return is_attention;
    }

    public void setIs_attention(int is_attention) {
        this.is_attention = is_attention;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public String getContrastPhotoTitle() {
        return contrastPhotoTitle;
    }

    public void setContrastPhotoTitle(String contrastPhotoTitle) {
        this.contrastPhotoTitle = contrastPhotoTitle;
    }

    public String getBeforeOperationPhoto() {
        return beforeOperationPhoto;
    }

    public void setBeforeOperationPhoto(String beforeOperationPhoto) {
        this.beforeOperationPhoto = beforeOperationPhoto;
    }

    public String getAfterOperationPhoto() {
        return afterOperationPhoto;
    }

    public void setAfterOperationPhoto(String afterOperationPhoto) {
        this.afterOperationPhoto = afterOperationPhoto;
    }

    public String getContrastPhotoContent() {
        return contrastPhotoContent;
    }

    public void setContrastPhotoContent(String contrastPhotoContent) {
        this.contrastPhotoContent = contrastPhotoContent;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(long hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(long doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getCommentCnt() {
        return commentCnt;
    }

    public void setCommentCnt(int commentCnt) {
        this.commentCnt = commentCnt;
    }

    public int getReadCnt() {
        return readCnt;
    }

    public void setReadCnt(int readCnt) {
        this.readCnt = readCnt;
    }

    public int getLikeCnt() {
        return likeCnt;
    }

    public void setLikeCnt(int likeCnt) {
        this.likeCnt = likeCnt;
    }

    public int getIsComment() {
        return isComment;
    }

    public void setIsComment(int isComment) {
        this.isComment = isComment;
    }
}
