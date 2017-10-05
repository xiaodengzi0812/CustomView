package com.dengzi.customview.parallax;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.dengzi.customview.R;
import com.djk.parallax.ParallaxBaseFragment;

/**
 * @author Djk
 * @Title: 最后一个页面跳转
 * @Time: 2017/10/5.
 * @Version:1.0.0
 */
public class ParallaxFragment extends ParallaxBaseFragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View clickView = view.findViewById(R.id.click_tv);
        if (clickView != null) {
            clickView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "页面跳转", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
