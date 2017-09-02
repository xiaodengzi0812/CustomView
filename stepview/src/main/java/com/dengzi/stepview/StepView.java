package com.dengzi.stepview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * @author Djk
 * @Title: 运动app中步数view
 * @Time: 2017/8/29.
 * @Version:1.0.0
 */
public class StepView extends View {
    /*默认字体大小*/
    private static final int DEFAULT_TEXT_SIZE = 16;
    /*默认的线粗*/
    private static final int DEFAULT_LINE_SIZE = 3;

    private String mStepText = "0";
    private int mOuterColor = getResources().getColor(R.color.outer_color);
    private int mInnerColor = getResources().getColor(R.color.inner_color);
    private int mTextColor = getResources().getColor(R.color.text_color);
    private int mTextSize = DEFAULT_TEXT_SIZE;
    private int mLineSize = DEFAULT_LINE_SIZE;
    private int mOuterStartAngle = 135;
    private int mOuterAddAngle = 270;
    private int mInnerStartAngle = 135;
    /*默认动画时间*/
    private int mDuration = 1000;
    /*是否需要动画*/
    private boolean mIsDuration = false;

    /*三个画笔*/
    private Paint mOuterPaint;
    private Paint mInnerPaint;
    private Paint mTextPaint;

    /*最大值与现在值*/
    private int mMaxStep;
    private int mDrawPercent;

    public StepView(Context context) {
        this(context, null);
    }

    public StepView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            /*获取对应的属性*/
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.custom_step);
            mOuterColor = typedArray.getColor(R.styleable.custom_step_stepOuterColor, getResources().getColor(R.color.outer_color));
            mInnerColor = typedArray.getColor(R.styleable.custom_step_stepInnerColor, getResources().getColor(R.color.inner_color));
            mTextColor = typedArray.getColor(R.styleable.custom_step_stepTextColor, getResources().getColor(R.color.text_color));
            mTextSize = typedArray.getDimensionPixelSize(R.styleable.custom_step_stepTextSize, DEFAULT_TEXT_SIZE);
            mLineSize = (int) typedArray.getDimension(R.styleable.custom_step_stepLineSize, DEFAULT_LINE_SIZE);
            mOuterStartAngle = typedArray.getInteger(R.styleable.custom_step_stepOuterStartAngle, 135);
            mOuterAddAngle = typedArray.getInteger(R.styleable.custom_step_stepOuterAddAngle, 270);
            mInnerStartAngle = typedArray.getInteger(R.styleable.custom_step_stepInnerStartAngle, 135);
            mDuration = typedArray.getInteger(R.styleable.custom_step_stepDuration, 1000);
            mIsDuration = typedArray.getBoolean(R.styleable.custom_step_stepIsDuration, false);
            typedArray.recycle();
        }
        /*初始化画笔*/
        initOuterPaint();
        initInnerPaint();
        initTextpain();
    }

    private void initOuterPaint() {
        mOuterPaint = new Paint();
        mOuterPaint.setAntiAlias(true);
        mOuterPaint.setColor(mOuterColor);
        mOuterPaint.setStyle(Paint.Style.STROKE);
        mOuterPaint.setStrokeCap(Paint.Cap.ROUND);
        mOuterPaint.setStrokeWidth(mLineSize);
    }

    private void initInnerPaint() {
        mInnerPaint = new Paint();
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setColor(mInnerColor);
        mInnerPaint.setStyle(Paint.Style.STROKE);
        mInnerPaint.setStrokeCap(Paint.Cap.ROUND);
        mInnerPaint.setStrokeWidth(mLineSize);
    }

    private void initTextpain() {
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*图形的区域*/
        int space = mLineSize / 2;
        RectF oval = new RectF(space, space, getWidth() - space, getHeight() - space);

        /*画最外面的一个狐*/
        canvas.drawArc(oval, mOuterStartAngle, mOuterAddAngle, false, mOuterPaint);

        /*画里面的一个狐*/
        canvas.drawArc(oval, mInnerStartAngle, mDrawPercent, false, mInnerPaint);

        /*画文字*/
        Rect bounds = new Rect();
        mTextPaint.getTextBounds(mStepText, 0, mStepText.length(), bounds);
        int x = (getWidth() - bounds.width()) / 2;
        Paint.FontMetricsInt fm = mOuterPaint.getFontMetricsInt();
        int dy = (fm.bottom - fm.top) / 2 - fm.bottom;
        int baseLineY = getHeight() / 2 + dy;
        canvas.drawText(mStepText, x, baseLineY, mTextPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        /*保证正方形，取宽高的最小值*/
        setMeasuredDimension(Math.min(widthSize, heightSize), Math.min(widthSize, heightSize));
    }

    public int sp2px(final float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, getResources().getDisplayMetrics());
    }

    /*设置步数*/
    public void setStep(final int maxStep, int nowStep) {
        if (nowStep > maxStep) {
            throw new NullPointerException("nowStep > mMaxStep Error!");
        }
        this.mMaxStep = maxStep;

        if (mIsDuration) {// 需要动画
            /*属性动画*/
            ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0, nowStep);
            valueAnimator.setDuration(mDuration);
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    float step = (float) animator.getAnimatedValue();
                    startAnimator((int) step);
                }
            });
            valueAnimator.start();
        } else {
            startAnimator(nowStep);
        }
    }

    /*绘图*/
    private void startAnimator(int showStep) {
        mDrawPercent = showStep * mOuterAddAngle / mMaxStep;
        mStepText = String.valueOf(showStep);
        invalidate();
    }
}
