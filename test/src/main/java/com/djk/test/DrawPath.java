package com.djk.test;

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
 * @Title: drawPath 绘制任意多边形
 * @Time: 2017/9/2.
 * @Version:1.0.0
 */
public class DrawPath extends View {
    private Paint mLinePaint;
    private Paint mTitlePaint;
    private int itemHeight = 400;

    public DrawPath(Context context) {
        this(context, null);
    }

    public DrawPath(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawPath(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLinePaint = getDefaultPaint(Color.GRAY);
        mLinePaint.setStrokeWidth(1f);
        mTitlePaint = getDefaultPaint(Color.BLACK);
        mTitlePaint.setTextSize(60);// 设置文字大小
    }

    private Paint getDefaultPaint(int color) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);// 设置画笔的锯齿效果
        paint.setDither(true);// 设置画笔的防抖动
        paint.setColor(color);// 设置画笔颜色颜色
        paint.setStrokeWidth(10f);
        return paint;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = itemHeight * 6;
        setMeasuredDimension(width, height);
    }

    /**
     * drawPath 绘制任意多边形
     * Path.Direction.CW时，顺时针绘制；
     * Path.Direction.CCW时，逆时针绘制。
     */
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLine(canvas);
        drawCommon(canvas);
        drawArc(canvas);
        drawText(canvas);
        drawMeasure(canvas);
        drawBsr(canvas);
    }

    private void drawTextAndLine(Canvas canvas, String text) {
        /*计算baselineY*/
        Paint.FontMetricsInt fm = mTitlePaint.getFontMetricsInt();
        int baseLine = (fm.bottom - fm.top) / 2 - fm.bottom + itemHeight / 2;
        /*画文字
        第一个参数是文字
        第二个参数是x轴坐标
        第三个参数是baselineY*/
        canvas.drawText(text, 50, baseLine, mTitlePaint);

         /*画线*/
        canvas.drawLine(50, itemHeight, getWidth() - 50, itemHeight, mLinePaint);
    }

    /**
     * 画线
     *
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        drawTextAndLine(canvas, "画线：");
        /*------------------------------------------------------------------*/
        Paint paint = getDefaultPaint(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);

        //设置Path
        Path path = new Path();
        path.moveTo(250, 250);
        path.lineTo(300, 350);
        /*(350, 300)到（400, 100)）画一条直线*/
        path.lineTo(400, 100);
        /*以（400, 100）为起始点（0,0）偏移量为（200, -50）
        画一条直线，其终点坐标实际在屏幕的位置为（600,50）*/
        path.rLineTo(200, -50);
        canvas.drawPath(path, paint);

        /*------------------------------------------------------------------*/
        //设置Path
        paint.setColor(Color.RED);
        Path path2 = new Path();
        path2.moveTo(500, 350);
        path2.lineTo(600, 350);
        /*起点位置偏移(0, -100)
        从(600, 350)到(600, 250)
        */
        path2.rMoveTo(0, -100);
        path2.rLineTo(200, -200);
        canvas.drawPath(path2, paint);
    }

    /**
     * 常规
     *
     * @param canvas
     */
    private void drawCommon(Canvas canvas) {
        /*画笔位置移动到当前位置*/
        canvas.translate(0, itemHeight);
        drawTextAndLine(canvas, "常规：");
        /*------------------------------------------------------------------*/
        Paint paint = getDefaultPaint(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);

        //绘制圆
        Path path = new Path();
        //以（300, 100）为圆心，半径为50绘制圆
        path.addCircle(300, 100, 50, Path.Direction.CW);
        /*------------------------------------------------------------------*/

        //绘制椭圆
        RectF rectF = new RectF(400, 50, 600, 150);
        path.addOval(rectF, Path.Direction.CW);
        /*------------------------------------------------------------------*/

        //绘制矩形
        RectF rect = new RectF(650, 50, 850, 150);
        path.addRect(rect, Path.Direction.CW);
        /*------------------------------------------------------------------*/

        //绘制圆角矩形
        RectF roundRect = new RectF(250, 250, 450, 350);
        path.addRoundRect(roundRect, 20, 20, Path.Direction.CW);
        /*------------------------------------------------------------------*/

        //绘制圆角矩形
        //float[] radii中有8个值，依次为左上角，右上角，右下角，左下角的rx,ry
        RectF roundRectT = new RectF(500, 250, 700, 350);
        float[] radii = new float[]{20, 20, 0, 0, 20, 20, 0, 0};
        path.addRoundRect(roundRectT, radii, Path.Direction.CCW);

        canvas.drawPath(path, paint);
    }

    /**
     * 画弧
     *
     * @param canvas
     */
    private void drawArc(Canvas canvas) {
        /*画笔位置移动到当前位置*/
        canvas.translate(0, itemHeight);
        drawTextAndLine(canvas, "画弧：");
        /*------------------------------------------------------------------*/
        Paint paint = getDefaultPaint(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);

        Path path = new Path();
        //在(250, 100, 450, 300)区域内绘制一个300度的圆弧
        RectF rectF = new RectF(250, 50, 350, 150);
        path.addArc(rectF, 0, 270);
        //在(400, 600, 600, 800)区域内绘制一个90度的圆弧
        RectF rectFTo = new RectF(450, 50, 550, 150);
        // true 表示不连接两个点
        path.arcTo(rectFTo, 270, 90, true);
        //等价于path.addArc(rectFTo, 0, 90);
        canvas.drawPath(path, paint);

        /*------------------------------------------------------------------*/
        Path path2 = new Path();
        RectF rectF2 = new RectF(650, 50, 750, 150);
        path2.addArc(rectF2, 0, 270);
        RectF rectFTo2 = new RectF(850, 50, 950, 150);
        // false 表示连接两个点
        path2.arcTo(rectFTo2, 270, 90, false);
        paint.setColor(Color.RED);
        canvas.drawPath(path2, paint);

        /*------------------------------------------------------------------*/
        Path path3 = new Path();
        RectF rectF3 = new RectF(250, 250, 350, 350);
        path3.addArc(rectF3, 0, 270);
        RectF rectFTo3 = new RectF(450, 250, 550, 350);
        // false 表示连接两个点
        path3.arcTo(rectFTo3, 270, 90, false);
        /*close 表示起点与终点相连*/
        path3.close();
        paint.setColor(Color.MAGENTA);
        canvas.drawPath(path3, paint);
    }

    /**
     * 文字
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        /*画笔位置移动到当前位置*/
        canvas.translate(0, itemHeight);
        drawTextAndLine(canvas, "文字：");
        /*------------------------------------------------------------------*/
        Paint paint = getDefaultPaint(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);

        //绘制半圆弧
        Path path = new Path();
        RectF rectF = new RectF(250, 50, 550, 350);
        path.addArc(rectF, 180, 180);
        canvas.drawPath(path, paint);

        //沿path绘制文字
        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.setTextSize(40);
        textPaint.setColor(Color.RED);
        /*
        第三个参数： 沿path线方向移动的距离
        第四个参数： 距离path线的距离
        */
        canvas.drawTextOnPath("是非成败转头空。", path, 90, 40, textPaint);
        textPaint.setColor(Color.MAGENTA);
        canvas.drawTextOnPath("青山依旧在，几度夕阳红", path, 20, -10, textPaint);

        /*------------------------------------------------------------------*/
        //绘制
        Path path2 = new Path();
        /* path线的绘制会影响到text的方向绘制
        Path.Direction.CW时，顺时针绘制；
        Path.Direction.CCW时，逆时针绘制。
        */
        path2.addCircle(800, 200, 150, Path.Direction.CCW);
        canvas.drawPath(path2, paint);
        //沿path绘制文字
        textPaint.setColor(Color.RED);
        canvas.drawTextOnPath("是非成败转头空。", path2, 550, 40, textPaint);
        textPaint.setColor(Color.MAGENTA);
        canvas.drawTextOnPath("青山依旧在，几度夕阳红", path2, 20, 40, textPaint);
    }

    /**
     * 测量
     *
     * @param canvas
     */
    private void drawMeasure(Canvas canvas) {
        /*画笔位置移动到当前位置*/
        canvas.translate(0, itemHeight);
        drawTextAndLine(canvas, "测量：");
        /*------------------------------------------------------------------*/
        Paint paint = getDefaultPaint(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);

        //绘制半圆弧
        Path path = new Path();
        RectF rectF = new RectF(250, 50, 550, 350);
        path.addRect(rectF, Path.Direction.CCW);
        canvas.drawPath(path, paint);
        /*测量path的总长度*/
        PathMeasure measure = new PathMeasure();
        measure.setPath(path, false);

        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.setTextSize(40);
        textPaint.setColor(Color.RED);

        canvas.drawText("Path->" + (int) measure.getLength(), 300, itemHeight / 2, textPaint);

        /*------------------------------------------------------------------*/
        //绘制半圆弧
        Path path2 = new Path();
        RectF rectF2 = new RectF(650, 50, 950, 350);
        path2.addArc(rectF2, 180, 180);
        canvas.drawPath(path2, paint);

        /*第二个参数forceClosed
        false:测量未闭合的长度
        true:测量闭合的长度
        */
        measure.setPath(path2, false);
        canvas.drawText("Path->" + (int) measure.getLength(), 700, itemHeight / 2, textPaint);

        measure.setPath(path2, true);
        textPaint.setColor(Color.BLACK);
        /*这里测量了闭合的长度，正好是半圆的长度+直径的长度*/
        canvas.drawText("Path->" + (int) measure.getLength(), 700, itemHeight / 2 + 100, textPaint);
    }

    /**
     * 贝塞尔
     *
     * @param canvas
     */
    private void drawBsr(Canvas canvas) {
        /*画笔位置移动到当前位置*/
        canvas.translate(0, itemHeight);
        drawTextAndLine(canvas, "贝塞尔：");
        /*------------------------------------------------------------------*/
        Paint paint = getDefaultPaint(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);

        //绘制二阶贝塞尔
        Path path = new Path();
        /*贝塞尔曲线的起点位置*/
        path.moveTo(264, 327);
        /*
        600, 400 为控制点坐标
        450, 50  为终点坐标
        */
        path.quadTo(600, 400, 450, 50);
        canvas.drawPath(path, paint);

        /*------------------------------------------------------------------*/
        //绘制三阶贝塞尔
        Path path2 = new Path();
        /*贝塞尔曲线的起点位置*/
        path2.moveTo(600, 335);
        /*
        792, 360 为控制点坐标
        672, 48  为控制点坐标
        927, 86  为终点坐标
        */
        path2.cubicTo(792, 360, 672, 48, 927, 86);
        paint.setColor(Color.RED);
        canvas.drawPath(path2, paint);
    }

}
