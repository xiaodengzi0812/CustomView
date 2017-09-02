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
 * @Title: 常使用的item view
 * @Time: 2017/8/30.
 * @Version:1.0.0
 */
public class ItemView extends View {
    /*各种属性*/
    /*左右的文字*/
    private String mLeftText, mRightText;
    /*左文字颜色*/
    private int mLeftTextColor = getResources().getColor(R.color.item_text_color);
    /*右文字颜色*/
    private int mRightTextColor = getResources().getColor(R.color.item_text_color);
    /*左文字大小，给个默认值15*/
    private int mLeftTextSize = 15;
    /*右文字大小，给个默认值12*/
    private int mRightTextSize = 12;
    /*左右图片id*/
    private int mLeftDrawableId, mRightDrawableId;
    /*左文字距左图片的距离*/
    private float mRightDrawablePadding = 10;
    /*右文字距右图片的距离*/
    private float mLeftDrawablePadding = 10;
    /*上下线的宽度*/
    private float mTopLineHeight, mBottomLineHeight;
    /*上下线的颜色*/
    private int mTopLineColor, mBottomLineColor;
    /*上线的左右padding*/
    private float mTopLinePaddingLeft, mTopLinePaddingRight;
    /*下线的左右padding*/
    private float mBottomLinePaddingLeft, mBottomLinePaddingRight;

    /*画笔*/
    /*左文字的画笔*/
    private Paint mLeftPaint;
    /*右文字的画笔*/
    private Paint mRightPaint;
    /*上线的画笔*/
    private Paint mTopPaint;
    /*下线的画笔*/
    private Paint mBottomPaint;

    public ItemView(Context context) {
        this(context, null);
    }

    public ItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.custom_item);
            mLeftText = typedArray.getString(R.styleable.custom_item_itemLeftText);
            mRightText = typedArray.getString(R.styleable.custom_item_itemRightText);
            mLeftTextColor = typedArray.getColor(R.styleable.custom_item_itemLeftTextColor, getResources().getColor(R.color.item_text_color));
            mRightTextColor = typedArray.getColor(R.styleable.custom_item_itemRightTextColor, getResources().getColor(R.color.item_text_color));
            mLeftTextSize = typedArray.getDimensionPixelSize(R.styleable.custom_item_itemLeftTextSize, 15);
            mRightTextSize = typedArray.getDimensionPixelSize(R.styleable.custom_item_itemRightTextSize, 12);
            mLeftDrawableId = typedArray.getResourceId(R.styleable.custom_item_itemLeftDrawable, 0);
            mRightDrawableId = typedArray.getResourceId(R.styleable.custom_item_itemRightDrawable, 0);
            mLeftDrawablePadding = typedArray.getDimension(R.styleable.custom_item_itemLeftDrawablePadding, 10);
            mRightDrawablePadding = typedArray.getDimension(R.styleable.custom_item_itemRightDrawablePadding, 10);
            mTopLineHeight = typedArray.getDimension(R.styleable.custom_item_itemTopLineHeight, 1);
            mBottomLineHeight = typedArray.getDimension(R.styleable.custom_item_itemBottomLineHeight, 1);
            mTopLineColor = typedArray.getColor(R.styleable.custom_item_itemTopLineColor, getResources().getColor(R.color.item_line_color));
            mBottomLineColor = typedArray.getColor(R.styleable.custom_item_itemBottomLineColor, getResources().getColor(R.color.item_line_color));
            mTopLinePaddingLeft = typedArray.getDimension(R.styleable.custom_item_itemTopLinePaddingLeft, 0);
            mTopLinePaddingRight = typedArray.getDimension(R.styleable.custom_item_itemTopLinePaddingRight, 0);
            mBottomLinePaddingLeft = typedArray.getDimension(R.styleable.custom_item_itemBottomLinePaddingLeft, 0);
            mBottomLinePaddingRight = typedArray.getDimension(R.styleable.custom_item_itemBottomLinePaddingRight, 0);
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

        /*右文字的画笔*/
        mRightPaint = new Paint();
        mRightPaint.setAntiAlias(true);
        mRightPaint.setTextSize(mRightTextSize);
        mRightPaint.setColor(mRightTextColor);

        /*上线的画笔*/
        mTopPaint = new Paint();
        mTopPaint.setAntiAlias(true);
        mTopPaint.setStrokeWidth(mTopLineHeight);
        mTopPaint.setStyle(Paint.Style.STROKE);
        mTopPaint.setColor(mTopLineColor);

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
        /*画top的线*/
        if (mTopLineHeight > 0) {
            canvas.drawLine(mTopLinePaddingLeft, 0, getWidth() - mTopLinePaddingRight, 0, mTopPaint);
        }
        /*画bottom的线*/
        if (mBottomLineHeight > 0) {
            canvas.drawLine(mBottomLinePaddingLeft, getHeight(), getWidth() - mBottomLinePaddingRight, getHeight(), mBottomPaint);
        }

        /*画左边的图片*/
        int leftDrawableWidth = 0;
        if (mLeftDrawableId != 0) {
            Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), mLeftDrawableId);
            int imgHeith = bitmap.getHeight();
            leftDrawableWidth = bitmap.getWidth();
            canvas.drawBitmap(bitmap, getPaddingLeft(), (getHeight() - imgHeith) / 2, mLeftPaint);
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

        /*画右边的图片*/
        int rightDrawableWidth = 0;
        if (mRightDrawableId != 0) {
            Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), mRightDrawableId);
            int imgHeith = bitmap.getHeight();
            rightDrawableWidth = bitmap.getWidth();
            canvas.drawBitmap(bitmap, getWidth() - rightDrawableWidth - getPaddingRight(), (getHeight() - imgHeith) / 2, mRightPaint);
        }

        /*画右边的文字*/
        if (!TextUtils.isEmpty(mRightText)) {
            Rect rightRect = new Rect();
            mRightPaint.getTextBounds(mRightText, 0, mRightText.length(), rightRect);
            int textWidth = rightRect.width();
            Paint.FontMetricsInt rightFm = mRightPaint.getFontMetricsInt();
            int rightDy = (rightFm.bottom - rightFm.top) / 2 - rightFm.bottom;
            float rightBaseLineY = getHeight() / 2 + rightDy;

            float rightPadding = getPaddingRight();
            if (rightDrawableWidth != 0) {
                rightPadding += rightDrawableWidth + mRightDrawablePadding;
            }
            canvas.drawText(mRightText, getWidth() - textWidth - rightPadding, rightBaseLineY, mRightPaint);
        }
    }
}
