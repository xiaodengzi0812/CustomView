package com.dengzi.slidingmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;

/**
 * @author Djk
 * @Title: 滑动菜单
 * @Time: 2017/9/12.
 * @Version:1.0.0
 */
public class SlidingMenu extends HorizontalScrollView {
    private int mMenuWidth;/*菜单的宽度*/
    private ViewGroup mMenuView;/*菜单view*/
    private ViewGroup mContentView;/*主页view*/
    private boolean mIsMenuOpened = false;/*菜单是否打开*/
    private boolean mIsIntercept = false; /*是否拦截*/

    /*实现快速滑动效果*/
    GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            /*如果打开的情况*/
            if (mIsMenuOpened) {
                /*向左滑动*/
                if (Math.abs(velocityX) > Math.abs(velocityY) && velocityX < 0) {
                    closeMenu();
                    return true;
                }
            } else {
                /*向右滑动*/
                if (Math.abs(velocityX) > Math.abs(velocityY) && velocityX > 0) {
                    openMenu();
                    return true;
                }
            }
            return false;
        }
    });

    public SlidingMenu(Context context) {
        this(context, null);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        float rightMargin = dip2px(context, 60);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.custom_slid_menu);
            rightMargin = dip2px(context, typedArray.getDimension(R.styleable.custom_slid_menu_menuRightMargin, 60));
            typedArray.recycle();
        }
        mMenuWidth = (int) (getScreenWidth(context) - rightMargin);
    }

    @Override
    protected void onFinishInflate() {
        /*这个方法是XML文件解析完毕会调用*/
        super.onFinishInflate();
        /*获取它的子view*/
        ViewGroup parentView = (ViewGroup) getChildAt(0);
        int childCount = parentView.getChildCount();
        /*子view个数必须是两个*/
        if (childCount != 2) {
            throw new ArrayIndexOutOfBoundsException("child view count mast be 2!");
        }
        /*设置菜单的宽度*/
        mMenuView = (ViewGroup) parentView.getChildAt(0);
        ViewGroup.LayoutParams menuLp = mMenuView.getLayoutParams();
        menuLp.width = mMenuWidth;
        mMenuView.setLayoutParams(menuLp);
        /*设置主内容的宽度*/
        mContentView = (ViewGroup) parentView.getChildAt(1);
        ViewGroup.LayoutParams contentLp = mContentView.getLayoutParams();
        contentLp.width = getScreenWidth(getContext());
        mContentView.setLayoutParams(contentLp);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        /*默认进来的时候，关闭菜单*/
        scrollTo(mMenuWidth, 0);
    }

    /*事件分发*/
    private int mStartDisX;
    private int mStartDisY;
    /*事件分发给子类*/
    private boolean mDispathToChild = false;
    /*事件分发给自己*/
    private boolean mDispathToSuper = false;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        /*Down事件的时候，初始化一些信息*/
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mDispathToChild = false;
            mDispathToSuper = false;
            mStartDisX = (int) ev.getX();
            mStartDisY = (int) ev.getY();
        }
        /*如果本次事件要分发给子类，直接分发*/
        if (mDispathToChild) {
            return mContentView.dispatchTouchEvent(ev);
        }
        /*如果本次事件要分发给自己，直接分发*/
        if (mDispathToSuper) {
            return super.dispatchTouchEvent(ev);
        }
        if (!mIsMenuOpened) {/*如果是未打开菜单的情况*/
            if (ev.getAction() == MotionEvent.ACTION_MOVE) {
                /*算出刚开始移动的x和y轴的距离*/
                int disX = (int) Math.abs(ev.getX() - mStartDisX);
                int disY = (int) Math.abs(ev.getY() - mStartDisY);
                /*如果y方向滑动大于10 或者y方向的滑动距离大于x方向，则我们认为这个事件是子类的*/
                if (disY > 10 || disY > disX) {
                    mDispathToChild = true;
                    return mContentView.dispatchTouchEvent(ev);
                } else {/*如果不是子类的事件，那么将本次事件分发给自己去处理*/
                    mDispathToSuper = true;
                    return super.dispatchTouchEvent(ev);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /*事件拦截*/
    private int mStartX;
    private int mStartY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        mIsIntercept = false;
        // 如果是打开菜单的状态
        if (mIsMenuOpened) {
            // 在打开单的时候按下右边区域
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                mStartX = (int) ev.getX();
                mStartY = (int) ev.getY();
                if (mStartX > mMenuWidth) {
                    mIsIntercept = true;
                    return true;
                }
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        /*如果是被拦截状态*/
        if (mIsIntercept) {
            /*判断是否为up事件，因为我们自己要消费move事件*/
            if (ev.getAction() == MotionEvent.ACTION_UP) {
                int endX = (int) ev.getX();
                int endY = (int) ev.getY();
                // 判断它为点击事件，点击事件就关闭菜单，否则就走正常的滑动事件
                if (Math.abs(endX - mStartX) < 10 && Math.abs(endY - mStartY) < 10) {
                    closeMenu();
                    return true;
                }
            }
        }
        if (gestureDetector.onTouchEvent(ev)) {
            return true;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                if (scrollX > mMenuWidth / 2) {
                    closeMenu();
                } else {
                    openMenu();
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        // 缩放大小  从1 -> 0
        float scale = 1.0f * l / mMenuWidth;
        /*右边content的缩放效果*/
        float rightScale = 0.7f + 0.3f * scale;
        /*设置缩放中心点*/
        ViewCompat.setPivotX(mContentView, 0);
        ViewCompat.setPivotY(mContentView, mContentView.getHeight() / 2);
        /*设置缩放比例*/
        ViewCompat.setScaleX(mContentView, rightScale);
        ViewCompat.setScaleY(mContentView, rightScale);

        /*左边menu的缩放效果*/
        float leftAlpha = 0.5f + 0.5f * (1 - scale);
        /*透明度效果*/
        ViewCompat.setAlpha(mMenuView, leftAlpha);
        /*设置缩放比例*/
        float leftScale = 0.7f + 0.3f * (1 - scale);
        ViewCompat.setScaleX(mMenuView, leftScale);
        ViewCompat.setScaleY(mMenuView, leftScale);
        /*设置平移*/
        ViewCompat.setTranslationX(mMenuView, 0.25f * l);
    }

    /*关闭菜单*/
    private void closeMenu() {
        /*带动画*/
        smoothScrollTo(mMenuWidth, 0);
        mIsMenuOpened = false;
    }

    /*打开菜单*/
    private void openMenu() {
        /*带动画*/
        smoothScrollTo(0, 0);
        mIsMenuOpened = true;
    }

    /*获取屏幕的宽*/
    private int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
