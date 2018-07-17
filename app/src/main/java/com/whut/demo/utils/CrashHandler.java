package com.whut.demo.utils;

import android.content.Context;

/**
 * <pre>
 *     author : 杨丽金
 *     time   : 2018/07/01
 *     desc   : 全局异常捕获类
 * </pre>
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler{
    // 单例：整个应用程序中只需要一个异常捕获类就可以了
    // 上下文
    private Context mContext;
    private CrashHandler(){}

    @Override
    public void uncaughtException(Thread t, Throwable e) {
         // 出现未捕获异常时的处理办法
        // 在此可以把用户手机的一些信息以及异常信息捕获并上传,由于UMeng在这方面有很程序的api接口来调用，故没有考虑

        //干掉当前的程序
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private static class SingltonHolder{
        private static final CrashHandler instance = new CrashHandler();
    }

    public static CrashHandler getInstance(){
        return SingltonHolder.instance;
    }

    /**
     * 初始化变量
     * @param context
     */
    public void init(Context context) {
        this.mContext=context;
    }


}
