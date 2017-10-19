package com.djk.topbarlib;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * @author Djk
 * @Title: 默认的参数类
 * @Time: 2017/8/19.
 * @Version:1.0.0
 */
public class DefParams extends AbsParams {
    // 背景色/底部线
    public int bgColor, bgHeight;
    public int lineHeight, lineColor;

    // 左text
    public String leftText;
    public int leftIconId, leftTextSize, leftTextColor;
    public View.OnClickListener leftListener;

    // 中间text
    public String middleText;
    public int middleIconId, middleTextSize, middleTextColor, middleTextMaxLength;
    public View.OnClickListener middleListener;

    // 右text
    public String rightText;
    public int rightIconId, rightTextSize, rightTextColor;
    public View.OnClickListener rightListener;

    // 右1text
    public String rightText1;
    public int rightIconId1, rightTextSize1, rightTextColor1;
    public View.OnClickListener rightListener1;

    public DefParams(Context context, LinearLayout parentView) {
        super(context, parentView);
    }
}
