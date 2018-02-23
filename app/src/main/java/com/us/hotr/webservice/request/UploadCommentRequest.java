package com.us.hotr.webservice.request;

import java.io.Serializable;

/**
 * Created by liaobo on 2018/1/19.
 */

public class UploadCommentRequest implements Serializable {
    private Long hotTopicId;
    private Long contrastPhotoId;
    private String content;

    public long getContrastPhotoId() {
        return contrastPhotoId;
    }

    public void setContrastPhotoId(long contrastPhotoId) {
        this.contrastPhotoId = contrastPhotoId;
    }

    public long getHotTopicId() {
        return hotTopicId;
    }

    public void setHotTopicId(long hotTopicId) {
        this.hotTopicId = hotTopicId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
