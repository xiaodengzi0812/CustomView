package com.dengzi.bannerlib;

import android.view.View;
import android.view.ViewGroup;

/**
 * @author Djk
 * @Title: 自定义bannerView的adapter
 * @Time: 2017/10/19.
 * @Version:1.0.0
 */
public abstract class BannerBaseAdapter {

    /**
     * 获取view的数量
     *
     * @return
     */
    public abstract int getCount();

    /**
     * 通过position去获取view
     *
     * @param position   当前位置
     * @param parentView 父类
     * @param reuseView  可复用的view
     * @return
     */
    public abstract View getView(int position, ViewGroup parentView, View reuseView);

    /**
     * 通过position去获取广告描述
     *
     * @param position
     * @return
     */
    public String getDescTitle(int position) {
        return "";
    }

}
