package com.dengzi.customview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dengzi.lettersidebar.LetterSideBar;
import com.dengzi.togglebutton.ToggleBtn;

/**
 * @author Djk
 * @Title: 滑动切换开关
 * @Time: 2017/9/4.
 * @Version:1.0.0
 */
public class ToggleBtnActivity extends AppCompatActivity {

    private ToggleBtn btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toggle_btn_activity);
        initView();
    }

    private void initView() {
        btn = (ToggleBtn) findViewById(R.id.tog_btn);
        btn.setOnScrollListener(new ToggleBtn.OnScrollListener() {
            @Override
            public void onResult(boolean isChecked) {
                Toast.makeText(ToggleBtnActivity.this, "当前状态-->" + isChecked, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
