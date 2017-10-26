package com.dengzi.imageselector.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dengzi.imageselector.R;
import com.dengzi.imageselector.bean.Image;

import java.util.ArrayList;

/**
 * @Title: 图片列表Adapter
 * @Author: DJK
 * @Time: 2017/10/25
 * @Version:1.0.0
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private Context mContext;
    // 最大选择数
    private int mMaxCount;
    // 是否单选
    private boolean isSingle;
    // 图片列表集合
    private ArrayList<Image> mImages = new ArrayList<>();
    //保存选中的图片
    private ArrayList<Image> mSelectImages = new ArrayList<>();
    // 图片选择监听事件
    private OnImageSelectListener mSelectListener;
    // 图片点击监听事件
    private OnItemClickListener mItemClickListener;
    // 是否显示相机
    private boolean mIsShowCamera;

    /**
     * @param maxCount 图片的最大选择数量，小于等于0时，不限数量，isSingle为false时才有用。
     * @param isSingle 是否单选
     */
    public ImageAdapter(Context context, int maxCount, boolean isSingle, boolean isShowCamera) {
        this.mContext = context;
        this.mMaxCount = maxCount;
        this.isSingle = isSingle;
        this.mIsShowCamera = isShowCamera;
    }

    @Override
    public int getItemCount() {
        return mImages == null ? 0 : mImages.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_images, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Image image = mImages.get(position);
        // 如果要拍照，则显示拍照按钮
        if (mIsShowCamera && position == 0) {
            holder.imageIv.setImageResource(R.drawable.ic_camera);
            holder.maskingIv.setVisibility(View.GONE);
            holder.selectCtv.setVisibility(View.GONE);
        } else {
            holder.maskingIv.setVisibility(View.VISIBLE);
            holder.selectCtv.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(image.getPath())
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                    .into(holder.imageIv);
            setItemSelect(holder, mSelectImages.contains(image));
            //点击选中/取消选中图片
            holder.selectCtv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSelectImages.contains(image)) {
                        //如果图片已经选中，就取消选中
                        unSelectImage(image);
                    } else if (isSingle) {
                        //如果是单选，就先清空已经选中的图片，再选中当前图片
                        mSelectImages.clear();
                        selectImage(image);
                    } else if (mMaxCount <= 0 || mSelectImages.size() < mMaxCount) {
                        //如果不限制图片的选中数量，或者图片的选中数量
                        // 还没有达到最大限制，就直接选中当前图片。
                        selectImage(image);
                    }
                    notifyDataSetChanged();
                }
            });
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.OnItemClick(image, holder.getAdapterPosition());
                }
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageIv;
        View maskingIv;
        CheckedTextView selectCtv;

        public ViewHolder(View itemView) {
            super(itemView);
            imageIv = (ImageView) itemView.findViewById(R.id.image_iv);
            maskingIv = itemView.findViewById(R.id.masking_iv);
            selectCtv = (CheckedTextView) itemView.findViewById(R.id.select_ctv);
        }
    }

    /**
     * 设置已选择的图片
     *
     * @param selectImages
     */
    public void setSelectImages(ArrayList<Image> selectImages) {
        mSelectImages = selectImages;
    }

    /**
     * 选中图片
     *
     * @param image
     */
    private void selectImage(Image image) {
        mSelectImages.add(image);
        if (mSelectListener != null) {
            mSelectListener.OnImageSelect(image, true, mSelectImages.size());
        }
    }

    /**
     * 取消选中图片
     *
     * @param image
     */
    private void unSelectImage(Image image) {
        mSelectImages.remove(image);
        if (mSelectListener != null) {
            mSelectListener.OnImageSelect(image, false, mSelectImages.size());
        }
    }

    /**
     * 刷新数据
     *
     * @param data
     */
    public void refreshData(ArrayList<Image> data) {
        mImages = data;
        notifyDataSetChanged();
    }

    /**
     * 设置图片选中和未选中的效果
     */
    private void setItemSelect(ViewHolder holder, boolean isSelect) {
        holder.selectCtv.setChecked(isSelect);
        if (isSelect) {
            holder.maskingIv.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorSelectImage));
        } else {
            holder.maskingIv.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorUnSelectImage));
        }
    }

    /**
     * 获取已选中的图片
     *
     * @return
     */
    public ArrayList<Image> getSelectImages() {
        return mSelectImages;
    }

    /**
     * 设置图片已选中的回调事件
     *
     * @param listener
     */
    public void setOnImageSelectListener(OnImageSelectListener listener) {
        this.mSelectListener = listener;
    }

    /**
     * 设置item点击事件
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    /**
     * 图片已选中的回调
     */
    public interface OnImageSelectListener {
        void OnImageSelect(Image image, boolean isSelect, int selectCount);
    }

    /**
     * item点击事件回调
     */
    public interface OnItemClickListener {
        void OnItemClick(Image image, int position);
    }
}
