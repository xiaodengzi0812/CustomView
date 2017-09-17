package com.dengzi.customview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.dengzi.lettersidebar.LetterSideBar;
import com.dengzi.ninelockview.NineLock;

/**
 * @author Djk
 * @Title: 九宫格解锁
 * @Time: 2017/9/4.
 * @Version:1.0.0
 */
public class NineLockActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nine_lock_activity);

        initView();
    }

    private void initView() {
        NineLock nineLock = (NineLock) findViewById(R.id.nine_view);
        final TextView tv = (TextView) findViewById(R.id.result_tv);

        nineLock.setOnResultListener(new NineLock.OnResultListener() {
            @Override
            public void onResult(String result) {
                tv.setText(result);
            }
        });
    }
}
