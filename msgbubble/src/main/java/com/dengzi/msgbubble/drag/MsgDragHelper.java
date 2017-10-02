package com.dengzi.msgbubble.drag;

import android.view.View;

/**
 * @author Djk
 * @Title: 拖拽工具类
 * @Time: 2017/10/2.
 * @Version:1.0.0
 */
public class MsgDragHelper {

    /**
     * 绑定要拖拽的view
     *
     * @param view     拖拽的view
     * @param listener 拖拽的监听回调
     */
    public static void bindView(View view, DragDisappearListener listener) {
        // 给要拖拽的view设置touch监听事件
        view.setOnTouchListener(new MsgDragTouchListener(view, view.getContext(), listener));
    }

    /**
     * 消失监听事件
     */
    public interface DragDisappearListener {
        void dismiss(View view);
    }
}
