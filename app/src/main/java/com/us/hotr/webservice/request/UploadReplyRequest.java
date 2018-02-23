package com.us.hotr.webservice.request;

import java.io.Serializable;

/**
 * Created by liaobo on 2018/1/19.
 */

public class UploadReplyRequest implements Serializable {
    private long commentId;
    private Long hotTopicId;
    private Long contrastPhotoId;
    private String content;
    private Long replyToId;
    private long replyToUserId;

    public long getContrastPhotoId() {
        return contrastPhotoId;
    }

    public void setContrastPhotoId(long contrastPhotoId) {
        this.contrastPhotoId = contrastPhotoId;
    }

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
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

    public Long getReplyToId() {
        return replyToId;
    }

    public void setReplyToId(Long replyToId) {
        this.replyToId = replyToId;
    }

    public long getReplyToUserId() {
        return replyToUserId;
    }

    public void setReplyToUserId(long replyToUserId) {
        this.replyToUserId = replyToUserId;
    }
}
