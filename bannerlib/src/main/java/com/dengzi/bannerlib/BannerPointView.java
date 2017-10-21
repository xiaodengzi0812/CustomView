package com.dengzi.bannerlib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Djk
 * @Title: BannerView中的指示点
 * @Time: 2017/10/20.
 * @Version:1.0.0
 */
public class BannerPointView extends View {
    private Drawable mDrawable;

    public BannerPointView(Context context) {
        this(context, null);
    }

    public BannerPointView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerPointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap drawableBitmap = getBitmapFromDrawable(mDrawable);
        Bitmap circleBitmap = getCilcleBitmap(drawableBitmap);
        canvas.drawBitmap(circleBitmap, 0, 0, null);
    }

    /**
     * 获取一个圆形的bitmap
     *
     * @param drawableBitmap
     * @return
     */
    private Bitmap getCilcleBitmap(Bitmap drawableBitmap) {
        // 创建一个空Bitmap
        Bitmap cilcleBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        // 创建一个画面，里面的大小是空Bitmap
        Canvas canvas = new Canvas(cilcleBitmap);
        // 创建一个画笔
        Paint paint = new Paint();
        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        // 在空Bitmap上画一个圆
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, getMeasuredWidth() / 2, paint);
        // 设置画笔模式，SRC_IN表示两个图形的交集
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        // 再将传来的drawableBitmap画到圆形的cilcleBitmap中，这时画出来的就是他们的交集，就是一个圆形的Bitmap
        canvas.drawBitmap(drawableBitmap, 0, 0, paint);
        return cilcleBitmap;
    }

    /**
     * 从一个Drawable中获取Bitmap
     *
     * @param drawable
     * @return
     */
    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        // 创建一个空Bitmap
        Bitmap drawableBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        // 创建一个画面，里面的大小是空Bitmap
        Canvas canvas = new Canvas(drawableBitmap);
        // 将我们Drawable画到画布上面，这样空Bitmap就会拿到Drawable的信息
        drawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
        drawable.draw(canvas);
        return drawableBitmap;
    }

    /**
     * 设置我们的Drawable
     *
     * @param drawable
     */
    public void setDrawable(Drawable drawable) {
        this.mDrawable = drawable;
        invalidate();
    }

}
