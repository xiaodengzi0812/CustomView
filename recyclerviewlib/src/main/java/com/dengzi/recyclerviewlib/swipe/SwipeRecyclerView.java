package com.dengzi.recyclerviewlib.swipe;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author Djk
 * @Title: 侧滑 RecyclerView
 * @Time: 2017/11/8.
 * @Version:1.0.0
 */
public class SwipeRecyclerView extends RecyclerView {
    // 当前touch的view
    private SwipeMenuLayout mTouchView;
    //事件起始的x、y坐标
    private float mStartX;
    private float mStartY;
    //事件分发给子类
    private boolean mDispathToChild = false;
    //事件分发给自己
    private boolean mDispathToSuper = false;

    public SwipeRecyclerView(Context context) {
        this(context, null);
    }

    public SwipeRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        /*Down事件的时候，初始化一些信息*/
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mDispathToChild = false;
            mDispathToSuper = false;
            mStartX = ev.getX();
            mStartY = ev.getY();
            initSwipeView();
        }
        /*如果本次事件要分发给子类，直接分发*/
        if (mDispathToChild && mTouchView != null) {
            return mTouchView.dispatchTouchEvent(ev);
        }
        /*如果本次事件要分发给自己，直接分发*/
        if (mDispathToSuper) {
            return super.dispatchTouchEvent(ev);
        }
        // move事件的时候，做判断
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            /*算出刚开始移动的x和y轴的距离*/

            float disX = Math.abs(ev.getX() - mStartX);
            float disY = Math.abs(ev.getY() - mStartY);
            if (disX > 2 || disY > 2) {
                //如果x方向的滑动距离大于y方向，则我们认为这个事件是子类的
                if (disX > disY) {
                    mDispathToChild = true;
                    return mTouchView.dispatchTouchEvent(ev);
                } else {/*如果不是子类的事件，那么将本次事件分发给自己去处理*/
                    mDispathToSuper = true;
                    // 如果自己处理事件，则将上次滑动的view关闭
                    closeSwipMenu();
                    return super.dispatchTouchEvent(ev);
                }
            }
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            // 如果能到这里的up事件，关闭打开的菜单
            closeSwipMenu();
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 初始化itemview
     */
    private void initSwipeView() {
        int touchPosition = getChildAdapterPosition(findChildViewUnder(mStartX, mStartY));
        ViewHolder viewHolder = findViewHolderForLayoutPosition(touchPosition);
        View touchView = viewHolder.itemView;
        // 当滑动的不是之前的view的时候，就将之滑动的view关闭
        if (touchView != mTouchView) {
            closeSwipMenu();
        }
        if (touchView != null && touchView instanceof SwipeMenuLayout) {
            mTouchView = (SwipeMenuLayout) touchView;
        }
    }

    /**
     * 关闭菜单view
     */
    private void closeSwipMenu() {
        if (mTouchView != null) {
            mTouchView.closeMenu();
        }
    }
}
