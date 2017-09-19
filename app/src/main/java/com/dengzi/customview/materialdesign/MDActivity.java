package com.dengzi.customview.materialdesign;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.dengzi.customview.R;

/**
 * @author Djk
 * @Title: Material Design 1
 * @Time: 2017/9/4.
 * @Version:1.0.0
 */
public class MDActivity extends AppCompatActivity {

    private String mTitle = "临江仙";
    private String mContent = "滚滚长江东逝水，浪花淘尽英雄。\n" +
            "是非成败转头空。\n" +
            "青山依旧在，几度夕阳红。\n" +
            "白发渔樵江渚上，惯看秋月春风。\n" +
            "一壶浊酒喜相逢。\n" +
            "古今多少事，都付笑谈中。";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.md_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        TextView fruitContentText = (TextView) findViewById(R.id.fruit_content_text);
        collapsingToolbar.setTitle(mTitle);
        fruitContentText.setText(mContent);
    }

}
