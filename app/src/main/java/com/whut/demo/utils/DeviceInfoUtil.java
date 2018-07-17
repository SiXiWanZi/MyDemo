package com.whut.demo.utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.FileProvider;

import com.whut.demo.App;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * <pre>
 *  desc: 有关设备信息的工具类
 *  Created by 忘尘无憾 on 2018/01/21.
 *  version:
 * </pre>
 */

public class DeviceInfoUtil {

    /**
     * 获取MAC地址
     *
     * @return
     */
    public static String getMAC() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if ("wlan0".equalsIgnoreCase(nif.getName())) {
                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null || macBytes.length == 0) {
                        return "";
                    }
                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02X:", b));
                    }
                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    return res1.toString();
                }
            }
        } catch (Exception e) {
            LogUtil.e("获取MAC地址失败");
        }
        return "02:00:00:00:00:00";
    }

    /**
     * 获取IP地址
     *
     * @return
     */
    public static String getLocalHostIp() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            // 遍历所用的网络接口
            while (en.hasMoreElements()) {
                NetworkInterface nif = en.nextElement();// 得到每一个网络接口绑定的所有ip
                Enumeration<InetAddress> inet = nif.getInetAddresses();
                // 遍历每一个接口绑定的所有ip
                while (inet.hasMoreElements()) {
                    InetAddress ip = inet.nextElement();
                    if (!ip.isLoopbackAddress() && !ip.isLinkLocalAddress()) {
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            LogUtil.e("获取本地ip地址失败");
        }
        return "";

    }

    /**
     * 是否打开GPS<br />
     *
     * @return true 打开<br />
     * false 没打开
     */
    public static boolean isOpenGps() {
        LocationManager locationManager = (LocationManager) App.getContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 打开GPS设置页面，不带返回值
     *
     * @param context
     */
    public static void openGPSSetting(Context context) {
        Intent intent = new Intent(
                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 打开wifi设置页面
     * @param context
     */
    public static void openWifiSetting(Context context) {
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 检测手机是否在充电
     *
     * @return{true:在充电;flase：未充电}
     */
    public static boolean checkForPower(Context context) {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, filter);
        // There are currently three ways a device can be plugged in. We should check them all.
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = (chargePlug == BatteryManager.BATTERY_PLUGGED_USB);
        boolean acCharge = (chargePlug == BatteryManager.BATTERY_PLUGGED_AC);
        boolean wirelessCharge = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wirelessCharge = (chargePlug == BatteryManager.BATTERY_PLUGGED_WIRELESS);
        }
        return (usbCharge || acCharge || wirelessCharge);
    }

    /**
     * yx 2018/02/01
     * android N 安装APK文件
     */
    public static void installApk(Context mContext, String mSavePath, String apk_name) {

        File apkfile = new File(mSavePath, apk_name);
        if (!apkfile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //判断是否是Android N以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(mContext, "com.whut.innervehicleexternal.fileProvider", apkfile);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
        }
        mContext.startActivity(intent);
    }

    /**
     * 获取wifi状态
     * @param context
     * @return
     */
    public static int getWifiState(Context context) {
        WifiManager wifiManager = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(wifiManager != null)
            return wifiManager.getWifiState();  // 可能返回 0 1 2 3 4， 1 代表已关闭
        else
            return WifiManager.WIFI_STATE_UNKNOWN;
    }
}
