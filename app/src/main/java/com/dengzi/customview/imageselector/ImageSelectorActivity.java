package com.dengzi.customview.imageselector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.dengzi.customview.R;
import com.dengzi.imageselector.utils.ImageSelectorUtils;
import com.dengzi.itemview.TopView;

import java.util.ArrayList;

/**
 * @author Djk
 * @Title: 图片选择器
 * @Time: 2017/9/4.
 * @Version:1.0.0
 */
public class ImageSelectorActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView rvImage;
    private ImageAdapter mAdapter;
    private ArrayList<String> mImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_selector_activity);

        rvImage = (RecyclerView) findViewById(R.id.rv_image);
        rvImage.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new ImageAdapter(this);
        rvImage.setAdapter(mAdapter);

        findViewById(R.id.btn_single).setOnClickListener(this);
        findViewById(R.id.btn_limit).setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImageSelectorUtils.REQUEST_CODE && resultCode == ImageSelectorUtils.RESULT_CODE && data != null) {
            mImages = data.getStringArrayListExtra(ImageSelectorUtils.SELECT_IMAGES);
            mAdapter.refresh(mImages);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_single:
                //单选
                ImageSelectorUtils.with(this).singleSelect()
                        .showCamera()
                        .hasSelectImages(mImages)
                        .start(ImageSelectorUtils.REQUEST_CODE);
                break;
            case R.id.btn_limit:
                //多选(最多9张)
                ImageSelectorUtils.with(this).maxCout(6)
                        .hasSelectImages(mImages)
                        .multiSelect().start(ImageSelectorUtils.REQUEST_CODE);
                break;
        }
    }
}
