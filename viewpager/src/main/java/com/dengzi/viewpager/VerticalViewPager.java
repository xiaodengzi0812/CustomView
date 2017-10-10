package com.dengzi.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Scroller;

/**
 * @author Djk
 * @Title: 竖直方向滑动的viewpager
 * @Time: 2017/10/9.
 * @Version:1.0.0
 */
public class VerticalViewPager extends ViewGroup {
    private BaseAdapter mAdapter;
    private Context mContext;
    //系统的手势工具类
    private GestureDetector mGesture;
    // 滑动工具类
    private Scroller mScroller;
    //当前view的位置
    private int mPosition = 0;
    //view前一次的位置
    private int mLastPosition = 0;
    //down 事件时的xy坐标
    private float mStartX = 0;
    private float mStartY = 0;
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
        this.mContext = context;
        mGesture = new GestureDetector(mContext, gestureListener);
        mScroller = new Scroller(mContext);
    }

    /**
     * 调用系统的手势控件来帮我们解析手指在屏幕上的触摸事件
     */
    private GestureDetector.OnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        /**
         * 响应手指在屏幕上的滑动事件
         */
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
    };

    /**
     * 计算子view的大小
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            v.measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).layout(0, i * getHeight(), getWidth(), (i + 1) * getHeight());
        }
    }

    @Override
    /**
     * 是否中断事件的传递   true为中断
     */
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mGesture.onTouchEvent(ev);
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
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        //把触摸事件交由系统的组件去帮我们解析
        if (mGesture.onTouchEvent(event)) {
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                float lastY = event.getY();
                if (lastY > mStartY && lastY - mStartY > getHeight() / 2) {//向下滑动
                    mPosition--;
                } else if (lastY < mStartY && mStartY - lastY > getHeight() / 2) {//向上滑动
                    mPosition++;
                }
                moveToDest();
                break;
        }
        return true;
    }

    /**
     * 移动到下一个页面
     */
    private void moveToDest() {
        mPosition = (mPosition > 0) ? mPosition : 0;
        mPosition = (mPosition < getChildCount()) ? mPosition : getChildCount() - 1;
        int dis = mPosition * getHeight() - getScrollY(); // 最终的位置 - 现在的位置  = 要移动的距离
        mScroller.startScroll(0, getScrollY(), 0, dis, 500);
        if (mListener != null && mLastPosition != mPosition) {
            mListener.moveTo(mPosition);
            mLastPosition = mPosition;
        }
        invalidate();
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
        int childCount = mAdapter.getCount();
        for (int i = 0; i < childCount; i++) {
            View childView = mAdapter.getView(i, null, this);
            addView(childView);
        }
    }

    /**
     * invalidate();  会导致  computeScroll（）这个方法的执行
     */
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int newY = mScroller.getCurrY();
            scrollTo(0, newY);
            invalidate();
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
