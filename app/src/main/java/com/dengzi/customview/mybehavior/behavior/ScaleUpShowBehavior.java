package com.dengzi.customview.mybehavior.behavior;

import android.animation.Animator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ViewAnimator;

/**
 * @author Djk
 * @Title: 上拉时显示FAB，下拉时隐藏
 * @Time: 2017/9/4.
 * @Version:1.0.0
 */
public class ScaleUpShowBehavior extends FloatingActionButton.Behavior {
    // 当前view是否滑出屏幕
    private boolean isViewOut = false;
    // 当前view的动画是否结束
    private boolean isAnimatingFinished = true;

    public ScaleUpShowBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 当coordinatorLayout 的子View试图开始嵌套滑动的时候被调用。当返回值为true的时候表明
     * coordinatorLayout 充当nested scroll parent 处理这次滑动，需要注意的是只有当返回值为true
     * 的时候，Behavior 才能收到后面的一些nested scroll 事件回调（如：onNestedPreScroll、onNestedScroll等）
     * 这个方法有个重要的参数nestedScrollAxes，表明处理的滑动的方向。
     *
     * @param coordinatorLayout 和Behavior 绑定的View的父CoordinatorLayout
     * @param child             和Behavior 绑定的View
     * @param directTargetChild
     * @param target
     * @param nestedScrollAxes  嵌套滑动 应用的滑动方向，看 {@link ViewCompat#SCROLL_AXIS_HORIZONTAL},
     *                          {@link ViewCompat#SCROLL_AXIS_VERTICAL}
     * @return
     */
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        // 当前只监听竖方向的滑动
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    /**
     * 进行嵌套滚动时被调用
     *
     * @param coordinatorLayout
     * @param child
     * @param target
     * @param dxConsumed        target 已经消费的x方向的距离
     * @param dyConsumed        target 已经消费的y方向的距离
     * @param dxUnconsumed      x 方向剩下的滚动距离
     * @param dyUnconsumed      y 方向剩下的滚动距离
     */
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
//        Log.e("dengzi", "onNestedScroll- dxConsumed:" + dxConsumed + "; dyConsumed: " + dyConsumed + "; dxUnconsumed: " + dxUnconsumed + "; dyUnconsumed: " + dyUnconsumed);
//        if (dyConsumed > 0 && dyUnconsumed == 0) {
//            Log.e("dengzi", "上滑中。。。");
//        }
//        if (dyConsumed < 0 && dyUnconsumed == 0) {
//            Log.e("dengzi", "下滑中。。。");
//        }
//        if (dyConsumed == 0 && dyUnconsumed > 0) {
//            Log.e("dengzi", "到边界了还在上滑。。。");
//        }
//        if (dyConsumed == 0 && dyUnconsumed < 0) {
//            Log.e("dengzi", "到边界了，还在下滑。。。");
//        }

        // 获取当前view距离底部的高度，用来将view竖方向移动隐藏
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        float marginBottom = params.bottomMargin;
        float childHeight = child.getMeasuredHeight();
        float translationY = childHeight + marginBottom;

        // 判断向上滑动中，显示view
        if (dyConsumed > 0 || dyUnconsumed > 0) {// 显示
            // 当前view已经隐藏，且动画已经结束
            if (isViewOut && isAnimatingFinished) {
                AnimatorUtil.scaleShow(child, viewAnimatorListener);
                isViewOut = false;
            }
        } else if (dyConsumed < 0 || dyUnconsumed < 0) {// 判断向下滑动中，隐藏view
            // 当前view已经显示，且动画已经结束
            if (!isViewOut && isAnimatingFinished) {
                AnimatorUtil.scaleHide(child, translationY, viewAnimatorListener);
                isViewOut = true;
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
