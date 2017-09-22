package com.dengzi.recyclerviewlib.section;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;

/**
 * @author Djk
 * @Title: 分割线用来分组
 * @Time: 2017/9/22.
 * @Version:1.0.0
 */
public class SectionDecoration extends RecyclerView.ItemDecoration {
    //分组回调
    private DecorationCallback mCallback;
    //两个画笔
    private Paint mTextPaint;
    private Paint mBackPaint;
    //分组的高度
    private int mTopGap;

    public SectionDecoration(Context context, DecorationCallback callback) {
        this.mCallback = callback;
        //初始化两个画笔
        mBackPaint = new Paint();
        mBackPaint.setColor(Color.parseColor("#DDDDDD"));

        mTextPaint = new TextPaint();
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(60);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        //高度默认给个100
        mTopGap = 100;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildAdapterPosition(view);
        //获取groupid
        long groupId = mCallback.getGroupId(pos);
        if (groupId < 0) return;
        if (pos == 0 || isFirstInGroup(pos)) {
            outRect.top = mTopGap;
        } else {
            outRect.top = 0;
        }
    }

    /*@Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            long groupId = mCallback.getGroupId(position);
            if (groupId < 0) return;
            //获取要绘制的文本
            String textLine = mCallback.getGroupText(position).toUpperCase();
            if (position == 0 || isFirstInGroup(position)) {
                float top = view.getTop() - mTopGap;
                float bottom = view.getTop();
                //绘制矩形
                c.drawRect(left, top, right, bottom, mBackPaint);
                //绘制文本
                Paint.FontMetricsInt textFm = mTextPaint.getFontMetricsInt();
                int dy = (textFm.bottom - textFm.top) / 2 - textFm.bottom;
                int baseLine = mTopGap / 2 - dy;
                c.drawText(textLine, left + 50, view.getTop() - baseLine, mTextPaint);
            }
        }
    }*/

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int itemCount = state.getItemCount();
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        long preGroupId, groupId = -1;
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            //两个groupid来判断是不是变化了
            preGroupId = groupId;
            groupId = mCallback.getGroupId(position);
            if (groupId < 0 || groupId == preGroupId) continue;
            //获取绘制文本
            String textLine = mCallback.getGroupText(position).toUpperCase();
            if (TextUtils.isEmpty(textLine)) continue;
            int viewBottom = view.getBottom();
            float textY = Math.max(mTopGap, view.getTop());
            if (position + 1 < itemCount) { //下一个和当前不一样移动当前
                long nextGroupId = mCallback.getGroupId(position + 1);
                if (nextGroupId != groupId && viewBottom < textY) {//组内最后一个view进入了header
                    textY = viewBottom;
                }
            }
            //绘制矩形
            c.drawRect(left, textY - mTopGap, right, textY, mBackPaint);
            //绘制文本
            Paint.FontMetricsInt textFm = mTextPaint.getFontMetricsInt();
            int dy = (textFm.bottom - textFm.top) / 2 - textFm.bottom;
            int baseLine = mTopGap / 2 - dy;
            c.drawText(textLine, left + 50, textY - baseLine, mTextPaint);
        }
    }

    //判断是否为第一个
    private boolean isFirstInGroup(int pos) {
        if (pos == 0) {
            return true;
        } else {
            long prevGroupId = mCallback.getGroupId(pos - 1);
            long groupId = mCallback.getGroupId(pos);
            return prevGroupId != groupId;
        }
    }

}
