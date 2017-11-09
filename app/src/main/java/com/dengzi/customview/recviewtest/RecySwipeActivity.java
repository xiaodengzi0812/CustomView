package com.dengzi.customview.recviewtest;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.dengzi.customview.R;
import com.dengzi.recyclerviewlib.adapter.RecyBaseAdapter;
import com.dengzi.recyclerviewlib.adapter.RecyBaseViewHolder;
import com.dengzi.recyclerviewlib.swipe.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Djk
 * @Title: 侧滑删除
 * @Time: 2017/9/4.
 * @Version:1.0.0
 */
public class RecySwipeActivity extends AppCompatActivity {
    private SwipeRecyclerView mSwipRv;
    private MyAdapter mAdapter;
    private List<String> mDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recy_swipe_activity);
        mSwipRv = (SwipeRecyclerView) findViewById(R.id.swip_rv);
        mSwipRv.setLayoutManager(new LinearLayoutManager(this));
        initData();
    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            mDataList.add("数据->" + i);
        }
        mAdapter = new MyAdapter(this, mDataList);
        mSwipRv.setAdapter(mAdapter);
    }

    class MyAdapter extends RecyBaseAdapter<String> {
        public MyAdapter(Context context, List<String> data) {
            super(context, data, R.layout.recy_swipe_item_view);
        }

        @Override
        protected void onBindView(final RecyBaseViewHolder holder, String itemData, final int position) {
            holder.setText(R.id.name_tv, itemData);
            holder.itemView.findViewById(R.id.delete_tv).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "删除->" + mDataList.get(position), Toast.LENGTH_SHORT).show();
                    mDataList.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                    notifyItemRangeChanged(0, mDataList.size());
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "点击->" + mDataList.get(position), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
