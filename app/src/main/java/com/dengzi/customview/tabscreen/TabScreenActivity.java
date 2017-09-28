package com.dengzi.customview.tabscreen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dengzi.customview.R;
import com.dengzi.customview.taglayout.MyTagAdapter;
import com.dengzi.tabscreen.TabScreenView;

/**
 * @author Djk
 * @Title: 常见多条目菜单筛选
 * @Version:1.0.0
 */
public class TabScreenActivity extends AppCompatActivity {
    private TabScreenView mTabScreenView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_screen_activity);
        initView();
    }

    private void initView() {
        mTabScreenView = (TabScreenView) findViewById(R.id.tab_view);
        // 设置菜单的最大高度百分比
        mTabScreenView.setContentMaxPercnet(70);
        MyTabAdapter adapter = new MyTabAdapter(this);
        mTabScreenView.setAdapter(adapter);
    }

}
