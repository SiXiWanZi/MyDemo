package com.whut.demo.base;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.whut.demo.config.Constant;
import com.whut.demo.module.gps.GPSListenerRecevicer;
import com.whut.demo.utils.ActivityCollector;
import com.whut.demo.utils.PermissionUtil;

import butterknife.ButterKnife;


/**
 * <pre>
 *     author : 杨丽金
 *     time   : 2018/03/23
 *     desc   :
 * </pre>
 */
public abstract class MyBaseActivity extends AppCompatActivity implements MyUiCallback {
    public static AlertDialog mAlertDialog;
    protected Activity context;
    // GPS检测监听
    private GPSListenerRecevicer receiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 将该Activity加入到专门的集合中，统一管理
        ActivityCollector.addActivity(this);

        Log.e(getClass().getSimpleName(), "onCreate");
        this.context = this;
        // 在setContentView之前调用的调度
        beforeSetConentView(savedInstanceState);
        // 绑定布局+ButterKnife
        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
            ButterKnife.bind(this);
        }
        // 初始化数据
        initData(savedInstanceState);
        // 设置监听事件
        setListener();
    }


    @Override
    public void beforeSetConentView(Bundle savedInstanceState) {

    }

    @Override
    public void setListener() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(getClass().getSimpleName(), "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(getClass().getSimpleName(), "onStop");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(getClass().getSimpleName(), "onDestroy");
        // 取消权限申请的回调
        PermissionUtil.clear();
        // 将该Activity从专门的集合中删除
        ActivityCollector.removeActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(getClass().getSimpleName(), "onPause");
        //暂停的时候进行解除是为了当应用程序进入后台后不必再监听
        unregisterGPSListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(getClass().getSimpleName(), "onResume");
        /**
         * 在每次resume过程中，都会注册监听并开启监听广播
         */
        registerGPSListener();
        sendGPSListenerBroadcast();
    }

    private void registerGPSListener() {
        IntentFilter intentFilter = new IntentFilter();
        // 用于监听手机状态栏GPS开关
        intentFilter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
        // 自定义的GPS监听行动
        intentFilter.addAction(Constant.GPS_LISTENER_ACTION);
        receiver = new GPSListenerRecevicer();
        registerReceiver(receiver, intentFilter);
    }

    /**
     * 发送GPS广播监听 <br />
     * 每次activity resume的时候进行一次监测GPS是否开启
     */
    private void sendGPSListenerBroadcast() {
        Intent intent = new Intent();
        intent.setAction(Constant.GPS_LISTENER_ACTION);
        sendBroadcast(intent);
    }

    /**
     * 用于运行时权限返回结果监测
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtil.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 解除GPS监听广播
     */
    private void unregisterGPSListener() {
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }
}
