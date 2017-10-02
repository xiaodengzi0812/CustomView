package com.dengzi.msgbubble.drag;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * @author Djk
 * @Title: 任意view拖拽
 * @Time: 2017/9/29.
 * @Version:1.0.0
 */
public class MsgDragView extends View {
    // 画笔
    private Paint mPaint;
    // 起始点（静止的点） 、 移动点
    private Point mStaticPoint, mMovePoint;
    // 起始点（静止的点）的半径
    private int mStaticRadius;
    // 起始点（静止的点）的最小半径
    private final int MIN_RADIUS = 10;
    // 移动点的半径
    private int mMoveRadius = 50;
    // 最大移动的距离
    private int mMaxDistance = 500;
    // 要拖拽的view的Bitmap
    private Bitmap mDragBitmap;
    // 回弹动画回调位置集合
    private float[] mPos = new float[2];
    private float[] mTan = new float[2];

    public MsgDragView(Context context) {
        this(context, null);
    }

    public MsgDragView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MsgDragView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);// 设置画笔的锯齿效果
        mPaint.setDither(true);// 设置画笔的防抖动
        mPaint.setColor(Color.RED);// 设置画笔颜色颜色
        mPaint.setStyle(Paint.Style.FILL);// 空心画笔属性
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 点为null时不画
        if (mStaticPoint == null || mMovePoint == null) return;

        // 静止的点半径随着两个点的距离的变化而变化，距离越大，半径越小
        mStaticRadius = (int) (mMoveRadius - mMoveRadius * getDistance() / mMaxDistance);
        // 当半径大于10才画静止的点和贝塞尔曲线
        if (mStaticRadius > MIN_RADIUS) {
            Path bezeierPath = getBezierPath();
            canvas.drawPath(bezeierPath, mPaint);
            canvas.drawCircle(mStaticPoint.x, mStaticPoint.y, mStaticRadius, mPaint);
        }

        if (mDragBitmap != null) {
            // 画移动的view 位置也是手指移动的位置 , 中心位置才是手指拖动的位置
            int centerX = mMovePoint.x - mDragBitmap.getWidth() / 2;
            int centerY = mMovePoint.y - mDragBitmap.getHeight() / 2;
            canvas.drawBitmap(mDragBitmap, centerX, centerY, null);
        }
    }

    /**
     * 初始化点
     */
    public void initPoint(float x, float y) {
        // 按下时初始化两个点的坐标
        mStaticPoint = new Point((int) x, (int) y);
        mMovePoint = new Point((int) x, (int) y);
    }

    /**
     * 移动时更新点坐标
     */
    public void updataPoint(float x, float y) {
        // 移动时设置移动点的坐标
        mMovePoint.x = (int) x;
        mMovePoint.y = (int) y;
        invalidate();
    }

    /**
     * 手指抬起
     */
    public void onTouchActionUp(View dragView) {
        // 还在回弹范围内，可以回弹
        if (mStaticRadius > MIN_RADIUS) {
            startMove();
        } else { // 不在回弹范围内，要消失
            if (mDragListener != null) {
                mDragListener.dismiss(mMovePoint);
            }
        }
    }

    /**
     * 回弹动画
     */
    public void startMove() {
        // 创建一个路径，从手指松开的位置到起始位置
        Path linePath = new Path();
        linePath.moveTo(mMovePoint.x, mMovePoint.y);
        linePath.lineTo(mStaticPoint.x, mStaticPoint.y);
        final PathMeasure pathMeasure = new PathMeasure();
        pathMeasure.setPath(linePath, false);
        //动画从0到path的长度
        float pathLength = pathMeasure.getLength();
        ValueAnimator animator = ObjectAnimator.ofFloat(0, pathLength);
        animator.setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float distance = (float) animation.getAnimatedValue();
                pathMeasure.getPosTan(distance, mPos, mTan);
                // 用代码更新拖拽点
                updataPoint(mPos[0], mPos[1]);
            }
        });
        // 设置一个差值器 在结束的时候回弹
        animator.setInterpolator(new BounceInterpolator());
        animator.start();
        // 监听动画的结束
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mDragListener != null) {
                    mDragListener.restore();
                }
            }
        });
    }

    /**
     * 获取两点的距离
     */
    private double getDistance() {
        int x1 = mStaticPoint.x;
        int y1 = mStaticPoint.y;
        int x2 = mMovePoint.x;
        int y2 = mMovePoint.y;
        double _x = Math.abs(x2 - x1);
        double _y = Math.abs(y2 - y1);
        return Math.sqrt(_x * _x + _y * _y);
    }

    /**
     * 获取贝塞尔的路径
     *
     * @return
     */
    private Path getBezierPath() {
        //定义一个路径
        Path path = new Path();
        // 获取两点的距离
        float dis = (float) getDistance();
        // 如果两点距离为0，则不画路径
        if (dis == 0) return path;
        // 两个点的x、y坐标距离
        float dy = mMovePoint.y - mStaticPoint.y;
        float dx = mMovePoint.x - mStaticPoint.x;
        // 求出sin、cos值
        float sin = dy / dis;
        float cos = dx / dis;

        // 分别求出四个对应的点
        int p0X = (int) (mStaticPoint.x + mStaticRadius * sin);
        int p0Y = (int) (mStaticPoint.y - mStaticRadius * cos);

        int p1X = (int) (mStaticPoint.x - mStaticRadius * sin);
        int p1Y = (int) (mStaticPoint.y + mStaticRadius * cos);

        int p2X = (int) (mMovePoint.x - mMoveRadius * sin);
        int p2Y = (int) (mMovePoint.y + mMoveRadius * cos);

        int p3X = (int) (mMovePoint.x + mMoveRadius * sin);
        int p3Y = (int) (mMovePoint.y - mMoveRadius * cos);

        // 求出贝塞尔曲线的控制点，我们将这个点设置到线中间的0.618位置处，黄金分割点处
        int controlX = (int) (mMovePoint.x - getDistance() * 0.618f * cos);
        int controlY = (int) (mMovePoint.y - getDistance() * 0.618f * sin);
        /*贝塞尔曲线的起点位置*/
        path.moveTo(p0X, p0Y);
        /*
        controlX, controlY 为控制点坐标
        p3X, p3Y  为终点坐标
        */
        path.quadTo(controlX, controlY, p3X, p3Y);
        path.lineTo(p2X, p2Y);
        /*
        controlX, controlY 为控制点坐标
        p1X, p1Y 为终点坐标
        */
        path.quadTo(controlX, controlY, p1X, p1Y);
        path.close();
        return path;
    }

    /**
     * 设置拖拽view的bitmap
     *
     * @param dragBitmap
     */
    public void setDragBitmap(Bitmap dragBitmap) {
        this.mDragBitmap = dragBitmap;
    }


    /**
     * 拖拽的监听回调
     */
    private MsgDragListener mDragListener;

    public void setMsgDragListener(MsgDragListener listener) {
        this.mDragListener = listener;
    }

    public interface MsgDragListener {
        // 还原
        void restore();

        // 消失爆炸
        void dismiss(Point point);
    }

}
