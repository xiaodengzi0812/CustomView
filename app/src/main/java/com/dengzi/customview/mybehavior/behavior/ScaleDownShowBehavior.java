package com.dengzi.customview.mybehavior.behavior;

import android.animation.Animator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dengzi.customview.R;

/**
 * @Title: 下拉时显示FAB，上拉隐藏，留出更多位置给用户。
 * @Author: Djk
 * @Time: 2017/9/25
 * @Version:1.0.0
 */
public class ScaleDownShowBehavior extends FloatingActionButton.Behavior {
    // 当前view是否滑出屏幕
    private boolean isViewOut = false;
    // 当前view的动画是否结束
    private boolean isAnimatingFinished = true;
    // 底部的view
    private View mBottomTabView;

    public ScaleDownShowBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    /**
     * 摆放子 View 的时候调用
     */
    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, FloatingActionButton child, int layoutDirection) {
        mBottomTabView = parent.findViewById(R.id.tab_layout);
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        // 测量自己的高度
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        float childTranslationY = params.bottomMargin + child.getMeasuredHeight();
        // 测量联动bottom的高度
        CoordinatorLayout.LayoutParams tabParams = (CoordinatorLayout.LayoutParams) mBottomTabView.getLayoutParams();
        float tabTranslationY = tabParams.bottomMargin + mBottomTabView.getMeasuredHeight();
        if (dyConsumed > 0 || dyUnconsumed > 0) {//往下滑
            // 当前view已经隐藏，且动画已经结束
            if (!isViewOut && isAnimatingFinished) {
                // 设置自己的动画
                AnimatorUtil.scaleHide(child, childTranslationY, viewAnimatorListener);
                // 设置联动bottom的动画
                AnimatorUtil.scaleHide(mBottomTabView, tabTranslationY, viewAnimatorListener);
                isViewOut = true;
            }
        } else if (dyConsumed < 0 || dyUnconsumed < 0) {
            // 当前view已经隐藏，且动画已经结束
            if (isViewOut && isAnimatingFinished) {
                AnimatorUtil.scaleShow(child, viewAnimatorListener);
                AnimatorUtil.scaleShow(mBottomTabView, viewAnimatorListener);
                isViewOut = false;
            }
        }
    }

    /**
     * view的动画监听事件
     */
    private Animator.AnimatorListener viewAnimatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            isAnimatingFinished = false;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            isAnimatingFinished = true;
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            isAnimatingFinished = true;
        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

}
