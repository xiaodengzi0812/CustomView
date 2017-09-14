package com.dengzi.slidingmenu;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;

/**
 * @author Djk
 * @Title: 竖方向滑动的菜单view（类似汽车之家的效果）
 * @Time: 2017/9/14.
 * @Version:1.0.0
 */
public class VerticalSlidingMenu extends FrameLayout {
    private View mMenuView, mContentView;
    private int mMenuHeight;
    private ViewDragHelper mDragHelper;
    private boolean mIsMenuOpened = false;
    private ViewDragHelper.Callback mDragHelperCallBack = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mContentView;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            int tempTop = top > mMenuHeight ? mMenuHeight : top < 0 ? 0 : top;
            return tempTop;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            // mContentView手指释放时可以自动选择上或下
            if (releasedChild == mContentView) {
                yvel = mContentView.getTop() > mMenuHeight / 2 ? mMenuHeight : 0;
                mIsMenuOpened = yvel == mMenuHeight;
                mDragHelper.settleCapturedViewAt(0, (int) yvel);
                invalidate();
            }
        }
    };

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    public VerticalSlidingMenu(Context context) {
        this(context, null);
    }

    public VerticalSlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalSlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDragHelper = ViewDragHelper.create(this, mDragHelperCallBack);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount != 2) {
            throw new ArrayIndexOutOfBoundsException("child view count mast be 2!");
        }
        mMenuView = getChildAt(0);
        mContentView = getChildAt(1);
    }

    /*事件拦截*/
    private int mStartX;
    private int mStartY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        /*Down事件的时候，初始化一些信息*/
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mDragHelper.processTouchEvent(ev);
            mStartX = (int) ev.getX();
            mStartY = (int) ev.getY();
            if (mIsMenuOpened && mStartY > mMenuHeight) {
                return true;
            }
        }
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            /*判断我们的主页view是否滑动到了顶部*/
            if (!canChildScrollUp()) {
                /*算出刚开始移动的x和y轴的距离*/
                int disY = (int) ev.getY() - mStartY;
                /*如果disY小于0，说明是向上滑动，我们把事件给子类*/
                if (disY < 0) {
                    return super.onInterceptTouchEvent(ev);
                } else {/*如果不是子类的事件，那么将本次事件分发给自己去处理*/
                    return true;
                }
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        /*处理点击事件，如果显示菜单的时候点击下面的区域，直接弹上去*/
        if (event.getAction() == MotionEvent.ACTION_UP && mIsMenuOpened && mStartY > mMenuHeight) {
            int disX = (int) Math.abs(event.getX() - mStartX);
            int disY = (int) Math.abs(event.getY() - mStartY);
            /*判断点击事件*/
            if (disX < 10 && disY < 10) {
                /*滑动到顶部*/
                mIsMenuOpened = false;
                requestLayout();
            }
        }
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mMenuHeight = mMenuView.getHeight();
    }

    /**
     * 判断当前view是否滑动到了顶部
     * 来自于 SwipeRefreshLayout 的 canChildScrollUp 源码
     *
     * @return
     */
    public boolean canChildScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mContentView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mContentView;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mContentView, -1) || mContentView.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mContentView, -1);
        }
    }
}
