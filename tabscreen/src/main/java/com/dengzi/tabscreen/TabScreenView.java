package com.dengzi.tabscreen;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * @author Djk
 * @Title: 常见多条目菜单筛选
 * @Time: 2017/9/27.
 * @Version:1.0.0
 */
public class TabScreenView extends LinearLayout {
    private Context mContext;
    // tab view
    private LinearLayout mTabLl;
    // content view
    private RelativeLayout mContentRl;
    // adapter
    private BaseTabAdapter mAdapter;
    // content的总高度
    private int mContentHeight;
    // content子view的最大高度
    private int mContentItemMaxHeight;
    // 已经选把的item
    private int mSelectPosition = -1;
    // 动画执行时间
    private final int DURATION_TIME = 400;
    // 是否正在执行动画
    private boolean isAnimator = false;
    // content 所占最大的百分比
    private float mContentMaxPercent = 80f;

    private TabObserver mObserver;

    public TabScreenView(Context context) {
        this(context, null);
    }

    public TabScreenView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabScreenView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initLayout();
    }

    /**
     * 设置菜单的最大高度百分比
     *
     * @param percent 百分比（0-100）
     */
    public void setContentMaxPercnet(float percent) {
        this.mContentMaxPercent = percent;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mContentHeight == 0) {
            mContentHeight = MeasureSpec.getSize(heightMeasureSpec);
            int tabHeight = mTabLl.getMeasuredHeight();
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mContentRl.getLayoutParams();
            params.topMargin = tabHeight;
            mContentRl.setLayoutParams(params);
            mContentItemMaxHeight = (int) ((mContentHeight - tabHeight) * mContentMaxPercent / 100);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        // 设置菜单的最大高度百分比
        int childCount = mContentRl.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = mContentRl.getChildAt(i);
            int childHeight = childView.getMeasuredHeight();
            if (childHeight > mContentItemMaxHeight) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) childView.getLayoutParams();
                params.height = mContentItemMaxHeight;
                childView.setLayoutParams(params);
            }
        }
    }

    /**
     * 初始化layout
     */
    private void initLayout() {
        LayoutInflater.from(mContext).inflate(R.layout.tab_screen_view, this);
        mTabLl = (LinearLayout) findViewById(R.id.tab_ll);
        mContentRl = (RelativeLayout) findViewById(R.id.content_rl);
    }

    /**
     * 打开菜单view
     */
    private class MyObserver extends TabObserver {
        @Override
        public void closeMenu() {
            closeContentView();
        }
    }

    /**
     * 设置adapter
     *
     * @param adapter
     */
    public void setAdapter(BaseTabAdapter adapter) {
        if (adapter == null) {
            throw new NullPointerException("Adapter can not be null!");
        }
        if (mAdapter != null && mObserver != null) {
            adapter.unregistObserver(mObserver);
        }
        mObserver = new MyObserver();
        adapter.registObserver(mObserver);
        this.mAdapter = adapter;
        perparRequestlayout();
        requestLayout();
    }

    /**
     * 打开菜单view
     */
    private void perparRequestlayout() {
        int count = mAdapter.getCount();
        for (int i = 0; i < count; i++) {
            View tabView = mAdapter.getTabView(this, i);
            mTabLl.addView(tabView);
            LayoutParams params = (LayoutParams) tabView.getLayoutParams();
            params.weight = 1;
            tabView.setLayoutParams(params);
            View contentView = mAdapter.getContentView(this, i);
            mContentRl.addView(contentView);
            mContentRl.setVisibility(GONE);
            contentView.setVisibility(GONE);

            contentView.setClickable(true);
            tabView.setOnClickListener(new TabClickListener(i, tabView, contentView));
        }
        mContentRl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAnimator) return;
                closeContentView();
            }
        });
    }

    /**
     * 打开菜单view
     */
    private class TabClickListener implements OnClickListener {
        private int position;
        private View tabView;
        private View contentView;

        public TabClickListener(int position, View tabView, View contentView) {
            this.position = position;
            this.tabView = tabView;
            this.contentView = contentView;
        }

        @Override
        public void onClick(View v) {
            if (isAnimator) return;
            if (mSelectPosition == -1) {// 未选中状态，直接打开菜单
                openContentView(contentView, position);
            } else {
                if (mSelectPosition == position) {// 点击的条目正是打开的条目，直接关闭菜单
                    closeContentView();
                } else {// 切换contect view
                    View lastContentView = mContentRl.getChildAt(mSelectPosition);
                    View lastTabView = mTabLl.getChildAt(mSelectPosition);
                    lastContentView.setVisibility(View.GONE);
                    mSelectPosition = position;
                    contentView.setVisibility(View.VISIBLE);
                    mAdapter.closeTabItem(lastTabView);
                    mAdapter.openTabItem(tabView);
                }
            }
        }
    }

    /**
     * 打开菜单view
     */
    private void openContentView(View contentView, final int position) {
        isAnimator = true;
        mContentRl.setVisibility(VISIBLE);
        contentView.setVisibility(VISIBLE);
        mSelectPosition = position;
        View tabItemView = mTabLl.getChildAt(mSelectPosition);
        mAdapter.openTabItem(tabItemView);

        ObjectAnimator transtantAnimator = ObjectAnimator.ofFloat(mContentRl, "translationY", -mContentHeight, 0);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mContentRl, "alpha", 0f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(DURATION_TIME);
        animatorSet.playTogether(transtantAnimator, alphaAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimator = false;
            }
        });
        animatorSet.start();
    }

    /**
     * 关闭菜单view
     */
    private void closeContentView() {
        isAnimator = true;
        ObjectAnimator transtantAnimator = ObjectAnimator.ofFloat(mContentRl, "translationY", 0, -mContentHeight);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mContentRl, "alpha", 1f, 0f);
        View tabItemView = mTabLl.getChildAt(mSelectPosition);
        mAdapter.closeTabItem(tabItemView);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(DURATION_TIME);
        animatorSet.playTogether(transtantAnimator, alphaAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mContentRl.setVisibility(GONE);
                mContentRl.getChildAt(mSelectPosition).setVisibility(GONE);
                mSelectPosition = -1;
                isAnimator = false;
            }
        });
        animatorSet.start();
    }

}
