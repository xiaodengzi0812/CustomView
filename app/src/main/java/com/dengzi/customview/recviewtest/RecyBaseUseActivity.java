package com.dengzi.customview.recviewtest;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.dengzi.customview.R;
import com.dengzi.recyclerviewlib.adapter.MultiTypeSupport;
import com.dengzi.recyclerviewlib.adapter.OnItemClickListener;
import com.dengzi.recyclerviewlib.adapter.RecyBaseAdapter;
import com.dengzi.recyclerviewlib.adapter.RecyBaseViewHolder;
import com.dengzi.recyclerviewlib.decoration.DefaultItemDecoration;
import com.dengzi.recyclerviewlib.header.HeaderRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Djk
 * @Title: RecyclerView 测试
 * @Time: 2017/9/4.
 * @Version:1.0.0
 */
public class RecyBaseUseActivity extends AppCompatActivity {
    private HeaderRecyclerView mRecyclerView;
    private List<String> mDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recy_activity);
        initData();
        initView();
    }

    private void initData() {
        for (int i = 'A'; i <= 'Z'; i++) {
            mDataList.add("" + (char) i);
        }
    }

    private void initView() {
        mRecyclerView = (HeaderRecyclerView) findViewById(R.id.recy_view);
        MyAdapter adapter = new MyAdapter(this, mDataList);
        mRecyclerView.setLayoutManager(new GridLayoutManager(RecyBaseUseActivity.this, 5));
        mRecyclerView.setAdapter(adapter);

        View headerView = LayoutInflater.from(this).inflate(R.layout.recy_header, mRecyclerView, false);
        mRecyclerView.addHeaderView(headerView);

        /*添加分格线*/
        mRecyclerView.addItemDecoration(new DefaultItemDecoration(this, R.drawable.list_divider, 1));
        /*点击事件*/
        mRecyclerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(RecyBaseUseActivity.this, "click -> " + mDataList.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void listClick(View view) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(RecyBaseUseActivity.this));
    }

    public void gridClick(View view) {
        mRecyclerView.setLayoutManager(new GridLayoutManager(RecyBaseUseActivity.this, 5));
    }

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

}
