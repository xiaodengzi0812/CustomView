package com.dengzi.customview.VerticalViewPager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.dengzi.customview.R;
import com.dengzi.customview.parallax.ParallaxFragment;
import com.dengzi.tabscreen.TabObserver;
import com.dengzi.viewpager.VerticalViewPager;
import com.djk.parallax.ParallaxBaseFragment;
import com.djk.parallax.ParallaxViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Djk
 * @Title: 竖直方向滑动的viewpager
 * @Version:1.0.0
 */
public class VerticalVpActivity extends AppCompatActivity {
    private VerticalViewPager mVerticalVp;
    private int[] mLayoutIds = {R.layout.vertical_page_1, R.layout.vertical_page_2, R.layout.vertical_page_3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vertical_vp_activity);
        mVerticalVp = (VerticalViewPager) findViewById(R.id.vertical_vp);
        initView();
    }

    private void initView() {
        MyAdapter adapter = new MyAdapter(this, mLayoutIds);
        mVerticalVp.setAdapter(adapter);
        mVerticalVp.setOnPagerChangeListener(new VerticalViewPager.OnPagerChangeListener() {
            @Override
            public void moveTo(int position) {
                Toast.makeText(VerticalVpActivity.this, "move to " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
