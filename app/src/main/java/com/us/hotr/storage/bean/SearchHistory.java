package com.us.hotr.storage.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Mloong on 2017/8/30.
 */
@Entity
public class SearchHistory {
    @Id
    private String keyword;

    @Generated(hash = 1787618573)
    public SearchHistory(String keyword) {
        this.keyword = keyword;
    }

    @Generated(hash = 1905904755)
    public SearchHistory() {
    }

    public String getKeyword() {
        return this.keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
