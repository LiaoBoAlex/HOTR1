package com.us.hotr.storage.bean;

import java.io.Serializable;

/**
 * Created by liaobo on 2017/12/5.
 */

public class SubjectDetail implements Serializable {
    private String price_range;
    private String projectIntroduction;
    private String treatmentTimes;
    private String restoreCycle;
    private String treatmentMethosDesc;

    public String getPrice_range() {
        return price_range;
    }

    public void setPrice_range(String price_range) {
        this.price_range = price_range;
    }

    public String getProjectIntroduction() {
        return projectIntroduction;
    }

    public void setProjectIntroduction(String projectIntroduction) {
        this.projectIntroduction = projectIntroduction;
    }

    public String getTreatmentTimes() {
        return treatmentTimes;
    }

    public void setTreatmentTimes(String treatmentTimes) {
        this.treatmentTimes = treatmentTimes;
    }

    public String getRestoreCycle() {
        return restoreCycle;
    }

    public void setRestoreCycle(String restoreCycle) {
        this.restoreCycle = restoreCycle;
    }

    public String getTreatmentMethosDesc() {
        return treatmentMethosDesc;
    }

    public void setTreatmentMethosDesc(String treatmentMethosDesc) {
        this.treatmentMethosDesc = treatmentMethosDesc;
    }
}
