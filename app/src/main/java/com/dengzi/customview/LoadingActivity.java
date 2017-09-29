package com.dengzi.customview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dengzi.loading.effect1.LoadingView;

/**
 * @author Djk
 * @Title: 加载动画view
 * @Version:1.0.0
 */
public class LoadingActivity extends AppCompatActivity {
    private LoadingView loadingView1;

    private com.dengzi.loading.effect2.LoadingView loadingView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_activity);
        loadingView1 = (LoadingView) findViewById(R.id.loading_view1);
        loadingView2 = (com.dengzi.loading.effect2.LoadingView) findViewById(R.id.loading_view2);
        loadingView1.setVisibility(View.GONE);
        loadingView2.setVisibility(View.GONE);
    }

    public void effect1(View view) {
        loadingView1.setVisibility(View.VISIBLE);
        loadingView2.setVisibility(View.GONE);
    }

    public void effect2(View view) {
        loadingView1.setVisibility(View.GONE);
        loadingView2.setVisibility(View.VISIBLE);
    }

}
