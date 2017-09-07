package com.dengzi.customview.taglayout;

import java.io.Serializable;

/**
 * @author Djk
 * @Title: 业务bean
 * @Time: 2017/9/7.
 * @Version:1.0.0
 */
public class TagBean implements Serializable {
    private String tagName;
    private boolean isSelect;

    public TagBean(String tagName) {
        this.tagName = tagName;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
