package com.dengzi.loading.effect3;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.LinearInterpolator;

import com.dengzi.loading.R;

/**
 * @author Djk
 * @Title: 加载动画
 * @Time: 2017/10/6.
 * @Version:1.0.0
 */
public class LoadingView extends View {
    // 旋转动画执行的时间
    private final long ROTATION_ANIMATION_TIME = 2000;
    // 当前大圆旋转的角度（弧度）
    private float mCurrentRotationAngle = 0F;
    // 每一份的角度
    private float mPercentAngle;
    // 小圆的颜色列表
    private int[] mCircleColors;
    // 大圆里面包含很多小圆的半径 - 整宽度的 1/4
    private float mRotationRadius;
    // 每个小圆的半径 - 大圆半径的 1/8
    private float mCircleRadius;
    // 绘制所有效果的画笔
    private Paint mPaint;
    // 中心点
    private int mCenterX, mCenterY;
    // 整体的颜色背景
    private int mSplashColor = Color.WHITE;
    // 代表当前状态所画动画
    private DrawState mDrawState;
    // 当前大圆的半径
    private float mCurrentRotationRadius = mRotationRadius;
    // 空心圆初始半径
    private float mHoleRadius = 0F;
    // 屏幕对角线的一半
    private float mDiagonalDist;
    // 是否停止动画
    private boolean mIsStopAnimator = false;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 小圆的颜色列表
        mCircleColors = getContext().getResources().getIntArray(R.array.splash_circle_colors);
        // 每一份小圆角度
        mPercentAngle = (float) (Math.PI * 2 / mCircleColors.length);
        // 初始化画笔
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取屏幕的中心点坐标
        mCenterX = MeasureSpec.getSize(widthMeasureSpec) / 2;
        mCenterY = MeasureSpec.getSize(heightMeasureSpec) / 2;
        // 大圆里面包含很多小圆的半径 - 整宽度的 1/4
        mRotationRadius = MeasureSpec.getSize(widthMeasureSpec) / 4;
        // 每个小圆的半径 - 大圆半径的 1/8
        mCircleRadius = mRotationRadius / 8;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mIsStopAnimator) return;
        if (mDrawState == null) {
            mDrawState = new RotationState();
        }
        mDrawState.draw(canvas);
    }

    /**
     * 消失
     */
    public void disappear() {
        // 关闭旋转动画
        if (mDrawState instanceof RotationState) {
            mDrawState.cancel();
            // 开始聚合动画
            mDrawState = new MergeState();
        }
    }

    public abstract class DrawState {
        public abstract void draw(Canvas canvas);

        public abstract void cancel();
    }

    /**
     * 旋转动画
     */
    public class RotationState extends DrawState {
        private ValueAnimator mAnimator;

        public RotationState() {
            mAnimator = ObjectAnimator.ofFloat(0, 2 * (float) Math.PI);
            mAnimator.setDuration(ROTATION_ANIMATION_TIME);
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentRotationAngle = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            mAnimator.setInterpolator(new LinearInterpolator());
            // 不断反复执行
            mAnimator.setRepeatCount(-1);
            mAnimator.start();
        }

        @Override
        public void draw(Canvas canvas) {
            // 画一个背景 白色
            canvas.drawColor(mSplashColor);
            for (int i = 0; i < mCircleColors.length; i++) {
                mPaint.setColor(mCircleColors[i]);
                // 当前具体的角度
                float currentAngle = (mCurrentRotationAngle + mPercentAngle * i);
                float cx = (float) (mCenterX + mRotationRadius * Math.cos(currentAngle));
                float cy = (float) (mCenterY + mRotationRadius * Math.sin(currentAngle));
                canvas.drawCircle(cx, cy, mCircleRadius, mPaint);
            }
        }

        /**
         * 取消动画
         */
        @Override
        public void cancel() {
            mAnimator.cancel();
        }
    }

    /**
     * 聚合动画
     */
    public class MergeState extends DrawState {
        private ValueAnimator mAnimator;

        public MergeState() {
            mAnimator = ObjectAnimator.ofFloat(mRotationRadius, 0);
            mAnimator.setDuration(ROTATION_ANIMATION_TIME / 2);
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentRotationRadius = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            // 等聚合完毕画展开
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mDrawState = new ExpendState();
                }
            });
            // 开始的时候向后然后向前甩
            mAnimator.setInterpolator(new AnticipateInterpolator(5f));
            mAnimator.start();
        }

        @Override
        public void draw(Canvas canvas) {
            // 画一个背景 白色
            canvas.drawColor(mSplashColor);
            for (int i = 0; i < mCircleColors.length; i++) {
                mPaint.setColor(mCircleColors[i]);
                // 当前具体的角度
                float currentAngle = (mCurrentRotationAngle + mPercentAngle * i);
                float cx = (float) (mCenterX + mCurrentRotationRadius * Math.cos(currentAngle));
                float cy = (float) (mCenterY + mCurrentRotationRadius * Math.sin(currentAngle));
                canvas.drawCircle(cx, cy, mCircleRadius, mPaint);
            }
        }

        /**
         * 取消动画
         */
        @Override
        public void cancel() {
            mAnimator.cancel();
        }
    }

    /**
     * 展开动画
     */
    public class ExpendState extends DrawState {
        private ValueAnimator mAnimator;
        private Paint mPaint;

        public ExpendState() {
            // 设置画笔属性
            mPaint = new Paint();
            mPaint.setDither(true);
            mPaint.setAntiAlias(true);
            mPaint.setColor(mSplashColor);
            mPaint.setStyle(Paint.Style.STROKE);

            // 求出中心点到左上角的距离，以此距离为空心圆的半径
            mDiagonalDist = (float) Math.sqrt(mCenterX * mCenterX + mCenterY * mCenterY);
            mAnimator = ObjectAnimator.ofFloat(0, mDiagonalDist);
            mAnimator.setDuration(ROTATION_ANIMATION_TIME / 2);
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mHoleRadius = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.start();
        }

        @Override
        public void draw(Canvas canvas) {
            // 空心圆画笔的宽度
            float strokeWidth = mDiagonalDist - mHoleRadius;
            mPaint.setStrokeWidth(strokeWidth);
            // 半径，空心圆半径加上画笔宽度的一半
            float radius = mHoleRadius + strokeWidth / 2;
            canvas.drawCircle(mCenterX, mCenterY, radius, mPaint);
        }

        /**
         * 取消动画
         */
        @Override
        public void cancel() {
            mAnimator.cancel();
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == View.VISIBLE && mIsStopAnimator) {
            mIsStopAnimator = false;
            invalidate();
        } else if (visibility == View.INVISIBLE || visibility == View.GONE) {
            // 清理动画
            if (mDrawState != null) {
                mDrawState.cancel();
            }
            mDrawState = null;
            mIsStopAnimator = true;
        }
    }

}
