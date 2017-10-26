package com.dengzi.imageselector.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dengzi.imageselector.R;
import com.dengzi.imageselector.adapter.FolderAdapter;
import com.dengzi.imageselector.adapter.ImageAdapter;
import com.dengzi.imageselector.bean.Folder;
import com.dengzi.imageselector.bean.Image;
import com.dengzi.imageselector.utils.CameraUtil;
import com.dengzi.imageselector.utils.ImageModel;
import com.dengzi.imageselector.utils.ImageSelectorUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * @Title: 图片选择Activity
 * @Author: djk
 * @Time: 2017/10/25
 * @Version:1.0.0
 */
public class ImageSelectorActivity extends AppCompatActivity implements View.OnClickListener {
    // 文件夹名
    private TextView mFolderNameTv;
    // 确定布局，用来点击
    private FrameLayout mConfirmLayout;
    // 确定按钮
    private TextView mConfirmTv;
    // 预览布局，用来点击
    private FrameLayout mPreviewLayout;
    // 预览按钮
    private TextView mPreviewTv;
    // 展示图片列表的RecyclerView
    private RecyclerView mImageRecycler;
    // 展示文件夹列表的RecyclerView
    private RecyclerView mFolderRecycler;
    // 展示文件夹列表时的遮罩
    private View mMaskingView;
    // 展示图片列表的Adapter
    private ImageAdapter mImageAdapter;
    private GridLayoutManager mLayoutManager;
    // 文件夹列表
    private ArrayList<Folder> mFolderList;
    // 当前选中的文件夹
    private Folder mCurrentFolder;
    // 是否已打开文件夹菜单
    private boolean mIsOpenFolder;
    // 是否为单选
    private boolean mIsSingle;
    // 已选择的图片集合
    private ArrayList<String> mSelectImages;
    // 最大选择张数
    private int mMaxCount;
    // 是否显示相机
    private boolean mIsShowCamera;
    // 相机工具类
    private CameraUtil mCameraUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);
        initView();
        initData();
        initImageRecyclerView();
        initListener();
        checkPermissionAndLoadImages();
    }

    /**
     * 初始化view
     */
    private void initView() {
        mImageRecycler = (RecyclerView) findViewById(R.id.image_recycler);
        mConfirmTv = (TextView) findViewById(R.id.confirm_tv);
        mPreviewTv = (TextView) findViewById(R.id.preview_tv);
        mConfirmLayout = (FrameLayout) findViewById(R.id.confirm_layout);
        mPreviewLayout = (FrameLayout) findViewById(R.id.preview_layout);
        mFolderNameTv = (TextView) findViewById(R.id.folder_name_tv);
        mFolderRecycler = (RecyclerView) findViewById(R.id.folder_recycler);
        mMaskingView = findViewById(R.id.masking_view);
    }

    /**
     * 初始化传来的数据
     */
    private void initData() {
        Intent intent = getIntent();
        mMaxCount = intent.getIntExtra(ImageSelectorUtils.MAX_SELECT_COUNT, 0);
        mIsSingle = intent.getBooleanExtra(ImageSelectorUtils.IS_SINGLE, false);
        mIsShowCamera = intent.getBooleanExtra(ImageSelectorUtils.IS_SHOW_CAMERA, false);
        mSelectImages = intent.getStringArrayListExtra(ImageSelectorUtils.SELECT_IMAGES);
        if (mSelectImages == null) {
            mSelectImages = new ArrayList<>();
        }
    }

    /**
     * 初始化图片列表
     */
    private void initImageRecyclerView() {
        // 判断屏幕方向
        Configuration configuration = getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mLayoutManager = new GridLayoutManager(this, 3);
        } else {
            mLayoutManager = new GridLayoutManager(this, 5);
        }
        // 设置LayoutManager
        mImageRecycler.setLayoutManager(mLayoutManager);
        mImageAdapter = new ImageAdapter(this, mMaxCount, mIsSingle, mIsShowCamera);
        mImageRecycler.setAdapter(mImageAdapter);
        mImageAdapter.setOnImageSelectListener(new ImageAdapter.OnImageSelectListener() {
            @Override
            public void OnImageSelect(Image image, boolean isSelect, int selectCount) {
                setSelectImageCount(selectCount);
            }
        });
        mImageAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(Image image, int position) {
                if (mIsShowCamera && position == 0) {
                    if (mCameraUtil == null) {
                        mCameraUtil = new CameraUtil(ImageSelectorActivity.this);
                    }
                    mCameraUtil.checkPermissionAndOpenCamera();
                } else {
                    ArrayList<Image> images = new ArrayList<>();
                    images.add(image);
                    toPreviewActivity(images);
                }
            }
        });
    }

    /**
     * 初始化监听事件
     */
    private void initListener() {
        findViewById(R.id.back_layout).setOnClickListener(this);
        findViewById(R.id.folder_layout).setOnClickListener(this);
        mMaskingView.setOnClickListener(this);
        mConfirmLayout.setOnClickListener(this);
        mPreviewLayout.setOnClickListener(this);
    }

    /**
     * 跳转到预览页面
     *
     * @param images 选中图片集合
     */
    private void toPreviewActivity(ArrayList<Image> images) {
        if (images != null && !images.isEmpty()) {
            ArrayList<String> imageList = new ArrayList<>();
            for (Image image : images) {
                imageList.add(image.getPath());
            }
            PreviewActivity.openActivity(this, imageList);
        }
    }

    /**
     * 跳转到裁切页面
     *
     * @param images 选中图片集合
     */
    private void toClipActivity(ArrayList<Image> images) {
        if (images != null && !images.isEmpty()) {
            ArrayList<String> imageList = new ArrayList<>();
            for (Image image : images) {
                imageList.add(image.getPath());
            }
            ClipImageActivity.openActivity(this, imageList);
        }
    }

    /**
     * 初始化图片文件夹列表
     */
    private void initFolderList() {
        if (mFolderList != null && !mFolderList.isEmpty()) {
            mFolderRecycler.setLayoutManager(new LinearLayoutManager(ImageSelectorActivity.this));
            FolderAdapter adapter = new FolderAdapter(ImageSelectorActivity.this, mFolderList);
            adapter.setOnFolderSelectListener(new FolderAdapter.OnFolderSelectListener() {
                @Override
                public void OnFolderSelect(Folder folder) {
                    setFolder(folder);
                    closeFolder();
                }
            });
            mFolderRecycler.setAdapter(adapter);
        }
    }

    /**
     * 设置选中的文件夹，同时刷新图片列表
     *
     * @param folder 选中的文件夹
     */
    private void setFolder(Folder folder) {
        if (folder != null && mImageAdapter != null && !folder.equals(mCurrentFolder)) {
            mCurrentFolder = folder;
            mFolderNameTv.setText(folder.getName());
            mImageRecycler.scrollToPosition(0);
            mImageAdapter.refreshData(folder.getImages());
        }
    }

    /**
     * 设置已选中图片后的各种状态
     *
     * @param count 选中数量
     */
    private void setSelectImageCount(int count) {
        if (count == 0) {
            mConfirmLayout.setEnabled(false);
            mPreviewLayout.setEnabled(false);
            mConfirmTv.setText("确定");
            mPreviewTv.setText("预览");
        } else {
            mConfirmLayout.setEnabled(true);
            mPreviewLayout.setEnabled(true);
            mPreviewTv.setText("预览(" + count + ")");
            if (mIsSingle) {
                mConfirmTv.setText("确定");
            } else if (mMaxCount > 0) {
                mConfirmTv.setText("确定(" + count + "/" + mMaxCount + ")");
            } else {
                mConfirmTv.setText("确定(" + count + ")");
            }
        }
    }

    /**
     * 弹出文件夹列表
     */
    private void openFolder() {
        if (!mIsOpenFolder) {
            mMaskingView.setVisibility(View.VISIBLE);
            ObjectAnimator animator = ObjectAnimator.ofFloat(mFolderRecycler, "translationY",
                    mFolderRecycler.getHeight(), 0).setDuration(300);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    mFolderRecycler.setVisibility(View.VISIBLE);
                }
            });
            animator.start();
            mIsOpenFolder = true;
        }
    }

    /**
     * 收起文件夹列表
     */
    private void closeFolder() {
        if (mIsOpenFolder) {
            mMaskingView.setVisibility(View.GONE);
            ObjectAnimator animator = ObjectAnimator.ofFloat(mFolderRecycler, "translationY",
                    0, mFolderRecycler.getHeight()).setDuration(300);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mFolderRecycler.setVisibility(View.GONE);
                }
            });
            animator.start();
            mIsOpenFolder = false;
        }
    }

    /**
     * 确定后返回数据
     */
    private void confirm() {
        if (mImageAdapter == null) {
            return;
        }
        //因为图片的实体类是Image，而我们返回的是String数组，所以要进行转换。
        ArrayList<Image> selectImages = mImageAdapter.getSelectImages();
        mSelectImages.clear();
        for (Image image : selectImages) {
            mSelectImages.add(image.getPath());
        }
        sendResult();
    }

    /**
     * 发送一个result
     */
    private void sendResult() {
        //点击确定，把选中的图片通过Intent传给上一个Activity。
        Intent intent = new Intent();
        intent.putStringArrayListExtra(ImageSelectorUtils.SELECT_IMAGES, mSelectImages);
        setResult(ImageSelectorUtils.RESULT_CODE, intent);
        finish();
    }

    /**
     * 横竖屏切换处理
     *
     * @param newConfig 切换参数
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mLayoutManager != null && mImageAdapter != null) {
            //切换为竖屏
            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                mLayoutManager.setSpanCount(3);
            }
            //切换为横屏
            else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mLayoutManager.setSpanCount(5);
            }
            mImageAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 检查权限并加载SD卡里的图片。
     */
    private void checkPermissionAndLoadImages() {
        RxPermissions mRxPermissions = new RxPermissions(this);
        // 申请权限
        mRxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            //有权限，加载图片。
                            loadImageForSDCard();
                        } else {
                            Toast.makeText(ImageSelectorActivity.this, "相册访问权限被拒绝！", Toast.LENGTH_LONG).show();
                            ImageSelectorActivity.this.finish();
                        }
                    }
                });
    }

    /**
     * 从SDCard加载图片。
     */
    private void loadImageForSDCard() {
        ImageModel.loadImageForSDCard(this, new ImageModel.DataCallback() {
            @Override
            public void onSuccess(ArrayList<Folder> folders) {
                mFolderList = folders;
                if (mFolderList != null && !mFolderList.isEmpty()) {
                    initSelectImages();
                    if (mIsShowCamera) {
                        initCameraData();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initFolderList();
                            setFolder(mFolderList.get(0));
                        }
                    });
                }
            }
        });
    }

    /**
     * 初始化有拍照时的数据
     */
    private void initCameraData() {
        for (Folder folder : mFolderList) {
            ArrayList<Image> imageList = folder.getImages();
            // 在第0个位置添加一个空view，用来放置拍照view
            imageList.add(0, new Image("", 0, ""));
        }
    }

    /**
     * 初始化选中的图片
     */
    private void initSelectImages() {
        if (mSelectImages == null || mSelectImages.size() == 0) {
            setSelectImageCount(0);
            return;
        }
        ArrayList<Image> selectImageList = new ArrayList<>();
        Folder folder = mFolderList.get(0);
        if (folder != null) {
            ArrayList<Image> images = folder.getImages();
            for (Image image : images) {
                for (String mSelectImage : mSelectImages) {
                    if (mSelectImage.equals(image.getPath())) {
                        selectImageList.add(image);
                    }
                }
            }
        }
        mImageAdapter.setSelectImages(selectImageList);
        setSelectImageCount(selectImageList.size());
    }

    /**
     * 返回键关闭文件夹菜单
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN && mIsOpenFolder) {
            closeFolder();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.back_layout) {// 返回键
            ImageSelectorActivity.this.finish();
        } else if (id == R.id.folder_layout) {// 文件夹选择
            if (mFolderRecycler.getAdapter() != null) {
                if (mIsOpenFolder) {
                    closeFolder();
                } else {
                    openFolder();
                }
            }
        } else if (id == R.id.masking_view) {
            // 点击遮罩view就去关闭文件夹选择
            closeFolder();
        } else if (id == R.id.confirm_layout) {
            // 点击确定按钮
            confirm();
        } else if (id == R.id.preview_layout) {
            // 预览
            toPreviewActivity(mImageAdapter.getSelectImages());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ImageSelectorUtils.TAKE_PICTURE_FROM_CAMERA://拍照
                    String imagePath = mCameraUtil.mCameraImagePath;
                    if (!TextUtils.isEmpty(imagePath)) {
                        mSelectImages.add(0, imagePath);
                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + imagePath)));
                        sendResult();
                    }
                    break;
            }
        }
    }
}
