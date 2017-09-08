package com.dengzi.customview.draw;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.dengzi.customview.R;
import com.dengzi.customview.trackcolor.TrackAdapter;
import com.dengzi.trackcolor.TrackColorTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Djk
 * @Title: draw测试
 * @Time: 2017/9/4.
 * @Version:1.0.0
 */
public class DrawActivity extends AppCompatActivity {
    private String[] topStr = {"普通", "路径", "动画"};
    private int[] types = {0, 1, 2};
    LinearLayout topLl;
    ViewPager vp;
    private List<TrackColorTextView> trackList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draw_activity);
        initView();
    }

    private void initView() {
        topLl = (LinearLayout) findViewById(R.id.top_ll);
        vp = (ViewPager) findViewById(R.id.vp);
        initTopView();
        initVp();
    }

    private void initVp() {
        DrawAdapter adapter = new DrawAdapter(getSupportFragmentManager(), types);
        vp.setAdapter(adapter);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                try {
                    TrackColorTextView leftView = trackList.get(position);
                    TrackColorTextView rightView = trackList.get(position + 1);
                    leftView.setDirection(TrackColorTextView.Direction.RIGHT_TO_LEFT);
                    leftView.setProgress(1 - positionOffset);

                    rightView.setDirection(TrackColorTextView.Direction.LEFT_TO_RIGHT);
                    rightView.setProgress(positionOffset);
                } catch (Exception e) {
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initTopView() {
        for (String s : topStr) {
            TrackColorTextView trackView = (TrackColorTextView) View.inflate(this, R.layout.track_top_item, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp.weight = 1;
            trackView.setLayoutParams(lp);
            trackView.setText(s);
            topLl.addView(trackView);
            trackList.add(trackView);
        }
    }
}
