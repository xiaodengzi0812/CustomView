package com.dengzi.customview.slid;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.dengzi.customview.R;

/**
 * @author Djk
 * @Title: 滑动菜单
 * @Time: 2017/9/4.
 * @Version:1.0.0
 */
public class SlidingMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int resourceId = R.layout.slid_menu_activity;
        int type = getIntent().getIntExtra("type", 0);
        if (type == 1) {
            resourceId = R.layout.qqslid_menu_activity;
        }
        setContentView(resourceId);
        initView();
    }

    private void initView() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.menu_fl, MenuFragment.getInstence());
        ft.replace(R.id.content_fl, ContentFragment.getInstence());
        ft.commit();
    }

}
