package com.dengzi.customview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.dengzi.itemview.TopView;
import com.dengzi.loading.LoadingView;

/**
 * @author Djk
 * @Title: 58同程的加载动画view
 * @Version:1.0.0
 */
public class LoadingActivity extends AppCompatActivity {
    private LoadingView loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_activity);
        loadingView = (LoadingView) findViewById(R.id.loading_view);
    }

}
