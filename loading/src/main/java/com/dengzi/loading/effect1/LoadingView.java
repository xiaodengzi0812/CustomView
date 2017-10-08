package com.dengzi.loading.effect1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.dengzi.loading.R;

/**
 * @author Djk
 * @Title: 动画加载
 * @Time: 2017/9/26.
 * @Version:1.0.0
 */
public class LoadingView extends LinearLayout {
    // 上面的形状
    private ShapeView mShapeView;
    // 中间的阴影
    private View mShadowView;
    // 上下执行动画的高度
    private int mTranslationDistance = 0;
    // 动画执行的时间
    private final long ANIMATOR_DURATION = 500;
    // 是否停止动画
    private boolean mIsStopAnimator = false;
    // 下落和上弹动画集合
    private AnimatorSet mFallAnimatorSet, mUpAnimatorSet;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout();
    }

    /**
     * 初始化当前view
     */
    private void initLayout() {
        mTranslationDistance = dip2px(80);
        inflate(getContext(), R.layout.loading_view, this);
        mShapeView = (ShapeView) findViewById(R.id.shape_view);
        mShadowView = findViewById(R.id.shadow_view);
        // 初始化动画
        initFallAnimator();
        initUpAnimator();
        // view的绘制流程走完后再调用动画
        post(new Runnable() {
            @Override
            public void run() {
                startFallAnimator();
            }
        });
    }

    /**
     * 初始化下落动画
     */
    private void initFallAnimator() {
        // 图形的位移动画
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(mShapeView, "translationY", 0, mTranslationDistance);
        // 配合中间阴影缩小
        ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(mShadowView, "scaleX", 1.0f, 0.3f);
        // 动画集合
        mFallAnimatorSet = new AnimatorSet();
        mFallAnimatorSet.setDuration(ANIMATOR_DURATION);
        mFallAnimatorSet.setInterpolator(new AccelerateInterpolator());
        mFallAnimatorSet.playTogether(translationAnimator, scaleAnimator);
        // 添加一个结束的监听事件，本次动画结束后就去执行向上弹的动画
        mFallAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 改变图形样式
                mShapeView.exchange();
                startUpAnimator();
            }
        });
    }

    /**
     * 初始化上弹动画
     */
    private void initUpAnimator() {
        // 图形的位移动画
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(mShapeView, "translationY", mTranslationDistance, 0);
        // 配合中间阴影放大
        ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(mShadowView, "scaleX", 0.3f, 1.0f);
        // 图形的旋转
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(mShapeView, "rotation", 0, 180);
        // 动画集合
        mUpAnimatorSet = new AnimatorSet();
        mUpAnimatorSet.setDuration(ANIMATOR_DURATION);
        mUpAnimatorSet.setInterpolator(new DecelerateInterpolator());
        mUpAnimatorSet.playTogether(translationAnimator, scaleAnimator, rotationAnimator);
        // 添加一个结束的监听事件，本次动画结束后就去执行向上弹的动画
        mUpAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 上抛完之后就下落了
                startFallAnimator();
            }
        });
    }

    /**
     * 下落动画
     */
    private void startFallAnimator() {
        if (mIsStopAnimator) return;
        mFallAnimatorSet.start();
    }

    /**
     * 上弹动画
     */
    private void startUpAnimator() {
        if (mIsStopAnimator) return;
        mUpAnimatorSet.start();
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
            mShapeView.clearAnimation();
            mShadowView.clearAnimation();
            removeAllViews();// 移除自己所有的View
            mIsStopAnimator = true;
        }
    }
}
