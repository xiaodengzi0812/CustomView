package com.dengzi.customview.draw;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dengzi.customview.R;

/**
 * @author Djk
 * @Title: 普通draw
 * @Time: 2017/8/31.
 * @Version:1.0.0
 */
public class DrawCommonFragment extends Fragment {
    public static DrawCommonFragment getInstence() {
        DrawCommonFragment fragment = new DrawCommonFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mRootView = View.inflate(getActivity(), R.layout.draw_common_fragment, null);
        return mRootView;
    }
}
