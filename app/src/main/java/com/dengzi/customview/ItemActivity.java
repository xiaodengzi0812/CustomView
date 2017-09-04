package com.dengzi.customview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.dengzi.itemview.TopView;

public class ItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_activity);

        initTop();
    }

    private void initTop() {
        TopView topView = (TopView) findViewById(R.id.topview);
        topView.setOnItemClickListener(new TopView.OnItemClickListener() {
            @Override
            public void onLeftClick() {
                Toast.makeText(ItemActivity.this, "onLeftClick", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightClick() {
                Toast.makeText(ItemActivity.this, "onRightClick", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRight2Click() {
                Toast.makeText(ItemActivity.this, "onRight2Click", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMiddleClick() {
                Toast.makeText(ItemActivity.this, "onMiddleClick", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
