package com.dengzi.customview.mybehavior.behavior;

import android.animation.Animator;
import android.view.View;

/**
 * @Title: 动画工具类
 * @Author: Djk
 * @Time: 2017/9/25
 * @Version:1.0.0
 */
public class AnimatorUtil {

    // 显示view
    public static void scaleShow(View view, Animator.AnimatorListener viewAnimatorListener) {
        view.animate().translationY(0).setDuration(200).setListener(viewAnimatorListener).start();
    }

    // 隐藏view
    public static void scaleHide(View view, float translationY, Animator.AnimatorListener viewAnimatorListener) {
        view.animate().translationY(translationY).setDuration(200).setListener(viewAnimatorListener).start();
    }

}
