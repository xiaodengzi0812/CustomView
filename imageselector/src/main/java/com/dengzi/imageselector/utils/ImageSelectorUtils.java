package com.dengzi.imageselector.utils;

import android.app.Activity;
import android.content.Intent;

import com.dengzi.imageselector.activity.ImageSelectorActivity;

import java.util.ArrayList;

/**
 * @Title: 提供给外界相册的调用的工具类
 * @Author: djk
 * @Time: 2017/10/25
 * @Version:1.0.0
 */
public class ImageSelectorUtils {
    // 请求码
    public static final int REQUEST_CODE = 0x0021;
    // 回传码
    public static final int RESULT_CODE = 0x0022;
    // 已选择的图片集合
    public static final String SELECT_IMAGES = "SELECT_IMAGES";
    // 是否单选
    public static final String IS_SINGLE = "IS_SINGLE";
    // 最大的图片选择数
    public static final String MAX_SELECT_COUNT = "MAX_SELECT_COUNT";
    // 是否显示相机
    public final static String IS_SHOW_CAMERA = "IS_SHOW_CAMERA";
    // 拍照code
    public final static int TAKE_PICTURE_FROM_CAMERA = 0x0023;

    private Activity mActivity;
    // 是否显示相机
    private boolean mIsShowCamera = false;
    // 是否为单选
    private boolean mIsSingle = false;
    // 最大的图片选择数
    private int mMaxCount = 0;
    // 已选择图片集合
    private ArrayList<String> mSelectImages;

    public ImageSelectorUtils(Activity activity) {
        this.mActivity = activity;
    }

    /**
     * 创建
     *
     * @return
     */
    public static ImageSelectorUtils with(Activity activity) {
        return new ImageSelectorUtils(activity);
    }

    /**
     * 单选
     */
    public ImageSelectorUtils singleSelect() {
        mIsSingle = true;
        return this;
    }

    /**
     * 单选
     */
    public ImageSelectorUtils showCamera() {
        mIsShowCamera = true;
        return this;
    }

    /**
     * 多选
     */
    public ImageSelectorUtils multiSelect() {
        mIsSingle = false;
        return this;
    }

    /**
     * 多选最大数
     */
    public ImageSelectorUtils maxCout(int maxCount) {
        mMaxCount = maxCount;
        return this;
    }

    /**
     * 已选择的图片集合
     */
    public ImageSelectorUtils hasSelectImages(ArrayList<String> selectImages) {
        mSelectImages = selectImages;
        return this;
    }

    /**
     * 开始跳转
     */
    public void start(int requestCode) {
        Intent intent = new Intent(mActivity, ImageSelectorActivity.class);
        intent.putExtra(MAX_SELECT_COUNT, mMaxCount);
        intent.putExtra(IS_SINGLE, mIsSingle);
        intent.putExtra(IS_SHOW_CAMERA, mIsShowCamera);
        intent.putStringArrayListExtra(SELECT_IMAGES, mSelectImages);
        mActivity.startActivityForResult(intent, requestCode);
        mActivity = null;
    }

//    /**
//     * 打开相册，单选图片并剪裁。
//     *
//     * @param activity
//     * @param requestCode
//     */
//    public static void openPhotoAndClip(Activity activity, int requestCode) {
//        ClipImageActivity.openActivity(activity, requestCode);
//    }
}
