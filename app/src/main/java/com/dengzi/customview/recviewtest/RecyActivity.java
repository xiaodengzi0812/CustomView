package com.dengzi.customview.recviewtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dengzi.customview.R;
import com.dengzi.recyclerviewlib.adapter.MultiTypeSupport;
import com.dengzi.recyclerviewlib.adapter.OnItemClickListener;
import com.dengzi.recyclerviewlib.decoration.MyItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Djk
 * @Title: RecyclerView 测试
 * @Time: 2017/9/4.
 * @Version:1.0.0
 */
public class RecyActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<String> mDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recy_activity);

        mRecyclerView = (RecyclerView) findViewById(R.id.recy_view);
        initData();
        initView();
    }

    private void initData() {
        for (int i = 'A'; i <= 'Z'; i++) {
            mDataList.add("" + (char) i);
        }
    }

    private void initView() {
        MyAdapter adapter = new MyAdapter(this, mDataList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addItemDecoration(new MyItemDecoration(this, R.drawable.list_divider));

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(RecyActivity.this, "click -> " + mDataList.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void listClick(View view) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(RecyActivity.this));
    }

    public void gridClick(View view) {
        mRecyclerView.setLayoutManager(new GridLayoutManager(RecyActivity.this, 5));
    }

}