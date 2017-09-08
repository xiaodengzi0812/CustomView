package com.dengzi.customview.draw;

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
public class DrawAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments = new ArrayList<>();
    int length;

    public DrawAdapter(FragmentManager fm, int[] types) {
        super(fm);
        length = types.length;
        for (int type : types) {
            switch (type) {
                case 0:
                    fragments.add(DrawCommonFragment.getInstence());
                case 1:
                    fragments.add(DrawPathFragment.getInstence());
                case 2:
                    fragments.add(DrawAnimatorFragment.getInstence());
            }
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
