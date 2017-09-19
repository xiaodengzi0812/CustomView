package com.dengzi.customview.statusbar;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dengzi.customview.R;

/**
 * @author Djk
 * @Title: StatusBar
 * @Time: 2017/9/4.
 * @Version:1.0.0
 */
public class StatusBarActivity extends AppCompatActivity {
    private RelativeLayout mTitleRl;
    private MyScrollView mScrollView;
    private ImageView mImageView;
    private float mImageHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_bar_activity);
        StatusBarUtil.setStatusBarTranslucent(this);
        initView();
    }

    private void initView() {
        mTitleRl = (RelativeLayout) findViewById(R.id.title_rl);
        mTitleRl.getBackground().setAlpha(0);
        mScrollView = (MyScrollView) findViewById(R.id.scroll_view);
        mImageView = (ImageView) findViewById(R.id.iv);
        mImageView.post(new Runnable() {
            @Override
            public void run() {
                mImageHeight = mImageView.getMeasuredHeight();
            }
        });

        mScrollView.setOnScrollChangedListener(new MyScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                float alpha = (float) t * 255 / mImageHeight;
                if (alpha <= 0) {
                    alpha = 0;
                }

                if (alpha > 255) {
                    alpha = 255;
                }

                mTitleRl.getBackground().setAlpha((int) alpha);
            }
        });
    }

}
