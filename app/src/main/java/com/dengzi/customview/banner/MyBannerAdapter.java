package com.dengzi.customview.banner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dengzi.bannerlib.BannerBaseAdapter;
import com.dengzi.customview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Djk
 * @Title: bannerAdapter
 * @Time: 2017/10/20.
 * @Version:1.0.0
 */
public class MyBannerAdapter extends BannerBaseAdapter {
    private List<BannerBean> mDataList = new ArrayList<>();
    private Context mContext;

    public MyBannerAdapter(Context context, List<BannerBean> dataList) {
        this.mContext = context;
        this.mDataList = dataList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public View getView(ViewGroup parentView, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.banner_item, parentView, false);
        ImageView iv = (ImageView) view.findViewById(R.id.iv);
        iv.setBackgroundResource(mDataList.get(position).getImageRes());
        return view;
    }

    @Override
    public String getDescTitle(int position) {
        return mDataList.get(position).getDescText();
    }
}
