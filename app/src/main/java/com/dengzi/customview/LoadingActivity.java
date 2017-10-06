package com.dengzi.customview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dengzi.loading.effect1.LoadingView;

/**
 * @author Djk
 * @Title: 加载动画view
 * @Version:1.0.0
 */
public class LoadingActivity extends AppCompatActivity {
    private com.dengzi.loading.effect1.LoadingView loadingView1;
    private com.dengzi.loading.effect2.LoadingView loadingView2;
    private com.dengzi.loading.effect3.LoadingView loadingView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_activity);
        loadingView1 = (com.dengzi.loading.effect1.LoadingView) findViewById(R.id.loading_view1);
        loadingView2 = (com.dengzi.loading.effect2.LoadingView) findViewById(R.id.loading_view2);
        loadingView3 = (com.dengzi.loading.effect3.LoadingView) findViewById(R.id.loading_view3);
    }

    public void effect1(View view) {
        loadingView2.setVisibility(View.GONE);
        loadingView3.setVisibility(View.GONE);
        loadingView1.setVisibility(View.VISIBLE);
    }

    public void effect2(View view) {
        loadingView1.setVisibility(View.GONE);
        loadingView3.setVisibility(View.GONE);
        loadingView2.setVisibility(View.VISIBLE);
    }

    public void effect3(View view) {
        loadingView1.setVisibility(View.GONE);
        loadingView2.setVisibility(View.GONE);
        loadingView3.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingView3.disappear();
            }
        }, 3000);
    }

}
