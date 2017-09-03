package com.dengzi.itemview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Djk
 * @Title: 常使用的top view
 * @Time: 2017/8/30.
 * @Version:1.0.0
 */
public class TopView extends View {
    /*各种属性*/
    /*左中右右2的文字*/
    private String mLeftText, mMiddleText, mRightText, mRight2Text;

    /*左中右右2文字颜色*/
    private int mLeftTextColor = getResources().getColor(R.color.item_text_color);
    private int mMiddleTextColor = getResources().getColor(R.color.item_text_color);
    private int mRightTextColor = getResources().getColor(R.color.item_text_color);
    private int mRight2TextColor = getResources().getColor(R.color.item_text_color);

    /*左中右右2文字大小*/
    private int mLeftTextSize = 14;
    private int mMiddleTextSize = 14;
    private int mRightTextSize = 14;
    private int mRight2TextSize = 14;

    /*左中右右2图片id*/
    private int mLeftDrawableId, mMiddleDrawableId, mRightDrawableId, mRight2DrawableId;
    private Bitmap mLeftBitmap, mMiddleBitmap, mRightBitmap, Mright2Bitmap;

    /*左中右右2文字距左图片的距离*/
    private float mLeftDrawablePadding = 10;
    private float mMiddleDrawablePadding = 10;
    private float mRightDrawablePadding = 10;
    private float mRight2DrawablePadding = 10;

    /*右边两个text的padding*/
    private float mRightRight2Padding = 10;

    /*下线的宽度*/
    private float mBottomLineHeight;
    /*下线的颜色*/
    private int mBottomLineColor;
    /*下线的左右padding*/
    private float mBottomLinePaddingLeft, mBottomLinePaddingRight;

    /*画笔*/
    /*左中右右2文字的画笔*/
    private Paint mLeftPaint;
    private Paint mMiddlePaint;
    private Paint mRightPaint;
    private Paint mRight2Paint;
    /*下线的画笔*/
    private Paint mBottomPaint;

    public TopView(Context context) {
        this(context, null);
    }

