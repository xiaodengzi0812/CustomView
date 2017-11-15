package com.dengzi.recyclerviewlib.refresh;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.dengzi.recyclerviewlib.header.HeaderRecyclerView;


/**
 * @author Djk
 * @Title: 下拉刷新、上拉加载的RecyclerView
 * @Time: 2017/9/21.
 * @Version:1.0.0
 */
public class RefreshRecyclerView extends HeaderRecyclerView {
    // 下拉刷新、上拉加载帮助类
    private RefreshCreator mRefreshCreator, mPullCreator;
    // 下拉刷新、上拉加载的view
    private View mRefreshView, mPullView;
    // 下拉刷新、上拉加载view的高度
    private int mRefreshViewHeight, mPullViewHeight;
    // 手指按下的Y位置
    private int mFingerDownY;
    // 手指拖拽的阻力指数
    protected float mDragIndex = 0.35f;
    // 当前是否正在拖动
    private boolean mCurrentDrag = false;
    // 当前下拉刷新、上拉加载的状态
    private int mCurrentRefreshStatus, mCurrentPullStatus;
    // 默认状态
    private int REFRESH_STATUS_NORMAL = 0x0011;
    // 下拉刷新状态
    private int REFRESH_STATUS_PULL_DOWN_REFRESH = 0x0022;
    // 松开刷新状态
    private int REFRESH_STATUS_LOOSEN_REFRESHING = 0x0033;
    // 正在刷新状态
    private int REFRESH_STATUS_REFRESHING = 0x0044;
    // 当前滑动类型的状态（CURRENT_REFRESH： 下拉刷新，CURRENT_PULL： 上拉加载）
    private int mCurrentType;
    // 下拉刷新状态
    private int CURRENT_REFRESH = 0x0066;
    // 上拉加载状态
    private int CURRENT_PULL = 0x0055;
    // 下拉刷新回调监听
    private OnRefreshListener mRefreshListener;
    // 上拉加载回调监听
    private OnPullLoadMoreListener mPullLoadMorehListener;
    // 自动加载回调监听
    private OnAtuoLoadMoreListener mAutoLoadMorehListener;
    // 上拉加载是否可用
    private boolean mIsPullLoadMoreEnable = true;
    // 自动加载是否可用
    private boolean mIsAutoLoadMoreEnable = true;
    // 是否正在自动加载
    private boolean mIsAutoLoading = false;

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

