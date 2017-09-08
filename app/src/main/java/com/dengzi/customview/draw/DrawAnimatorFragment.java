package com.dengzi.customview.draw;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dengzi.customview.R;
import com.djk.test.DrawAnimator;

/**
 * @author Djk
 * @Title: 加载不同的布局Fragment
 * @Time: 2017/8/31.
 * @Version:1.0.0
 */
public class DrawAnimatorFragment extends Fragment {
    private DrawAnimator animatorView;
    private View mRootView;

    public static DrawAnimatorFragment getInstence() {
        DrawAnimatorFragment fragment = new DrawAnimatorFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = View.inflate(getActivity(), R.layout.draw_path_animator_fragment, null);
        initView();
        return mRootView;
    }

    private void initView() {
        animatorView = (DrawAnimator) mRootView.findViewById(R.id.draw_anim);
        mRootView.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animatorView.startMove();
            }
        });
    }
}
