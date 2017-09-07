package com.dengzi.taglayout;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Djk
 * @Title: 流式布局
 * @Time: 2017/9/6.
 * @Version:1.0.0
 */
public class TagLayout extends RelativeLayout {
    /*一行view的list的list*/
    private List<List<View>> mChildLineViewList = new ArrayList<>();
    /*每行的高度*/
    private List<Integer> mChildLineHeightList = new ArrayList<>();

    private BaseAdapter mTagAdapter;

    public TagLayout(Context context) {
        super(context);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /*onMeasure有可能会调好几次，所以每次清除一下数据*/
        mChildLineViewList.clear();
        mChildLineHeightList.clear();
        /*获取当前的宽度*/
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        /*子view的个数*/
        int childCount = getChildCount();
        /*子view的一行宽度*/
        int lineWidth = 0;
        /*一行子view的list集合*/
        List<View> childList = new ArrayList<>();
        /*每行的高度*/
        int lineHeight = 0;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();
            int childWidth = childView.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            int childHeight = childView.getMeasuredHeight() + params.bottomMargin + params.topMargin;

            if ((lineWidth + childWidth) > parentWidth) {/*要换行*/
                lineWidth = childWidth;
                mChildLineViewList.add(childList);
                childList = new ArrayList<>();
                childList.add(childView);
                mChildLineHeightList.add(lineHeight);
                lineHeight = childHeight;
            } else {/*不换行*/
                childList.add(childView);
                lineWidth += childWidth;
                lineHeight = Math.max(childHeight, lineHeight);
            }
        }
        /*添加最后一行的数据*/
        mChildLineViewList.add(childList);
        mChildLineHeightList.add(lineHeight);

        /*计算tagLayout的高度，每个item的高度相加*/
        int parentHeight = 0;
        for (Integer itemHeight : mChildLineHeightList) {
            parentHeight += itemHeight;
        }
        setMeasuredDimension(parentWidth, parentHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int tempLeft;
        int tempTop = 0;
        for (int i = 0; i < mChildLineViewList.size(); i++) {
            /*一行里有多少个view*/
            List<View> lineViewList = mChildLineViewList.get(i);
            /*一行的高度(包含margin值)*/
            int lineHeight = mChildLineHeightList.get(i);
            tempLeft = 0;
            for (View childView : lineViewList) {
                MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();
                int left = tempLeft + params.leftMargin;
                int top = tempTop + lineHeight / 2 - childView.getMeasuredHeight() / 2;
                int right = left + childView.getMeasuredWidth();
                int bottom = top + childView.getMeasuredHeight();

                childView.layout(left, top, right, bottom);
                tempLeft += childView.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            }
            tempTop += lineHeight;
        }
    }

    /*设置adapter*/
    public void setAdapter(BaseAdapter adapter) {
        if (adapter == null) {
            throw new NullPointerException("Adapter can not be null!");
        }
        mTagAdapter = adapter;
        perparRequestlayout();
        requestLayout();
        MyDataSetObserver observer = new MyDataSetObserver();
        adapter.registerDataSetObserver(observer);
    }

    private class MyDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            perparRequestlayout();
            requestLayout();
        }
    }

    /*准备数据*/
    private void perparRequestlayout() {
        removeAllViews();
        int childCount = mTagAdapter.getCount();
        for (int i = 0; i < childCount; i++) {
            View childView = mTagAdapter.getView(i, null, this);
            addView(childView);
        }
    }
}
