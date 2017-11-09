package com.dengzi.recyclerviewlib.swipe;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.dengzi.recyclerviewlib.R;

/**
 * @author Djk
 * @Title: 侧滑菜单View
 * @Time: 2017/11/8.
 * @Version:1.0.0
 */
public class SwipeMenuLayout extends RelativeLayout {
    // 菜单view
    private View mMenuView;
    // 内容view
    private View mContentView;
    // 滑动的距离
    private float mDistanceX;
    // 菜单的宽度
    private int mMenuWidth;
    // 是否滑动，用来判断up的时候要不要传点击事件
    private boolean mIsScroll = false;
    // 菜单view的高度已设置
    private boolean mIsMenuHeightChanged = false;
    // 滑动type
    private int mDirection;
    // 从左向右滑
    public static final int LEFT_TO_RIGHT = 1;
    // 从右向左滑
    public static final int RIGHT_TO_LEFT = 2;

    public SwipeMenuLayout(Context context) {
        this(context, null);
    }

    public SwipeMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwipeMenu);
        mDirection = typedArray.getInt(R.styleable.SwipeMenu_swipe_direction, RIGHT_TO_LEFT);
        typedArray.recycle();
    }

    /**
     * 手势处理
     */
    GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // distanceX向左滑动为正值，向右滑动为负值
            mDistanceX = mDistanceX + distanceX;
            translationX();
            mIsScroll = true;
            return true;
        }
    });

    /**
     * x 方向的位移
     */
    private void translationX() {
        // 左滑
        if (mDirection == RIGHT_TO_LEFT) {
            mDistanceX = mDistanceX < 0 ? 0 : mDistanceX > mMenuWidth ? mMenuWidth : mDistanceX;
            mContentView.setTranslationX(-(int) mDistanceX);
        } else {// 右滑
            mDistanceX = mDistanceX > 0 ? 0 : Math.abs(mDistanceX) > mMenuWidth ? -mMenuWidth : mDistanceX;
            mContentView.setTranslationX(-(int) mDistanceX);
        }
    }

    /**
     * 手指up的时候，移动x方向的值
     */
    private void translationXWithUp() {
        // 左滑
        if (mDirection == RIGHT_TO_LEFT) {
            // 动画结束的值和动画要执行的时间
            float endX = mDistanceX < mMenuWidth / 2 ? 0 : mMenuWidth;
            float disTime = (mDistanceX < mMenuWidth / 2 ? mDistanceX : mMenuWidth - mDistanceX) * 2;
            startAnimator(mDistanceX, endX, (long) disTime);
        } else {// 右滑
            // 动画结束的值和动画要执行的时间
            float endX = Math.abs(mDistanceX) < mMenuWidth / 2 ? 0 : -mMenuWidth;
            float disTime = (Math.abs(mDistanceX) < mMenuWidth / 2 ? Math.abs(mDistanceX) : mMenuWidth - Math.abs(mDistanceX)) * 2;
            startAnimator(mDistanceX, endX, (long) disTime);
        }
    }

    /**
     * 开始一个位移动画
     *
     * @param start 开始位置
     * @param end   结束位置
     * @param time  动画时间
     */
    private void startAnimator(float start, float end, long time) {
        /*从现在的位置回弹到指定位置，开启一个动画*/
        ValueAnimator animator = ObjectAnimator.ofFloat(start, end).setDuration(Math.abs(time));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mDistanceX = (float) animation.getAnimatedValue();
                translationX();
            }
        });
        animator.start();
    }

    /**
     * 关闭侧滑菜单
     */
    public void closeMenu() {
        if (mDistanceX == 0) return;
        startAnimator(mDistanceX, 0, (long) Math.abs(mDistanceX));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        // 这里必须放两个view，下面的是菜单view，上面的是内容view
        if (childCount != 2) {
            throw new RuntimeException("child count mast be 2！");
        }
        mMenuView = getChildAt(0);
        mContentView = getChildAt(1);
        // 如果设置右边滑动
        if (mDirection == RIGHT_TO_LEFT) {
            LayoutParams params = (LayoutParams) mMenuView.getLayoutParams();
            params.addRule(ALIGN_PARENT_RIGHT);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        initMenuHeight();
    }

    /**
     * 初始化菜单的高度
     */
    private void initMenuHeight() {
        if (mMenuView != null && mContentView != null && !mIsMenuHeightChanged) {
            mMenuWidth = mMenuView.getMeasuredWidth();
            // 获取内容页的高度，并设置给菜单view
            int contentHeight = mContentView.getMeasuredHeight();
            int menuHeight = mMenuView.getMeasuredHeight();
            if (menuHeight == contentHeight) {
                mIsMenuHeightChanged = true;
                return;
            }
            ViewGroup.LayoutParams params = mMenuView.getLayoutParams();
            params.height = contentHeight;
            mMenuView.setLayoutParams(params);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        initMenuHeight();
        // 让手势帮助类去帮我们解决触摸的问题
        gestureDetector.onTouchEvent(ev);
        // down事件的时候将滑动判断位置为false
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mIsScroll = false;
        }
        // up事件的时候，跟据滑动的距离来处理菜单的显示与隐藏
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            translationXWithUp();
            // 如果是已滑动，则直接返回，从而将click事件截断
            if (mIsScroll) {
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
