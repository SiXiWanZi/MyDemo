package com.whut.demo.module.gps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;

import com.whut.demo.config.Constant;
import com.whut.demo.utils.DeviceInfoUtil;

/**
 * <pre>
 *  desc: 用于监听GPS
 *  Created by 忘尘无憾 on 2018/01/21.
 *  version:
 * </pre>
 */

public class GPSListenerRecevicer extends BroadcastReceiver {
    private static final String TAG = "GPSListenerRecevicer";


    @Override
    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();
        // GPS开关广播
        if (LocationManager.PROVIDERS_CHANGED_ACTION.equals(action) || Constant.GPS_LISTENER_ACTION.equals(action)) {
            Log.e("LoginActivity", "收到GPS广播");
            // 判断程序是否开启GPS
            if (!DeviceInfoUtil.isOpenGps()) {
                Log.e("LoginActivity", "没开GPS");
                 /*
                         * wgps处于关闭状态：若“请打开gps对话框”正在显示，将该对话框显示在前台；
                         *  未正在显示：创建新的并显示
                          */
                Intent intent1 = new Intent(context, OpenGPSActivity.class);
                // 以SingleTask的模式启动（栈内复用）
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
            } else {
                 /*
                                 * gps处于开启状态：若“请打开gps对话框”正在显示，将该对话框关闭；
                         *  未正在显示：Nothing
                                 */
                Log.e("LoginActivity", "GPS已开");
                OpenGpsActivityCollector.finishAll();
            }
        }
    }
}
