package com.djk.topbarlib;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

/**
 * @author Djk
 * @Title: 一个写好的默认topbar，可覆盖80%的topbar
 * @Time: 2017/8/19.
 * @Version:1.0.0
 */
public class DefTopBar extends AbsTopBar {

    protected DefTopBar(DefParams params) {
        super(params);
    }

    @Override
    public int bindLayoutId() {
        return R.layout.topbar;
    }

    @Override
    public void applyView() {
        // 强转成自己的参数bean
        DefParams dParams = (DefParams) mParams;
        setBgView(dParams);
        setLeftView(dParams);
        setMiddleView(dParams);
        setRightView(dParams);
        setRight1View(dParams);
    }

    /**
     * 默认的builder
     */
    public static class DefaultBuilder extends AbsTopBar.Builder {
        protected DefParams params;

        public DefaultBuilder(Context mContext, LinearLayout parentView) {
            super(mContext, parentView);
            params = new DefParams(mContext, parentView);
        }

        @Override
        public void show() {
            new DefTopBar(params);
        }


        // -----------------设置各种属性-----------------
        // 背景
        public DefaultBuilder setBgColor(int bgColor) {
            params.bgColor = bgColor;
            return this;
        }

        public DefaultBuilder setHeight(int height) {
            params.bgHeight = height;
            return this;
        }

        public DefaultBuilder setLineHeight(int height) {
            params.lineHeight = height;
            return this;
        }

        public DefaultBuilder setLineColor(int color) {
            params.lineColor = color;
            return this;
        }

        // 左text
        public DefaultBuilder dismissLeftView() {
            params.leftDismiss = true;
            return this;
        }

        public DefaultBuilder setLeftText(String text) {
            params.leftText = text;
            return this;
        }

        public DefaultBuilder setLeftIconId(int iconId) {
            params.leftIconId = iconId;
            return this;
        }

        public DefaultBuilder setLeftTextSize(int textSize) {
            params.leftTextSize = textSize;
            return this;
        }

        public DefaultBuilder setLeftTextColor(int color) {
            params.leftTextColor = color;
            return this;
        }

        public DefaultBuilder setLeftClickListener(View.OnClickListener listener) {
            params.leftListener = listener;
            return this;
        }

        //中间控件
        public DefaultBuilder setMiddleText(String text) {
            params.middleText = text;
            return this;
        }

        public DefaultBuilder setMiddleIconId(int iconId) {
            params.middleIconId = iconId;
            return this;
        }

        public DefaultBuilder setMiddleTextSize(int textSize) {
            params.middleTextSize = textSize;
            return this;
        }

        public DefaultBuilder setMiddleTextColor(int color) {
            params.middleTextColor = color;
            return this;
        }

        public DefaultBuilder setMiddleTextMaxLength(int maxLength) {
            params.middleTextMaxLength = maxLength;
            return this;
        }

        public DefaultBuilder setMiddleClickListener(View.OnClickListener listener) {
            params.middleListener = listener;
            return this;
        }

        //设置右控件
        public DefaultBuilder setRightTitle(String text) {
            params.rightText = text;
            return this;
        }

        public DefaultBuilder setRightIconId(int iconId) {
            params.rightIconId = iconId;
            return this;
        }

        public DefaultBuilder setRightTextSize(int textSize) {
            params.rightTextSize = textSize;
            return this;
        }

        public DefaultBuilder setRightTextColor(int color) {
            params.rightTextColor = color;
            return this;
        }

        public DefaultBuilder setRightClickListener(View.OnClickListener listener) {
            params.rightListener = listener;
            return this;
        }

        //设置右控件1
        public DefaultBuilder setRight1Title(String text) {
            params.rightText1 = text;
            return this;
        }

        public DefaultBuilder setRight1IconId(int iconId) {
            params.rightIconId1 = iconId;
            return this;
        }

        public DefaultBuilder setRight1TextSize(int textSize) {
            params.rightTextSize1 = textSize;
            return this;
        }

        public DefaultBuilder setRight1TextColor(int color) {
            params.rightTextColor1 = color;
            return this;
        }

        public DefaultBuilder setRight1ClickListener(View.OnClickListener listener) {
            params.rightListener1 = listener;
            return this;
        }
    }

    private void setBgView(DefParams dParams) {
        setBgColor(R.id.topbar_bg, dParams.bgColor);
        setBgColor(R.id.line, dParams.lineColor);
    }

    private void setLeftView(final DefParams dParams) {
        if (dParams.leftDismiss) return;
        setText(R.id.left_tv, dParams.leftText);
        setTextSize(R.id.left_tv, dParams.leftTextSize);
        setTextColor(R.id.left_tv, dParams.leftTextColor);
        setTextIcon(R.id.left_tv, dParams.leftIconId);
        setClickListener(R.id.left_tv, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) dParams.context).finish();
            }
        });
        setClickListener(R.id.left_tv, dParams.leftListener);
    }

    private void setMiddleView(DefParams dParams) {
        setTextMaxlength(R.id.middle_tv, dParams.middleTextMaxLength);
        setText(R.id.middle_tv, dParams.middleText);
        setTextSize(R.id.middle_tv, dParams.middleTextSize);
        setTextColor(R.id.middle_tv, dParams.middleTextColor);
        setTextIcon(R.id.middle_tv, dParams.middleIconId);
        setClickListener(R.id.middle_tv, dParams.middleListener);
    }

    private void setRightView(DefParams dParams) {
        setText(R.id.right_tv, dParams.rightText);
        setTextSize(R.id.right_tv, dParams.rightTextSize);
        setTextColor(R.id.right_tv, dParams.rightTextColor);
        setTextIcon(R.id.right_tv, dParams.rightIconId);
        setClickListener(R.id.right_tv, dParams.rightListener);
    }

    private void setRight1View(DefParams dParams) {
        setText(R.id.right_tv1, dParams.rightText1);
        setTextSize(R.id.right_tv1, dParams.rightTextSize1);
        setTextColor(R.id.right_tv1, dParams.rightTextColor1);
        setTextIcon(R.id.right_tv1, dParams.rightIconId1);
        setClickListener(R.id.right_tv1, dParams.rightListener1);
    }
}
