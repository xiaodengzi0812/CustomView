package com.djk.topbarlib;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * @author Djk
 * @Title: topbar 的基类
 * @Time: 2017/8/19.
 * @Version:1.0.0
 */
public abstract class AbsTopBar<E extends AbsParams> implements ITopBar {
    // 参数
    protected E params;
    // topbar的真正view
    private View topbarView;

    protected AbsTopBar(E params) {
        this.params = params;
        creatAndBindView();
    }

    /**
     * 创建和绑定view
     */
    protected void creatAndBindView() {
        // 如果父类为null，则去拿系统的父类
        if (params.parentView == null) {
            ViewGroup parentFramelayoutView = (ViewGroup) ((Activity) params.context).findViewById(android.R.id.content);
            if (parentFramelayoutView == null) {
                throw new Resources.NotFoundException("parent view can not found!");
            }
            params.parentView = parentFramelayoutView;
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            lp.topMargin = dp2px(topbarDpHeight());
            View outView = parentFramelayoutView.getChildAt(0);
            if (outView == null) {
                throw new Resources.NotFoundException("your view can not found!");
            }
            outView.setLayoutParams(lp);
        }
        // 如果还是找不到父类控件，则报错
        if (params.parentView == null) {
            throw new Resources.NotFoundException("parent view can not found!");
        }

        // 也有这种写法，总感觉有问题
//        if (params.parentView == null) {
//            ViewGroup viewGroup = (ViewGroup) ((Activity) params.context).getWindow().getDecorView();
//            params.parentView = (ViewGroup) viewGroup.getChildAt(0);
//        }

        // 创建topbar view
        topbarView = LayoutInflater.from(params.context).inflate(bindLayoutId(), params.parentView, false);
        // 将创建的topbar添加到父类中
        params.parentView.addView(topbarView);
        // 去调用子类的设置view的属性
        applyView();
    }

    /**
     * 父类builder，构建一个框架
     */
    public static abstract class Builder {

        /**
         * 构造
         *
         * @param mContext   上下文
         * @param parentView 父类控件
         */
        public Builder(Context mContext, ViewGroup parentView) {
        }

        /**
         * 最后要显示的时候加载的view
         */
        public abstract void show();
    }

    /**
     * 设置text
     *
     * @param viewId  id
     * @param textStr text
     */
    protected void setText(int viewId, String textStr) {
        if (TextUtils.isEmpty(textStr)) {
            return;
        }
        TextView tv = findViewById(viewId);
        if (tv != null) {
            tv.setText(textStr);
        }
    }

    /**
     * 设置textColor
     *
     * @param viewId id
     * @param color  颜色
     */
    protected void setTextColor(int viewId, int color) {
        if (color == 0) {
            return;
        }
        TextView tv = findViewById(viewId);
        if (tv != null) {
            tv.setTextColor(color);
        }
    }

    /**
     * 设置textColor
     *
     * @param viewId id
     * @param size   大小
     */
    protected void setTextSize(int viewId, int size) {
        if (size == 0) {
            return;
        }
        TextView tv = findViewById(viewId);
        if (tv != null) {
            tv.setTextSize(size);
        }
    }

    /**
     * 设置text图片
     *
     * @param viewId  id
     * @param iconRes 图片资源id
     */
    protected void setTextIcon(int viewId, int iconRes) {
        if (iconRes == 0) {
            return;
        }
        TextView tv = findViewById(viewId);
        Drawable drawable = params.context.getResources().getDrawable(iconRes, null);
        if (tv != null && drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv.setCompoundDrawables(drawable, null, null, null);
        }
    }

    /**
     * 设置text 文字长度
     *
     * @param viewId    id
     * @param maxLength 最大长度
     */
    protected void setTextMaxlength(int viewId, int maxLength) {
        if (maxLength == 0) {
            return;
        }
        TextView tv = findViewById(viewId);
        if (tv != null) {
            tv.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        }
    }

    /**
     * 设置bg色
     *
     * @param viewId id
     * @param color  颜色
     */
    protected void setBgColor(int viewId, int color) {
        if (color == 0) {
            return;
        }
        View view = findViewById(viewId);
        if (view != null) {
            view.setBackgroundColor(color);
        }
    }

    /**
     * 设置点击事件
     *
     * @param viewId   id
     * @param listener 点击listener
     */
    protected void setClickListener(int viewId, View.OnClickListener listener) {
        if (listener == null) {
            return;
        }
        View view = findViewById(viewId);
        if (view != null) {
            topbarView.findViewById(viewId).setOnClickListener(listener);
        }
    }


    /**
     * 获取view by id
     *
     * @param viewId
     * @param <E>
     * @return
     */
    protected <E extends View> E findViewById(int viewId) {
        return (E) topbarView.findViewById(viewId);
    }

    /**
     * dp转px
     *
     * @param dpValue dp值
     * @return px值
     */
    private int dp2px(final float dpValue) {
        final float scale = params.context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
