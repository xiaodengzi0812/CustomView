package com.dengzi.recyclerviewlib.section;

/**
 * @author Djk
 * @Title: 分组回调
 * @Time: 2017/9/22.
 * @Version:1.0.0
 */
public interface DecorationCallback {

    /*获取groupId，用此来判断一个类型的group*/
    long getGroupId(int position);

    /*获取当前分组的标识字符*/
    String getGroupText(int position);
}
