package com.dengzi.imageselector.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.dengzi.imageselector.R;
import com.dengzi.imageselector.adapter.ImageWatcherFragmentPagerAdapter;
import com.dengzi.imageselector.utils.ImageSelectorUtils;
import com.dengzi.imageselector.view.MyViewPager;

import java.util.ArrayList;

/**
 * @Title: 图片预览
 * @Author: djk
 * @Time: 2017/10/25
 * @Version:1.0.0
 */
public class PreviewActivity extends AppCompatActivity {
    private MyViewPager mPreviewPager;
    private ArrayList<String> mImageList = new ArrayList<>();// 图片url集
    private ImageWatcherFragmentPagerAdapter mPagerAdapter;

    public static void openActivity(Activity activity, ArrayList<String> images) {
        Intent intent = new Intent(activity, PreviewActivity.class);
        intent.putStringArrayListExtra(ImageSelectorUtils.SELECT_IMAGES, images);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        initData();
        initPageView();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mImageList = getIntent().getStringArrayListExtra(ImageSelectorUtils.SELECT_IMAGES);
        if (mImageList == null || mImageList.size() == 0) return;
    }

    /**
     * 初始化pagerView的子view
     */
    private void initPageView() {
        mPreviewPager = (MyViewPager) findViewById(R.id.image_watcher_pager);
        if (mPagerAdapter == null) {
            mPagerAdapter = new ImageWatcherFragmentPagerAdapter(getSupportFragmentManager(), mImageList);
            mPreviewPager.setAdapter(mPagerAdapter);
        } else {
            mPagerAdapter.notifyDataSetChanged();
        }
    }

}
