package com.dengzi.customview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dengzi.customview.draw.DrawActivity;
import com.dengzi.customview.materialdesign.MDActivity;
import com.dengzi.customview.mybehavior.BehaviorMainActivity;
import com.dengzi.customview.parallax.ParallaxActivity;
import com.dengzi.customview.recviewtest.RecyAnimatorActivity;
import com.dengzi.customview.recviewtest.RecyBaseUseActivity;
import com.dengzi.customview.recviewtest.RecyRefreshActivity;
import com.dengzi.customview.recviewtest.RecySectionActivity;
import com.dengzi.customview.slid.SlidingMenuActivity;
import com.dengzi.customview.statusbar.StatusBarActivity;
import com.dengzi.customview.tabscreen.TabScreenActivity;
import com.dengzi.customview.taglayout.TagLayoutActivity;
import com.dengzi.customview.trackcolor.TrackActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void drawClick(View view) {
        startActivity(new Intent(MainActivity.this, DrawActivity.class));
    }

    public void itemClick(View view) {
        startActivity(new Intent(MainActivity.this, ItemActivity.class));
    }

    public void stepClick(View view) {
        startActivity(new Intent(MainActivity.this, StepActivity.class));
    }

    public void trackColorClick(View view) {
        startActivity(new Intent(MainActivity.this, TrackActivity.class));
    }

    public void letterSideClick(View view) {
        startActivity(new Intent(MainActivity.this, LetterSideActivity.class));
    }

    public void toggleBtnClick(View view) {
        startActivity(new Intent(MainActivity.this, ToggleBtnActivity.class));
    }

    public void taglayoutBtnClick(View view) {
        startActivity(new Intent(MainActivity.this, TagLayoutActivity.class));
    }

    public void slidMenuClick(View view) {
        startActivity(new Intent(MainActivity.this, SlidingMenuActivity.class));
    }

    public void qqslidMenuClick(View view) {
        Intent intent = new Intent(MainActivity.this, SlidingMenuActivity.class);
        intent.putExtra("type", 1);
        startActivity(intent);
    }

    public void verticalSlidMenuClick(View view) {
        Intent intent = new Intent(MainActivity.this, SlidingMenuActivity.class);
        intent.putExtra("type", 2);
        startActivity(intent);
    }

    public void nineLockClick(View view) {
        startActivity(new Intent(MainActivity.this, NineLockActivity.class));
    }

    public void statusBarClick(View view) {
        startActivity(new Intent(MainActivity.this, StatusBarActivity.class));
    }

    public void mdClick(View view) {
        startActivity(new Intent(MainActivity.this, MDActivity.class));
    }

    public void behaviorClick(View view) {
        startActivity(new Intent(MainActivity.this, BehaviorMainActivity.class));
    }

    public void recyClick(View view) {
        startActivity(new Intent(MainActivity.this, RecyBaseUseActivity.class));
    }

    public void recyClick2(View view) {
        startActivity(new Intent(MainActivity.this, RecyAnimatorActivity.class));
    }

    public void recyClick3(View view) {
        startActivity(new Intent(MainActivity.this, RecyRefreshActivity.class));
    }

    public void recyClick4(View view) {
        startActivity(new Intent(MainActivity.this, RecySectionActivity.class));
    }

    public void loadingClick(View view) {
        startActivity(new Intent(MainActivity.this, LoadingActivity.class));
    }

    public void tabScreenClick(View view) {
        startActivity(new Intent(MainActivity.this, TabScreenActivity.class));
    }

    public void msgBubbleClick(View view) {
        startActivity(new Intent(MainActivity.this, MsgBubbleActivity.class));
    }

    public void msgDragClick(View view) {
        startActivity(new Intent(MainActivity.this, MsgDragActivity.class));
    }

    public void loveClick(View view) {
        startActivity(new Intent(MainActivity.this, LoveActivity.class));
    }

    public void parallaxClick(View view) {
        startActivity(new Intent(MainActivity.this, ParallaxActivity.class));
    }

}
