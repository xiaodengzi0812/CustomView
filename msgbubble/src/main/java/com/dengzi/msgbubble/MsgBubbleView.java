package com.dengzi.msgbubble;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author Djk
 * @Title: 消息拖拽
 * @Time: 2017/9/29.
 * @Version:1.0.0
 */
public class MsgBubbleView extends View {
    // 画笔
    private Paint mPaint;
    // 起始点（静止的点） 、 移动点
    private Point mStaticPoint, mMovePoint;
    // 起始点（静止的点）的半径
    private int mStaticRadius;
    // 移动点的半径
    private int mMoveRadius = 50;
    // 最大移动的距离
    private int mMaxDistance = 500;

    public MsgBubbleView(Context context) {
        this(context, null);
    }

    public MsgBubbleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MsgBubbleView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        if (mStaticRadius > 10) {
            Path bezeierPath = getBezeierPath();
            canvas.drawPath(bezeierPath, mPaint);
            canvas.drawCircle(mStaticPoint.x, mStaticPoint.y, mStaticRadius, mPaint);
        }
        // 画移动的点，半径大小不变
        canvas.drawCircle(mMovePoint.x, mMovePoint.y, mMoveRadius, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 按下时初始化两个点的坐标
                mStaticPoint = new Point((int) event.getX(), (int) event.getY());
                mMovePoint = new Point((int) event.getX(), (int) event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                // 移动时设置移动点的坐标
                mMovePoint.x = (int) event.getX();
                mMovePoint.y = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                // 抬起时将两个点置为null
                mStaticPoint = null;
                mMovePoint = null;
                break;
        }
        invalidate();
        return true;
    }

    /*获取两点的距离*/
    public double getDistance() {
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
    public Path getBezeierPath() {
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
}
