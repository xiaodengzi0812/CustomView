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

    private String stepText = "0";
    private int outerColor = getResources().getColor(R.color.outer_color);
    private int innerColor = getResources().getColor(R.color.inner_color);
    private int textColor = getResources().getColor(R.color.text_color);
    private int textSize = DEFAULT_TEXT_SIZE;
    private int lineSize = DEFAULT_LINE_SIZE;
    /*三个画笔*/
    private Paint outerPaint;
    private Paint innerPaint;
    private Paint textPaint;

    /*最大值与现在值*/
    private int maxStep;
    private int drawPercent;

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
            outerColor = typedArray.getColor(R.styleable.custom_step_outerColor, getResources().getColor(R.color.outer_color));
            innerColor = typedArray.getColor(R.styleable.custom_step_innerColor, getResources().getColor(R.color.inner_color));
            textColor = typedArray.getColor(R.styleable.custom_step_stepTextColor, getResources().getColor(R.color.text_color));
            textSize = typedArray.getDimensionPixelSize(R.styleable.custom_step_stepTextSize, DEFAULT_TEXT_SIZE);
            lineSize = (int) typedArray.getDimension(R.styleable.custom_step_stepLineSize, DEFAULT_LINE_SIZE);
        }
        /*初始化画笔*/
        initOuterPaint();
        initInnerPaint();
        initTextpain();
    }


    private void initOuterPaint() {
        outerPaint = new Paint();
        outerPaint.setAntiAlias(true);
        outerPaint.setColor(outerColor);
        outerPaint.setStyle(Paint.Style.STROKE);
        outerPaint.setStrokeCap(Paint.Cap.ROUND);
        outerPaint.setStrokeWidth(lineSize);
    }

    private void initInnerPaint() {
        innerPaint = new Paint();
        innerPaint.setAntiAlias(true);
        innerPaint.setColor(innerColor);
        innerPaint.setStyle(Paint.Style.STROKE);
        innerPaint.setStrokeCap(Paint.Cap.ROUND);
        innerPaint.setStrokeWidth(lineSize);
    }

    private void initTextpain() {
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*图形的区域*/
        int space = lineSize / 2;
        RectF oval = new RectF(space, space, getWidth() - space, getHeight() - space);

        /*画最外面的一个狐*/
        canvas.drawArc(oval, 135, 270, false, outerPaint);

        /*画里面的一个狐*/
        canvas.drawArc(oval, 135, drawPercent, false, innerPaint);

        /*画文字*/
        Rect bounds = new Rect();
        textPaint.getTextBounds(stepText, 0, stepText.length(), bounds);
        int x = (getWidth() - bounds.width()) / 2;
        Paint.FontMetricsInt fm = outerPaint.getFontMetricsInt();
        int dy = (fm.bottom - fm.top) / 2 - fm.bottom;
        int baseLineY = getHeight() / 2 + dy;
        canvas.drawText(stepText, x, baseLineY, textPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthSize < heightSize ? widthSize : heightSize,
                widthSize < heightSize ? widthSize : heightSize);
    }

    public int sp2px(final float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, getResources().getDisplayMetrics());
    }

    /*设置步数*/
    public void setStep(final int maxStep, int nowStep) {
        if (nowStep > maxStep) {
            throw new NullPointerException("nowStep > maxStep Error!");
        }
        this.maxStep = maxStep;

        /*属性动画*/
        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0, nowStep);
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                float step = (float) animator.getAnimatedValue();
                startAnimator((int) step);
            }
        });
        valueAnimator.start();
    }

    /*绘图*/
    private void startAnimator(int showStep) {
        drawPercent = showStep * 270 / maxStep;
        stepText = String.valueOf(showStep);
        invalidate();
    }
}
