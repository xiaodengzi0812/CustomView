package com.dengzi.customview.slid;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dengzi.customview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Djk
 * @Title: 普通draw
 * @Time: 2017/8/31.
 * @Version:1.0.0
 */
public class ContentFragment extends Fragment {
    public static ContentFragment getInstence() {
        ContentFragment fragment = new ContentFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mRootView = View.inflate(getActivity(), R.layout.slid_home_content, null);
        initView(mRootView);
        return mRootView;
    }

    private void initView(View view) {
        MyListView lv = (MyListView) view.findViewById(R.id.lv);
        List<String> dataList = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            dataList.add("首页数据 -> " + i);
        }
        MyAdapter adapter = new MyAdapter(getActivity(), dataList);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "click -> " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class MyAdapter extends BaseAdapter {
        private List<String> mDataList = new ArrayList<>();
        private Context mContext;

        public MyAdapter(Context context, List<String> dataList) {
            this.mDataList = dataList;
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View contentView = View.inflate(mContext, R.layout.slid_home_item, null);
            TextView tv = (TextView) contentView.findViewById(R.id.tv);
            tv.setText(mDataList.get(position));
            return contentView;
        }
    }

}

