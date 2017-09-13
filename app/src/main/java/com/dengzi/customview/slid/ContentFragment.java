package com.dengzi.customview.slid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dengzi.customview.R;

/**
 * @author Djk
 * @Title: 普通draw
 * @Time: 2017/8/31.
 * @Version:1.0.0
 */
public class ContentFragment extends Fragment {
    public static ContentFragment getInstence() {
        ContentFragment fragment = new ContentFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mRootView = View.inflate(getActivity(), R.layout.slid_home_content, null);
        return mRootView;
    }

}
