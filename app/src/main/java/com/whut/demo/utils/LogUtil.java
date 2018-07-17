package com.whut.demo.utils;


import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.whut.demo.BuildConfig;


/**
 * <pre>
 *  desc: 日志工具类
 *  Created by 忘尘无憾 on 2017/10/11.
 *  version:
 * </pre>
 */

public class LogUtil {
    /**********************************************
     * https://github.com/orhanobut/logger
     * compile 'com.orhanobut:logger:2.1.1'
     * ********************************************/
    private static final boolean IS_DEBUG = BuildConfig.ENABLE_LOG;
    private static final String TAG = "TCWCWH";

    /*********************
     *                   *
     *       Debug       *
     *                   *
     * ******************/
    /**
     * debug日志
     * 有默认TAG
     *
     * @param msg
     */
    public static void d(String msg) {
        d(TAG, msg);
    }

    /**
     * 带TAG的debug日志
     *
     * @param TAG
     * @param msg
     */
    public static void d(String TAG, String msg) {
        if (IS_DEBUG) {
            log("d", TAG, msg);
        }
    }

    /**
     * 格式化的debug日志
     * 默认TAG
     *
     * @param format 格式，形如"hello %s"
     * @param args   参数，"world"，类似C语言的输出
     */
    public static void d(String format, Object... args) {
        d(TAG, format, args);
    }

    /**
     * 带TAG的debug日志
     *
     * @param TAG
     * @param format
     * @param args
     */
    public static void d(String TAG, String format, Object... args) {
        if (IS_DEBUG) {
            log("d", TAG, format, args);
        }
    }

    /*********************
     *                   *
     *       Error       *
     *                   *
     * ******************/
    /**
     * Error日志
     *
     * @param msg
     */
    public static void e(String msg) {
        e(TAG, msg);
    }

    /**
     * Error日志
     *
     * @param TAG
     * @param msg
     */
    public static void e(String TAG, String msg) {
        if (IS_DEBUG) {
            log("e", TAG, msg);
        }
    }

    /**
     * Error日志
     *
     * @param TAG
     * @param format
     * @param args
     */
    public static void e(String TAG, String format, Object... args) {
        if (IS_DEBUG) {
            log("e", TAG, format, args);
        }
    }

    /*********************
     *                   *
     *      warning      *
     *                   *
     * ******************/
    /**
     * Warning日志
     *
     * @param msg
     */
    public static void w(String msg) {
        w(TAG, msg);
    }

    /**
     * Warning日志
     *
     * @param TAG
     * @param msg
     */
    public static void w(String TAG, String msg) {
        if (IS_DEBUG) {
            log("w", TAG, msg);
        }
    }

    /**
     * Warning日志
     *
     * @param format
     * @param args
     */
    public static void w(String format, Object... args) {
        w(TAG, format, args);
    }

    /**
     * Warning日志
     *
     * @param TAG
     * @param format
     * @param args
     */
    public static void w(String TAG, String format, Object... args) {
        if (IS_DEBUG) {
            log("w", TAG, format, args);
        }
    }

    /*********************
     *                   *
     *      verbose      *
     *                   *
     * ******************/


    /*********************
     *                   *
     *    information    *
     *                   *
     * ******************/
    /**
     * information日志
     *
     * @param msg
     */
    public static void i(String msg) {
        i(TAG, msg);
    }

    /**
     * information日志
     *
     * @param TAG
     * @param msg
     */
    public static void i(String TAG, String msg) {
        if (IS_DEBUG) {
            log("i", TAG, msg);
        }
    }

    /**
     * information日志
     *
     * @param format
     * @param args
     */
    public static void i(String format, Object... args) {
        i(TAG, format, args);
    }

    /**
     * information日志
     *
     * @param TAG
     * @param format
     * @param args
     */
    public static void i(String TAG, String format, Object... args) {
        if (IS_DEBUG) {
            log("i", TAG, format, args);
        }
    }

    /*********************
     *                   *
     *        wtf        *
     *                   *
     * ******************/
    /**
     * wtf日志
     *
     * @param msg
     */
    public static void wtf(String msg) {
        wtf(TAG, msg);
    }

    /**
     * wtf日志
     *
     * @param TAG
     * @param msg
     */
    public static void wtf(String TAG, String msg) {
        if (IS_DEBUG) {
            log("wtf", TAG, msg);
        }
    }

    /**
     * wtf日志
     *
     * @param format
     * @param args
     */
    public static void wtf(String format, Object... args) {
        wtf(TAG, format, args);
    }

    /**
     * wtf日志
     *
     * @param TAG
     * @param format
     * @param args
     */
    public static void wtf(String TAG, String format, Object... args) {
        if (IS_DEBUG) {
            log("wtf", TAG, format, args);
        }
    }

    /*********************
     *                   *
     *        Json       *
     *                   *
     * ******************/
    /**
     * 可打印Json的日志
     *
     * @param json
     */
    public static void json(String json) {
        json(TAG, json);
    }

    /**
     * 可打印Json的日志
     *
     * @param TAG
     * @param json
     */
    public static void json(String TAG, String json) {
        if (IS_DEBUG) {
            log("json", TAG, json);
        }
    }

    private static void log(String flag, String TAG, String msg) {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .tag(TAG)
                .build();
        Logger.clearLogAdapters();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
        switch (flag) {
            case "d":
                Logger.d(msg);
                break;
            case "e":
                Logger.e(msg);
                break;
            case "w":
                Logger.w(msg);
                break;
            case "v":
                Logger.v(msg);
                break;
            case "i":
                Logger.i(msg);
                break;
            case "wtf":
                Logger.wtf(msg);
                break;
            case "json":
                Logger.json(msg);
                break;
        }
    }

    private static void log(String flag, String TAG, String format, Object... args) {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .tag(TAG)
                .build();
        Logger.clearLogAdapters();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
        switch (flag) {
            case "d":
                Logger.d(format, args);
                break;
            case "e":
                Logger.e(format, args);
                break;
            case "w":
                Logger.w(format, args);
                break;
            case "v":
                Logger.v(format, args);
                break;
            case "i":
                Logger.i(format, args);
                break;
            case "wtf":
                Logger.wtf(format, args);
                break;
        }
    }
}
