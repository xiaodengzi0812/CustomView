package com.dengzi.customview.recviewtest;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.dengzi.customview.R;
import com.dengzi.recyclerviewlib.adapter.OnItemClickListener;
import com.dengzi.recyclerviewlib.adapter.RecyBaseAdapter;
import com.dengzi.recyclerviewlib.adapter.RecyBaseViewHolder;
import com.dengzi.recyclerviewlib.decoration.DefaultItemDecoration;
import com.dengzi.recyclerviewlib.refresh.DefaultRefreshCreator;
import com.dengzi.recyclerviewlib.refresh.RefreshRecyclerView;
import com.dengzi.recyclerviewlib.section.DecorationCallback;
import com.dengzi.recyclerviewlib.section.SectionDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Djk
 * @Title: 分组
 * @Time: 2017/9/4.
 * @Version:1.0.0
 */
public class RecySectionActivity extends AppCompatActivity {
    private RefreshRecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private List<ItemBean> mItems = new ArrayList<ItemBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recy_refresh_activity);
        initData();
        initView();
        initRefresh();
    }

    private void initRefresh() {
        mRecyclerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(RecySectionActivity.this, "click -> " + mItems.get(position).text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        mRecyclerView = (RefreshRecyclerView) findViewById(R.id.recy_view);
        mAdapter = new MyAdapter(this, mItems);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        /*添加分格线*/
        mRecyclerView.addItemDecoration(new SectionDecoration(this, new DecorationCallback() {
            @Override
            public long getGroupId(int position) {
                return mItems.get(position).groupId;
            }

            @Override
            public String getGroupText(int position) {
                return mItems.get(position).text.substring(0, 1).toUpperCase();
            }
        }));
        /*默认动画*/
        final DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        mRecyclerView.setItemAnimator(itemAnimator);
    }

    protected void initData() {
        for (int i = 'A'; i <= 'Z'; i++) {
            for (int j = 0; j < 5; j++) {
                mItems.add(new ItemBean(i, (char) i + " -> " + j));
            }
        }
    }

    class MyAdapter extends RecyBaseAdapter<ItemBean> {
        public MyAdapter(Context context, List<ItemBean> data) {
            super(context, data, R.layout.recy_item_section);
        }

        @Override
        protected void onBindView(RecyBaseViewHolder holder, ItemBean itemData, int position) {
            holder.setText(R.id.item_text, itemData.text);
        }
    }

    public class ItemBean {
        public int groupId;
        public String text;

        public ItemBean(int groupId, String text) {
            this.groupId = groupId;
            this.text = text;
        }
    }
}
