package com.dengzi.recyclerviewlib.adapter;

/**
 * @Title: 多布局支持接口
 * @Author: Djk
 * @Time: 2017/9/20
 * @Version:1.0.0
 */
public interface MultiTypeSupport<T> {
    // 根据当前位置或者条目数据返回布局
    int getLayoutId(T item, int position);
}
