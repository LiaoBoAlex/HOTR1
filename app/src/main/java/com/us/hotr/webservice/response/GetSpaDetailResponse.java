package com.us.hotr.webservice.response;

import com.us.hotr.storage.bean.Massage;
import com.us.hotr.storage.bean.Masseur;
import com.us.hotr.storage.bean.Spa;
import com.us.hotr.storage.bean.Type;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liaobo on 2017/12/14.
 */

public class GetSpaDetailResponse implements Serializable {
    private Spa massage;
    private List<Type> typeList;
    private List<Masseur> masseurList;
    private List<Massage> massageList;
    private int totalMassageCount;
    private int totalMasseurCount;
    private int is_collected;

    public int getIs_collected() {
        return is_collected;
    }

    public void setIs_collected(int is_collected) {
        this.is_collected = is_collected;
    }

    public int getTotalMasseurCount() {
        return totalMasseurCount;
    }

    public void setTotalMasseurCount(int totalMasseurCount) {
        this.totalMasseurCount = totalMasseurCount;
    }

    public Spa getMassage() {
        return massage;
    }

    public void setMassage(Spa massage) {
        this.massage = massage;
    }

    public List<Type> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<Type> typeList) {
        this.typeList = typeList;
    }

    public List<Masseur> getMasseurList() {
        return masseurList;
    }

    public void setMasseurList(List<Masseur> masseurList) {
        this.masseurList = masseurList;
    }

    public List<Massage> getMassageList() {
        return massageList;
    }

    public void setMassageList(List<Massage> massageList) {
        this.massageList = massageList;
    }

    public int getTotalMassageCount() {
        return totalMassageCount;
    }

    public void setTotalMassageCount(int totalMassageCount) {
        this.totalMassageCount = totalMassageCount;
    }
}
