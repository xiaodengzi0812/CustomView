package com.dengzi.imageselector.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dengzi.imageselector.R;
import com.dengzi.imageselector.bean.Folder;
import com.dengzi.imageselector.bean.Image;

import java.io.File;
import java.util.ArrayList;

/**
 * @Title: 文件夹列表Adapter
 * @Author: DJK
 * @Time: 2017/10/25
 * @Version:1.0.0
 */
public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {
    private Context mContext;
    // 文件夹列表
    private ArrayList<Folder> mFolders = new ArrayList<>();
    // 已选中的文件夹item
    private int mSelectItem;
    // 文件夹选中回调
    private OnFolderSelectListener mListener;

    public FolderAdapter(Context context, ArrayList<Folder> folders) {
        this.mContext = context;
        this.mFolders = folders;
    }

    @Override
    public int getItemCount() {
        return mFolders == null ? 0 : mFolders.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_folder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Folder folder = mFolders.get(position);
        ArrayList<Image> images = folder.getImages();
        holder.folderNameTv.setText(folder.getName());
        holder.selectIv.setVisibility(mSelectItem == position ? View.VISIBLE : View.GONE);
        if (images != null && !images.isEmpty()) {
            holder.imageSizeTv.setText(images.size() + "张");
            Glide.with(mContext).load(new File(images.get(0).getPath()))
                    .into(holder.imageIv);
        } else {
            holder.imageSizeTv.setText("0张");
            holder.imageIv.setImageBitmap(null);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectItem = holder.getAdapterPosition();
                notifyDataSetChanged();
                if (mListener != null) {
                    mListener.OnFolderSelect(folder);
                }
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageIv;
        ImageView selectIv;
        TextView folderNameTv;
        TextView imageSizeTv;

        public ViewHolder(View itemView) {
            super(itemView);
            imageIv = (ImageView) itemView.findViewById(R.id.image_iv);
            selectIv = (ImageView) itemView.findViewById(R.id.select_iv);
            folderNameTv = (TextView) itemView.findViewById(R.id.folder_name_tv);
            imageSizeTv = (TextView) itemView.findViewById(R.id.image_size_tv);
        }
    }

    /**
     * 设置文件夹选择回调
     *
     * @param listener
     */
    public void setOnFolderSelectListener(OnFolderSelectListener listener) {
        this.mListener = listener;
    }

    /**
     * 文件夹选择回调事件
     */
    public interface OnFolderSelectListener {
        void OnFolderSelect(Folder folder);
    }

}
