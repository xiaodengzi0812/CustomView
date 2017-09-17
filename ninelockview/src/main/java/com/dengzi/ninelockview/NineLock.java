package com.dengzi.ninelockview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Djk
 * @Title: 九宫格解锁
 * @Time: 2017/9/15.
 * @Version:1.0.0
 */
public class NineLock extends View {
    /*内环画笔*/
    private Paint mInnerDefaultPaint, mInnerSelectedPaint, mInnerErrorPaint;
    /*外环画笔*/
    private Paint mOuterDefaultPaint, mOuterSelectedPaint, mOuterErrorPaint;
    /*线画笔*/
    private Paint mLinePaint, mLineErrorPaint;
    /*箭头画笔*/
    private Paint mArrawPaint, mArrawErrorPaint;
    /*内半径，外半径*/
    private int mInnerRadius, mOuterRadius;
    /*3*3的九个点*/
    private Point[][] mPoints = new Point[3][3];
    /*已选中的点*/
    private List<Point> mSelectPointList = new ArrayList<>();
    /*当前状态是不是up*/
    private boolean mIsActionUp = false;
    /*密码校验成功*/
    private boolean mIsSuccess = false;
    /*当前所记录的密码*/
    private String mPassword = "03678";

    public void setPassword(String password) {
        this.mPassword = password;
    }

    private OnResultListener mOnResultListener;

    public void setOnResultListener(OnResultListener onResultListener) {
        this.mOnResultListener = onResultListener;
    }

    public interface OnResultListener {
        void onResult(String result);
    }

    public NineLock(Context context) {
        this(context, null);
    }

