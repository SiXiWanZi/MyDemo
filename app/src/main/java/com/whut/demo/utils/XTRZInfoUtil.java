package com.whut.demo.utils;

import com.whut.demo.bean.XTRZBean;


/**
 * <pre>
 *  desc: 获取系统日志信息的单例类
 *  Created by 忘尘无憾 on 2018/01/20.
 *  version:
 * </pre>
 */

public class XTRZInfoUtil {
    private XTRZBean xtrzBean = new XTRZBean();

    /**
     * 定义一个私有的构造函数，用于单例模式
     */
    private XTRZInfoUtil() {
    }

    /**
     * 当进行登录操作的时候，进行初始化。初始化一次就可以了
     * <br/>
     * 保证getInstance()获取到的类是一个
     */
    public static void init() {
        getInstance().onInit();
    }

    /**
     * 单例初始化过程时，将YHZH、YHMC等信息存入xtrzBean中
     */
    private void onInit() {
        xtrzBean.setYHZH(LoginInfoUtil.getYHBH());// 0001
        xtrzBean.setYHMC(LoginInfoUtil.getYHMC());//用户名称：果园港
        xtrzBean.setIPDZ(LoginInfoUtil.getIP());//IP地址
        xtrzBean.setMACDZ(LoginInfoUtil.getMAC());//MAC地址
    }

    private static XTRZInfoUtil getInstance() {
        return SingletonHolder.xtrzInfoUtil;
    }

    /**
     * 获取系统日志信息
     *
     * @return
     */
    public static XTRZBean getXTRZInfo() {
        return getInstance().xtrzBean;
    }

    /**
     * 内部类的单例模式，由JVM保证线程安全性
     */
    private static class SingletonHolder {
        private static XTRZInfoUtil xtrzInfoUtil = new XTRZInfoUtil();
    }
}
