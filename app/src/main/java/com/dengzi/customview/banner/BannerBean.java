package com.dengzi.customview.banner;

/**
 * @author Djk
 * @Title: 模拟一个BannerBean
 * @Time: 2017/10/20.
 * @Version:1.0.0
 */
public class BannerBean {
    private int imageRes;
    private String descText;

    public BannerBean(int imageRes, String descText) {
        this.imageRes = imageRes;
        this.descText = descText;
    }

    public int getImageRes() {
        return imageRes;
    }

    public String getDescText() {
        return descText;
    }
}
