package com.dengzi.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * @author Djk
 * @Title: 竖直方向滑动的viewpager
 * @Time: 2017/10/9.
 * @Version:1.0.0
 */
public class VerticalViewPager extends ScrollView {
    private BaseAdapter mAdapter;
    // 当前位置
    private int mPosition = 0;
    // 上一个选中的位置
    private int mLastPosition = 0;
    // 子view的数量
    private int mChildCount;
    private float mStartX;
    private float mStartY;
    // 是否拦截touch事件
    private boolean mInterceptFlag = false;

    public VerticalViewPager(Context context) {
        this(context, null);
    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /*实现快速滑动效果*/
    GestureDetector mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            scrollBy(0, (int) distanceY);
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(velocityX) < Math.abs(velocityY) && velocityY < -800) {// 向上快速滑动
                mPosition++;
                moveToDest();
                return true;
            } else if (Math.abs(velocityX) < Math.abs(velocityY) && velocityY > 800) {// 向下快速滑动
                mPosition--;
                moveToDest();
                return true;
            }
            return false;
        }
    });

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() == 1) {
            int height = MeasureSpec.getSize(heightMeasureSpec);
            int width = MeasureSpec.getSize(widthMeasureSpec);
            ViewGroup parentView = (ViewGroup) getChildAt(0);
            mChildCount = parentView.getChildCount();
            for (int i = 0; i < mChildCount; i++) {
                View childView = parentView.getChildAt(i);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
                childView.setLayoutParams(params);
            }
        }
    }

    /**
     * 设置adapter
     *
     * @param adapter
     */
    public void setAdapter(BaseAdapter adapter) {
        if (adapter == null) {
            throw new NullPointerException("Adapter can not be null!");
        }
        mAdapter = adapter;
        perparRequestlayout();
        requestLayout();
    }

    /**
     * 准备数据
     */
    private void perparRequestlayout() {
        removeAllViews();
        LinearLayout parentView = new LinearLayout(getContext());
        parentView.setOrientation(LinearLayout.VERTICAL);
        int childCount = mAdapter.getCount();
        for (int i = 0; i < childCount; i++) {
            View childView = mAdapter.getView(i, null, this);
            parentView.addView(childView);
        }
        addView(parentView);
    }

    @Override
    /**
     * 是否中断事件的传递   true为中断
     */
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mGestureDetector.onTouchEvent(ev);
                mInterceptFlag = false;
                mStartX = ev.getX();
                mStartY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float disx = Math.abs(ev.getX() - mStartX);
                float disy = Math.abs(ev.getY() - mStartY);
                mInterceptFlag = disy > disx && disy > 10 && mStartX < getWidth() / 2;
                break;
        }
        return mInterceptFlag;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mGestureDetector.onTouchEvent(ev)) {
            return true;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                int endY = (int) ev.getY();
                if (endY > mStartY && (endY - mStartY) > getHeight() / 2) {// 向下滑动
                    mPosition--;
                } else if (mStartY > endY && (mStartY - endY) > getHeight() / 2) {// 向上滑动
                    mPosition++;
                }
                moveToDest();
                return true;
        }
        return true;
    }

    /**
     * 移动到指定位置
     */
    private void moveToDest() {
        mPosition = mPosition < 0 ? 0 : mPosition > mChildCount ? mChildCount : mPosition;
        smoothScrollTo(0, getHeight() * mPosition);
        if (mListener != null && mLastPosition != mPosition) {
            mListener.moveTo(mPosition);
            mLastPosition = mPosition;
        }
    }

    //-----------事件的回调-------------------
    public OnPagerChangeListener mListener;

    public void setOnPagerChangeListener(OnPagerChangeListener listener) {
        this.mListener = listener;
    }

    public interface OnPagerChangeListener {
        void moveTo(int position);
    }

}
