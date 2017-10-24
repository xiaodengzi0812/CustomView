package com.dengzi.bannerlib;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Djk
 * @Title: PagerAdapter的一个普通类，如果以后有什么特殊需求可以在这里修改
 * @Time: 2017/10/23.
 * @Version:1.0.0
 */
public class CommonPagerAdapter extends PagerAdapter {
    // view的复用集合
    private List<View> mReuseViewList = new ArrayList<>();
    // 设置的BannerBaseAdapter
    private BannerBaseAdapter mAdapter;
    // item点击事件
    private OnBannerItemClickListener mBannerItemClickListener;

    public CommonPagerAdapter(BannerBaseAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    @Override
    public int getCount() {
        // 如果只有一条，则只显示一条
        if (mAdapter.getCount() < 2) {
            return 1;
        }
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
        initListener(itemView, index);
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
     * 初始化点击事件
     */
    private void initListener(View view, final int position) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBannerItemClickListener != null) {
                    mBannerItemClickListener.onBannerItemClick(position);
                }
            }
        });
    }

    /**
     * 设置点击事件
     */
    public void setOnBannerItemClickListener(OnBannerItemClickListener bannerItemClickListener) {
        this.mBannerItemClickListener = bannerItemClickListener;
    }
}
