package com.dengzi.recyclerviewlib.header;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.dengzi.recyclerviewlib.adapter.OnItemClickListener;
import com.dengzi.recyclerviewlib.adapter.OnItemLongClickListener;

/**
 * @author Djk
 * @Title: 可以添加头部的recyclerview
 * @Time: 2017/9/21.
 * @Version:1.0.0
 */
public class HeaderRecyclerView extends RecyclerView {
    // 包裹了一层的头部底部Adapter
    private HeaderViewRecyAdapter mHeaderRecyclerAdapter;
    // 这个是列表数据的Adapter
    private RecyclerView.Adapter mAdapter;

    public HeaderRecyclerView(Context context) {
        super(context);
    }

    public HeaderRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        // 为了防止多次设置Adapter
        if (mAdapter != null) {
            mAdapter.unregisterAdapterDataObserver(mDataObserver);
            mAdapter = null;
        }

        this.mAdapter = adapter;

        if (adapter instanceof HeaderViewRecyAdapter) {
            mHeaderRecyclerAdapter = (HeaderViewRecyAdapter) adapter;
        } else {
            mHeaderRecyclerAdapter = new HeaderViewRecyAdapter(adapter);
        }

        super.setAdapter(mHeaderRecyclerAdapter);

        // 注册一个观察者
        mAdapter.registerAdapterDataObserver(mDataObserver);

        // 解决GridLayout添加头部也要占据一行
        mHeaderRecyclerAdapter.adjustSpanSize(this);

        if (mItemClickListener != null) {
            mHeaderRecyclerAdapter.setOnItemClickListener(mItemClickListener);
        }

        if (mLongClickListener != null) {
            mHeaderRecyclerAdapter.setOnLongClickListener(mLongClickListener);
        }
    }

    // 添加头部
    public void addHeaderView(View view) {
        // 如果没有Adapter那么就不添加，也可以选择抛异常提示
        // 让他必须先设置Adapter然后才能添加，这里是仿照ListView的处理方式
        if (mHeaderRecyclerAdapter != null) {
            mHeaderRecyclerAdapter.addHeaderView(view);
        }
    }

    // 移除头部
    public void removeHeaderView(View view) {
        if (mHeaderRecyclerAdapter != null) {
            mHeaderRecyclerAdapter.removeHeaderView(view);
        }
    }

    // 添加底部
    public void addFooterView(View view) {
        if (mHeaderRecyclerAdapter != null) {
            mHeaderRecyclerAdapter.addFooterView(view);
        }
    }

    // 移除底部
    public void removeFooterView(View view) {
        if (mHeaderRecyclerAdapter != null) {
            mHeaderRecyclerAdapter.removeFooterView(view);
        }
    }

    /*为了保证两个adapter的数据的一致性，搞一个观察者来时时同步数据*/
    private AdapterDataObserver mDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyDataSetChanged没效果
            if (mHeaderRecyclerAdapter != mAdapter)
                mHeaderRecyclerAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyDataSetChanged没效果
            if (mHeaderRecyclerAdapter != mAdapter)
                mHeaderRecyclerAdapter.notifyItemRemoved(positionStart);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemMoved没效果
            if (mHeaderRecyclerAdapter != mAdapter)
                mHeaderRecyclerAdapter.notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemChanged没效果
            if (mHeaderRecyclerAdapter != mAdapter)
                mHeaderRecyclerAdapter.notifyItemChanged(positionStart);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemChanged没效果
            if (mHeaderRecyclerAdapter != mAdapter)
                mHeaderRecyclerAdapter.notifyItemChanged(positionStart, payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemInserted没效果
            if (mHeaderRecyclerAdapter != mAdapter)
                mHeaderRecyclerAdapter.notifyItemInserted(positionStart);
        }
    };

    /**
     * 给条目设置点击和长按事件
     **/
    public OnItemClickListener mItemClickListener;
    public OnItemLongClickListener mLongClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;

        if (mHeaderRecyclerAdapter != null) {
            mHeaderRecyclerAdapter.setOnItemClickListener(mItemClickListener);
        }
    }

    public void setOnLongClickListener(OnItemLongClickListener longClickListener) {
        this.mLongClickListener = longClickListener;

        if (mHeaderRecyclerAdapter != null) {
            mHeaderRecyclerAdapter.setOnLongClickListener(mLongClickListener);
        }
    }

}
