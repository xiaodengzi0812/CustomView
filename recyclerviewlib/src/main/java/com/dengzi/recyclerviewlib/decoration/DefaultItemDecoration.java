package com.dengzi.recyclerviewlib.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.dengzi.recyclerviewlib.R;

/**
 * @author Djk
 * @Title: 通用的分格线
 * @Time: 2017/9/19.
 * @Version:1.0.0
 */
public class DefaultItemDecoration extends RecyclerView.ItemDecoration {
    private Context mContext;
    private Drawable mDrawable;
    private int mHeaderNum;

    public DefaultItemDecoration(Context context) {
        this(context, R.drawable.list_divider, 0);
    }

    /**
     * @param context
     * @param headerNum 添加头部的数量
     */
    public DefaultItemDecoration(Context context, int headerNum) {
        this(context, R.drawable.list_divider, headerNum);
    }

    /**
     * @param context
     * @param drawableResource 分格线的drawable资源
     * @param headerNum        添加头部的数量
     */
    public DefaultItemDecoration(Context context, int drawableResource, int headerNum) {
        this.mContext = context;
        this.mHeaderNum = headerNum;
        this.mDrawable = ContextCompat.getDrawable(context, drawableResource);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // 四个方向的偏移值
        int right = mDrawable.getIntrinsicWidth();
        int bottom = mDrawable.getIntrinsicHeight();
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        if (isLastColumn(position, parent)) {// 是否是最后一列
            right = 0;
        }
        if (isLastRow(position, parent)) {// 是否是最后一行
            bottom = 0;
        }
        if (position < mHeaderNum) {
            bottom = 0;
            right = 0;
        }
        outRect.set(0, 0, right, bottom);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawVertical(c, parent);
        drawHorizontal(c, parent);
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        // 绘制水平间隔线
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getLeft() - params.leftMargin;
            int right = child.getRight() + params.rightMargin + mDrawable.getIntrinsicWidth();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDrawable.getIntrinsicHeight();
            mDrawable.setBounds(left, top, right, bottom);
            mDrawable.draw(c);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        //绘制垂直间隔线(垂直的矩形)
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getRight() + params.rightMargin;
            int right = left + mDrawable.getIntrinsicWidth();
            int top = child.getTop() - params.topMargin;
            int bottom = child.getBottom() + params.bottomMargin;
            mDrawable.setBounds(left, top, right, bottom);
            mDrawable.draw(c);
        }
    }

    /**
     * 是否是最后一列
     */
    public boolean isLastColumn(int itemPosition, RecyclerView parent) {
        int spanCount = getSpanCount(parent);
        if ((itemPosition - mHeaderNum + 1) % spanCount == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否是最后一行
     */
    public boolean isLastRow(int itemPosition, RecyclerView parent) {
        int spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();
        int rowNumber = childCount % spanCount == 0 ? childCount / spanCount : (childCount / spanCount) + 1;
        if (itemPosition > ((rowNumber - 1) * spanCount - 1)) {
            return true;
        }
        return false;
    }

    /**
     * 获取一行有多少列
     */
    public int getSpanCount(RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            // 获取一行的spanCount
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            int spanCount = gridLayoutManager.getSpanCount();
            return spanCount;
        }
        return 1;
    }
}
