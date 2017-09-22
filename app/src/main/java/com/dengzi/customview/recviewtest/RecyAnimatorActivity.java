package com.dengzi.customview.recviewtest;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.dengzi.customview.R;
import com.dengzi.recyclerviewlib.adapter.RecyBaseAdapter;
import com.dengzi.recyclerviewlib.adapter.RecyBaseViewHolder;
import com.dengzi.recyclerviewlib.decoration.DefaultItemDecoration;
import com.dengzi.recyclerviewlib.header.HeaderRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Djk
 * @Title: 列表条目拖动排序和删除
 * @Time: 2017/9/4.
 * @Version:1.0.0
 */
public class RecyAnimatorActivity extends AppCompatActivity {
    private HeaderRecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private List<ItemBean> mItems = new ArrayList<ItemBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recy_activity);
        initData();
        initView();
        initListener();
    }

    private void initListener() {
        // 实现左边侧滑删除 和 拖动排序
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                //侧滑删除swipeFlags,左右方向都可以侧滑删除
                int swipeFlags = 0;
                // 拖动dragFlags
                int dragFlags = 0;

                if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    // GridView 样式四个方向都可以拖动
                    dragFlags = ItemTouchHelper.UP | ItemTouchHelper.LEFT |
                            ItemTouchHelper.DOWN | ItemTouchHelper.RIGHT;
                    // GridView 样式不支持侧滑删除
                    swipeFlags = 0;
                } else {
                    // ListView 样式不支持左右拖动
                    dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                    // ListView 样式支持左右侧滑删除
                    swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                }
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            /**
             * 拖动的时候不断的回调方法
             */
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // 获取原来的位置
                int fromPosition = viewHolder.getAdapterPosition();
                // 目标的位置
                int targetPosition = target.getAdapterPosition();
                /*交换两个位置的数据*/
                if (fromPosition > targetPosition) {
                    for (int i = fromPosition; i < targetPosition; i++) {
                        Collections.swap(mItems, i, i + 1);// 改变实际的数据集
                    }
                } else {
                    for (int i = fromPosition; i > targetPosition; i--) {
                        Collections.swap(mItems, i, i - 1);// 改变实际的数据集
                    }
                }
                // 交换两个位置显示
                mAdapter.notifyItemMoved(fromPosition, targetPosition);
                return true;
            }

            /**
             * 侧滑删除后会回调的方法
             */
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // 获取当前删除的位置
                int position = viewHolder.getAdapterPosition();
                mItems.remove(position);
                // adapter 更新notify当前位置删除
                mAdapter.notifyItemRemoved(position);
            }

            /**
             * 拖动选择状态改变回调
             */
            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    // ItemTouchHelper.ACTION_STATE_IDLE ：ItemTouchHelper is in idle state. At this state, either there is no related motion event by
                    // the user or latest motion events have not yet triggered a swipe or drag.
                    // 侧滑或者拖动的时候背景设置为灰色
                    viewHolder.itemView.setBackgroundColor(Color.GRAY);
                }
            }


            /**
             * 回到正常状态的时候回调
             */
            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                // 正常默认状态下背景恢复默认
                viewHolder.itemView.setBackgroundColor(0);
                // 解决侧滑删除后view的复用导致下面的view空白的bug
                // 原理就是侧滑之后view被移动到了左右看不到的位置，我们下面的view复用这个view就会导致下面的view看不见，
                // 现在我们再把它移回来，下面的view就能正常显示了
                ViewCompat.setTranslationX(viewHolder.itemView, 0);
            }
        });
        // 这个就不多解释了，就这么attach
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void initView() {
        mRecyclerView = (HeaderRecyclerView) findViewById(R.id.recy_view);
        mAdapter = new MyAdapter(this, mItems);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mRecyclerView.setAdapter(mAdapter);
        /*添加分格线*/
        mRecyclerView.addItemDecoration(new DefaultItemDecoration(this));
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

    public void listClick(View view) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(RecyAnimatorActivity.this));
    }

    public void gridClick(View view) {
        mRecyclerView.setLayoutManager(new GridLayoutManager(RecyAnimatorActivity.this, 4));
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
