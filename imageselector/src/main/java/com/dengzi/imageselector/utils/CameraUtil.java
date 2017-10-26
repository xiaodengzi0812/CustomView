package com.dengzi.imageselector.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * @author Djk
 * @Title:
 * @Time: 2017/10/25.
 * @Version:1.0.0
 */
public class CameraUtil {
    public String mCameraImagePath = "";
    private RxPermissions mRxPermissions;
    private Activity mActivity;

    public CameraUtil(Activity activity) {
        this.mActivity = activity;
        mRxPermissions = new RxPermissions(mActivity);
    }

    /**
     * 获取权限并开户相机
     */
    public void checkPermissionAndOpenCamera() {
        mRxPermissions.request(Manifest.permission.CAMERA)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            openCamera();
                        } else {
                            Toast.makeText(mActivity, "相机访问权限被拒绝！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 打开相机
     */
    private void openCamera() {
        // 跳转到系统照相机
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            Log.v("TestFile", "SD card is not avaiable/writeable right now.");
            return;
        }

        long t = System.currentTimeMillis() / 1000;
        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        mCameraImagePath = rootPath + File.separator + t + ".jpg";
        File file = new File(mCameraImagePath);//FileUtil.getUserHeadFile();
        if (file.exists()) {
            file.delete();
        }

        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(mActivity, "com.dengzi.customview.provider", file);//通过FileProvider创建一个content类型的Uri
        } else {
            uri = Uri.fromFile(file);
        }

        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        openCameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        mActivity.startActivityForResult(openCameraIntent, ImageSelectorUtils.TAKE_PICTURE_FROM_CAMERA);
    }
}
