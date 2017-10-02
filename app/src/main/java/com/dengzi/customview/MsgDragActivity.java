package com.dengzi.customview;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dengzi.msgbubble.drag.MsgDragView;

/**
 * @author Djk
 * @Title: 任意view拖拽
 * @Time: 2017/9/4.
 * @Version:1.0.0
 */
public class MsgDragActivity extends AppCompatActivity {
    private TextView mDragTv;
    private ImageView mDragIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.msg_drag_activity);
        initView();
    }

    private void initView() {
        mDragTv = (TextView) findViewById(R.id.drag_tv);
        mDragTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MsgDragActivity.this, "click", Toast.LENGTH_SHORT).show();
            }
        });
        mDragIv = (ImageView) findViewById(R.id.drag_iv);
        MsgDragView.bindView(mDragIv, new MsgDragView.MsgDragListener() {
            @Override
            public void restore() {

            }

            @Override
            public void dismiss(PointF pointF) {

            }
        });
    }

}
