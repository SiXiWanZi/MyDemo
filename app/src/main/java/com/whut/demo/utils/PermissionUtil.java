package com.whut.demo.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *  desc: 运行时权限工具类
 *  Created by 忘尘无憾 on 2017/11/01.
 *  version:
 * </pre>
 */

public class PermissionUtil {
    //请求码
    private static int sRequestCode = -1;
    private static OnPermissionListener sListener;

    /**
     * 权限监听接口
     */
    public interface OnPermissionListener {
        //授权成功的回调
        void onPermissionGranted();

        /**
         * 请求权限失败
         *
         * @param deniedPerssions 被拒绝的权限集合
         * @param alwaysDenied    拒绝后是否提示
         */
        void onPermissionDenied(String[] deniedPerssions, boolean alwaysDenied);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void requestPermissionsAgain(@NonNull Context context, @Nullable String[] permissions, @NonNull int requestCode) {
        if (context instanceof Activity) {
            ((Activity) context).requestPermissions(permissions, requestCode);
        } else {
            throw new IllegalArgumentException("Context must be an Activity");
        }
    }

    /**
     * 请求权限
     *
     * @param context
     * @param permissions
     * @param requestCode
     * @param listener
     */
    public static void requestPermissions(@Nullable Context context, @Nullable String[] permissions, @Nullable int requestCode, OnPermissionListener listener) {
        sRequestCode = requestCode;
        sListener = listener;
        //获取需要得到授权的权限组
        String[] deniedPermissions = getDeniedPermissions(context, permissions);
        //如果有，且SDK版本大于23
        if (deniedPermissions.length > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissionsAgain(context, permissions, requestCode);
        } else {
            if (sListener != null) {
                sListener.onPermissionGranted();
            }
        }
    }

    /**
     * @param activity
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public static void onRequestPermissionsResult(@Nullable Activity activity, int requestCode, @Nullable String[] permissions, int[] grantResults) {
        if (sRequestCode != -1 && requestCode == sRequestCode) {
            if (sListener != null) {
                String[] deniedPermissions = getDeniedPermissions(activity, permissions);
                if (deniedPermissions.length > 0) {
                    boolean alwaysDenied = hasAlwaysDeniedPermission(activity, permissions);
                    sListener.onPermissionDenied(deniedPermissions, alwaysDenied);
                } else {
                    sListener.onPermissionGranted();
                }
            }
        }
    }

    /**
     * 获取未授权的权限
     *
     * @param context
     * @param permissions
     * @return
     */
    private static String[] getDeniedPermissions(@Nullable Context context, @Nullable String[] permissions) {
        List<String> deniedPermissions = new ArrayList<>();
        for (String permission : permissions) {
            //如果拒绝访问
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(permission);
            }
        }
        return deniedPermissions.toArray(new String[deniedPermissions.size()]);
    }

    /**
     * 是否彻底拒绝了某项权限
     */
    private static boolean hasAlwaysDeniedPermission(@NonNull Context context, @NonNull String... deniedPermissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }
        boolean rationale;
        for (String permission : deniedPermissions) {
            rationale = ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission);
            if (!rationale) {
                return true;
            }
        }
        return false;
    }

    public static void clear() {
        sListener = null;
        sRequestCode = -1;
    }

    /**
     * 开启app设置
     *
     * @param activity
     * @param requestCode
     */
    public static void startApplicationDetailsSettings(@Nullable Activity activity, int requestCode) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, requestCode);
    }
}
