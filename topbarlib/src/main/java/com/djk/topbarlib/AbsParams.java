package com.djk.topbarlib;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * @author Djk
 * @Title: 参数基类
 * @Time: 2017/8/19.
 * @Version:1.0.0
 */
public class AbsParams {
    // 上下文
    public Context context;
    // 父控件
    public ViewGroup parentView;

    public AbsParams(Context context, LinearLayout parentView) {
        this.context = context;
        this.parentView = parentView;
    }
}
