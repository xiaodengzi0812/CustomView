package com.dengzi.customview.parallax;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dengzi.customview.R;
import com.djk.parallax.ParallaxBaseFragment;
import com.djk.parallax.ParallaxViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Djk
 * @Title: 视差引导页效果
 * @Version:1.0.0
 */
public class ParallaxActivity extends AppCompatActivity {
    private ParallaxViewPager mParallaxVP;
    private int[] mLayoutIds = {R.layout.parallax_fragment_page_first, R.layout.parallax_fragment_page_second, R.layout.parallax_fragment_page_third};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parallax_activity);
        mParallaxVP = (ParallaxViewPager) findViewById(R.id.parallax_vp);
//        initView();
        initView2();
    }

    /**
     * 第一种写法
     */
    private void initView() {
        // 直接设置布局xml集合
        mParallaxVP.addLayout(getSupportFragmentManager(), mLayoutIds);
    }

    /**
     * 第二种写法
     */
    private void initView2() {
        List<ParallaxBaseFragment> fragmentList = new ArrayList<>();
        for (int layoutId : mLayoutIds) {
            ParallaxFragment fragment = new ParallaxFragment();
            Bundle args = new Bundle();
            args.putInt(ParallaxBaseFragment.LAYOUT_ID_KEY, layoutId);
            fragment.setArguments(args);
            fragmentList.add(fragment);
        }
        // 直接设置布局fragment集合
        mParallaxVP.addLayout(getSupportFragmentManager(), fragmentList);
    }


}
