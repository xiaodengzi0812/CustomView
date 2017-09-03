package com.djk.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author Djk
 * @Title: 自定义ratingBar ,（学习使用）
 * @Time: 2017/9/3.
 * @Version:1.0.0
 */
public class MyRatingBar extends View {
    private int mRatingNum = 5;
    private Bitmap mUnCheckBitmap;
    private Bitmap mCheckBitmap;
    private int mBitmapWidth;
    private int mCurrentRating;

    public MyRatingBar(Context context) {
        this(context, null);
    }

    public MyRatingBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mUnCheckBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_uncheck);
        mCheckBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_check);
        mBitmapWidth = mUnCheckBitmap.getWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mRatingNum; i++) {
            if (i < mCurrentRating) {
                canvas.drawBitmap(mCheckBitmap, i * mBitmapWidth, 0, null);
            } else {
                canvas.drawBitmap(mUnCheckBitmap, i * mBitmapWidth, 0, null);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                int tempCurrent = (int) (x / mBitmapWidth) + 1;
                if (tempCurrent == mCurrentRating) {
                    return true;
                }
                mCurrentRating = tempCurrent;
                invalidate();
                break;
        }
        return true;
    }
}
