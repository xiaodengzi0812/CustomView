package com.djk.test;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Djk
 * @Title: drawPath 绘制动画
 * @Time: 2017/9/2.
 * @Version:1.0.0
 */
public class DrawAnimator extends View {
    /*矩形和圆的画笔*/
    private Paint mRectPaint, mCirclePaint;
    private Path mPath;
    private PathMeasure mPathMeasure;
    private float[] mPos = new float[2];
    private float[] mTan = new float[2];
    private int mRadius = 15;/*圆的半径*/

    public DrawAnimator(Context context) {
        this(context, null);
    }

    public DrawAnimator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawAnimator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //初始化矩形Paint
        mRectPaint = new Paint();
        mRectPaint.setDither(true);
        mRectPaint.setAntiAlias(true);
        mRectPaint.setStyle(Paint.Style.STROKE);
        mRectPaint.setStrokeWidth(6f);
        mRectPaint.setColor(Color.BLACK);
        //初始化圆形Paint
        mCirclePaint = new Paint();
        mCirclePaint.setDither(true);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(Color.RED);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
        int num = 3;
        int itemWidth = (width - mRadius * 2) / num;
        /*画一个图形，貌似是个眼镜*/
        mPath = new Path();
        RectF rectF = new RectF(mRadius, mRadius, itemWidth, itemWidth);
        mPath.addArc(rectF, 0, 270);
        RectF rectFTo = new RectF(mRadius + itemWidth * (num - 1), mRadius, mRadius + itemWidth * num, itemWidth);
        // false 表示连接两个点
        mPath.arcTo(rectFTo, 270, 270, false);
        /*close 表示起点与终点相连*/
        mPath.close();

        //初始化PathMeasure
        mPathMeasure = new PathMeasure();
        mPathMeasure.setPath(mPath, false);

        /*圆的起始位置*/
        mPos[0] = itemWidth;
        mPos[1] = (mRadius + itemWidth) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制矩形
        canvas.drawPath(mPath, mRectPaint);
        //绘制圆
        canvas.drawCircle(mPos[0], mPos[1], mRadius, mCirclePaint);
    }

    public void startMove() {
        /*动画从0到path的长度*/
        float pathLength = mPathMeasure.getLength();
        ValueAnimator animator = ValueAnimator.ofFloat(0, pathLength);
        animator.setDuration(5 * 1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float distance = (float) animation.getAnimatedValue();
                // pos[]  distance在path上的坐标，即pos[]存的该点的坐标x，y值
                // tan[]  distance在path上对应坐标点在path上的方向，tan[0]是邻边边长，tan[1]是对边边长。通过Math.atan2(tan[1], tan[0])*180.0/Math.PI 可以得到正切角的弧度值。
                mPathMeasure.getPosTan(distance, mPos, mTan);
                invalidate();
            }
        });
        animator.start();
    }
}
