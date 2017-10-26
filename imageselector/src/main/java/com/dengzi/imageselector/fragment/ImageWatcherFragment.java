package com.dengzi.imageselector.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.dengzi.imageselector.R;
import com.dengzi.imageselector.photoview.PhotoView;

/**
 * @Title: 图片预览
 * @Author: djk
 * @Time: 2017/10/25
 * @Version:1.0.0
 */
public class ImageWatcherFragment extends Fragment {
    private static final String PATH = "PHOTO_PATH";
    private PhotoView mPhotoIV;
    private String mImagePath;// 图片url

    public static ImageWatcherFragment getInstance(String path) {
        ImageWatcherFragment imageFragment = new ImageWatcherFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PATH, path);
        imageFragment.setArguments(bundle);
        return imageFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = View.inflate(getActivity(), R.layout.fragment_image_watcher, null);
        initData();
        initView(rootView);
        return rootView;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mImagePath = bundle.getString(PATH);
        }
    }

    /**
     * 初始化view
     *
     * @param view
     */
    private void initView(View view) {
        mPhotoIV = (PhotoView) view.findViewById(R.id.photo_iv);
        Glide.with(this).load(mImagePath).into(mPhotoIV);
    }


}
