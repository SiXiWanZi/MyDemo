package com.whut.demo.config;

import android.Manifest;

/**
 * <pre>
 *  desc: 一些全局静态常量 eg：加速度阈值等
 *  Created by 忘尘无憾 on 2018/01/20.
 *  version:
 * </pre>
 */

public class Constant {

    //GPS监听action
    public static final String GPS_LISTENER_ACTION = "GpsBroadcast";
    // wifi广播
    public static final String WIFI_LISTENER_ACTION = "WifiBroadcast";

    //系统权限配置，读写SD、获取手机状态、获取位置信息
    public static final String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,

            Manifest.permission.READ_PHONE_STATE,

            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,

            Manifest.permission.CAMERA

    };


    /*******fjj*************/
    /*
     * SharedPref中使用的key
     */
    public static final String KEY_YHZH = "yhzh";
    public static final String KEY_YHBH = "yhbh";
    public static final String KEY_YHMC = "yhmc";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_MAC = "mac";
    public static final String KEY_IP = "ip";

    /*************************/

    /***********ylj**************/
    public static final int BAIDU_GPS_INTERVAL = 1000;
    public static final float USABLE_RADIUS=5;
    public static final String APP_NAME = "InnerVehicleExternal";
    /*************************/

}
