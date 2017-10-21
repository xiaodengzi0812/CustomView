package com.dengzi.bannerlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author Djk
 * @Title: 自定义BannerView
 * @Time: 2017/10/20.
 * @Version:1.0.0
 */
public class BannerView extends RelativeLayout {
    private Context mContext;
    // BannerViewPager
    private BannerViewPager mBannerVp;
    // 底部控件的容器
    private RelativeLayout mBottomLayout;
    // 广告文字
    private TextView mDescTv;
    // 点的容器
    private LinearLayout mPointsLl;
    // 设置数据的Adapter
    private BannerBaseAdapter mAdapter;
    // 当前选中的位置
    private int mCurrentPosition = 0;
    // 圆点的大小
    private int mPointSize = 6;
    // 点之前的距离
    private int mPointDistance = 8;
    // 默认点的Drawable
    private Drawable mPointDefaultDrawable;
    // 选中点的Drawable
    private Drawable mPointSelectDrawable;
    // 宽比例
    private float mWidthProportion = 0;
    // 高比例
    private float mHeightProportion = 0;

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        addView(LayoutInflater.from(context).inflate(R.layout.banner_view, this, false));
        initView();
        initAttrs(attrs);
    }

    /**
     * 初始化view
     */
    private void initView() {
        mBannerVp = (BannerViewPager) findViewById(R.id.banner_vp);
        mDescTv = (TextView) findViewById(R.id.desc_tv);
        mPointsLl = (LinearLayout) findViewById(R.id.points_ll);
        mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
    }

    /**
     * 初始化属性
     *
     * @param attrs 传来的属性
     */
    private void initAttrs(AttributeSet attrs) {
        // 圆点默认颜色
        final int DEFAULT_POINT_COLOR = Color.GRAY;
        // 圆点默认选中颜色
        final int DEFAULT_POINT_SELECT_COLOR = Color.WHITE;
        // 圆点默认位置 （-1:left 0:center 1:right）
        final int DEFAULT_POINT_GRAVITY = 0;
        // 文本默认字体大小
        final int DEFAULT_TEXT_SIZE = 12;
        // 文本默认字体颜色
        final int DEFAULT_TEXT_COLOR = Color.WHITE;
        // 文本默认位置 （-1:left 0:center 1:right）
        final int DEFAULT_TEXT_GRAVITY = -1;
        // 底部控件默认透明
        final int DEFAULT_BG_COLOR = Color.TRANSPARENT;
        // 底部控件左右padding
        final int DEFAULT_BG_PADDING_LEFT_RIGHT = 12;
        // 底部控件上下padding
        final int DEFAULT_BG_PADDING_TOP_BOTTOM = 6;

        if (attrs != null) {
            TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.banner_view);
            // 宽高比例
            mWidthProportion = typedArray.getFloat(R.styleable.banner_view_widthProportion, mWidthProportion);
            mHeightProportion = typedArray.getFloat(R.styleable.banner_view_heightProportion, mHeightProportion);

            // 点的一些属性
            mPointSize = typedArray.getDimensionPixelSize(R.styleable.banner_view_pointSize, dp2px(mPointSize));
            mPointSelectDrawable = typedArray.getDrawable(R.styleable.banner_view_pointSelectDrawable);
            mPointDefaultDrawable = typedArray.getDrawable(R.styleable.banner_view_pointDefaultDrawable);
            mPointDistance = typedArray.getDimensionPixelSize(R.styleable.banner_view_pointDistance, dp2px(mPointDistance));
            int pointGravity = typedArray.getInt(R.styleable.banner_view_pointGravity, DEFAULT_POINT_GRAVITY);

            // 文本的一些属性
            float descTextSize = typedArray.getDimensionPixelSize(R.styleable.banner_view_descTextSize, sp2px(DEFAULT_TEXT_SIZE));
            int descTextColor = typedArray.getColor(R.styleable.banner_view_descTextColor, DEFAULT_TEXT_COLOR);
            int descTextGravity = typedArray.getInt(R.styleable.banner_view_descTextGravity, DEFAULT_TEXT_GRAVITY);

            // 底部控件的一些属性
            int bottomLayoutBackGroundColor = typedArray.getColor(R.styleable.banner_view_bottomLayoutBackGroundColor, DEFAULT_BG_COLOR);
            int bottomLayoutPaddingLeft = typedArray.getDimensionPixelSize(R.styleable.banner_view_bottomLayoutPaddingLeft, dp2px(DEFAULT_BG_PADDING_LEFT_RIGHT));
            int bottomLayoutPaddingRight = typedArray.getDimensionPixelSize(R.styleable.banner_view_bottomLayoutPaddingRight, dp2px(DEFAULT_BG_PADDING_LEFT_RIGHT));
            int bottomLayoutPaddingTop = typedArray.getDimensionPixelSize(R.styleable.banner_view_bottomLayoutPaddingTop, dp2px(DEFAULT_BG_PADDING_TOP_BOTTOM));
            int bottomLayoutPaddingBottom = typedArray.getDimensionPixelSize(R.styleable.banner_view_bottomLayoutPaddingBottom, dp2px(DEFAULT_BG_PADDING_TOP_BOTTOM));
            typedArray.recycle();

            // 如果没有设置过点的颜色，给一个默认颜色的ColorDrawable
            if (mPointDefaultDrawable == null) {
                mPointDefaultDrawable = new ColorDrawable(DEFAULT_POINT_COLOR);
            }
            if (mPointSelectDrawable == null) {
                mPointSelectDrawable = new ColorDrawable(DEFAULT_POINT_SELECT_COLOR);
            }

            // 指示点的控件设置属性
            RelativeLayout.LayoutParams pointParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            pointParams.addRule(getGravity(pointGravity));
            pointParams.addRule(CENTER_VERTICAL);
            mPointsLl.setLayoutParams(pointParams);

            // 文本设置属性
            mDescTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, descTextSize);
            mDescTv.setTextColor(descTextColor);
            RelativeLayout.LayoutParams descParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            descParams.addRule(getGravity(descTextGravity));
            descParams.addRule(CENTER_VERTICAL);
            mDescTv.setLayoutParams(descParams);

            // 底部控件设置属性
            mBottomLayout.setBackgroundColor(bottomLayoutBackGroundColor);
            mBottomLayout.setPadding(bottomLayoutPaddingLeft, bottomLayoutPaddingTop, bottomLayoutPaddingRight, bottomLayoutPaddingBottom);
        }
    }

    /**
     * 获取位置信息
     *
     * @return 根据int值返回位置
     */
    private int getGravity(int gravity) {
        return gravity == -1 ? ALIGN_PARENT_LEFT : gravity == 0 ? CENTER_HORIZONTAL : gravity == 1 ? ALIGN_PARENT_RIGHT : ALIGN_PARENT_LEFT;
    }

    /**
     * 设置自定义的BannerAdapter
     *
     * @param adapter
     */
    public void setAdapter(BannerBaseAdapter adapter) {
        this.mAdapter = adapter;
        mBannerVp.post(new Runnable() {
            @Override
            public void run() {
                mBannerVp.setAdapter(mAdapter);
                mBannerVp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        int index = position % mAdapter.getCount();
                        refreshDesc(index);
                        refreshPoint(index);
                    }
                });
                initPoints();
                refreshDesc(0);
                initHeight();
            }
        });
    }

    /**
     * 根据宽高比例来动态设置高度
     */
    private void initHeight() {
        // 如果有设置宽高比例,则根据宽度去动态设置高度
        if (mHeightProportion != 0 && mWidthProportion != 0) {
            int width = getMeasuredWidth();
            int height = (int) (width * mHeightProportion / mWidthProportion);
            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = height;
            setLayoutParams(params);
        }
    }

    /**
     * 开始滚动
     */
    public void startScroll() {
        mBannerVp.startScroll();
    }

    /**
     * 隐藏点
     */
    public void dismissPointView() {
        mPointsLl.setVisibility(View.GONE);
    }

    /**
     * 刷新文本
     *
     * @param index
     */
    private void refreshDesc(int index) {
        String textStr = mAdapter.getDescTitle(index);
        if (!TextUtils.isEmpty(textStr)) {
            mDescTv.setText(mAdapter.getDescTitle(index));
        }
    }

    /**
     * 刷新点的状态
     *
     * @param index
     */
    private void refreshPoint(int index) {
        // 获取上一个选中点的view，并将其状态恢复为默认值
        BannerPointView oldPointView = (BannerPointView) mPointsLl.getChildAt(mCurrentPosition);
        oldPointView.setDrawable(mPointDefaultDrawable);
        // 将当前选中位置赋值给mCurrentPosition
        mCurrentPosition = index;
        // 获取当前选中点的view，交将其状态设置为选中
        BannerPointView currentPointView = (BannerPointView) mPointsLl.getChildAt(mCurrentPosition);
        currentPointView.setDrawable(mPointSelectDrawable);
    }

    /**
     * 初始化指示点
     */
    private void initPoints() {
        int count = mAdapter.getCount();
        for (int i = 0; i < count; i++) {
            BannerPointView pointView = new BannerPointView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mPointSize, mPointSize);
            if (i == 0) {
                pointView.setDrawable(mPointSelectDrawable);
                params.leftMargin = 0;
            } else {
                pointView.setDrawable(mPointDefaultDrawable);
                params.leftMargin = mPointDistance;
            }
            pointView.setLayoutParams(params);
            mPointsLl.addView(pointView);
        }
    }

    private int dp2px(final float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int sp2px(final float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, getResources().getDisplayMetrics());
    }
}
