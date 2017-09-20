package com.dengzi.recyclerviewlib.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author Djk
 * @Title: 通用adapter
 * @Time: 2017/9/20.
 * @Version:1.0.0
 */
public abstract class RecyBaseAdapter<T> extends RecyclerView.Adapter<RecyBaseViewHolder> {
    /*布局资源id*/
    protected int mLayoutId;
    /*数据集*/
    protected List<T> mDataList;
    protected Context mContext;
    protected LayoutInflater mLayoutInflater;
    /*多布局支持*/
    protected MultiTypeSupport mMultiTypeSupport;

    public RecyBaseAdapter(Context context, List<T> dataList, int layoutId) {
        this.mDataList = dataList;
        this.mContext = context;
        this.mLayoutId = layoutId;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    /**
     * 多布局支持
     */
    public RecyBaseAdapter(Context context, List<T> dataList, MultiTypeSupport<T> multiTypeSupport) {
        this(context, dataList, -1);
        this.mMultiTypeSupport = multiTypeSupport;
    }

    /**
     * 根据当前位置获取不同的viewType
     */
    @Override
    public int getItemViewType(int position) {
        // 多布局支持
        if (mMultiTypeSupport != null) {
            return mMultiTypeSupport.getLayoutId(mDataList.get(position), position);
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 多布局支持
        if (mMultiTypeSupport != null) {
            mLayoutId = viewType;
        }
        View itemView = mLayoutInflater.inflate(mLayoutId, parent, false);
        return new RecyBaseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyBaseViewHolder holder, int position) {
        onBindView(holder, mDataList.get(position), position);
        // 设置点击和长按事件
        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(holder.getAdapterPosition());
                }
            });
        }
        if (mLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mLongClickListener.onLongClick(holder.getAdapterPosition());
                }
            });
        }
    }

    /*设置view的数据*/
    protected abstract void onBindView(RecyBaseViewHolder holder, T itemData, int position);

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * 给条目设置点击和长按事件
     **/
    public OnItemClickListener mItemClickListener;
    public OnLongClickListener mLongClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public void setOnLongClickListener(OnLongClickListener longClickListener) {
        this.mLongClickListener = longClickListener;
    }
}