    public NineLock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NineLock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    /*画笔创建类*/
    private Paint getPaint(int color, int strokeWidth) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        return paint;
    }

    /*初始化画笔*/
    private void initPaint() {
        mInnerDefaultPaint = getPaint(Color.parseColor("#BBBBBB"), 5);
        mInnerSelectedPaint = getPaint(Color.parseColor("#3A99FF"), 8);
        mInnerErrorPaint = getPaint(Color.parseColor("#CC0033"), 8);
        mOuterDefaultPaint = getPaint(Color.parseColor("#CCCCCC"), 10);
        mOuterSelectedPaint = getPaint(Color.parseColor("#00D8FD"), 13);
        mOuterErrorPaint = getPaint(Color.parseColor("#CC0033"), 13);
        mLinePaint = getPaint(Color.parseColor("#3A99FF"), 10);
        mLineErrorPaint = getPaint(Color.parseColor("#CC0033"), 10);
        mArrawPaint = getPaint(Color.parseColor("#3A99FF"), 10);
        mArrawPaint.setStyle(Paint.Style.FILL);
        mArrawErrorPaint = getPaint(Color.parseColor("#CC0033"), 10);
        mArrawErrorPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        /*竖屏的时候，计算每个点的位置*/
        if (height > width) {
            int marTopY = (height - width) / 2;
            initPoint(width, marTopY);
        }
    }

    /*初始化点的坐标*/
    private void initPoint(int width, int marTopY) {
        /*第一行*/
        int height0 = marTopY + width / 6;
        mPoints[0][0] = new Point(width / 6, height0, 0);
        mPoints[0][1] = new Point(width / 2, height0, 1);
        mPoints[0][2] = new Point(width * 5 / 6, height0, 2);

        /*第二行*/
        int height1 = marTopY + width / 2;
        mPoints[1][0] = new Point(width / 6, height1, 3);
        mPoints[1][1] = new Point(width / 2, height1, 4);
        mPoints[1][2] = new Point(width * 5 / 6, height1, 5);

        /*第三行*/
        int height2 = marTopY + width * 5 / 6;
        mPoints[2][0] = new Point(width / 6, height2, 6);
        mPoints[2][1] = new Point(width / 2, height2, 7);
        mPoints[2][2] = new Point(width * 5 / 6, height2, 8);

        /*内外环的半径*/
        mInnerRadius = width / 50;
        mOuterRadius = width / 12;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawPoint(canvas);
        drawLine(canvas);
    }

    /*画点*/
    private void drawPoint(Canvas canvas) {
        /*画默认的*/
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Point point = mPoints[i][j];
                canvas.drawCircle(point.x, point.y, mInnerRadius, mInnerDefaultPaint);
                canvas.drawCircle(point.x, point.y, mOuterRadius, mOuterDefaultPaint);
            }
        }
        /*画选中的点*/
        for (Point point : mSelectPointList) {
            canvas.drawCircle(point.x, point.y, mInnerRadius, mInnerSelectedPaint);
            canvas.drawCircle(point.x, point.y, mOuterRadius, mOuterSelectedPaint);
        }

        /*当密码错误时*/
        if (!mIsSuccess && mIsActionUp) {
            for (Point point : mSelectPointList) {
                canvas.drawCircle(point.x, point.y, mInnerRadius, mInnerErrorPaint);
                canvas.drawCircle(point.x, point.y, mOuterRadius, mOuterErrorPaint);
            }
        }
    }

    /*画线*/
    private void drawLine(Canvas canvas) {
        int selectPointCount = mSelectPointList.size();
        /*画已经选择的两个点之间的线*/
        for (int i = 0; i < selectPointCount - 1; i++) {
            Point point0 = mSelectPointList.get(i);
            Point point1 = mSelectPointList.get(i + 1);
            drawPoint2PointLine(canvas, point0.x, point0.y, point1.x, point1.y);
        }
        /*画最后一个选择的点到手指触摸位置的线*/
        if (selectPointCount > 0 && !mIsActionUp) {
            Point lastPoint = mSelectPointList.get(selectPointCount - 1);
            drawPoint2EndLine(canvas, lastPoint.x, lastPoint.y, mEventX, mEventY);
        }
    }

    /*画已经选择的两个点之间的线*/
    private void drawPoint2PointLine(Canvas canvas, int x1, int y1, int x2, int y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double ds = Math.sqrt(dx * dx + dy * dy);
        double cos = dx / ds;
        double sin = dy / ds;

        int radiusX = (int) (mInnerRadius * cos);
        int radiusY = (int) (mInnerRadius * sin);
        int newX1 = x1 + radiusX;
        int newY1 = y1 + radiusY;

        int newX2 = x2 - radiusX;
        int newY2 = y2 - radiusY;
        canvas.drawLine(newX1, newY1, newX2, newY2, mLinePaint);
        if (!mIsSuccess && mIsActionUp) {
            canvas.drawLine(newX1, newY1, newX2, newY2, mLineErrorPaint);
        }
        /*箭头的位置,将箭头放到连线的二分之一的位置*/
        double arrowDs = ds * 1 / 2;
        /*线上的两个点，在线上找两个点，前面的点是箭头三角形的一个点*/
        double arrowDX1 = x1 + arrowDs * cos;
        double arrowDY1 = y1 + arrowDs * sin;
        float arrowDX2 = (float) (x1 + (arrowDs + 20 * 1.7d) * cos);
        float arrowDY2 = (float) (y1 + (arrowDs + 20 * 1.7d) * sin);

        /*连线的垂直偏移量*/
        double arrowPointX = 20 * sin;
        double arrowPointY = 20 * cos;

        /*第一个点，需要一点点数学知识*/
        float arraw1X, arraw1Y;
        arraw1X = (float) (arrowDX1 + arrowPointX);
        arraw1Y = (float) (arrowDY1 - arrowPointY);

        /*第二个点，需要一点点数学知识*/
        float arraw2X, arraw2Y;
        arraw2X = (float) (arrowDX1 - arrowPointX);
        arraw2Y = (float) (arrowDY1 + arrowPointY);

        /*在线上画一个三角形*/
        Path path = new Path();
        path.moveTo(arraw1X, arraw1Y);// 此点为多边形的起点
        path.lineTo(arraw2X, arraw2Y);
        path.lineTo(arrowDX2, arrowDY2);
        path.close();
        canvas.drawPath(path, mArrawPaint);
        if (!mIsSuccess && mIsActionUp) {
            canvas.drawPath(path, mArrawErrorPaint);
        }
    }

    /*画最后一个选择的点到手指触摸位置的线*/
    private void drawPoint2EndLine(Canvas canvas, int x1, int y1, int x2, int y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double ds = Math.sqrt(dx * dx + dy * dy);
        if (ds < mInnerRadius) {
            return;
        }
        double cos = dx / ds;
        double sin = dy / ds;

        int radiusX = (int) (mInnerRadius * cos);
        int radiusY = (int) (mInnerRadius * sin);
        int newX1 = x1 + radiusX;
        int newY1 = y1 + radiusY;

        int newX2 = x2;
        int newY2 = y2;
        canvas.drawLine(newX1, newY1, newX2, newY2, mLinePaint);
    }

    /*手指触摸的xy坐标*/
    private int mEventX;
    private int mEventY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mEventX = (int) event.getX();
        mEventY = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsActionUp = false;
                /*按下时先将之前选中的清除掉，然后添加当前按下的位置的点*/
                mSelectPointList.clear();
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        Point point = mPoints[i][j];
                        if (point.isInPoint(mEventX, mEventY)) {
                            mSelectPointList.add(point);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                /*move时根据位置添加点*/
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        Point point = mPoints[i][j];
                        if (point.isInPoint(mEventX, mEventY) && !mSelectPointList.contains(point)) {
                            mSelectPointList.add(point);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mIsActionUp = true;
                checkPassword();
                break;
        }
        invalidate();
        return true;
    }

    /*检查密码*/
    private void checkPassword() {
        StringBuffer sb = new StringBuffer();
        for (Point point : mSelectPointList) {
            sb.append(point.key + "");
        }
        String pwdStr = sb.toString();
        if (mOnResultListener != null) {
            mOnResultListener.onResult(pwdStr);
        }
        /*如果设置的密码为null，则不检查密码*/
        if (TextUtils.isEmpty(mPassword)) {
            return;
        }
        if (mPassword.equals(pwdStr)) {
            /*密码通过*/
            mIsSuccess = true;
        } else {
            /*密码检查失败，处理*/
            mIsSuccess = false;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSelectPointList.clear();
                    invalidate();
                }
            }, 2000);
        }

    }

    /*圆的业务bean*/
    private class Point {
        /*xy坐标*/
        public int x;
        public int y;
        /*key是返回时用的值*/
        public int key;

        public Point(int x, int y, int key) {
            this.x = x;
            this.y = y;
            this.key = key;
        }

        /*当前的xy坐标是否在这个圆之内*/
        public boolean isInPoint(int ox, int oy) {
            double _x = Math.abs(this.x - ox);
            double _y = Math.abs(this.y - oy);
            return Math.sqrt(_x * _x + _y * _y) < mOuterRadius;
        }
    }


    /*获取两点的距离*/
    public double getDistance(int x1, int y1, int x2, int y2) {
        double _x = Math.abs(x2 - x1);
        double _y = Math.abs(y2 - y1);
        return Math.sqrt(_x * _x + _y * _y);
    }

}
