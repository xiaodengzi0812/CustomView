package com.djk.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Djk
 * @Title: 普通的绘图
 * @Time: 2017/9/2.
 * @Version:1.0.0
 */
public class DrawCommon extends View {
    private Paint mLinePaint;
    private Paint mTitlePaint;
    private int itemHeight = 150;

    public DrawCommon(Context context) {
        this(context, null);
    }

    public DrawCommon(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawCommon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLinePaint = getDefaultPaint(Color.GRAY);
        mTitlePaint = getDefaultPaint(Color.BLACK);
        mTitlePaint.setTextSize(60);// 设置文字大小
    }

    private Paint getDefaultPaint(int color) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);// 设置画笔的锯齿效果
        paint.setDither(true);// 设置画笔的防抖动
        paint.setColor(color);// 设置画笔颜色颜色
        return paint;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = itemHeight * 8;
        setMeasuredDimension(width, height);
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
        drawClip(canvas);
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
     * 画圆
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        drawTextAndLine(canvas, "画圆：");
        /*------------------------------------------------------------------*/

        Paint circlePaint = getDefaultPaint(Color.RED);
        /*实心圆
        第一个参数是x轴坐标
        第二个参数是y轴坐标
        第三个参数是圆的半径
        */
        canvas.drawCircle(250, itemHeight / 2, 30, circlePaint);
        /*------------------------------------------------------------------*/

        Paint circlePaint2 = getDefaultPaint(Color.BLUE);
        circlePaint2.setStyle(Paint.Style.STROKE);// 空心画笔属性
        circlePaint2.setStrokeWidth(5);/// 画笔的粗
        /*空心圆*/
        canvas.drawCircle(350, itemHeight / 2, 30, circlePaint2);
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
        RectF oval1 = new RectF(250, 40, 300, 70);
        /*画笑脸弧
        第一个参数绘画区域
        第二个参数起始角度
        第三个参数绘画角度
        第四个参数是否需要闭合
        第五个参数是画笔
        */
        canvas.drawArc(oval1, 180, 180, false, arcPaint);//弧形
        oval1.set(330, 40, 380, 70);
        canvas.drawArc(oval1, 180, 180, false, arcPaint);//弧形
        oval1.set(280, 70, 360, 110);
        canvas.drawArc(oval1, 0, 180, false, arcPaint);//弧形
        /*------------------------------------------------------------------*/
        Paint arcPaint2 = getDefaultPaint(Color.BLUE);
        arcPaint2.setStyle(Paint.Style.STROKE);
        arcPaint2.setStrokeWidth(5);
        RectF oval2 = new RectF(420, 30, 600, 210);
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
        RectF oval3 = new RectF(640, 30, 820, 210);
        /*实心扇形*/
        canvas.drawArc(oval3, 200, 130, true, arcPaint3);
    }

    /**
     * 矩形
     *
     * @param canvas
     */
    private void drawRect(Canvas canvas) {
        /*画笔位置移动到当前位置*/
        canvas.translate(0, itemHeight);
        drawTextAndLine(canvas, "矩形：");
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
        canvas.drawRect(250, 40, 320, 110, rectPaint);

        /*------------------------------------------------------------------*/
        Paint rectPaint2 = getDefaultPaint(Color.BLUE);
        rectPaint2.setStyle(Paint.Style.STROKE);//设置空心
        rectPaint2.setStrokeWidth(5);// 画笔的粗度
        rectPaint2.setStrokeCap(Paint.Cap.ROUND);// 画笔边缘圆融化
        /*空心矩形*/
        canvas.drawRect(350, 40, 450, 110, rectPaint2);
        /*------------------------------------------------------------------*/
        Paint rectPaint3 = getDefaultPaint(Color.BLUE);
        rectPaint3.setStyle(Paint.Style.STROKE);//设置空心
        rectPaint3.setStrokeWidth(5);// 画笔的粗度
        rectPaint3.setStrokeCap(Paint.Cap.ROUND);// 画笔边缘圆融化
        RectF oval3 = new RectF(480, 40, 600, 110);
        /*圆角矩形
        第二个参数是x半径
        第三个参数是y半径
        */
        canvas.drawRoundRect(oval3, 20, 20, rectPaint3);
    }

    /**
     * 椭圆
     *
     * @param canvas
     */
    private void drawOval(Canvas canvas) {
        /*画笔位置移动到当前位置*/
        canvas.translate(0, itemHeight);
        drawTextAndLine(canvas, "椭圆：");
        /*------------------------------------------------------------------*/

        Paint ovalPaint = getDefaultPaint(Color.RED);
        ovalPaint.setStyle(Paint.Style.FILL);//设置实心
        RectF rectF = new RectF(250, 40, 350, 110);
        canvas.drawOval(rectF, ovalPaint);
        /*------------------------------------------------------------------*/

        Paint ovalPaint2 = getDefaultPaint(Color.BLUE);
        ovalPaint2.setStyle(Paint.Style.STROKE);//设置实心
        ovalPaint2.setStrokeCap(Paint.Cap.ROUND);// 画笔边缘圆融化
        ovalPaint2.setStrokeWidth(5);
        RectF rectF2 = new RectF(400, 40, 500, 110);
        canvas.drawOval(rectF2, ovalPaint2);
    }

    /**
     * 路径
     *
     * @param canvas
     */
    private void drawPath(Canvas canvas) {
        /*画笔位置移动到当前位置*/
        canvas.translate(0, itemHeight);
        drawTextAndLine(canvas, "路径：");
        /*------------------------------------------------------------------*/

        Paint pathPaint = getDefaultPaint(Color.BLUE);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeWidth(5);

        // 绘制三角形,你可以绘制任意多边形（一直连线就可以）
        Path path = new Path();
        path.moveTo(250, 30);// 此点为多边形的起点
        path.lineTo(250, 120);
        path.lineTo(340, 120);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, pathPaint);
        /*------------------------------------------------------------------*/

        Paint pathPaint2 = getDefaultPaint(Color.BLUE);
        pathPaint2.setStyle(Paint.Style.STROKE);
        pathPaint2.setStrokeWidth(5);
        pathPaint2.setTextSize(25);

        // 贝塞尔曲线
        Path path2 = new Path();
        path2.moveTo(400, 30);//设置Path的起点
        //贝塞尔曲线的控制点坐标和终点坐标
        path2.quadTo(410, 120, 500, 120);
        canvas.drawPath(path2, pathPaint2);//画出贝塞尔曲线
        /*------------------------------------------------------------------*/
        /*路径文字*/
        Paint pathTextPaint = getDefaultPaint(Color.RED);
        pathTextPaint.setTextSize(30);
        canvas.drawTextOnPath("路径文字", path2, 0, 30, pathTextPaint);
    }

    /**
     * 画点
     *
     * @param canvas
     */
    private void drawPoint(Canvas canvas) {
        /*画笔位置移动到当前位置*/
        canvas.translate(0, itemHeight);
        drawTextAndLine(canvas, "画点：");
        /*------------------------------------------------------------------*/

        Paint pointPaint = getDefaultPaint(Color.RED);
        canvas.drawPoint(250, 75, pointPaint);//画一个点
        pointPaint.setColor(Color.BLUE);
        canvas.drawPoints(new float[]{270, 75, 290, 75, 310, 75}, pointPaint);//画多个点
    }

    /**
     * 画图
     *
     * @param canvas
     */
    private void drawBitmap(Canvas canvas) {
        /*画笔位置移动到当前位置*/
        canvas.translate(0, itemHeight);
        drawTextAndLine(canvas, "画图：");
        /*------------------------------------------------------------------*/

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        canvas.drawBitmap(bitmap, 250, 25, null);
    }

    /**
     * 裁切
     *
     * @param canvas
     */
    private void drawClip(Canvas canvas) {
        /*画笔位置移动到当前位置*/
        canvas.translate(0, itemHeight);
        drawTextAndLine(canvas, "裁切：");
        /*------------------------------------------------------------------*/

        Paint textPaint = getDefaultPaint(Color.BLACK);
        textPaint.setTextSize(60);// 设置文字大小
        /*计算baselineY*/
        Paint.FontMetricsInt fm = textPaint.getFontMetricsInt();
        int baseLine = (fm.bottom - fm.top) / 2 - fm.bottom + itemHeight / 2;

        String text = "文字";
        int textWidth = (int) textPaint.measureText(text);
        int textStart0 = 250;
        canvas.save();

        /*准备一个矩形区域，用于画布的绘画区域*/
        Rect clipRect0 = new Rect(textStart0, 0, textStart0 + textWidth / 2, itemHeight);
        /*当我们调用这个方法之后，我们的画布空间就变成上面指点的矩形区域了*/
        canvas.clipRect(clipRect0);
        canvas.drawText(text, textStart0, baseLine, textPaint);
        canvas.restore();

        canvas.save();
        Rect clipRect1 = new Rect(textStart0 + textWidth / 2, 0, textStart0 + textWidth, itemHeight);
        canvas.clipRect(clipRect1);
        textPaint.setColor(Color.RED);
        canvas.drawText(text, textStart0, baseLine, textPaint);
        canvas.restore();

        /*------------------------------------------------------------------*/
        String text1 = "裁切";
        int textStart1 = 400;
        canvas.save();
        Rect clipRect10 = new Rect(textStart1, 0, textStart1 + textWidth, itemHeight / 2);
        canvas.clipRect(clipRect10);
        textPaint.setColor(Color.BLACK);
        canvas.drawText(text1, textStart1, baseLine, textPaint);
        canvas.restore();

        canvas.save();
        Rect clipRect11 = new Rect(textStart1, itemHeight / 2, textStart1 + textWidth, itemHeight);
        canvas.clipRect(clipRect11);
        textPaint.setColor(Color.RED);
        canvas.drawText(text1, textStart1, baseLine, textPaint);
        canvas.restore();
    }

}
