package com.whut.demo.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * <pre>
 *  desc: APP信息工具类
 *  Created by 忘尘无憾 on 2018/01/21.
 *  version:
 * </pre>
 */

public class AppInfoUtil {

    /**
     * 判断本应用是否存活
     * @param mContext
     * @param packageName
     * @return
     */
    public static boolean isAppAlive(Context mContext, String packageName) {
        boolean isAPPRunning = false;
        // 获取activity管理对象
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        // 获取所有正在运行的app
        List<ActivityManager.RunningAppProcessInfo> appProcessInfoList = activityManager.getRunningAppProcesses();
        // 遍历，进程名即包名
        for (ActivityManager.RunningAppProcessInfo appInfo : appProcessInfoList) {
            if (packageName.equals(appInfo.processName)) {
                isAPPRunning = true;
                break;
            }
        }
        return isAPPRunning;
    }

    /**
     * 获取当前app版本号
     * yx 2018/02/01
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }
}
