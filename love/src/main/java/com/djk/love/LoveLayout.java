package com.djk.love;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

/**
 * @author Djk
 * @Title: 花束直播点赞效果
 * @Time: 2017/10/4.
 * @Version:1.0.0
 */
public class LoveLayout extends RelativeLayout {
    private Context mContext;
    // 随机数生成器
    private Random mRandom;
    // 三个花形的图片资源
    private int[] mImageRes = {R.drawable.pl_blue, R.drawable.pl_red, R.drawable.pl_yellow};
    // 差值器集合
    private Interpolator[] mInterpolator = new Interpolator[]{
            new AccelerateDecelerateInterpolator(), new AccelerateInterpolator(),
            new DecelerateInterpolator(), new LinearInterpolator()};
    // view的宽、高
    private int mWidth, mHeight;
    // 添加图片的宽、高
    private int mImageWidth, mImageHeight;

    public LoveLayout(Context context) {
        this(context, null);
    }

    public LoveLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoveLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mRandom = new Random();
        // 获取添加图片的宽、高
        Drawable imageDrawable = ContextCompat.getDrawable(context, R.drawable.pl_blue);
        mImageWidth = imageDrawable.getIntrinsicWidth();
        mImageHeight = imageDrawable.getIntrinsicHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取当前view的宽、高
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    /**
     * 添加花束
     *
     * @param number 每次添加花束的个数
     */
    public void addLove(int number) {
        for (int i = 0; i < number; i++) {
            ImageView loveIv = new ImageView(mContext);
            RelativeLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(ALIGN_PARENT_BOTTOM);
            params.addRule(CENTER_HORIZONTAL);
            loveIv.setLayoutParams(params);
            loveIv.setImageResource(mImageRes[mRandom.nextInt(mImageRes.length - 1)]);
            addView(loveIv);
            startAnimator(loveIv);
        }
    }

    /**
     * 开启动画
     */
    private void startAnimator(final ImageView loveIv) {
        AnimatorSet allSet = new AnimatorSet();
        AnimatorSet showSet = getShowAnimator(loveIv);
        ValueAnimator bezierAnimator = getBezierAnimator(loveIv);
        // 按顺序执行
        allSet.playSequentially(showSet, bezierAnimator);
        allSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                removeView(loveIv);
            }
        });
        allSet.start();
    }

    /**
     * 显示动画
     */
    private AnimatorSet getShowAnimator(ImageView loveIv) {
        AnimatorSet showSet = new AnimatorSet();
        // 透明度动画
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(loveIv, "alpha", 0.2f, 1f);
        // x方向的缩放
        ObjectAnimator scaleAnimatorX = ObjectAnimator.ofFloat(loveIv, "scaleX", 0.2f, 1f);
        // y方向的缩放
        ObjectAnimator scaleAnimatorY = ObjectAnimator.ofFloat(loveIv, "scaleY", 0.2f, 1f);
        showSet.playTogether(alphaAnimator, scaleAnimatorX, scaleAnimatorY);
        showSet.setDuration(360);
        return showSet;
    }

    /**
     * 贝塞尔移动动画
     */
    private ValueAnimator getBezierAnimator(final ImageView loveIv) {
        PointF pointF0 = new PointF(mWidth / 2 - mImageWidth / 2, mHeight - mImageHeight);
        PointF pointF1 = new PointF(mRandom.nextInt(mWidth - mImageWidth), mRandom.nextInt(mHeight / 2) + mHeight / 2);
        PointF pointF2 = new PointF(mRandom.nextInt(mWidth - mImageWidth), mRandom.nextInt(mHeight / 2));
        PointF pointF3 = new PointF(mRandom.nextInt(mWidth - mImageWidth), 0);

        LoveTypeEvaluator typeEvaluator = new LoveTypeEvaluator(pointF1, pointF2);
        // ofObject  第一个参数 LoveTypeEvaluator 第二个参数 p0, 第三个是 p3
        ValueAnimator bezierAnimator = ObjectAnimator.ofObject(typeEvaluator, pointF0, pointF3);
        bezierAnimator.setDuration(2000);
        // 设置差值器
        bezierAnimator.setInterpolator(mInterpolator[mRandom.nextInt(mInterpolator.length - 1)]);
        bezierAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 设置图片的位置
                PointF pointF = (PointF) animation.getAnimatedValue();
                loveIv.setX(pointF.x);
                loveIv.setY(pointF.y);
                // 设置图片的透明度
                float t = animation.getAnimatedFraction();
                loveIv.setAlpha(1 - t);
            }
        });
        return bezierAnimator;
    }

}
