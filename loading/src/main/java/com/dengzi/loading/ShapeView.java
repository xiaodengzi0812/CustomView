package com.dengzi.loading;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * @Title: 三种图形切换自定义view
 * @Author: Djk
 * @Time: 2017/9/26
 * @Version:1.0.0
 */
public class ShapeView extends View {
    // 当前形状
    private Shape mCurrentShape = Shape.Circle;
    // 圆、矩形、三角形的画笔
    private Paint mCilclePaint, mRectPaint, mPathPaint;
    // 三角形的路径
    private Path mPath;

    // 形状枚举
    public enum Shape {
        Circle, Square, Triangle
    }

    public ShapeView(Context context) {
        this(context, null);
    }

    public ShapeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    // 初始化画笔
    private void initPaint() {
        mCilclePaint = getPaint(ContextCompat.getColor(getContext(), R.color.circle));
        mRectPaint = getPaint(ContextCompat.getColor(getContext(), R.color.rect));
        mPathPaint = getPaint(ContextCompat.getColor(getContext(), R.color.triangle));
    }

    // 获取画笔
    private Paint getPaint(int color) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);// 设置画笔的锯齿效果
        paint.setDither(true);// 设置画笔的防抖动
        paint.setColor(color);// 设置画笔颜色颜色
        return paint;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 只保证是正方形
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(Math.min(width, height), Math.min(width, height));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        switch (mCurrentShape) {
            case Circle:
                // 画圆形
                int center = getWidth() / 2;
                canvas.drawCircle(center, center, center, mCilclePaint);
                break;
            case Square:
                // 画正方形
                canvas.drawRect(0, 0, getWidth(), getHeight(), mRectPaint);
                break;
            case Triangle:
                // 画三角  Path 画路线
                if (mPath == null) {
                    // 画路径
                    mPath = new Path();
                    mPath.moveTo(getWidth() / 2, 0);
                    mPath.lineTo(0, (float) ((getWidth() / 2) * Math.sqrt(3)));
                    mPath.lineTo(getWidth(), (float) ((getWidth() / 2) * Math.sqrt(3)));
                    // path.lineTo(getWidth()/2,0);
                    mPath.close();// 把路径闭合
                }
                canvas.drawPath(mPath, mPathPaint);
                break;
        }
    }

    /**
     * 改变形状
     */
    public void exchange() {
        switch (mCurrentShape) {
            case Circle:
                mCurrentShape = Shape.Square;
                break;
            case Square:
                mCurrentShape = Shape.Triangle;
                break;
            case Triangle:
                // 画三角  Path 画路线
                mCurrentShape = Shape.Circle;
                break;
        }
        // 不断重新绘制形状
        invalidate();
    }

    /**
     * 获取当前的形状
     */
    public Shape getCurrentShape() {
        return mCurrentShape;
    }
}
