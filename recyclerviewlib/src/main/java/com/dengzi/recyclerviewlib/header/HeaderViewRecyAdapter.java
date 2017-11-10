package com.dengzi.recyclerviewlib.header;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.dengzi.recyclerviewlib.adapter.OnItemClickListener;
import com.dengzi.recyclerviewlib.adapter.OnItemLongClickListener;

/**
 * @author Djk
 * @Title: 可以添加头部的recyclerView adapter
 * @Time: 2017/9/20.
 * @Version:1.0.0
 */
public class HeaderViewRecyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // 列表的Adapter
    private RecyclerView.Adapter mAdapter;
    // 用来存放头部View的集合  比Map要高效一些
    private SparseArray<View> mHeaderViewList;
    // 用来存放底部View的集合  比Map要高效一些
    private SparseArray<View> mFooterViewList;
    // 基本的头部类型开始位置  用于viewType
    private static int HEADER_KEY = 10000000;
    private static int FOOTER_KEY = 20000000;

    public HeaderViewRecyAdapter(RecyclerView.Adapter mAdapter) {
        this.mAdapter = mAdapter;
        mHeaderViewList = new SparseArray<>();
        mFooterViewList = new SparseArray<>();
    }

    @Override
    public int getItemViewType(int position) {
        /*返回headview的key做为type*/
        if (isHeaderPosition(position)) {
            return mHeaderViewList.keyAt(position);
        }
        if (isFooterPosition(position)) {
            return mFooterViewList.keyAt(position - mHeaderViewList.size() - mAdapter.getItemCount());
        }
        /*列表adapter的位置要减去头部的个数*/
        position = position - mHeaderViewList.size();
        return mAdapter.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*判断是不是头部*/
        if (isHeaderViewType(viewType)) {
            View headView = mHeaderViewList.get(viewType);
            return createHeaderViewHolder(headView);
        }
        /*判断是不是头部*/
        if (isFooterViewType(viewType)) {
            View footView = mFooterViewList.get(viewType);
            return createHeaderViewHolder(footView);
        }
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    private RecyclerView.ViewHolder createHeaderViewHolder(View headView) {
        return new RecyclerView.ViewHolder(headView) {
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        /*如果是头部，则不需要绑定view，直接返回*/
        if (isHeaderPosition(position) || isFooterPosition(position)) {
            return;
        }

        final int adapterPosition = position - mHeaderViewList.size();
        mAdapter.onBindViewHolder(holder, adapterPosition);
        // 设置点击和长按事件
        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(adapterPosition);
                }
            });
        }
        if (mLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mLongClickListener.onLongClick(adapterPosition);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        /*总个数为原来的个数加上headerview的个数*/
        return mAdapter.getItemCount() + mHeaderViewList.size() + mFooterViewList.size();
    }

    /**
     * 解决GridLayoutManager添加头部和底部不占用一行的问题
     *
     * @param recycler
     * @version 1.0
     */
    public void adjustSpanSize(RecyclerView recycler) {
        if (recycler.getLayoutManager() instanceof GridLayoutManager) {
            final GridLayoutManager layoutManager = (GridLayoutManager) recycler.getLayoutManager();
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    boolean isHeaderOrFooter = isHeaderPosition(position) || isFooterPosition(position);
                    return isHeaderOrFooter ? layoutManager.getSpanCount() : 1;
                }
            });
        }
    }

    /**
     * 添加头部View
     */
    public void addHeaderView(View headerView) {
        int position = mHeaderViewList.indexOfValue(headerView);
        if (position < 0) {
            mHeaderViewList.put(HEADER_KEY++, headerView);
        }
        notifyDataSetChanged();
    }

    /**
     * 移除头部
     */
    public void removeHeaderView(View view) {
        int index = mHeaderViewList.indexOfValue(view);
        if (index < 0) return;
        mHeaderViewList.removeAt(index);
        notifyDataSetChanged();
    }

    /**
     * 是不是头部类型
     */
    private boolean isHeaderViewType(int viewType) {
        int position = mHeaderViewList.indexOfKey(viewType);
        return position >= 0;
    }

    /**
     * 是不是头部位置
     */
    private boolean isHeaderPosition(int position) {
        return position < mHeaderViewList.size();
    }

    /**
     * 添加底部View
     */
    public void addFooterView(View footerView) {
        int position = mFooterViewList.indexOfValue(footerView);
        if (position < 0) {
            mFooterViewList.put(FOOTER_KEY++, footerView);
        }
        notifyDataSetChanged();
    }

    /**
     * 移除底部
     */
    public void removeFooterView(View view) {
        int index = mFooterViewList.indexOfValue(view);
        if (index < 0) return;
        mFooterViewList.removeAt(index);
        notifyDataSetChanged();
    }

    /**
     * 是不是底部类型
     */
    private boolean isFooterViewType(int viewType) {
        int position = mFooterViewList.indexOfKey(viewType);
        return position >= 0;
    }

    /**
     * 是不是底部位置
     */
    private boolean isFooterPosition(int position) {
        return position >= mHeaderViewList.size() + mAdapter.getItemCount();
    }

    /**
     * 给条目设置点击和长按事件
     **/
    public OnItemClickListener mItemClickListener;
    public OnItemLongClickListener mLongClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public void setOnLongClickListener(OnItemLongClickListener longClickListener) {
        this.mLongClickListener = longClickListener;
    }

}
