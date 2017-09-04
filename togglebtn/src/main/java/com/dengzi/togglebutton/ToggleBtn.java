package com.dengzi.togglebutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author Djk
 * @Title: 滑动切换开关
 * @Time: 2017/9/4.
 * @Version:1.0.0
 */
public class ToggleBtn extends View {
    private boolean mIsChecked;
    private Bitmap mBgBitmap, mSlideBitmap;
    /*滑块滑动时的距左边的最大值*/
    private float mMaxLeft;
    /*滑块滑动时的矩左边的距离*/
    private float mSlideLeft;
    private float mFirstX = 0;
    private float mLastX = 0;
    /*判断是否为滑动状态 false为点击状态 true为滑动状态*/
    private boolean mIsDrag = false;

    /*回调监听*/
    private OnScrollListener mListener;

    public interface OnScrollListener {
        void onResult(boolean isChecked);
    }

    public void setOnScrollListener(OnScrollListener listener) {
        this.mListener = listener;
    }

    public ToggleBtn(Context context) {
        this(context, null);
    }

    public ToggleBtn(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToggleBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.custom_toggle_btn);
            int bgImage = typedArray.getResourceId(R.styleable.custom_toggle_btn_backgroundImage, 0);
            int sliddeImage = typedArray.getResourceId(R.styleable.custom_toggle_btn_sliddeImage, 0);
            mIsChecked = typedArray.getBoolean(R.styleable.custom_toggle_btn_isChecked, false);
            typedArray.recycle();
            mBgBitmap = BitmapFactory.decodeResource(getResources(), bgImage);
            mSlideBitmap = BitmapFactory.decodeResource(getResources(), sliddeImage);
            mMaxLeft = mBgBitmap.getWidth() - mSlideBitmap.getWidth();
            if (mIsChecked) {
                mSlideLeft = mMaxLeft;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        /*计算宽度*/
        if (widthMode == MeasureSpec.AT_MOST) {
            width = mBgBitmap.getWidth();
        }
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST) {
            height = mBgBitmap.getHeight();
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBgBitmap, 0, 0, null);
        canvas.drawBitmap(mSlideBitmap, mSlideLeft, 0, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mFirstX = mLastX = event.getX();
                mIsDrag = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(event.getX() - mFirstX) > 8) {/*说明是滑动*/
                    mIsDrag = true;
                }
                float disX = event.getX() - mLastX;
                mLastX = event.getX();
                mSlideLeft = mSlideLeft + disX;
                break;
            case MotionEvent.ACTION_UP:
                if (!mIsDrag) {/*点击*/
                    mSlideLeft = event.getX();
                }
                mSlideLeft = (mSlideLeft > mMaxLeft / 2) ? mMaxLeft : 0;
                refreshListener();
                break;
        }
        refreshView();
        return true;
    }

    /*刷新view*/
    private void refreshView() {
        mSlideLeft = (mSlideLeft > 0) ? mSlideLeft : 0;// 确保滑块在滑动的时候不会超出左边界
        mSlideLeft = (mSlideLeft < mMaxLeft) ? mSlideLeft : mMaxLeft;// 确保滑块在滑动的时候不会超出右边界
        invalidate();
    }

    /*刷新监听事件*/
    private void refreshListener() {
        if (mListener != null) {
            boolean tempChecked = mSlideLeft == mMaxLeft;
            if (mIsChecked != tempChecked) {
                mIsChecked = tempChecked;
                mListener.onResult(mIsChecked);
            }
        }
    }

}
