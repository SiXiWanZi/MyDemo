package com.whut.demo.base;

/**
 * <pre>
 *     author : 杨丽金
 *     time   : 2018/07/08
 *     desc   : 封装View中的通用操作
 * </pre>
 */
public interface BaseView {
    // 显示错误信息
    void showMsg(String msg);
    // 显示进度条
    void showLoading();
    // 隐藏进度条
    void hideLoading();
}
