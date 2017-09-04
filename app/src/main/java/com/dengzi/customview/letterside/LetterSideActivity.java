package com.dengzi.customview.letterside;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.dengzi.customview.R;
import com.dengzi.lettersidebar.LetterSideBar;

/**
 * @author Djk
 * @Title: 字母索引
 * @Time: 2017/9/4.
 * @Version:1.0.0
 */
public class LetterSideActivity extends AppCompatActivity {
    private TextView resultTv;
    private LetterSideBar letterSideBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.letter_side_activity);
        initView();
    }

    private void initView() {
        resultTv = (TextView) findViewById(R.id.result_tv);
        letterSideBar = (LetterSideBar) findViewById(R.id.letter);
        letterSideBar.setOnScrollListener(new LetterSideBar.OnScrollListener() {
            @Override
            public void onResult(boolean isShow, String result) {
                if (isShow) {
                    resultTv.setVisibility(View.VISIBLE);
                    resultTv.setText(result);
                } else {
                    resultTv.setVisibility(View.GONE);
                }
            }
        });
    }
}
