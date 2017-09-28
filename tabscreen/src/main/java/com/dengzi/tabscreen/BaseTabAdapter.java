package com.dengzi.tabscreen;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Djk
 * @Title: 多条目菜单筛选adapter基类
 * @Time: 2017/9/27.
 * @Version:1.0.0
 */
public abstract class BaseTabAdapter {
    // 观察者模式去调用关闭菜单
    private List<TabObserver> tabObserverList = new ArrayList<>();

    public void registObserver(TabObserver observer) {
        if (!tabObserverList.contains(observer)) {
            tabObserverList.add(observer);
        }
    }

    public void unregistObserver(TabObserver observer) {
        if (tabObserverList.contains(observer)) {
            tabObserverList.remove(observer);
        }
    }

    public void closeMenu() {
        for (TabObserver tabObserver : tabObserverList) {
            tabObserver.closeMenu();
        }
    }

    // 获取tab个数
    public abstract int getCount();

    // 获取tab view
    public abstract View getTabView(ViewGroup parent, int position);

    // 获取content view
    public abstract View getContentView(ViewGroup parent, int position);

    public void openTabItem(View tabItemView) {

    }

    public void closeTabItem(View tabItemView) {

    }
}
