package com.dengzi.customview.trackcolor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * @author Djk
 * @Title: ViewPager的adapter
 * @Time: 2017/8/31.
 * @Version:1.0.0
 */
public class TrackAdapter extends FragmentPagerAdapter {
    private ArrayList<TrackFragment> fragments = new ArrayList<>();
    int length;

    public TrackAdapter(FragmentManager fm, String[] topStr) {
        super(fm);
        length = topStr.length;
        for (String s : topStr) {
            TrackFragment trackFragment = TrackFragment.getInstence(s);
            fragments.add(trackFragment);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return length;
    }
}
