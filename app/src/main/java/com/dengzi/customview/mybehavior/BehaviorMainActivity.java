package com.dengzi.customview.mybehavior;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dengzi.customview.R;

/**
 * @author Djk
 * @Title: 自定义behavior首页
 * @Time: 2017/9/4.
 * @Version:1.0.0
 */
public class BehaviorMainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.behavior_main_activity);
        findViewById(R.id.btn_back_top).setOnClickListener(this);
        findViewById(R.id.btn_zhihu).setOnClickListener(this);
        findViewById(R.id.btn_bottom_sheet).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_back_top) {// 回到顶部按钮动画。
            startActivity(new Intent(this, BackTopActivity.class));
        } else if (v.getId() == R.id.btn_zhihu) {// 仿知乎首页隐藏按钮动画。
            startActivity(new Intent(this, ZhihuActivity.class));
        } else if (v.getId() == R.id.btn_bottom_sheet) {// 底部覆盖。
            startActivity(new Intent(this, BottomSheetBehaviorActivity.class));
        }
    }
}
