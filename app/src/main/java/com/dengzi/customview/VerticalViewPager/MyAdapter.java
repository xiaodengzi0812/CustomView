package com.dengzi.customview.VerticalViewPager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.dengzi.customview.R;
import com.djk.parallax.ParallaxBaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Djk
 * @Title: 对应的adapter
 * @Time: 2017/10/9.
 * @Version:1.0.0
 */
public class MyAdapter extends BaseAdapter {
    private Context mContext;
    private int[] mLayoutIds;

    public MyAdapter(Context context, int[] layoutIds) {
        mLayoutIds = layoutIds;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(mLayoutIds[position], parent, false);
        View btn = view.findViewById(R.id.btn);
        if (btn != null) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "点击事件还在", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return view;
    }

    @Override
    public int getCount() {
        return mLayoutIds != null ? mLayoutIds.length : 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}