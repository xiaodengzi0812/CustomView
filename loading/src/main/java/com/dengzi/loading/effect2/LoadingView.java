package com.dengzi.loading.effect2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

/**
 * @Title: 圆切换加载动画
 * @Author: Djk
 * @Time: 2017/9/26
 * @Version:1.0.0
 */
public class LoadingView extends RelativeLayout {
    // 五个圆
    private CircleView mLeftView, mMiddleView, mRightView, mTopView, mBottomView;
    // 圆球上下左右移动的距离
    private int mTranslationDistance = 30;
    // 动画时间
    private final long ANIMATION_TIME = 400;
    // 是否停止动画
    private boolean mIsStopAnimator = false;
    // 小球往外跑和往里跑的动画集合
    private AnimatorSet mExpendAnimatorSet, mInnerAnimatorSet;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTranslationDistance = dip2px(mTranslationDistance);
        setBackgroundColor(Color.WHITE);
        // 添加三个圆形View
        mLeftView = getCircleView(getContext(), Color.BLUE);
        mMiddleView = getCircleView(getContext(), Color.RED);
        mRightView = getCircleView(getContext(), Color.GREEN);
        mTopView = getCircleView(getContext(), Color.BLACK);
        mBottomView = getCircleView(getContext(), Color.CYAN);
        initLayout();
    }

    /**
     * 获取圆形
     *
     * @param context
     * @return
     */
    public CircleView getCircleView(Context context, int color) {
        CircleView circleView = new CircleView(context);
        LayoutParams params = new LayoutParams(dip2px(10), dip2px(10));
        params.addRule(CENTER_IN_PARENT);
        circleView.setLayoutParams(params);
        circleView.exchangeColor(color);
        return circleView;
    }

    private void initLayout() {
        addView(mLeftView);
        addView(mRightView);
        addView(mTopView);
        addView(mBottomView);
        addView(mMiddleView);

        initExpendAnimation();
        initInnerAnimation();

        // 让布局实例化好之后再去开启动画
        post(new Runnable() {
            @Override
            public void run() {
                expendAnimation();
            }
        });
    }

    /**
     * 初始化小球往外跑动画
     */
    private void initExpendAnimation() {
        // 上边跑
        ObjectAnimator topTranslationAnimator = ObjectAnimator.ofFloat(mTopView, "translationY", 0, -mTranslationDistance);
        // 下边跑
        ObjectAnimator bottomTranslationAnimator = ObjectAnimator.ofFloat(mBottomView, "translationY", 0, mTranslationDistance);
        // 左边跑
        ObjectAnimator leftTranslationAnimator = ObjectAnimator.ofFloat(mLeftView, "translationX", 0, -mTranslationDistance);
        // 右边跑
        ObjectAnimator rightTranslationAnimator = ObjectAnimator.ofFloat(mRightView, "translationX", 0, mTranslationDistance);
        // 弹性效果  荡秋千一样 差值器   刚开始快越来越慢
        mExpendAnimatorSet = new AnimatorSet();
        mExpendAnimatorSet.setDuration(ANIMATION_TIME);
        mExpendAnimatorSet.playTogether(leftTranslationAnimator, rightTranslationAnimator, topTranslationAnimator, bottomTranslationAnimator);
        mExpendAnimatorSet.setInterpolator(new DecelerateInterpolator());
        mExpendAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 从外往里面跑
                innerAnimation();
            }
        });
    }

    /**
     * 初始化小球往里跑动画
     */
    private void initInnerAnimation() {
        // 上边跑
        ObjectAnimator topTranslationAnimator = ObjectAnimator.ofFloat(mTopView, "translationY", -mTranslationDistance, 0);
        // 下边跑
        ObjectAnimator bottomTranslationAnimator = ObjectAnimator.ofFloat(mBottomView, "translationY", mTranslationDistance, 0);
        // 左边跑
        ObjectAnimator leftTranslationAnimator = ObjectAnimator.ofFloat(mLeftView, "translationX", -mTranslationDistance, 0);
        // 右边跑
        ObjectAnimator rightTranslationAnimator = ObjectAnimator.ofFloat(mRightView, "translationX", mTranslationDistance, 0);
        mInnerAnimatorSet = new AnimatorSet();
        mInnerAnimatorSet.setInterpolator(new AccelerateInterpolator());
        mInnerAnimatorSet.setDuration(ANIMATION_TIME);
        mInnerAnimatorSet.playTogether(leftTranslationAnimator, rightTranslationAnimator, topTranslationAnimator, bottomTranslationAnimator);
        mInnerAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 切换颜色顺序  上边的给中间 中间的给左边  左边的给下边 下边的给右边  右边的给上面
                int leftColor = mLeftView.getColor();
                int rightColor = mRightView.getColor();
                int middleColor = mMiddleView.getColor();
                int topColor = mTopView.getColor();
                int bottomColor = mBottomView.getColor();
                mMiddleView.exchangeColor(topColor);
                mRightView.exchangeColor(bottomColor);
                mLeftView.exchangeColor(middleColor);
                mTopView.exchangeColor(rightColor);
                mBottomView.exchangeColor(leftColor);
                // 从里往外面跑
                expendAnimation();
            }
        });
    }

    /**
     * 小球往外跑
     */
    private void expendAnimation() {
        if (mIsStopAnimator) return;
        mExpendAnimatorSet.start();
    }

    /**
     * 小球往里跑
     */
    private void innerAnimation() {
        if (mIsStopAnimator) return;
        mInnerAnimatorSet.start();
    }

    private int dip2px(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == View.VISIBLE) {
            if (getChildCount() == 0) {
                mIsStopAnimator = false;
                initLayout();
            }
        } else {
            // 清理动画
            mLeftView.clearAnimation();
            mMiddleView.clearAnimation();
            mRightView.clearAnimation();
            removeAllViews();// 移除自己所有的View
            mIsStopAnimator = true;
        }
    }

}
