/*
 * 我们解决问题的思路是手指按下的时在WindowManager中加入拖拽的控件，
 * 然后在拖拽的控件中去draw一个我们要拖拽的view的bitmap
 * 如果拖拽距离小于我们设定的最大值时，我们要将拖拽view回弹到原来的位置
 * 如果拖拽距离大于我们设定的最大值时，我们要开启一个消失动画
 */
package com.dengzi.msgbubble.drag;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.AnimationDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.dengzi.msgbubble.R;

/**
 * @author Djk
 * @Title: 要拖拽的view的touch监听事件
 * @Time: 2017/9/30.
 * @Version:1.0.0
 */
public class MsgDragTouchListener implements OnTouchListener, MsgDragView.MsgDragListener {
    private Context mContext;
    // 要拖拽的view
    private View mDragView;
    // 拖拽控件
    private MsgDragView mMsgDragView;
    // 当前屏幕的WindowManager
    private WindowManager mWindowManager;
    // WindowManager 参数
    private WindowManager.LayoutParams mParams;
    // 消失监听事件
    private MsgDragHelper.DragDisappearListener mDisappearListener;
    // 爆炸view
    private FrameLayout mBombLayout;
    private ImageView mBombImageView;

    public MsgDragTouchListener(View dragView, Context context, MsgDragHelper.DragDisappearListener listener) {
        this.mDragView = dragView;
        this.mContext = context;
        this.mDisappearListener = listener;
        // 初始化windowManager
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        // 初始化拖拽view
        mMsgDragView = new MsgDragView(mContext);
        mMsgDragView.setMsgDragListener(this);
        mParams = new WindowManager.LayoutParams();
        // 背景要透明
        mParams.format = PixelFormat.TRANSPARENT;
        // 创建爆炸消息view
        mBombLayout = new FrameLayout(mContext);
        mBombImageView = new ImageView(mContext);
        mBombImageView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mBombLayout.addView(mBombImageView);
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
                // 更新拖拽view
                mMsgDragView.updataPoint(event.getRawX(), event.getRawY() - getStatusBarHeight());
                break;
            case MotionEvent.ACTION_UP:
                // 抬起事件处理
                mMsgDragView.onTouchActionUp(mDragView);
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

    /**
     * 回弹回调
     */
    @Override
    public void restore() {
        // 移除添加的拖拽控件
        mWindowManager.removeView(mMsgDragView);
        // 将原来的被拖拽view显示
        mDragView.setVisibility(View.VISIBLE);
    }

    /**
     * 消失回调
     */
    @Override
    public void dismiss(Point point) {
        // 移除添加的拖拽控件
        mWindowManager.removeView(mMsgDragView);
        // 添加爆炸动画view
        mWindowManager.addView(mBombLayout, mParams);
        // 设置爆炸动画Animation
        mBombImageView.setBackgroundResource(R.drawable.anim_bomb_pop);
        AnimationDrawable animationDrawable = (AnimationDrawable) mBombImageView.getBackground();
        // 设置爆炸view的显示位置
        mBombImageView.setX(point.x - animationDrawable.getIntrinsicWidth() / 2);
        mBombImageView.setY(point.y - animationDrawable.getIntrinsicHeight() / 2);
        // 开启动画
        animationDrawable.start();
        // 等它执行完之后我要移除掉这个 爆炸动画也就是 mBombLayout
        mBombImageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 移除爆炸动画view
                mWindowManager.removeView(mBombLayout);
                // 通知一下外面该消失
                if (mDisappearListener != null) {
                    mDisappearListener.dismiss(mDragView);
                }
            }
        }, getAnimationDrawableTime(animationDrawable));
    }

    /**
     * 获取动画的时间
     *
     * @param drawable
     * @return
     */
    private long getAnimationDrawableTime(AnimationDrawable drawable) {
        // 获取有几个动画
        int numberOfFrames = drawable.getNumberOfFrames();
        long time = 0;
        for (int i = 0; i < numberOfFrames; i++) {
            time += drawable.getDuration(i);
        }
        return time;
    }

}
