package com.dengzi.imageselector.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.dengzi.imageselector.R;
import com.dengzi.imageselector.utils.ImageSelectorUtils;
import com.dengzi.imageselector.utils.ImageUtil;
import com.dengzi.imageselector.view.ClipImageView;

import java.io.File;
import java.util.ArrayList;

public class ClipImageActivity extends Activity {

    private FrameLayout btnConfirm;
    private FrameLayout btnBack;
    private ClipImageView imageView;
    private ArrayList<String> mImageList = new ArrayList<>();// 图片url集

    public static void openActivity(Activity activity, ArrayList<String> images) {
        Intent intent = new Intent(activity, ClipImageActivity.class);
        intent.putStringArrayListExtra(ImageSelectorUtils.SELECT_IMAGES, images);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_clip_image);
        initView();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mImageList = getIntent().getStringArrayListExtra(ImageSelectorUtils.SELECT_IMAGES);
        if (mImageList == null || mImageList.size() == 0) return;
        Bitmap bitmap = ImageUtil.decodeSampledBitmapFromFile(mImageList.get(0), 720, 1080);
        if (bitmap != null) {
            imageView.setBitmapData(bitmap);
        } else {
            finish();
        }
    }

    private void initView() {
        imageView = (ClipImageView) findViewById(R.id.process_img);
        btnConfirm = (FrameLayout) findViewById(R.id.btn_confirm);
        btnBack = (FrameLayout) findViewById(R.id.btn_back);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageView.getDrawable() != null) {
                    btnConfirm.setEnabled(false);
                    confirm(imageView.clipImage());
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void confirm(Bitmap bitmap) {
        String imagePath = null;
        if (bitmap != null) {
            imagePath = ImageUtil.saveImage(bitmap, getCacheDir().getPath() + File.separator + "image_select");
            bitmap.recycle();
            bitmap = null;
        }

        if (!TextUtils.isEmpty(imagePath)) {
            ArrayList<String> selectImages = new ArrayList<>();
            selectImages.add(imagePath);
            Intent intent = new Intent();
            intent.putStringArrayListExtra(ImageSelectorUtils.SELECT_IMAGES, selectImages);
            setResult(RESULT_OK, intent);
        }
        finish();
    }
}
