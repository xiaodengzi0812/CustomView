package com.dengzi.imageselector.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @Title: 正方形的ImageView
 * @Author: djk
 * @Time: 2017/10/25
 * @Version:1.0.0
 */
public class SquareImageView extends ImageView {

    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
