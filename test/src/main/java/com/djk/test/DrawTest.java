package com.djk.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Djk
 * @Title:
 * @Time: 2017/9/2.
 * @Version:1.0.0
 */
public class DrawTest extends View {
    private Paint mLinePaint;

    public DrawTest(Context context) {
        this(context, null);
    }

    public DrawTest(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private Paint getDefaultPaint(int color) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);// 设置画笔的锯齿效果
        paint.setDither(true);// 设置画笔的防抖动
        paint.setColor(color);// 设置画笔颜色颜色
        return paint;
    }

    public DrawTest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLinePaint = getDefaultPaint(Color.GRAY);
    }

    /**
     * 画圆
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        /*默认给当前测试的高度*/
        int height = 150;

        Paint textPaint = getDefaultPaint(Color.BLACK);
        textPaint.setTextSize(60);// 设置文字大小
        /*计算baselineY*/
        Paint.FontMetricsInt fm = textPaint.getFontMetricsInt();
        int baseLine = (fm.bottom - fm.top) / 2 - fm.bottom + height / 2;
        /*画文字
        第一个参数是文字
        第二个参数是x轴坐标
        第三个参数是baselineY*/
        canvas.drawText("画圆：", 50, baseLine, textPaint);

        /*------------------------------------------------------------------*/

        Paint circlePaint = getDefaultPaint(Color.RED);
        /*实心圆
        第一个参数是x轴坐标
        第二个参数是y轴坐标
        第三个参数是圆的半径
        */
        canvas.drawCircle(250, height / 2, 30, circlePaint);
        /*------------------------------------------------------------------*/

        Paint circlePaint2 = getDefaultPaint(Color.BLUE);
        circlePaint2.setStyle(Paint.Style.STROKE);// 空心画笔属性
        circlePaint2.setStrokeWidth(5);/// 画笔的粗
        /*空心圆
        第一个参数是x轴坐标
        第二个参数是y轴坐标
        第三个参数是圆的半径
        */
        canvas.drawCircle(350, height / 2, 30, circlePaint2);

        /*画线*/
        canvas.drawLine(50, height, getWidth() - 50, height, mLinePaint);
    }

    /**
     * 画弧
     *
     * @param canvas
     */
    private void drawArc(Canvas canvas) {
         /*默认给当前测试的高度*/
        int marTop = 150;// 距上高度
        int height = 150;// 自己的高度

        Paint textPaint = getDefaultPaint(Color.BLACK);
        textPaint.setTextSize(60);// 设置文字大小
        /*计算baselineY*/
        Paint.FontMetricsInt fm = textPaint.getFontMetricsInt();
        int baseLine = (fm.bottom - fm.top) / 2 - fm.bottom + height / 2 + marTop;
        /*画文字
        第一个参数是文字
        第二个参数是x轴坐标
        第三个参数是baselineY*/
        canvas.drawText("画弧：", 50, baseLine, textPaint);
        /*------------------------------------------------------------------*/

        Paint arcPaint = getDefaultPaint(Color.BLUE);
        arcPaint.setStyle(Paint.Style.STROKE);//设置空心
        arcPaint.setStrokeWidth(5);// 画笔的粗度
        arcPaint.setStrokeCap(Paint.Cap.ROUND);// 画笔边缘圆融化

        /*绘画区域
        第一个参数left坐标
        第二个参数top坐标
        第三个参数right坐标
        第四个参数bottom坐标
        */
        RectF oval1 = new RectF(250, 40 + marTop, 300, 70 + marTop);
        /*画笑脸弧
        第一个参数绘画区域
        第二个参数起始角度
        第三个参数绘画角度
        第四个参数是否需要闭合
        第五个参数是画笔
        */
        canvas.drawArc(oval1, 180, 180, false, arcPaint);//弧形
        oval1.set(330, 40 + marTop, 380, 70 + marTop);
        canvas.drawArc(oval1, 180, 180, false, arcPaint);//弧形
        oval1.set(280, 70 + marTop, 360, 110 + marTop);
        canvas.drawArc(oval1, 0, 180, false, arcPaint);//弧形
        /*------------------------------------------------------------------*/
        Paint arcPaint2 = getDefaultPaint(Color.BLUE);
        arcPaint2.setStyle(Paint.Style.STROKE);
        arcPaint2.setStrokeWidth(5);
        RectF oval2 = new RectF(420, 30 + marTop, 600, 210 + marTop);
        /*空心扇形*/
        canvas.drawArc(oval2, 200, 130, true, arcPaint2);
        /*------------------------------------------------------------------*/
        Paint arcPaint3 = getDefaultPaint(Color.BLUE);
        arcPaint3.setStyle(Paint.Style.FILL);
        /*渐变色*/
        Shader mShader = new LinearGradient(0, 0, 100, 100,
                new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,
                        Color.LTGRAY}, null, Shader.TileMode.REPEAT); // 一个材质,打造出一个线性梯度沿著一条线。
        arcPaint3.setShader(mShader);
        RectF oval3 = new RectF(640, 30 + marTop, 820, 210 + marTop);
        /*实心扇形*/
        canvas.drawArc(oval3, 200, 130, true, arcPaint3);

        /*画线*/
        canvas.drawLine(50, height + marTop, getWidth() - 50, height + marTop, mLinePaint);
    }

    /**
     * 矩形
     *
     * @param canvas
     */
    private void drawRect(Canvas canvas) {
         /*默认给当前测试的高度*/
        int marTop = 300;// 距上高度
        int height = 150;// 自己的高度

        Paint textPaint = getDefaultPaint(Color.BLACK);
        textPaint.setTextSize(60);// 设置文字大小
        /*计算baselineY*/
        Paint.FontMetricsInt fm = textPaint.getFontMetricsInt();
        int baseLine = (fm.bottom - fm.top) / 2 - fm.bottom + height / 2 + marTop;
        /*画文字
        第一个参数是文字
        第二个参数是x轴坐标
        第三个参数是baselineY*/
        canvas.drawText("矩形：", 50, baseLine, textPaint);
        /*------------------------------------------------------------------*/

        Paint rectPaint = getDefaultPaint(Color.RED);
        rectPaint.setStyle(Paint.Style.FILL);//设置实心

        /*实心矩形
        第一个参数left坐标
        第二个参数top坐标
        第三个参数right坐标
        第四个参数bottom坐标
        第五个参数是画笔
         */
        canvas.drawRect(250, 40 + marTop, 320, 110 + marTop, rectPaint);

        /*------------------------------------------------------------------*/
        Paint rectPaint2 = getDefaultPaint(Color.BLUE);
        rectPaint2.setStyle(Paint.Style.STROKE);//设置空心
        rectPaint2.setStrokeWidth(5);// 画笔的粗度
        rectPaint2.setStrokeCap(Paint.Cap.ROUND);// 画笔边缘圆融化
        /*空心矩形
        第一个参数left坐标
        第二个参数top坐标
        第三个参数right坐标
        第四个参数bottom坐标
        第五个参数是画笔
         */
        canvas.drawRect(350, 40 + marTop, 450, 110 + marTop, rectPaint2);
        /*------------------------------------------------------------------*/
        Paint rectPaint3 = getDefaultPaint(Color.BLUE);
        rectPaint3.setStyle(Paint.Style.STROKE);//设置空心
        rectPaint3.setStrokeWidth(5);// 画笔的粗度
        rectPaint3.setStrokeCap(Paint.Cap.ROUND);// 画笔边缘圆融化
        RectF oval3 = new RectF(480, 40 + marTop, 600, 110 + marTop);
        /*圆角矩形
        第二个参数是x半径
        第三个参数是y半径
        */
        canvas.drawRoundRect(oval3, 20, 20, rectPaint3);

        /*画线*/
        canvas.drawLine(50, height + marTop, getWidth() - 50, height + marTop, mLinePaint);
    }

    /**
     * 椭圆
     *
     * @param canvas
     */
    private void drawOval(Canvas canvas) {
         /*默认给当前测试的高度*/
        int marTop = 450;// 距上高度
        int height = 150;// 自己的高度

        Paint textPaint = getDefaultPaint(Color.BLACK);
        textPaint.setTextSize(60);// 设置文字大小
        /*计算baselineY*/
        Paint.FontMetricsInt fm = textPaint.getFontMetricsInt();
        int baseLine = (fm.bottom - fm.top) / 2 - fm.bottom + height / 2 + marTop;
        /*画文字
        第一个参数是文字
        第二个参数是x轴坐标
        第三个参数是baselineY*/
        canvas.drawText("椭圆：", 50, baseLine, textPaint);
        /*------------------------------------------------------------------*/

        Paint ovalPaint = getDefaultPaint(Color.RED);
        ovalPaint.setStyle(Paint.Style.FILL);//设置实心
        RectF rectF = new RectF(250, 40 + marTop, 350, 110 + marTop);
        canvas.drawOval(rectF, ovalPaint);
        /*------------------------------------------------------------------*/

        Paint ovalPaint2 = getDefaultPaint(Color.BLUE);
        ovalPaint2.setStyle(Paint.Style.STROKE);//设置实心
        ovalPaint2.setStrokeCap(Paint.Cap.ROUND);// 画笔边缘圆融化
        ovalPaint2.setStrokeWidth(5);
        RectF rectF2 = new RectF(400, 40 + marTop, 500, 110 + marTop);
        canvas.drawOval(rectF2, ovalPaint2);

        /*画线*/
        canvas.drawLine(50, height + marTop, getWidth() - 50, height + marTop, mLinePaint);
    }

    /**
     * 路径
     *
     * @param canvas
     */
    private void drawPath(Canvas canvas) {
         /*默认给当前测试的高度*/
        int marTop = 600;// 距上高度
        int height = 150;// 自己的高度

        Paint textPaint = getDefaultPaint(Color.BLACK);
        textPaint.setTextSize(60);// 设置文字大小
        /*计算baselineY*/
        Paint.FontMetricsInt fm = textPaint.getFontMetricsInt();
        int baseLine = (fm.bottom - fm.top) / 2 - fm.bottom + height / 2 + marTop;
        /*画文字
        第一个参数是文字
        第二个参数是x轴坐标
        第三个参数是baselineY*/
        canvas.drawText("路径：", 50, baseLine, textPaint);
        /*------------------------------------------------------------------*/

        Paint pathPaint = getDefaultPaint(Color.BLUE);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeWidth(5);

        // 绘制三角形,你可以绘制任意多边形（一直连线就可以）
        Path path = new Path();
        path.moveTo(250, 30 + marTop);// 此点为多边形的起点
        path.lineTo(250, 120 + marTop);
        path.lineTo(340, 120 + marTop);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, pathPaint);
        /*------------------------------------------------------------------*/

        Paint pathPaint2 = getDefaultPaint(Color.BLUE);
        pathPaint2.setStyle(Paint.Style.STROKE);
        pathPaint2.setStrokeWidth(5);

        // 贝塞尔曲线
        Path path2 = new Path();
        path2.moveTo(400, 30 + marTop);//设置Path的起点
        //贝塞尔曲线的控制点坐标和终点坐标
        path2.quadTo(410, 120 + marTop, 500, 120 + marTop);
        canvas.drawPath(path2, pathPaint2);//画出贝塞尔曲线

        /*画线*/
        canvas.drawLine(50, height + marTop, getWidth() - 50, height + marTop, mLinePaint);
    }

    /**
     * 画点
     *
     * @param canvas
     */
    private void drawPoint(Canvas canvas) {
         /*默认给当前测试的高度*/
        int marTop = 750;// 距上高度
        int height = 150;// 自己的高度

        Paint textPaint = getDefaultPaint(Color.BLACK);
        textPaint.setTextSize(60);// 设置文字大小
        /*计算baselineY*/
        Paint.FontMetricsInt fm = textPaint.getFontMetricsInt();
        int baseLine = (fm.bottom - fm.top) / 2 - fm.bottom + height / 2 + marTop;
        /*画文字
        第一个参数是文字
        第二个参数是x轴坐标
        第三个参数是baselineY*/
        canvas.drawText("画点：", 50, baseLine, textPaint);
        /*------------------------------------------------------------------*/

        Paint pointPaint = getDefaultPaint(Color.RED);
        canvas.drawPoint(250, 75 + marTop, pointPaint);//画一个点
        pointPaint.setColor(Color.BLUE);
        canvas.drawPoints(new float[]{270, 75 + marTop, 290, 75 + marTop, 310, 75 + marTop}, pointPaint);//画多个点

        /*画线*/
        canvas.drawLine(50, height + marTop, getWidth() - 50, height + marTop, mLinePaint);
    }

    /**
     * 画图
     *
     * @param canvas
     */
    private void drawBitmap(Canvas canvas) {
         /*默认给当前测试的高度*/
        int marTop = 900;// 距上高度
        int height = 150;// 自己的高度

        Paint textPaint = getDefaultPaint(Color.BLACK);
        textPaint.setTextSize(60);// 设置文字大小
        /*计算baselineY*/
        Paint.FontMetricsInt fm = textPaint.getFontMetricsInt();
        int baseLine = (fm.bottom - fm.top) / 2 - fm.bottom + height / 2 + marTop;
        /*画文字
        第一个参数是文字
        第二个参数是x轴坐标
        第三个参数是baselineY*/
        canvas.drawText("画图：", 50, baseLine, textPaint);
        /*------------------------------------------------------------------*/

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        canvas.drawBitmap(bitmap, 250, 25 + marTop, null);

        /*画线*/
        canvas.drawLine(50, height + marTop, getWidth() - 50, height + marTop, mLinePaint);
    }


    /**
     * 方法 说明 drawRect 绘制矩形 drawCircle 绘制圆形 drawOval 绘制椭圆 drawPath 绘制任意多边形
     * drawLine 绘制直线 drawPoin 绘制点
     */
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircle(canvas);
        drawArc(canvas);
        drawRect(canvas);
        drawOval(canvas);
        drawPath(canvas);
        drawPoint(canvas);
        drawBitmap(canvas);
    }

}
