package com.dengzi.bannerlib;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * @author Djk
 * @Title:
 * @Time: 2017/10/19.
 * @Version:1.0.0
 */
public class BannerScroller extends Scroller {
    // 滑动切换持续的时间
    private int mScrollDuration = 1000;

    public BannerScroller(Context context) {
        super(context);
    }

    public BannerScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public BannerScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration);
    }

    /**
     * 设置滑动切换持续的时间
     *
     * @param scrollDurationTime
     */
    public void setScrollDurationTime(int scrollDurationTime) {
        this.mScrollDuration = scrollDurationTime;
    }
}
