package com.dengzi.trackcolor;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author Djk
 * @Title: 变换颜色的textview
 * @Time: 2017/8/31.
 * @Version:1.0.0
 */
public class TrackColorTextView extends TextView {
    /*默认画笔*/
    private Paint mDefaultPaint;
    /*变换的画笔*/
    private Paint mTrackPaint;
    private String mText;
    private int mTextWidth;
    private int mDy;
    private float mClipPercent = 0.0f;

    private Direction mDirection = Direction.LEFT_TO_RIGHT;

    public enum Direction {
        LEFT_TO_RIGHT,
        RIGHT_TO_LEFT
    }

    public void setDirection(Direction direction) {
        this.mDirection = direction;
    }

    public void setProgress(float progress) {
        this.mClipPercent = progress;
        invalidate();
    }

    public TrackColorTextView(Context context) {
        this(context, null);
    }

    public TrackColorTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TrackColorTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int defaultColor = 0;
        int trackColor = 0;
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.custom_track);
            defaultColor = typedArray.getColor(R.styleable.custom_track_defaultColor, getCurrentTextColor());
            trackColor = typedArray.getColor(R.styleable.custom_track_trackColor, getCurrentTextColor());
        }
        mDefaultPaint = getPaint(defaultColor);
        mTrackPaint = getPaint(trackColor);
    }

    /*初始化draw方法的参数*/
    private void initDraw() {
        mText = getText().toString();
        Rect rect = new Rect();
        mDefaultPaint.getTextBounds(mText, 0, mText.length(), rect);
        mTextWidth = rect.width();

        Paint.FontMetricsInt fm = mDefaultPaint.getFontMetricsInt();
        mDy = (fm.bottom - fm.top) / 2 - fm.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initDraw();
        if (mDirection == Direction.LEFT_TO_RIGHT) {// 从左到右
            drawText(canvas, mDefaultPaint, (int) (mTextWidth * mClipPercent), mTextWidth);
            drawText(canvas, mTrackPaint, 0, (int) (mTextWidth * mClipPercent));
        } else {//从右到左
            drawText(canvas, mDefaultPaint, 0, (int) (mTextWidth * (1 - mClipPercent)));
            drawText(canvas, mTrackPaint, (int) (mTextWidth * (1 - mClipPercent)), mTextWidth);
        }
    }

    private void drawText(Canvas canvas, Paint paint, int addFrom, int addTo) {
        int dx = getWidth() / 2 - mTextWidth / 2;
        int fromDraw = dx + addFrom;
        int toDraw = dx + addTo;
        int baseLineY = getHeight() / 2 + mDy;

        canvas.save();
        Rect clipRect = new Rect(fromDraw, 0, toDraw, getHeight());
        canvas.clipRect(clipRect);
        canvas.drawText(mText, dx, baseLineY, paint);
        canvas.restore();
    }

    private Paint getPaint(int textColor) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setTextSize(getTextSize());
        paint.setColor(textColor);
        return paint;
    }

}
