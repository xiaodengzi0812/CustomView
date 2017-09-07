package com.dengzi.customview.taglayout;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dengzi.customview.R;

import java.util.List;

/**
 * @author Djk
 * @Title: 流布局adapter
 * @Time: 2017/9/7.
 * @Version:1.0.0
 */
public class MyTagAdapter extends BaseAdapter {
    private List<TagBean> mDataList;
    private Context mContext;
    private ImageView iv;
    private TextView tv;

    public MyTagAdapter(List<TagBean> dataList, Context context) {
        this.mDataList = dataList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public TagBean getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.taglayout_item, parent, false);
        iv = (ImageView) view.findViewById(R.id.iv);
        tv = (TextView) view.findViewById(R.id.tv);
        final TagBean tagBean = getItem(position);
        tv.setText(tagBean.getTagName());
        iv.setImageResource(R.drawable.user);
        iv.setVisibility(position % 2 == 0 ? View.VISIBLE : View.GONE);
        if (tagBean.isSelect()) {
            tv.setTextColor(Color.parseColor("#ff0000"));
        } else {
            tv.setTextColor(Color.parseColor("#000000"));
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (TagBean bean : mDataList) {
                    bean.setSelect(false);
                }
                tagBean.setSelect(true);
                MyTagAdapter.this.notifyDataSetChanged();
            }
        });
        return view;
    }

}
