package com.dengzi.customview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.djk.love.LoveLayout;
import com.djk.parallax.ParallaxViewPager;

/**
 * @author Djk
 * @Title: 视差引导页效果
 * @Version:1.0.0
 */
public class ParallaxActivity extends AppCompatActivity {
    private ParallaxViewPager parallaxVP;
    private int[] layoutIds = {R.layout.parallax_fragment_page_first, R.layout.parallax_fragment_page_second, R.layout.parallax_fragment_page_third};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parallax_activity);
        initView();
    }

    private void initView() {
        parallaxVP = (ParallaxViewPager) findViewById(R.id.parallax_vp);
        parallaxVP.addLayout(getSupportFragmentManager(),layoutIds);
    }

}
