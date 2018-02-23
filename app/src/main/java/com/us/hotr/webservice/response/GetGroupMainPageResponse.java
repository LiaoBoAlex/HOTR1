package com.us.hotr.webservice.response;

import com.us.hotr.storage.bean.Group;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liaobo on 2018/1/5.
 */

public class GetGroupMainPageResponse implements Serializable {
    private List<Group> myCoshow;
    private List<Group> recommendCoshowList;
    private List<Group> restOfMyCoshow;



    public List<Group> getMyCoshow() {
        return myCoshow;
    }

    public void setMyCoshow(List<Group> myCoshow) {
        this.myCoshow = myCoshow;
    }

    public List<Group> getRecommendCoshowList() {
        return recommendCoshowList;
    }

    public void setRecommendCoshowList(List<Group> recommendCoshowList) {
        this.recommendCoshowList = recommendCoshowList;
    }

    public List<Group> getRestOfMyCoshow() {
        return restOfMyCoshow;
    }

    public void setRestOfMyCoshow(List<Group> restOfMyCoshow) {
        this.restOfMyCoshow = restOfMyCoshow;
    }
}
