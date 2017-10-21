package com.dengzi.customview.banner;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dengzi.bannerlib.BannerBaseAdapter;
import com.dengzi.bannerlib.BannerView;
import com.dengzi.bannerlib.BannerViewPager;
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
//        initBanner();
        initBanner1();
//        initBvp();
    }

    private void initBvp() {
//        final BannerViewPager bvp = (BannerViewPager) findViewById(R.id.bvp);
//
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                bvp.setAdapter(new BannerBaseAdapter() {
//                    @Override
//                    public int getCount() {
//                        return mDataList.size();
//                    }
//
//                    @Override
//                    public View getView(ViewGroup parentView, int position) {
//                        View view = LayoutInflater.from(BannerActivity.this).inflate(R.layout.banner_item, parentView, false);
//                        ImageView iv = (ImageView) view.findViewById(R.id.iv);
//                        iv.setBackgroundResource(mDataList.get(position).getImageRes());
//                        return view;
//                    }
//
//                    @Override
//                    public String getDescTitle(int position) {
//                        return mDataList.get(position).getDescText();
//                    }
//
//                });
//
//                bvp.startScroll();
//            }
//        }, 2000);
    }

    /**
     * 初始化一个默认banner
     */
    private void initBanner() {
//        mBannerView = (BannerView) findViewById(R.id.banner_view);
//        mBannerView.setAdapter(new BannerBaseAdapter() {
//            @Override
//            public int getCount() {
//                return mDataList.size();
//            }
//
//            @Override
//            public View getView(ViewGroup parentView, int position) {
//                View view = LayoutInflater.from(BannerActivity.this).inflate(R.layout.banner_item, parentView, false);
//                ImageView iv = (ImageView) view.findViewById(R.id.iv);
//                iv.setBackgroundResource(mDataList.get(position).getImageRes());
//                return view;
//            }
//        });
//        mBannerView.startScroll();
    }

    /**
     * 初始化一个带参数的banner
     */
    private void initBanner1() {
        mBannerView1 = (BannerView) findViewById(R.id.banner_view1);
        mBannerView1.setAdapter(new BannerBaseAdapter() {
            @Override
            public int getCount() {
                return mDataList.size();
            }

            @Override
            public View getView(int position, ViewGroup parentView, View reuseView) {
                // 如果复用view为null才去创建view，否则使用复用的view
                if (reuseView == null) {
                    reuseView = LayoutInflater.from(BannerActivity.this).inflate(R.layout.banner_item, parentView, false);
                }
                ImageView iv = (ImageView) reuseView.findViewById(R.id.iv);
                iv.setBackgroundResource(mDataList.get(position).getImageRes());
                return reuseView;
            }

            @Override
            public String getDescTitle(int position) {
                return mDataList.get(position).getDescText();
            }

        });

        mBannerView1.startScroll();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }
    };

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
