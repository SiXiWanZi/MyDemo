package com.whut.demo.utils;


import com.whut.demo.App;
import com.whut.demo.config.Constant;

/**
 * <pre>
 *      desc: 登录信息工具类，将登录信息保存到SharedPreference中
 *      Created by fanjiajia on 2018/2/2.
 *  desc:
 */

public class LoginInfoUtil {

    /**
     * 用户账户：admin
     * @param yhzh
     */
    public static void setYHZH(String yhzh) {
        SharedPref.getInstance(App.getContext()).putString(Constant.KEY_YHZH, yhzh);
    }
    public static String getYHZH() {
       return SharedPref.getInstance(App.getContext()).getString(Constant.KEY_YHZH, "");
    }

    /**
     * YHBH:0001
     * @param yhbh
     */
    public static void setYHBH(String yhbh) {
        SharedPref.getInstance(App.getContext()).putString(Constant.KEY_YHBH, yhbh);
    }
    public static String getYHBH() {
        return SharedPref.getInstance(App.getContext()).getString(Constant.KEY_YHBH, "");
    }

    /**
     * YHMC:果园港
     * @param yhmc
     */
    public static void setYHMC(String yhmc) {
        SharedPref.getInstance(App.getContext()).putString(Constant.KEY_YHMC, yhmc);
    }
    public static String getYHMC() {
        return SharedPref.getInstance(App.getContext()).getString(Constant.KEY_YHMC, "");
    }

    /**
     * 密码
     * @param password
     */
    public static void setPassword(String password) {
        SharedPref.getInstance(App.getContext()).putString(Constant.KEY_PASSWORD, password);
    }
    public static String getPassword() {
        return SharedPref.getInstance(App.getContext()).getString(Constant.KEY_PASSWORD, "");
    }

    /**
     * 删除密码
     */
    public static void removePassword() {
        SharedPref.getInstance(App.getContext()).remove(Constant.KEY_PASSWORD);
    }

    /**
     * 判断是否保存有密码
     * @return
     */
    public static boolean containPassword(){
        return SharedPref.getInstance(App.getContext()).contains(Constant.KEY_PASSWORD);
    }


    /**
     * MAC地址
     * @param mac
     */
    public static void setMAC(String mac) {
        SharedPref.getInstance(App.getContext()).putString(Constant.KEY_MAC, mac);
    }
    public static String getMAC() {
        return SharedPref.getInstance(App.getContext()).getString(Constant.KEY_MAC, "");
    }

    /**
     * IP地址
     * @param IP
     */
    public static void setIP(String IP) {
        SharedPref.getInstance(App.getContext()).putString(Constant.KEY_IP, IP);
    }
    public static String getIP() {
        return SharedPref.getInstance(App.getContext()).getString(Constant.KEY_IP, "");
    }
}
