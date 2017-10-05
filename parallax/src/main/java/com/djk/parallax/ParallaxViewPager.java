package com.djk.parallax;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Djk
 * @Title: 视差引导页效果ViewPager
 * @Time: 2017/10/5.
 * @Version:1.0.0
 */
public class ParallaxViewPager extends ViewPager {
    private List<ParallaxFragment> mFragmentList = new ArrayList<>();

    public ParallaxViewPager(Context context) {
        this(context, null);
    }

    public ParallaxViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 添加布局文件
     *
     * @param layoutIds 布局资源id
     */
    public void addLayout(FragmentManager fm, int[] layoutIds) {
        mFragmentList.clear();
        for (int layoutId : layoutIds) {
            ParallaxFragment fragment = new ParallaxFragment();
            Bundle args = new Bundle();
            args.putInt(ParallaxFragment.LAYOUT_ID_KEY, layoutId);
            fragment.setArguments(args);
            mFragmentList.add(fragment);
        }
        setAdapter(new MyAdapter(fm));

        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // 滚动  position 当前位置    positionOffset 0-1     positionOffsetPixels 0-屏幕的宽度px
                Log.e("dengzi", "position->" + position + " positionOffset->" + positionOffset + " positionOffsetPixels->" + positionOffsetPixels);
                ParallaxFragment outFragment = mFragmentList.get(position);
                List<View> outParallaxViews = outFragment.getParallaxViews();
                for (View parallaxView : outParallaxViews) {
                    ParallaxTag tag = (ParallaxTag) parallaxView.getTag(R.id.parallax_tag);
                    parallaxView.setTranslationX(tag.translationXOut * (positionOffsetPixels));
                    parallaxView.setTranslationY(tag.translationYOut * (positionOffsetPixels));
                }

                try {
                    ParallaxFragment inFragment = mFragmentList.get(position + 1);
                    List<View> inParallaxViews = inFragment.getParallaxViews();
                    for (View parallaxView : inParallaxViews) {
                        ParallaxTag tag = (ParallaxTag) parallaxView.getTag(R.id.parallax_tag);
                        parallaxView.setTranslationX(tag.translationXIn * (getMeasuredWidth() - positionOffsetPixels));
                        parallaxView.setTranslationY(tag.translationYIn * (getMeasuredWidth() - positionOffsetPixels));
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 自已的FragmentPagerAdapter
     */
    private class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList != null ? mFragmentList.size() : 0;
        }
    }

}
