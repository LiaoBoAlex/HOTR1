package com.us.hotr.storage.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mloong on 2017/11/29.
 */

public class Subject implements Serializable {

    private List<SubjectL2> children;
    private String typeName;
    private int key;

    public List<SubjectL2> getChildren() {
        return children;
    }

    public void setChildren(List<SubjectL2> children) {
        this.children = children;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public class SubjectL2 {

        private List<SubjectL3> projectChild;
        private String typeName;

        public List<SubjectL3> getProductChild() {
            return projectChild;
        }

        public void setProductChild(List<SubjectL3> projectChild) {
            this.projectChild = projectChild;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public class SubjectL3 implements Serializable {

            private String projectName;
            private int key;

            public String getProjectName() {
                return projectName;
            }

            public void setProjectName(String projectName) {
                this.projectName = projectName;
            }

            public int getKey() {
                return key;
            }

            public void setKey(int key) {
                this.key = key;
            }
        }
    }
}
