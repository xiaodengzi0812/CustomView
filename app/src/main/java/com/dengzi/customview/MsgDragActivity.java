package com.dengzi.customview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dengzi.msgbubble.drag.MsgDragHelper;
import com.dengzi.msgbubble.drag.MsgDragView;

/**
 * @author Djk
 * @Title: 任意view拖拽
 * @Time: 2017/9/4.
 * @Version:1.0.0
 */
public class MsgDragActivity extends AppCompatActivity {
    private ImageView mDragIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.msg_drag_activity);
        initView();
    }

    private void initView() {
        mDragIv = (ImageView) findViewById(R.id.drag_iv);
        MsgDragHelper.bindView(mDragIv, new MsgDragHelper.DragDisappearListener() {
            @Override
            public void dismiss(View view) {
                Toast.makeText(MsgDragActivity.this, "拖拽消失", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
