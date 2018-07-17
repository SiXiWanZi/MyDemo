package com.whut.demo.base;

import com.whut.demo.utils.LogUtil;

/**
 * <pre>
 *     author : 杨丽金
 *     time   : 2018/07/08
 *     desc   : 封装Presenter中的通用操作
 *     泛型的使用：Presenter是View和Model沟通的桥梁，必须同时持有Model和View的引用。
 *     但在底层接口中无法确定它们的类型，只能确定它们是BaseView和BaseModel的子类，使用泛型巧妙地解决了这个问题
 * </pre>
 */
public abstract class  BasePresenter <V extends BaseView,M extends BaseModel>{
    private static final String TAG = "BasePresenter";
    // presenter中持有Model、View的引用
    public V view;
    public M model;

    public BasePresenter(){
        model=createModel();
    }

    // 设置View实例
    public void attachView(V view) {
        this.view=view;
    }

    // 将view引用置为null，防止内存泄漏。但销毁view后需要考虑：
    // 例如现在的场景是：Button点击后通过网络请求获取一段数据显示在TextView中。
    // 当Button点击后，数据返回前，退出页面，可能会因为调用view的方法抛出空指针异常，
    // 1）所以要注意判空。
    // 2）或者，在将view置为null的同时将handler中的任务取消，防止资源浪费
    public void detachView(){
        LogUtil.e(TAG,"this.view = null");
        this.view = null;
    }

    // 创建Model实例
    protected abstract M createModel();
}
