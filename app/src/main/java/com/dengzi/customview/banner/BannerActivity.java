package com.dengzi.customview.banner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

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

    /**
     * 初始化一个基础的banner
     */
    private void initBanner() {
        mBannerView = (BannerView) findViewById(R.id.banner_view);
        mBannerView.setAdapter(new BannerBaseAdapter() {
            @Override
            public int getCount() {
                return mDataList.size();
            }

            @Override
            public View getView(final int position, ViewGroup parentView, View reuseView) {
                // 如果复用view为null才去创建view，否则使用复用的view
                if (reuseView == null) {
                    reuseView = LayoutInflater.from(BannerActivity.this).inflate(R.layout.banner_item, parentView, false);
                }
                ImageView iv = (ImageView) reuseView.findViewById(R.id.iv);
                iv.setBackgroundResource(mDataList.get(position).getImageRes());
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(BannerActivity.this, "click -> " + position, Toast.LENGTH_SHORT).show();
                    }
                });
                return reuseView;
            }
        });
    }

    /**
     * 初始化一个带参数的banner
     */
    private void initBanner1() {
        mBannerView1 = (BannerView) findViewById(R.id.banner_view1);
        // 设置一个奇葩的差值器
        mBannerView1.setScrollInterpolator(new BounceInterpolator());
        mBannerView1.setAdapter(new BannerBaseAdapter() {
            @Override
            public int getCount() {
                return mDataList.size();
            }

            @Override
            public View getView(final int position, ViewGroup parentView, View reuseView) {
                // 如果复用view为null才去创建view，否则使用复用的view
                if (reuseView == null) {
                    reuseView = LayoutInflater.from(BannerActivity.this).inflate(R.layout.banner_item, parentView, false);
                }
                ImageView iv = (ImageView) reuseView.findViewById(R.id.iv);
                iv.setBackgroundResource(mDataList.get(position).getImageRes());
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(BannerActivity.this, "click -> " + position, Toast.LENGTH_SHORT).show();
                    }
                });
                return reuseView;
            }

            @Override
            public String getDescTitle(int position) {
                return mDataList.get(position).getDescText();
            }
        });
    }

    /**
     * 初始化假数据
     */
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

}
