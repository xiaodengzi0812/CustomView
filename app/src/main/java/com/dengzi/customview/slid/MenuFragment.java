package com.dengzi.customview.slid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.dengzi.customview.R;

/**
 * @author Djk
 * @Title: 普通draw
 * @Time: 2017/8/31.
 * @Version:1.0.0
 */
public class MenuFragment extends Fragment {
    public static MenuFragment getInstence() {
        MenuFragment fragment = new MenuFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mRootView = View.inflate(getActivity(), R.layout.slid_home_menu, null);
        initView(mRootView);
        return mRootView;
    }

    private void initView(View mRootView) {
        mRootView.findViewById(R.id.login_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "登录", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
