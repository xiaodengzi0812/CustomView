package com.dengzi.customview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.djk.topbarlib.DefTopBar;
import com.djk.topbarlib.ExpandTopBar;

/**
 * @Title: builder模式创建TopBar
 * @Author: djk
 * @Time: 2017/10/19
 * @Version:1.0.0
 */
public class TopBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topbar_activity);

        initTitle();
        initTitle2();
    }

    private void initTitle() {
        new DefTopBar.DefaultBuilder(this, null)
                .dismissLeftView()
                .setMiddleText("middle")
                .setRightIconId(R.drawable.left)
                .setMiddleClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(TopBarActivity.this, "middle", Toast.LENGTH_SHORT).show();
                    }
                })
                .setRightClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(TopBarActivity.this, "right", Toast.LENGTH_SHORT).show();
                    }
                }).show();

        // 扩展一个的使用方式
        // new ExpandTopBar.ExpandBuilder(this, null).show();
    }

    private void initTitle2() {
        LinearLayout parentView = (LinearLayout) findViewById(R.id.ll);
        new DefTopBar.DefaultBuilder(this, parentView)
                .setMiddleText("这是一个相当长的标题，不知道控件能不能放得下")
                .setRightIconId(R.drawable.left)
                .setRight1IconId(R.drawable.user)
                .setLeftIconId(R.drawable.back)
                .setMiddleClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(TopBarActivity.this, "middle", Toast.LENGTH_SHORT).show();
                    }
                })
                .setRight1ClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(TopBarActivity.this, "right1", Toast.LENGTH_SHORT).show();
                    }
                })
                .setRightClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(TopBarActivity.this, "right0", Toast.LENGTH_SHORT).show();
                    }
                }).show();

    }
}
