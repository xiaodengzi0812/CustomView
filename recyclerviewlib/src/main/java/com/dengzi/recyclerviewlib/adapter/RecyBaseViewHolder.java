package com.dengzi.recyclerviewlib.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Djk
 * @Title: 通用的viewholder
 * @Time: 2017/9/20.
 * @Version:1.0.0
 */
public class RecyBaseViewHolder extends RecyclerView.ViewHolder {
    /*itemView的缓存*/
    private SparseArray<View> sparseArray;

    public RecyBaseViewHolder(View itemView) {
        super(itemView);
        sparseArray = new SparseArray();
    }

    /*获取具体的view*/
    public <V extends View> V getView(int viewId) {
        View view = sparseArray.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            sparseArray.put(viewId, view);
        }
        return (V) view;
    }

    /*设置文本*/
    public RecyBaseViewHolder setText(int viewId, CharSequence text) {
        TextView textView = getView(viewId);
        textView.setText(text);
        return this;
    }

    /*设置本地资源*/
    public RecyBaseViewHolder setImageResource(int viewId, int resourceId) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(resourceId);
        return this;
    }

    /*设置网络图片*/
    public RecyBaseViewHolder setImageUrl(int viewId, int imageUrl) {
        ImageView imageView = getView(viewId);
        // TODO 第三方加载网络图片
        return this;
    }

}
