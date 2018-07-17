package com.whut.demo.utils;

import android.widget.Toast;

import com.whut.demo.App;

/**
 * <pre>
 *  desc: Toast提示工具类
 *  Created by 忘尘无憾 on 2017/10/11.
 *  version:
 * </pre>
 */

public class ToastUtil {

    private static Toast toast;

    /**
     * 短时间显示Toast
     *
     * @param charSequence
     */
    public static void showShort(CharSequence charSequence) {
        if (toast == null) {
            toast = Toast.makeText(App.getContext(), charSequence, Toast.LENGTH_SHORT);
        } else {
            toast.setText(charSequence);
        }
        toast.show();
    }

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(int message) {
        if (toast == null) {
            toast = Toast.makeText(App.getContext(), message, Toast.LENGTH_LONG);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * 长时间显示Toast
     *
     * @param charSequence
     */
    public static void showLong(CharSequence charSequence) {
        if (toast == null) {
            toast = Toast.makeText(App.getContext(), charSequence, Toast.LENGTH_LONG);
        } else {
            toast.setText(charSequence);
        }
        toast.show();
    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong(int message) {
        if (toast == null) {
            toast = Toast.makeText(App.getContext(), message, Toast.LENGTH_LONG);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * 隐藏Toast
     */
    public static void hideToast() {
        if (toast != null) {
            toast.cancel();
        }
    }
}
