package com.dengzi.bannerlib;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import java.lang.reflect.Field;

/**
 * @author Djk
 * @Title: 自定义BannerViewPager
 * @Time: 2017/10/19.
 * @Version:1.0.0
 */
public class BannerViewPager extends ViewPager {
    private Context mContext;
    // 子类个数
    private int mChildCount = 0;
    private final int HANDLER_MSG = 0x0011;
    // 延时滚动的时间
    private int mDelayedTime = 3600;
    // 滚动动画执行的时间
    private int mDurationTime = 1200;
    // 滚动动画差值器(默认差值器：在动画开始与结束的地方速率改变比较慢，在中间的时候加速)
    private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    // 自定义Scroller，为了解决滑动太快的问题
    private BannerScroller mScroller;
    // 自动滚动
    private boolean mAutoScroll = false;

    // 用Handler来实现无限滚动
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mAutoScroll) {
                // 设置滚动到下一页
                setCurrentItem(getCurrentItem() + 1);
                // 再次开启滚动
                startAutoScroll();
            }
        }
    };

    public BannerViewPager(Context context) {
        this(context, null);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    /**
     * 设置自定义的BannerAdapter
     *
     * @param adapter
     */
    public void setAdapter(BannerBaseAdapter adapter) {
        mChildCount = adapter.getCount();
        setScroller();
        setAdapter(new CommonPagerAdapter(adapter));
        // 设置当前位置是轮播个数的100倍，这个值是随便写的，也没人会真的向后方向滑动100页吧？
        // 本来是想设置成 Integer.MAX_VALUE / 2 这个值的，但是设置这个值后启动的时候会有白屏
        if (mChildCount > 1) {
            setCurrentItem(mChildCount * 100);
        }
    }

    /**
     * 设置自动滚动
     */
    public void setAutoScroll(boolean autoScroll) {
        this.mAutoScroll = autoScroll;
    }

    /**
     * 开始滚动
     */
    public void startAutoScroll() {
//        Log.e("dengzi", "startAutoScroll");
        // 如果banner位只有一个图片，则不滚动
        if (mChildCount < 2) {
            mAutoScroll = false;
        }
        if (!mAutoScroll) return;
        mHandler.removeMessages(HANDLER_MSG);
        mHandler.sendEmptyMessageDelayed(HANDLER_MSG, mDelayedTime);
    }

    /**
     * 设置多长时间滚动一次
     *
     * @param delayedTime
     */
    public void setScrollDelayedTime(int delayedTime) {
        this.mDelayedTime = delayedTime;
    }

    /**
     * 设置滑动切换持续的时间
     *
     * @param scrollDurationTime
     */
    public void setScrollDurationTime(int scrollDurationTime) {
        this.mDurationTime = scrollDurationTime;
    }

    /**
     * 设置滑动动画执行的差值器
     *
     * @param interpolator
     */
    public void setScrollInterpolator(Interpolator interpolator) {
        this.mInterpolator = interpolator;
    }

    /**
     * 初始化滑动减速
     */
    private void setScroller() {
        // 解决滑动太快的问题，通过反射获取ViewPager的mScroller类，然后将我们自定义的BannerScroller设置给ViewPager
        try {
            mScroller = new BannerScroller(mContext, mInterpolator);
            mScroller.setScrollDurationTime(mDurationTime);
            Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            scrollerField.set(this, mScroller);
        } catch (Exception e) {
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        setUserTouch(ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 用户触摸屏幕事件，解决用户滑动过程中还自动滑动
     */
    public void setUserTouch(int MotionAction) {
        // 如果不自动滑动，则不关心这个事件
        if (!mAutoScroll) return;
        switch (MotionAction) {
            // down事件的时候，清空handler内的待待消息
            case MotionEvent.ACTION_DOWN:
                mHandler.removeMessages(HANDLER_MSG);
                break;
            // up事件的时候，继续自动滚动
            case MotionEvent.ACTION_UP:
                startAutoScroll();
                break;
        }
    }

    /**
     * 销毁hander，解决内存泄露
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeMessages(HANDLER_MSG);
        mHandler = null;
    }
}
