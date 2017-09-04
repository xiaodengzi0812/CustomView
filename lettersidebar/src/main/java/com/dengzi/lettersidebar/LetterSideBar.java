package com.dengzi.lettersidebar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author Djk
 * @Title: 字母索引view
 * @Time: 2017/9/4.
 * @Version:1.0.0
 */
public class LetterSideBar extends View {
    /*默认颜色与高亮颜色*/
    private int mDefaultTextColor, mChangeTextColor;
    /*字体大小*/
    private float mDefaultTextSize;
    /*当前选中的位置*/
    private int mPosition;
    /*默认画笔和高亮画笔*/
    private Paint mPaint, mColorPaint;
    /*每个条目的高度*/
    private int mItemHeight;
    private final String[] mLetters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};

    /*回调*/
    private OnScrollListener mScrollListener;

    public interface OnScrollListener {
        void onResult(boolean isShow, String result);
    }

    public void setOnScrollListener(OnScrollListener scrollListener) {
        this.mScrollListener = scrollListener;
    }

    public LetterSideBar(Context context) {
        this(context, null);
    }

    public LetterSideBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterSideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.custom_letter_side);
            mDefaultTextColor = typedArray.getColor(R.styleable.custom_letter_side_letterSideDefaultTextColor, getResources().getColor(R.color.default_text_color));
            mChangeTextColor = typedArray.getColor(R.styleable.custom_letter_side_letterSideChangeTextColor, getResources().getColor(R.color.change_text_color));
            mDefaultTextSize = typedArray.getDimension(R.styleable.custom_letter_side_letterSideDefaultTextSize, 13);
            typedArray.recycle();
        }
        initPaint();
    }

    /*初始化两个画笔*/
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setTextSize(mDefaultTextSize);
        mPaint.setColor(mDefaultTextColor);

        mColorPaint = new Paint();
        mColorPaint.setAntiAlias(true);
        mColorPaint.setDither(true);
        mColorPaint.setTextSize(mDefaultTextSize);
        mColorPaint.setColor(mChangeTextColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        /*如果布局是wrap_content，则计算它的宽度*/
        if (widthMode == MeasureSpec.AT_MOST) { // 如果是wrap_content
            width = (int) (getPaddingLeft() + getPaddingRight() + mPaint.measureText("W") + 10);
        } else {
            width = MeasureSpec.getSize(widthMeasureSpec);
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int num = mLetters.length;
        mItemHeight = (getHeight() - getPaddingTop() - getPaddingBottom()) / num;
        Paint.FontMetricsInt fm = mPaint.getFontMetricsInt();
        int dy = (fm.bottom - fm.top) / 2 - fm.bottom + mItemHeight / 2;
        for (int i = 0; i < num; i++) {
            int drawX = (int) (getWidth() / 2 - mPaint.measureText(mLetters[i]) / 2);
            int baseLineY = dy + i * mItemHeight + getPaddingTop();
            /*选中的高亮颜色*/
            if (mPosition == i) {
                canvas.drawText(mLetters[i], drawX, baseLineY, mColorPaint);
            } else {
                canvas.drawText(mLetters[i], drawX, baseLineY, mPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                int position = ((int) (y - getPaddingTop())) / mItemHeight;
                if (position < 0) {
                    position = 0;
                }
                if (position >= mLetters.length) {
                    position = mLetters.length - 1;
                }
                setResult(true, position);
                if (position != mPosition) {
                    mPosition = position;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                setResult(false, mPosition);
                break;
        }
        return true;
    }

    /*设置回调，将触摸信息返回*/
    private void setResult(final boolean show, final int position) {
        if (mScrollListener != null) {
            if (!show) {
                /*延迟回调*/
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mScrollListener.onResult(show, mLetters[position]);
                    }
                }, 500);
            } else {
                mScrollListener.onResult(show, mLetters[position]);
            }
        }
    }
}
