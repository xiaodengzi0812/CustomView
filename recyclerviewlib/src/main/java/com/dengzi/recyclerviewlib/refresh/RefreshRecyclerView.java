package com.dengzi.recyclerviewlib.refresh;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;

import com.dengzi.recyclerviewlib.header.HeaderRecyclerView;

/**
 * @author Djk
 * @Title: 下拉刷新的RecyclerView
 * @Time: 2017/9/21.
 * @Version:1.0.0
 */
public class RefreshRecyclerView extends HeaderRecyclerView {
    // 下拉刷新帮助类
    private RefreshCreator mRefreshCreator;
    // 下拉刷新的view
    private View mRefreshView;
    // 下拉刷新view的高度
    private int mRefreshViewHeight;
    // 手指按下的Y位置
    private int mFingerDownY;
    // 手指拖拽的阻力指数
    protected float mDragIndex = 0.35f;
    // 当前是否正在拖动
    private boolean mCurrentDrag = false;
    // 当前的状态
    private int mCurrentRefreshStatus;
    // 默认状态
    private int REFRESH_STATUS_NORMAL = 0x0011;
    // 下拉刷新状态
    private int REFRESH_STATUS_PULL_DOWN_REFRESH = 0x0022;
    // 松开刷新状态
    private int REFRESH_STATUS_LOOSEN_REFRESHING = 0x0033;
    // 正在刷新状态
    private int REFRESH_STATUS_REFRESHING = 0x0044;


    public RefreshRecyclerView(Context context) {
        super(context);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 添加头部的刷新控件
     */
    public void addRefreshCreator(RefreshCreator refreshCreator) {
        this.mRefreshCreator = refreshCreator;
        addRefreshView();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        addRefreshView();
    }

    /**
     * 添加头部的刷新View
     */
    private void addRefreshView() {
        RecyclerView.Adapter adapter = getAdapter();
        if (adapter != null && mRefreshCreator != null) {
            View refreshView = mRefreshCreator.getRefreshView(getContext(), this);
            if (refreshView != null) {
                addHeaderView(refreshView);
                this.mRefreshView = refreshView;
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录手指按下的位置 ,之所以写在dispatchTouchEvent那是因为如果我们处理了条目点击事件，
                // 那么就不会进入onTouchEvent里面，所以只能在这里获取
                mFingerDownY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                if (mCurrentDrag) {
                    restoreRefreshView();
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /*更新刷新view的显示状态*/
    private void restoreRefreshView() {
        /*获取当前的margin值*/
        int currentTopMargin = ((MarginLayoutParams) mRefreshView.getLayoutParams()).topMargin;
        /*设置最终的margin值，默认是回到默认值*/
        int finalTopMargin = -mRefreshViewHeight + 1;
        /*如果当前状态是松开刷新状态，说明下拉超过刷新view的高度*/
        if (mCurrentRefreshStatus == REFRESH_STATUS_LOOSEN_REFRESHING) {
            /*这里就将下拉刷新view显示出来*/
            finalTopMargin = 0;
            /*设置当前下拉刷新的状态正在刷新状态*/
            mCurrentRefreshStatus = REFRESH_STATUS_REFRESHING;
            /*回调下拉状态*/
            if (mRefreshCreator != null) {
                mRefreshCreator.onRefreshing();
            }
            if (mListener != null) {
                mListener.onRefresh();
            }
        }
        /*这个地方就是根据下拉的高度来决定动画时间，其实不要也没关系*/
        int distance = currentTopMargin - finalTopMargin;
        /*从现在的位置回弹到指定位置，开启一个动画*/
        ValueAnimator animator = ObjectAnimator.ofFloat(currentTopMargin, finalTopMargin).setDuration(distance);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentTopMargin = (float) animation.getAnimatedValue();
                setRefreshViewMarginTop((int) currentTopMargin);
            }
        });
        animator.start();
        /*将是否正在拖动设置为false*/
        mCurrentDrag = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                // 如果是在最顶部才处理，否则不需要处理
                if (canChildScrollUp() || mCurrentRefreshStatus == REFRESH_STATUS_REFRESHING
                        || mRefreshView == null || mRefreshCreator == null) {
                    // 如果没有到达最顶端，也就是说还可以向上滚动就什么都不处理
                    return super.onTouchEvent(e);
                }
                // 获取手指触摸拖拽的距离
                int distanceY = (int) ((e.getRawY() - mFingerDownY) * mDragIndex);
                // 如果是已经到达头部，并且不断的向下拉，那么不断的改变refreshView的marginTop的值
                if (distanceY > 0) {
                    int marginTop = distanceY - mRefreshViewHeight;
                    setRefreshViewMarginTop(marginTop);
                    updateRefreshStatus(distanceY);
                    mCurrentDrag = true;
                    return false;
                }
                break;
        }
        return super.onTouchEvent(e);
    }

    /*通过下拉的距离更新刷新状态*/
    private void updateRefreshStatus(int distanceY) {
        if (distanceY <= 0) {
            mCurrentRefreshStatus = REFRESH_STATUS_NORMAL;
        } else if (distanceY < mRefreshViewHeight) {/*下拉高度没超过刷新view的高度，这个时候是下拉刷新状态*/
            mCurrentRefreshStatus = REFRESH_STATUS_PULL_DOWN_REFRESH;
        } else {/*下拉高度超过刷新view的高度，这个时候是松开刷新状态*/
            mCurrentRefreshStatus = REFRESH_STATUS_LOOSEN_REFRESHING;
        }
        /*将事件回调出去让用户自己去处理*/
        if (mRefreshCreator != null) {
            mRefreshCreator.onPull(distanceY, mRefreshViewHeight, mCurrentRefreshStatus);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mRefreshView != null && mRefreshViewHeight <= 0) {
            // 获取头部刷新View的高度
            mRefreshViewHeight = mRefreshView.getMeasuredHeight();
            if (mRefreshViewHeight > 0) {
                // 隐藏头部刷新的View  marginTop  多留出1px防止无法判断是不是滚动到头部问题
                setRefreshViewMarginTop(-mRefreshViewHeight + 1);
            }
        }
    }

    /*设置marginTop值*/
    private void setRefreshViewMarginTop(int marginTop) {
        MarginLayoutParams params = (MarginLayoutParams) mRefreshView.getLayoutParams();
        /*保证最高的MarginTop为refreshView的高度*/
        if (marginTop < -mRefreshViewHeight + 1) {
            marginTop = -mRefreshViewHeight + 1;
        }
        params.topMargin = marginTop;
        mRefreshView.setLayoutParams(params);
    }

    /**
     * 判断当前view是否滑动到了顶部
     * 来自于 SwipeRefreshLayout 的 canChildScrollUp 源码
     *
     * @return
     */
    public boolean canChildScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            return ViewCompat.canScrollVertically(this, -1) || this.getScrollY() > 0;
        } else {
            return ViewCompat.canScrollVertically(this, -1);
        }
    }

    /**
     * 停止刷新
     */
    public void stopRefresh() {
        /*当前状态恢复为默认*/
        mCurrentRefreshStatus = REFRESH_STATUS_NORMAL;
        /*将refreshView恢复为默认*/
        restoreRefreshView();
        /*回调停止刷新方法去处理*/
        if (mRefreshCreator != null) {
            mRefreshCreator.onStopRefresh();
        }
    }

    // 处理刷新回调监听
    private OnRefreshListener mListener;

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mListener = listener;
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

}