    /**
     * 添加底部的上拉加载控件
     */
    public void addPullCreator(RefreshCreator pullCreator) {
        this.mPullCreator = pullCreator;
        addPullView();
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        super.setAdapter(adapter);
        addRefreshView();
        addPullView();
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
                refreshView.post(new Runnable() {
                    @Override
                    public void run() {
                        // 获取头部刷新View的高度
                        mRefreshViewHeight = mRefreshView.getMeasuredHeight();
                        if (mRefreshViewHeight > 0) {
                            // 隐藏头部刷新的View  marginTop  多留出1px防止无法判断是不是滚动到头部问题
                            setRefreshViewMarginTop(-mRefreshViewHeight - 1);
                        }
                    }
                });
            }
        }
    }

    /**
     * 添加底部的上拉加载控件
     */
    private void addPullView() {
        RecyclerView.Adapter adapter = getAdapter();
        if (adapter != null && mPullCreator != null) {
            View pullView = mPullCreator.getRefreshView(getContext(), this);
            if (pullView != null) {
                // 这里为什么要添addHeaderView然后算出高度再removeHeaderView呢？ 因为直接addFooterView算不出高度
                addHeaderView(pullView);
                addFooterView(pullView);
                this.mPullView = pullView;
                pullView.post(new Runnable() {
                    @Override
                    public void run() {
                        // 获取底部加载View的高度
                        mPullViewHeight = mPullView.getMeasuredHeight();
                        if (mPullViewHeight > 0) {
                            // 隐藏底部加载的View  marginBottom  多留出1px防止无法判断是不是滚动到底部问题
                            setPullViewMarginBottom(-mPullViewHeight - 1);
                            removeHeaderView(mPullView);
                        }
                    }
                });
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
                    // 如果当前是上拉加载，则去取消上拉加载的状态
                    if (mCurrentType == CURRENT_PULL) {
                        restorePullView();
                    } else if (mCurrentType == CURRENT_REFRESH) {// 如果当前是下拉刷新，则去取消下拉刷新的状态
                        restoreRefreshView();
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 更新刷新view的显示状态
     */
    private void restoreRefreshView() {
        //获取当前的margin值
        int currentTopMargin = ((ViewGroup.MarginLayoutParams) mRefreshView.getLayoutParams()).topMargin;
        //设置最终的margin值，默认是回到默认值
        int finalTopMargin = -mRefreshViewHeight + 1;
        //如果当前状态是松开刷新状态，说明下拉超过刷新view的高度
        if (mCurrentRefreshStatus == REFRESH_STATUS_LOOSEN_REFRESHING) {
            //这里就将下拉刷新view显示出来
            finalTopMargin = 0;
            //设置当前下拉刷新的状态正在刷新状态
            mCurrentRefreshStatus = REFRESH_STATUS_REFRESHING;
            //回调下拉状态
            if (mRefreshCreator != null) {
                mRefreshCreator.onRefreshing();
            }
            if (mRefreshListener != null) {
                mRefreshListener.onPullRefresh();
            }
        }
        //这个地方就是根据下拉的高度来决定动画时间，其实不要也没关系
        int distance = currentTopMargin - finalTopMargin;
        //从现在的位置回弹到指定位置，开启一个动画
        ValueAnimator animator = ObjectAnimator.ofFloat(currentTopMargin, finalTopMargin).setDuration(distance);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentTopMargin = (float) animation.getAnimatedValue();
                setRefreshViewMarginTop((int) currentTopMargin);
            }
        });
        animator.start();
        //将是否正在拖动设置为false
        mCurrentDrag = false;
    }

    /**
     * 更新刷新view的显示状态
     */
    private void restorePullView() {
        //获取当前的margin值
        int currentBottomMargin = ((ViewGroup.MarginLayoutParams) mPullView.getLayoutParams()).bottomMargin;
        //设置最终的margin值，默认是回到默认值
        int finalBottomMargin = -mPullViewHeight - 1;
        //如果当前状态是松开刷新状态，说明上拉超过刷新view的高度
        if (mCurrentPullStatus == REFRESH_STATUS_LOOSEN_REFRESHING) {
            //这里就将上拉view显示出来
            finalBottomMargin = 0;
            //设置当前上拉的状态正在刷新状态
            mCurrentPullStatus = REFRESH_STATUS_REFRESHING;
            //回调上拉状态
            if (mPullCreator != null) {
                mPullCreator.onRefreshing();
            }
            if (mPullLoadMorehListener != null) {
                mPullLoadMorehListener.onPullLoadMore();
            }
        }
        //这个地方就是根据下拉的高度来决定动画时间，其实不要也没关系
        int distance = currentBottomMargin - finalBottomMargin;
        //从现在的位置回弹到指定位置，开启一个动画
        ValueAnimator animator = ObjectAnimator.ofFloat(currentBottomMargin, finalBottomMargin).setDuration(distance);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentTopMargin = (float) animation.getAnimatedValue();
                setPullViewMarginBottom((int) currentTopMargin);
            }
        });
        animator.start();
        //将是否正在拖动设置为false
        mCurrentDrag = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                bottomAutoLoad();
                // 下拉刷新
                // 获取手指触摸拖拽的距离
                int distanceY = (int) ((e.getRawY() - mFingerDownY) * mDragIndex);
                if (!canChildScrollUp() && mRefreshView != null && mRefreshCreator != null && mCurrentRefreshStatus != REFRESH_STATUS_REFRESHING) {
                    // 如果是已经到达头部，并且不断的向下拉，那么不断的改变refreshView的marginTop的值
                    if (distanceY > 0) {
                        mCurrentType = CURRENT_REFRESH;
                        int marginTop = distanceY - mRefreshViewHeight;
                        setRefreshViewMarginTop(marginTop);
                        updateRefreshStatus(distanceY);
                        mCurrentDrag = true;
                        return false;
                    }
                }
                // 上拉加载
                if (!canChildScrollDown() && mPullView != null && mPullCreator != null && mCurrentPullStatus != REFRESH_STATUS_REFRESHING && mIsPullLoadMoreEnable) {
                    // 如果是已经到达头部，并且不断的向下拉，那么不断的改变refreshView的marginTop的值
                    if (distanceY < 0) {
                        mCurrentType = CURRENT_PULL;
                        int marginBottom = Math.abs(distanceY) - mPullViewHeight;
                        setPullViewMarginBottom(marginBottom);
                        updatePullStatus(distanceY);
                        mCurrentDrag = true;
                        return false;
                    }
                }
                break;
        }
        return super.onTouchEvent(e);
    }

    /**
     * 通过下拉的距离更新刷新状态
     */
    private void updateRefreshStatus(int distanceY) {
        if (distanceY <= 0) {
            mCurrentRefreshStatus = REFRESH_STATUS_NORMAL;
        } else if (distanceY < mRefreshViewHeight) {//下拉高度没超过刷新view的高度，这个时候是下拉刷新状态
            mCurrentRefreshStatus = REFRESH_STATUS_PULL_DOWN_REFRESH;
        } else {//下拉高度超过刷新view的高度，这个时候是松开刷新状态
            mCurrentRefreshStatus = REFRESH_STATUS_LOOSEN_REFRESHING;
        }
        //将事件回调出去让用户自己去处理
        if (mRefreshCreator != null) {
            mRefreshCreator.onPull(distanceY, mRefreshViewHeight, mCurrentRefreshStatus);
        }
    }

    /**
     * 通过上拉的距离更新刷新状态
     */
    private void updatePullStatus(int distanceY) {
        if (distanceY >= 0) {
            mCurrentPullStatus = REFRESH_STATUS_NORMAL;
        } else if (Math.abs(distanceY) < mRefreshViewHeight) {//下拉高度没超过刷新view的高度，这个时候是下拉刷新状态
            mCurrentPullStatus = REFRESH_STATUS_PULL_DOWN_REFRESH;
        } else {//下拉高度超过刷新view的高度，这个时候是松开刷新状态
            mCurrentPullStatus = REFRESH_STATUS_LOOSEN_REFRESHING;
        }
        //将事件回调出去让用户自己去处理
        if (mPullCreator != null) {
            mPullCreator.onPull(Math.abs(distanceY), mPullViewHeight, mCurrentPullStatus);
        }
    }

    /**
     * 设置marginTop值
     */
    private void setRefreshViewMarginTop(int marginTop) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mRefreshView.getLayoutParams();
        //保证最高的MarginTop为refreshView的高度
        if (marginTop < -mRefreshViewHeight + 1) {
            marginTop = -mRefreshViewHeight + 1;
        }
        params.topMargin = marginTop;
        mRefreshView.setLayoutParams(params);
    }

    /**
     * 设置marginBottom值
     */
    private void setPullViewMarginBottom(int marginBottom) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mPullView.getLayoutParams();
        //保证最高的marginBottom为refreshView的高度
        if (marginBottom < -mPullViewHeight + 1) {
            marginBottom = -mPullViewHeight + 1;
        }
        params.bottomMargin = marginBottom;
        mPullView.setLayoutParams(params);
    }

    /**
     * 判断当前view是否滑动到了顶部
     * 来自于 SwipeRefreshLayout 的 canChildScrollUp 源码
     */
    public boolean canChildScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            return ViewCompat.canScrollVertically(this, -1) || this.getScrollY() > 0;
        } else {
            return ViewCompat.canScrollVertically(this, -1);
        }
    }

    /**
     * 判断当前view是否滑动到了底部
     * 来自于 SwipeRefreshLayout 的 canChildScrollUp 源码
     */
    public boolean canChildScrollDown() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            return ViewCompat.canScrollVertically(this, 1) || this.getScrollY() > 0;
        } else {
            return ViewCompat.canScrollVertically(this, 1);
        }
    }

    /**
     * 下拉刷新成功
     */
    public void refreshFinish() {
        //当前状态恢复为默认
        mCurrentRefreshStatus = REFRESH_STATUS_NORMAL;
        //将refreshView恢复为默认
        restoreRefreshView();
        //回调停止刷新方法去处理
        if (mRefreshCreator != null) {
            mRefreshCreator.onStopRefresh();
        }
    }

    /**
     * 上拉加载成功
     */
    public void pullLoadMoreFinish() {
        //当前状态恢复为默认
        mCurrentPullStatus = REFRESH_STATUS_NORMAL;
        //将refreshView恢复为默认
        restorePullView();
        //回调停止刷新方法去处理
        if (mPullCreator != null) {
            mPullCreator.onStopRefresh();
        }
    }

    /**
     * 自动加载成功
     */
    public void autoLoadMoreFinish() {
        mIsAutoLoading = false;
    }

    /**
     * 上拉加载是否可以使用
     *
     * @param enable 是否可用
     */
    public void setPullLoadMoreEnable(boolean enable) {
        this.mIsPullLoadMoreEnable = enable;
    }

    /**
     * 自动加载是否可以使用
     *
     * @param enable 是否可用
     */
    public void setAutoLoadMoreEnable(boolean enable) {
        this.mIsAutoLoadMoreEnable = enable;
    }

    /**
     * 设置下拉刷新监听
     *
     * @param listener 监听事件
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mRefreshListener = listener;
    }

    /**
     * 设置上拉加载监听
     *
     * @param listener 监听事件
     */
    public void setOnPullLoadMoreListener(OnPullLoadMoreListener listener) {
        this.mPullLoadMorehListener = listener;
    }

    /**
     * 设置自动加载监听
     *
     * @param listener 监听事件
     */
    public void setOnAtuoLoadMoreListener(OnAtuoLoadMoreListener listener) {
        this.mAutoLoadMorehListener = listener;
    }

    /**
     * 下拉刷新监听
     */
    public interface OnRefreshListener {
        // 下拉刷新
        void onPullRefresh();
    }

    /**
     * 上拉加载监听
     */
    public interface OnPullLoadMoreListener {
        // 上拉加载
        void onPullLoadMore();
    }

    /**
     * 自动加载监听
     */
    public interface OnAtuoLoadMoreListener {
        // 自动加载
        void onAutoLoadMore();
    }

    /**
     * 底部自动加载
     */
    public void bottomAutoLoad() {
        // 自动加载可用 && 监听事件不为null && 滑动到了底部
        if (mIsAutoLoadMoreEnable && mAutoLoadMorehListener != null && isScrollToBottom()) {
            if (!mIsAutoLoading) {
                synchronized (this) {
                    if (!mIsAutoLoading) {
                        mIsAutoLoading = true;
                        mAutoLoadMorehListener.onAutoLoadMore();
                    }
                }
            }
        }
    }

    /**
     * 是否滑动到了底部
     *
     * @return true:到了底部   fase:没到底部
     */
    public boolean isScrollToBottom() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        //屏幕中最后一个可见子项的position
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        //当前屏幕所看到的子项个数
        int visibleItemCount = layoutManager.getChildCount();
        //当前RecyclerView的所有子项个数
        int totalItemCount = layoutManager.getItemCount();
        //RecyclerView的滑动状态
        int state = getScrollState();
        // 当总条目的倒数第8个开始就向客户端发送加载请求
        return visibleItemCount > 0 && lastVisibleItemPosition > totalItemCount - 8 && state == SCROLL_STATE_IDLE;
    }

}
