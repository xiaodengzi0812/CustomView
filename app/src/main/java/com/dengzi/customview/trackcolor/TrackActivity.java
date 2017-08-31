package com.dengzi.customview.trackcolor;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.dengzi.customview.R;
import com.dengzi.trackcolor.TrackColorTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Djk
 * @Title:
 * @Time: 2017/8/31.
 * @Version:1.0.0
 */

public class TrackActivity extends AppCompatActivity {
    private String[] topStr = {"新闻", "娱乐", "政治", "体育"};
    LinearLayout topLl;
    ViewPager vp;
    private List<TrackColorTextView> trackList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_activity);
        initView();
    }

    private void initView() {
        topLl = (LinearLayout) findViewById(R.id.top_ll);
        vp = (ViewPager) findViewById(R.id.vp);
        initTopView();
        initVp();
    }

    private void initVp() {
        TrackAdapter adapter = new TrackAdapter(getSupportFragmentManager(), topStr);
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
