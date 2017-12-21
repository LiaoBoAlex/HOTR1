package com.us.hotr.storage.bean;

import java.io.Serializable;

/**
 * Created by liaobo on 2017/12/5.
 */

public class Type implements Serializable {

    public Type(int typeId, String typeName){
        this.typeName = typeName;
        this. typeId = typeId;
    }

    private String typeName;
    private int typeId;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
}
