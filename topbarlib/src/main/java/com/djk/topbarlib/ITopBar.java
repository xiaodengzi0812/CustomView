package com.djk.topbarlib;

/**
 * @author Djk
 * @Title: topbar的公共结构
 * @Time: 2017/8/19.
 * @Version:1.0.0
 */
public interface ITopBar {
    /**
     * 加载view的id
     *
     * @return
     */
    int bindLayoutId();

    /**
     * 设置view的属性
     */
    void applyView();
}
