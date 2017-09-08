package com.dengzi.customview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dengzi.customview.draw.DrawActivity;
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

}
