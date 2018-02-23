package com.us.hotr.webservice.response;

import java.io.Serializable;

/**
 * Created by liaobo on 2018/1/19.
 */

public class UploadPostResponse implements Serializable {
    private long topicId;

    public long getTopicId() {
        return topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }
}
