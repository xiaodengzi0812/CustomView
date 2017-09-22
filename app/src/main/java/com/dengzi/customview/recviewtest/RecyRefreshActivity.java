package com.dengzi.customview.recviewtest;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dengzi.customview.R;
import com.dengzi.recyclerviewlib.adapter.RecyBaseAdapter;
import com.dengzi.recyclerviewlib.adapter.RecyBaseViewHolder;
import com.dengzi.recyclerviewlib.decoration.DefaultItemDecoration;
import com.dengzi.recyclerviewlib.refresh.DefaultRefreshCreator;
import com.dengzi.recyclerviewlib.refresh.RefreshCreator;
import com.dengzi.recyclerviewlib.refresh.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Djk
 * @Title: 下拉刷新
 * @Time: 2017/9/4.
 * @Version:1.0.0
 */
public class RecyRefreshActivity extends AppCompatActivity {
    private RefreshRecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private List<ItemBean> mItems = new ArrayList<ItemBean>();

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recy_refresh_activity);
        initData();
        initView();
        initRefresh();
    }

    private void initRefresh() {
        mRecyclerView.addRefreshCreator(new DefaultRefreshCreator());
        mRecyclerView.setOnRefreshListener(new RefreshRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.stopRefresh();
                    }
                }, 2000);
            }
        });

        View headerView = LayoutInflater.from(this).inflate(R.layout.recy_header, mRecyclerView, false);
        mRecyclerView.addHeaderView(headerView);
    }

    private void initView() {
        mRecyclerView = (RefreshRecyclerView) findViewById(R.id.recy_view);
        mAdapter = new MyAdapter(this, mItems);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        /*添加分格线*/
        mRecyclerView.addItemDecoration(new DefaultItemDecoration(this, 2));
        /*默认动画*/
        final DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        mRecyclerView.setItemAnimator(itemAnimator);
    }

    protected void initData() {
        for (int i = 0; i < 10; i++) {
            mItems.add(new ItemBean(i * 8 + 0, "收款", R.drawable.takeout_ic_category_brand));
            mItems.add(new ItemBean(i * 8 + 1, "转账", R.drawable.takeout_ic_category_flower));
            mItems.add(new ItemBean(i * 8 + 2, "余额宝", R.drawable.takeout_ic_category_fruit));
            mItems.add(new ItemBean(i * 8 + 3, "手机充值", R.drawable.takeout_ic_category_medicine));
            mItems.add(new ItemBean(i * 8 + 4, "医疗", R.drawable.takeout_ic_category_motorcycle));
            mItems.add(new ItemBean(i * 8 + 5, "彩票", R.drawable.takeout_ic_category_public));
            mItems.add(new ItemBean(i * 8 + 6, "电影", R.drawable.takeout_ic_category_store));
            mItems.add(new ItemBean(i * 8 + 7, "游戏", R.drawable.takeout_ic_category_sweet));
        }
        mItems.add(new ItemBean(mItems.size(), "更多", R.drawable.takeout_ic_more));
    }

    class MyAdapter extends RecyBaseAdapter<ItemBean> {

        public MyAdapter(Context context, List<ItemBean> data) {
            super(context, data, R.layout.recy_item_drag_sort_delete);
        }

        @Override
        protected void onBindView(RecyBaseViewHolder holder, ItemBean itemData, int position) {
            holder.setText(R.id.item_text, itemData.text);
            holder.setImageResource(R.id.item_img, itemData.icon);
        }
    }

    public class ItemBean {
        public int id;
        public String text;
        public int icon;

        public ItemBean(int id, String text, int icon) {
            this.id = id;
            this.text = text;
            this.icon = icon;
        }
    }
}
