package com.dengzi.customview.recviewtest;

import android.content.Context;

import com.dengzi.customview.R;
import com.dengzi.recyclerviewlib.adapter.MultiTypeSupport;
import com.dengzi.recyclerviewlib.adapter.RecyBaseAdapter;
import com.dengzi.recyclerviewlib.adapter.RecyBaseViewHolder;

import java.util.List;

/**
 * @author Djk
 * @Title: 具体的Adapter
 * @Time: 2017/9/20.
 * @Version:1.0.0
 */
public class MyAdapter extends RecyBaseAdapter<String> {

    public MyAdapter(Context context, List<String> dataList) {
        /*多布局支持*/
        super(context, dataList, new MultiTypeSupport<String>() {
            @Override
            public int getLayoutId(String item, int position) {
                if (position % 3 == 0) {
                    return R.layout.recy_item;
                } else {
                    return R.layout.recy_item2;
                }
            }
        });
    }

    @Override
    protected void onBindView(RecyBaseViewHolder holder, String itemData, int position) {
        if (holder.getItemViewType() == R.layout.recy_item) {
            holder.setText(R.id.tv, itemData);
        } else if (holder.getItemViewType() == R.layout.recy_item2) {
            holder.setText(R.id.tv2, itemData);
        }
    }
}
