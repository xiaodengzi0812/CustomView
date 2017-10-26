package com.dengzi.imageselector.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import com.dengzi.imageselector.fragment.ImageWatcherFragment;

import java.util.ArrayList;


/**
 * @Title: 图片预览
 * @Author: djk
 * @Time: 2017/10/25
 * @Version:1.0.0
 */
public class ImageWatcherFragmentPagerAdapter extends
        FragmentStatePagerAdapter {
    private ArrayList<String> mImageList;

    public ImageWatcherFragmentPagerAdapter(FragmentManager fm, ArrayList<String> imageList) {
        super(fm);
        this.mImageList = imageList;
    }

    @Override
    public int getCount() {
        return mImageList == null ? 0 : mImageList.size();
    }

    @Override
    public Fragment getItem(int arg0) {
        return mImageList == null ? null : ImageWatcherFragment.getInstance(mImageList
                .get(arg0));
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
