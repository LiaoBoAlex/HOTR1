package com.us.hotr.webservice.response;

import java.io.Serializable;

/**
 * Created by liaobo on 2017/12/27.
 */

public class GetUserImageResponse implements Serializable {
    private Pic result;

    public void setResult(Pic result) {
        this.result = result;
    }

    public String getImageUrl(){
        return result.getHead_image_path();
    }

    public class Pic implements Serializable{
        private String head_image_path;

        public String getHead_image_path() {
            return head_image_path;
        }

        public void setHead_image_path(String head_image_path) {
            this.head_image_path = head_image_path;
        }
    }

}
