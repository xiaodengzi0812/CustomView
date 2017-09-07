package com.dengzi.customview.taglayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.dengzi.customview.R;
import com.dengzi.taglayout.TagLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Djk
 * @Title: 流布局
 * @Time: 2017/9/7.
 * @Version:1.0.0
 */
public class TagLayoutActivity extends AppCompatActivity {
    private List<TagBean> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taglayout_activity);
        initData();
        initTag();
    }

    private void initTag() {
        TagLayout tagView = (TagLayout) findViewById(R.id.tag_view);
        MyTagAdapter adapter = new MyTagAdapter(dataList, this);
        tagView.setAdapter(adapter);
    }

    private void initData() {
        dataList.add(new TagBean("后来"));
        dataList.add(new TagBean("我总算学会了"));
        dataList.add(new TagBean("如何去爱"));
        dataList.add(new TagBean("可惜你"));
        dataList.add(new TagBean("早已远去，消失在人海"));
        dataList.add(new TagBean("后来。。"));
        dataList.add(new TagBean("终于在眼泪中明白"));
        dataList.add(new TagBean("有些人"));
        dataList.add(new TagBean("一旦失去就不在"));
    }
}
