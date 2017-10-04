package com.dengzi.customview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dengzi.loading.effect1.LoadingView;
import com.djk.love.LoveLayout;

/**
 * @author Djk
 * @Title: 花束直播点赞效果
 * @Version:1.0.0
 */
public class LoveActivity extends AppCompatActivity {
    private LoveLayout loveLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.love_activity);
        loveLayout = (LoveLayout) findViewById(R.id.love_layout);
    }

    public void add(View view) {
        loveLayout.addLove(5);
    }

}
