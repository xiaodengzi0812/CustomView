package com.dengzi.customview.banner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.dengzi.bannerlib.BannerBaseAdapter;
import com.dengzi.bannerlib.BannerView;
import com.dengzi.customview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Djk
 * @Title: 自定义Banner
 * @Time: 2017/9/4.
 * @Version:1.0.0
 */
public class BannerActivity extends AppCompatActivity {
    private BannerView mBannerView;
    private BannerView mBannerView1;
    private List<BannerBean> mDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.banner_activity);

        initData();
        initBanner();
        initBanner1();
    }

    private void initData() {
        for (int i = 0; i < 4; i++) {
            BannerBean bannerBean;
            if (i % 2 == 0) {
                bannerBean = new BannerBean(R.drawable.icon_banner1, "美女一枚");
            } else {
                bannerBean = new BannerBean(R.drawable.status_icon, "风景一副");
            }
            mDataList.add(bannerBean);
        }
    }

    private void initBanner() {
        mBannerView = (BannerView) findViewById(R.id.banner_view);
        mBannerView.setAdapter(new MyBannerAdapter(this, mDataList));
        mBannerView.startScroll();
    }

    private void initBanner1() {
        mBannerView1 = (BannerView) findViewById(R.id.banner_view1);
        mBannerView1.setAdapter(new MyBannerAdapter(this, mDataList));
        mBannerView1.startScroll();
    }

}
