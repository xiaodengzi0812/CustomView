package com.djk.topbarlib;

import android.content.Context;
import android.widget.LinearLayout;

/**
 * @author Djk
 * @Title: 扩展topbar(给一个最基本的扩展写法)
 * @Time: 2017/11/6.
 * @Version:1.0.0
 */
public class ExpandTopBar extends AbsTopBar {
    protected ExpandTopBar(AbsParams params) {
        super(params);
    }

    @Override
    public int bindLayoutId() {
        return R.layout.expand_topbar;
    }

    @Override
    public void applyView() {
        // 这里可以是一些view的初始化操作
    }

    /**
     * 继承builder
     */
    public static class ExpandBuilder extends AbsTopBar.Builder {
        protected AbsParams params;

        public ExpandBuilder(Context mContext, LinearLayout parentView) {
            super(mContext, parentView);
            params = new AbsParams(mContext, parentView);
        }

        @Override
        public void show() {
            new ExpandTopBar(params);
        }
    }
}
