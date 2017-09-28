package com.dengzi.customview.tabscreen;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dengzi.customview.R;
import com.dengzi.tabscreen.BaseTabAdapter;

/**
 * @author Djk
 * @Title:
 * @Time: 2017/9/27.
 * @Version:1.0.0
 */
public class MyTabAdapter extends BaseTabAdapter {
    private String[] mItems = {"类型", "品牌", "价格", "更多"};
    private Context mContext;

    public MyTabAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mItems.length;
    }

    @Override
    public View getTabView(ViewGroup parent, int position) {
        TextView view = (TextView) LayoutInflater.from(mContext).inflate(R.layout.tab_screen_tab, parent, false);
        view.setText(mItems[position]);
        view.setTextColor(Color.BLACK);
        return view;
    }

    @Override
    public View getContentView(ViewGroup parent, final int position) {
        TextView view = (TextView) LayoutInflater.from(mContext).inflate(R.layout.tab_screen_content, parent, false);
        view.setText(mItems[position]);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "关闭菜单", Toast.LENGTH_SHORT).show();
                closeMenu();
            }
        });
        return view;
    }

    @Override
    public void openTabItem(View tabItemView) {
        TextView view = (TextView) tabItemView;
        view.setTextColor(Color.RED);
    }

    @Override
    public void closeTabItem(View tabItemView) {
        TextView view = (TextView) tabItemView;
        view.setTextColor(Color.BLACK);
    }
}