    public TopView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.custom_top_item);
            mLeftText = typedArray.getString(R.styleable.custom_top_item_topLeftText);
            mMiddleText = typedArray.getString(R.styleable.custom_top_item_topMiddleText);
            mRightText = typedArray.getString(R.styleable.custom_top_item_topRightText);
            mRight2Text = typedArray.getString(R.styleable.custom_top_item_topRight2Text);

            mLeftTextColor = typedArray.getColor(R.styleable.custom_top_item_topLeftTextColor, getResources().getColor(R.color.item_text_color));
            mMiddleTextColor = typedArray.getColor(R.styleable.custom_top_item_topMiddleTextColor, getResources().getColor(R.color.item_text_color));
            mRightTextColor = typedArray.getColor(R.styleable.custom_top_item_topRightTextColor, getResources().getColor(R.color.item_text_color));
            mRight2TextColor = typedArray.getColor(R.styleable.custom_top_item_topRight2TextColor, getResources().getColor(R.color.item_text_color));

            mLeftTextSize = typedArray.getDimensionPixelSize(R.styleable.custom_top_item_topLeftTextSize, 15);
            mMiddleTextSize = typedArray.getDimensionPixelSize(R.styleable.custom_top_item_topMiddleTextSize, 12);
            mRightTextSize = typedArray.getDimensionPixelSize(R.styleable.custom_top_item_topRightTextSize, 12);
            mRight2TextSize = typedArray.getDimensionPixelSize(R.styleable.custom_top_item_topRight2TextSize, 12);

            mLeftDrawableId = typedArray.getResourceId(R.styleable.custom_top_item_topLeftDrawable, 0);
            mMiddleDrawableId = typedArray.getResourceId(R.styleable.custom_top_item_topMiddleDrawable, 0);
            mRightDrawableId = typedArray.getResourceId(R.styleable.custom_top_item_topRightDrawable, 0);
            mRight2DrawableId = typedArray.getResourceId(R.styleable.custom_top_item_topRight2Drawable, 0);
            if (mLeftDrawableId != 0) {
                mLeftBitmap = BitmapFactory.decodeResource(getContext().getResources(), mLeftDrawableId);
            }
            if (mMiddleDrawableId != 0) {
                mMiddleBitmap = BitmapFactory.decodeResource(getContext().getResources(), mMiddleDrawableId);
            }
            if (mRightDrawableId != 0) {
                mRightBitmap = BitmapFactory.decodeResource(getContext().getResources(), mRightDrawableId);
            }
            if (mRight2DrawableId != 0) {
                Mright2Bitmap = BitmapFactory.decodeResource(getContext().getResources(), mRight2DrawableId);
            }

            mLeftDrawablePadding = typedArray.getDimension(R.styleable.custom_top_item_topLeftDrawablePadding, 10);
            mMiddleDrawablePadding = typedArray.getDimension(R.styleable.custom_top_item_topMiddleDrawablePadding, 10);
            mRightDrawablePadding = typedArray.getDimension(R.styleable.custom_top_item_topRightDrawablePadding, 10);
            mRight2DrawablePadding = typedArray.getDimension(R.styleable.custom_top_item_topRight2DrawablePadding, 10);

            mRightRight2Padding = typedArray.getDimension(R.styleable.custom_top_item_topRightRight2Padding, 10);

            mBottomLineHeight = typedArray.getDimension(R.styleable.custom_top_item_topBottomLineHeight, 1);
            mBottomLineColor = typedArray.getColor(R.styleable.custom_top_item_topBottomLineColor, getResources().getColor(R.color.item_line_color));
            mBottomLinePaddingLeft = typedArray.getDimension(R.styleable.custom_top_item_topBottomLinePaddingLeft, 0);
            mBottomLinePaddingRight = typedArray.getDimension(R.styleable.custom_top_item_topBottomLinePaddingRight, 0);
            typedArray.recycle();
        }
        initPaint();
    }

    /*初始化画笔*/
    private void initPaint() {
        /*左文字的画笔*/
        mLeftPaint = new Paint();
        mLeftPaint.setAntiAlias(true);
        mLeftPaint.setTextSize(mLeftTextSize);
        mLeftPaint.setColor(mLeftTextColor);

        /*中文字的画笔*/
        mMiddlePaint = new Paint();
        mMiddlePaint.setAntiAlias(true);
        mMiddlePaint.setTextSize(mMiddleTextSize);
        mMiddlePaint.setColor(mMiddleTextColor);

        /*右文字的画笔*/
        mRightPaint = new Paint();
        mRightPaint.setAntiAlias(true);
        mRightPaint.setTextSize(mRightTextSize);
        mRightPaint.setColor(mRightTextColor);

        /*右2文字的画笔*/
        mRight2Paint = new Paint();
        mRight2Paint.setAntiAlias(true);
        mRight2Paint.setTextSize(mRight2TextSize);
        mRight2Paint.setColor(mRight2TextColor);

        /*下线的画笔*/
        mBottomPaint = new Paint();
        mBottomPaint.setAntiAlias(true);
        mBottomPaint.setStrokeWidth(mBottomLineHeight);
        mBottomPaint.setStyle(Paint.Style.STROKE);
        mBottomPaint.setColor(mBottomLineColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /*获取固定的宽高，在使用时必须指定固定的宽高*/
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*画bottom的线*/
        if (mBottomLineHeight > 0) {
            canvas.drawLine(mBottomLinePaddingLeft, getHeight(), getWidth() - mBottomLinePaddingRight, getHeight(), mBottomPaint);
        }

        /*画左边的图片*/
        int leftDrawableWidth = 0;
        if (mLeftDrawableId != 0) {
            int imgHeith = mLeftBitmap.getHeight();
            leftDrawableWidth = mLeftBitmap.getWidth();
            canvas.drawBitmap(mLeftBitmap, getPaddingLeft(), (getHeight() - imgHeith) / 2, mLeftPaint);
        }

        /*画左边的文字*/
        if (!TextUtils.isEmpty(mLeftText)) {
            Paint.FontMetricsInt leftFm = mLeftPaint.getFontMetricsInt();
            int leftDy = (leftFm.bottom - leftFm.top) / 2 - leftFm.bottom;
            float leftBaseLineY = getHeight() / 2 + leftDy;
            float leftPadding = getPaddingLeft();
            if (leftDrawableWidth != 0) {
                leftPadding += leftDrawableWidth + mLeftDrawablePadding;
            }
            canvas.drawText(mLeftText, leftPadding, leftBaseLineY, mLeftPaint);
        }

        /*画中间的*/
        if (mMiddleDrawableId != 0 && !TextUtils.isEmpty(mMiddleText)) {// 中间即有图片又有文字
            int imgHeith = mMiddleBitmap.getHeight();
            Rect middleRect = new Rect();
            mMiddlePaint.getTextBounds(mMiddleText, 0, mMiddleText.length(), middleRect);
            int textWidth = middleRect.width();
            Paint.FontMetricsInt middleFm = mMiddlePaint.getFontMetricsInt();
            int middleDy = (middleFm.bottom - middleFm.top) / 2 - middleFm.bottom;
            float middleBaseLineY = getHeight() / 2 + middleDy;
            /*中间文字加图片的宽度*/
            int middleTextWidth = (int) (mMiddleBitmap.getWidth() + mMiddleDrawablePadding + textWidth);
            canvas.drawBitmap(mMiddleBitmap, getWidth() / 2 - middleTextWidth / 2, (getHeight() - imgHeith) / 2, mMiddlePaint);
            canvas.drawText(mMiddleText, getWidth() / 2 - middleTextWidth / 2 + mMiddleBitmap.getWidth() + mMiddleDrawablePadding, middleBaseLineY, mMiddlePaint);
        } else if (!TextUtils.isEmpty(mMiddleText)) {// 中间只有文字
            Rect middleRect = new Rect();
            mMiddlePaint.getTextBounds(mMiddleText, 0, mMiddleText.length(), middleRect);
            int textWidth = middleRect.width();
            Paint.FontMetricsInt middleFm = mMiddlePaint.getFontMetricsInt();
            int middleDy = (middleFm.bottom - middleFm.top) / 2 - middleFm.bottom;
            float middleBaseLineY = getHeight() / 2 + middleDy;
            canvas.drawText(mMiddleText, getWidth() / 2 - textWidth / 2, middleBaseLineY, mMiddlePaint);
        } else if (mMiddleDrawableId != 0) {// 中间只有图片
            int imgHeith = mMiddleBitmap.getHeight();
            canvas.drawBitmap(mMiddleBitmap, getWidth() / 2 - mMiddleBitmap.getWidth() / 2, (getHeight() - imgHeith) / 2, mMiddlePaint);
        }

        /*画右2的图片*/
        int toRightWidth = 0;
        if (mRight2DrawableId != 0) {
            int imgHeith = Mright2Bitmap.getHeight();
            toRightWidth = Mright2Bitmap.getWidth() + getPaddingRight();
            canvas.drawBitmap(Mright2Bitmap, getWidth() - toRightWidth, (getHeight() - imgHeith) / 2, mRight2Paint);
        }

        /*画右2的文字*/
        if (!TextUtils.isEmpty(mRight2Text)) {
            Rect right2Rect = new Rect();
            mRight2Paint.getTextBounds(mRight2Text, 0, mRight2Text.length(), right2Rect);
            int textWidth = right2Rect.width();
            Paint.FontMetricsInt right2Fm = mRight2Paint.getFontMetricsInt();
            int right2Dy = (right2Fm.bottom - right2Fm.top) / 2 - right2Fm.bottom;
            float right2BaseLineY = getHeight() / 2 + right2Dy;
            if (mRight2DrawableId == 0) {/*说明没图片*/
                toRightWidth = textWidth + getPaddingRight();
            } else {/*说明已画了图片*/
                toRightWidth = (int) (toRightWidth + textWidth + mRight2DrawablePadding);
            }
            canvas.drawText(mRight2Text, getWidth() - toRightWidth, right2BaseLineY, mRight2Paint);
        }


        if (toRightWidth == 0) {
            toRightWidth = getPaddingRight();
            mRightRight2Padding = 0;
        }

        /*画右边的图片*/
        if (mRightDrawableId != 0) {
            int imgHeith = mRightBitmap.getHeight();
            toRightWidth = (int) (toRightWidth + mRightBitmap.getWidth() + mRightRight2Padding);
            canvas.drawBitmap(mRightBitmap, getWidth() - toRightWidth, (getHeight() - imgHeith) / 2, mRightPaint);
        }

        /*画右边的文字*/
        if (!TextUtils.isEmpty(mRightText)) {
            Rect rightRect = new Rect();
            mRightPaint.getTextBounds(mRightText, 0, mRightText.length(), rightRect);
            int textWidth = rightRect.width();
            Paint.FontMetricsInt rightFm = mRightPaint.getFontMetricsInt();
            int rightDy = (rightFm.bottom - rightFm.top) / 2 - rightFm.bottom;
            float rightBaseLineY = getHeight() / 2 + rightDy;

            toRightWidth = toRightWidth + textWidth;
            if (mRightDrawableId != 0) {/*说明右边没图*/
                toRightWidth += mRightDrawablePadding;
            }
            canvas.drawText(mRightText, getWidth() - toRightWidth, rightBaseLineY, mRightPaint);
        }
    }
}
