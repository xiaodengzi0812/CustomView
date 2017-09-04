package com.djk.test;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author Djk
 * @Title: 自定义textview
 * @Time: 2017/8/29.
 * @Version:1.0.0
 */
public class MyTextView extends View {
    private String mText;
    private int mTextSize = 15;
    private int mTextColor;

    private Paint mPaint;


    public MyTextView(Context context) {
        this(context, null);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.mytv);
            mTextColor = typedArray.getColor(R.styleable.mytv_myTextColor, Color.parseColor("#333333"));
            mTextSize = (int) typedArray.getDimensionPixelSize(R.styleable.mytv_myTextSize, 16);
            mText = typedArray.getString(R.styleable.mytv_myText);
            typedArray.recycle();
        }
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 获取宽高值
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // 测试的宽高
        Rect bounds = new Rect();
        mPaint.getTextBounds(mText, 0, mText.length(), bounds);
        int textWidth = bounds.width();
        int textHeight = bounds.height();

        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = textWidth;
        }

        if (heightMode == MeasureSpec.AT_MOST) {
            Paint.FontMetrics fm = mPaint.getFontMetrics();
            heightSize = (int) (fm.bottom - fm.top);
        }
        setMeasuredDimension(widthSize + getPaddingLeft() + getPaddingRight(), heightSize + getPaddingTop());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*算基线*/
        Paint.FontMetrics fm = mPaint.getFontMetrics();
        float dy = (fm.bottom - fm.top) / 2 - fm.bottom;
        float baseLineY = getHeight() / 2 + dy;
        canvas.drawText(mText, getPaddingLeft(), baseLineY, mPaint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    /**
     * sp转px
     *
     * @param spValue sp值
     * @return px值
     */
    public int sp2px(final float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, getResources().getDisplayMetrics());
    }

}
