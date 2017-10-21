package com.dengzi.bannerlib;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Djk
 * @Title: 自定义BannerViewPager
 * @Time: 2017/10/19.
 * @Version:1.0.0
 */
public class BannerViewPager extends ViewPager {
    private Context mContext;
    private BannerBaseAdapter mAdapter;

    private final int HANDLER_MSG = 0x0011;
    // 延时滚动的时间
    private int mDelayedTime = 3000;
    // 自定义Scroller，为了解决滑动太快的问题
    private BannerScroller mScroller;
    // view的复用集合
    private List<View> mReuseViewList = new ArrayList<>();

    // 用Handler来实现无限滚动
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // 设置滚动到下一页
            setCurrentItem(getCurrentItem() + 1);
            // 再次开启滚动
            startScroll();
        }
    };

    public BannerViewPager(Context context) {
        this(context, null);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setScroller();
    }

    /**
     * 初始化滑动减速
     */
    private void setScroller() {
        // 解决滑动太快的问题，通过反射获取ViewPager的mScroller类，然后将我们自定义的BannerScroller设置给ViewPager
        try {
            mScroller = new BannerScroller(mContext, new BounceInterpolator());
            Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            scrollerField.set(this, mScroller);
        } catch (Exception e) {
        }
    }

    /**
     * 设置滑动切换持续的时间
     *
     * @param scrollDurationTime
     */
    public void setScrollDurationTime(int scrollDurationTime) {
        this.mScroller.setScrollDurationTime(scrollDurationTime);
    }

    /**
     * 设置自定义的BannerAdapter
     *
     * @param adapter
     */
    public void setAdapter(BannerBaseAdapter adapter) {
        this.mAdapter = adapter;
        setAdapter(mPageAdapter);
        // 设置当前位置是轮播个数的100倍，这个值是随便写的，也没人会真的向后方向滑动100页吧？
        // 本来是想设置成 Integer.MAX_VALUE / 2 这个值的，但是设置这个值后启动的时候会有白屏
        setCurrentItem(mAdapter.getCount() * 100);
    }

    /**
     * 开始滚动
     */
    public void startScroll() {
        Log.e("dengzi", "startScroll");
        mHandler.removeMessages(HANDLER_MSG);
        mHandler.sendEmptyMessageDelayed(HANDLER_MSG, mDelayedTime);
    }

    /**
     * 设置多长时间滚动一次
     *
     * @param delayedTime
     */
    public void setDelayedTime(int delayedTime) {
        this.mDelayedTime = delayedTime;
    }

    private PagerAdapter mPageAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int index = position % mAdapter.getCount();
            View itemView = mAdapter.getView(index, container, getReuseView());
            container.addView(itemView);
            // 创建itemview
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // 销毁itemview
            View view = (View) object;
            container.removeView(view);
            // 在销毁的时候，把view缓存起来，传给下一个要显示的view去复用
            // 判断一下view是否存在，要不然会加好多相同的view
            if (!mReuseViewList.contains(view)) {
                mReuseViewList.add(view);
            }
        }
    };

    /**
     * 获取可复用的view
     */
    private View getReuseView() {
        for (View view : mReuseViewList) {
            if (view.getParent() == null) {
                return view;
            }
        }
        return null;
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
