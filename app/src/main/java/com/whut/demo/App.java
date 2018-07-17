package com.whut.demo;

import android.app.Application;
import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;
import com.whut.demo.config.ConfigSwitch;
import com.whut.demo.utils.CrashHandler;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;


/**
 * <pre>
 *  desc:
 *  Created by 忘尘无憾 on 2017/10/09.
 *  version:
 * </pre>
 */

public class App extends Application {
    // 全局Context
    private static Context sContext;

    /**
     * 得到全局Context
     *
     * @return
     */
    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        // 相关初始化
        init();
    }

    private void init() {
        configOkHttpClient();
        configTencentBugly();
    }

    /**
     * 配置OkHttpClient
     */
    private void configOkHttpClient() {
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);//忽略证书
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .hostnameVerifier(new HostnameVerifier() {//允许访问https网站,并忽略证书
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    /**
     * 配置腾讯Bugly
     */
    private void configTencentBugly() {
        if (ConfigSwitch.ENABLE_TENCENT_BUGLY) {//如果启用，使用腾讯Bugly
            CrashReport.initCrashReport(sContext, ConfigSwitch.APPID, BuildConfig.ENABLE_LOG);
        } else {//否则，开启全局异常管理类
            CrashHandler handler = CrashHandler.getInstance();
            handler.init(sContext);
            Thread.setDefaultUncaughtExceptionHandler(handler);
        }
    }
}
