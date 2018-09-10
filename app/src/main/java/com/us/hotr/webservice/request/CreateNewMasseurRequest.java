package com.us.hotr.webservice.request;

import java.io.Serializable;
import java.util.List;

public class CreateNewMasseurRequest implements Serializable {
    private String massagistName;
    private String realName;
    private String idCode;
    private String healthCertificate;
    private String healthCertificateTime;
    private String practiceLicense;
    private String jobStartTime;
    private final int  massagistSex = 1;
    private int massagistHeight;
    private String massagistInfo;
    private String photosUnchecked;
    private Long key;
    private int practiceLicenseCode;

    public int getPracticeLicenseCode() {
        return practiceLicenseCode;
    }

    public void setPracticeLicenseCode(int practiceLicenseCode) {
        this.practiceLicenseCode = practiceLicenseCode;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMassagistName() {
        return massagistName;
    }

    public void setMassagistName(String massagistName) {
        this.massagistName = massagistName;
    }

    public String getIdCode() {
        return idCode;
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }

    public String getHealthCertificate() {
        return healthCertificate;
    }

    public void setHealthCertificate(String healthCertificate) {
        this.healthCertificate = healthCertificate;
    }

    public String getHealthCertificateTime() {
        return healthCertificateTime;
    }

    public void setHealthCertificateTime(String healthCertificateTime) {
        this.healthCertificateTime = healthCertificateTime;
    }

    public String getPracticeLicense() {
        return practiceLicense;
    }

    public void setPracticeLicense(String practiceLicense) {
        this.practiceLicense = practiceLicense;
    }

    public String getJobStartTime() {
        return jobStartTime;
    }

    public void setJobStartTime(String jobStartTime) {
        this.jobStartTime = jobStartTime;
    }

    public int getMassagistSex() {
        return massagistSex;
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

    public String getPhotosUnchecked() {
        return photosUnchecked;
    }

    public void setPhotosUnchecked(String photosUnchecked) {
        this.photosUnchecked = photosUnchecked;
    }
}
