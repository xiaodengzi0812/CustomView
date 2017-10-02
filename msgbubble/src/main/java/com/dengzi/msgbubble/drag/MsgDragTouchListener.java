/*
 * 我们解决问题的思路是手指按下的时在WindowManager中加入拖拽的控件，
 * 然后在拖拽的控件中去draw一个我们要拖拽的view的bitmap
 */
package com.dengzi.msgbubble.drag;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

/**
 * @author Djk
 * @Title: 要拖拽的view的touch监听事件
 * @Time: 2017/9/30.
 * @Version:1.0.0
 */
public class MsgDragTouchListener implements OnTouchListener {
    // 要拖拽的view
    private View mDragView;
    private Context mContext;

    private MsgDragView mMsgDragView;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;

    public MsgDragTouchListener(View dragView, Context context) {
        this.mDragView = dragView;
        this.mContext = context;
        // 初始化windowManager
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        // 初始化拖拽view
        mMsgDragView = new MsgDragView(mContext);

        mParams = new WindowManager.LayoutParams();
        // 背景要透明
        mParams.format = PixelFormat.TRANSPARENT;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 添加拖拽view
                mWindowManager.addView(mMsgDragView, mParams);
                // 初始化贝塞尔View的点
                // 保证固定圆的中心在View的中心
                int[] location = new int[2];
                // 获取view在屏幕的位置，将xy值保存到location数组中，获取到的xy值为view的左上角坐标
                mDragView.getLocationOnScreen(location);
                int centerX = location[0] + mDragView.getWidth() / 2;
                int centerY = location[1] + mDragView.getWidth() / 2 - getStatusBarHeight();
                mMsgDragView.initPoint(centerX, centerY);
                // 获取view的图画截图
                Bitmap bitmap = getBitmapByView(mDragView);
                // 将view的图画截图设置到拖拽控件中
                mMsgDragView.setDragBitmap(bitmap);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mDragView.getVisibility() == View.VISIBLE) {
                    // 在move的时候，将拖拽view隐藏
                    mDragView.setVisibility(View.INVISIBLE);
                }
                mMsgDragView.updataPoint(event.getRawX(), event.getRawY() - getStatusBarHeight());
                break;
            case MotionEvent.ACTION_UP:
                mWindowManager.removeView(mMsgDragView);
                break;
        }
        return true;
    }

    /**
     * 从一个View中获取Bitmap
     *
     * @param view
     * @return
     */
    private Bitmap getBitmapByView(View view) {
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    /**
     * 获取状态栏的高度
     *
     * @return
     */
    private int getStatusBarHeight() {
        // 插件式换肤：怎么获取资源的，先获取资源id，根据id获取资源
        Resources resources = mContext.getResources();
        int statusBarHeightId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelOffset(statusBarHeightId);
    }

}
