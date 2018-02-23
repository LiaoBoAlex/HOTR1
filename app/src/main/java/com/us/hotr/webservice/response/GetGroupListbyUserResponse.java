package com.us.hotr.webservice.response;

import com.us.hotr.storage.bean.Group;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liaobo on 2018/1/26.
 */

public class GetGroupListbyUserResponse implements Serializable {
    private List<Group> myCoshow;

    public List<Group> getMyCoshow() {
        return myCoshow;
    }

    public void setMyCoshow(List<Group> myCoshow) {
        this.myCoshow = myCoshow;
    }
}
