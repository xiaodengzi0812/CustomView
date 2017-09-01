package com.dengzi.customview.trackcolor;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dengzi.customview.R;

/**
 * @author Djk
 * @Title: 单纯展示一个文字
 * @Time: 2017/8/31.
 * @Version:1.0.0
 */
public class TrackFragment extends Fragment {
    public static TrackFragment getInstence(String text) {
        TrackFragment trackFragment = new TrackFragment();
        Bundle args = new Bundle();
        args.putString("text", text);
        trackFragment.setArguments(args);
        return trackFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.track_fragment, null);
        Bundle args = getArguments();
        String text = args.getString("text");
        TextView tv = (TextView) view.findViewById(R.id.tv);
        tv.setText(text);
        return view;
    }
}
