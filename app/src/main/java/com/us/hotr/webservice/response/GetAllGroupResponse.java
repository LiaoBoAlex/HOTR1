package com.us.hotr.webservice.response;


import com.us.hotr.storage.bean.Group;
import com.us.hotr.storage.bean.Theme;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liaobo on 2018/1/12.
 */

public class GetAllGroupResponse implements Serializable {
    private List<Group> myAttentionCoshow;
    private List<Theme> themeList;

    public List<Group> getMyAttentionCoshow() {
        return myAttentionCoshow;
    }

    public void setMyAttentionCoshow(List<Group> myAttentionCoshow) {
        this.myAttentionCoshow = myAttentionCoshow;
    }

    public List<Theme> getThemeList() {
        return themeList;
    }

    public void setThemeList(List<Theme> themeList) {
        this.themeList = themeList;
    }
}
