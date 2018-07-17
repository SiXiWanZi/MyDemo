package com.whut.demo.base;

import android.os.Bundle;

/**
 * <pre>
 *     author : 杨丽金
 *     time   : 2018/03/23
 *     desc   :
 * </pre>
 */
public interface MyUiCallback {
    // 在setContentView（）之前调用的方法
    void beforeSetConentView(Bundle savedInstanceState);

    // 下面的三个方法都是在setContentView（）之后调用的
    void initData(Bundle savedInstanceState);

    void setListener();

    int getLayoutId();
}
