package com.whut.demo.module.splash;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.whut.demo.R;
import com.whut.demo.base.MyBaseActivity;
import com.whut.demo.config.Constant;
import com.whut.demo.module.login.LoginActivity;
import com.whut.demo.utils.AppInfoUtil;
import com.whut.demo.utils.DeviceInfoUtil;
import com.whut.demo.utils.LogUtil;
import com.whut.demo.utils.PermissionUtil;
import com.whut.demo.utils.ToastUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;

/**
 * <pre>
 *     author : 杨丽金
 *     time   : 2018/06/27
 *     desc   :
 * </pre>
 */
public class SplashActivity extends MyBaseActivity {
    private static final String TAG = "SplashActivity";
    private final MyPermissionListener mMyPermissionListener = new MyPermissionListener(this);
    @BindView(R.id.pb_splash_loading)
    ProgressBar pb_splash_loading;
    /**
     * 更新进度
     */
    private int progress;
    private ShowProgress showProgress;
    private Dialog mDownloadDialog;
    ;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void initData(Bundle savedInstanceState) {
        gainPermission();// 获取权限操作
    }

    /**
     * 获取权限
     */
    private void gainPermission() {
        PermissionUtil.clear();
        PermissionUtil.requestPermissions(context, Constant.PERMISSIONS, 0x00, mMyPermissionListener);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    /**
     * 获取到所需权限后<br />
     * 过渡页面初始化操作
     */
    private void init() {
        appUpdateCheck();//先进行软件更新检测操作
    }


    /**
     * 创建一个mHandler
     */
    private void appUpdateCheck() {
        // 显示progressBar进度条
        showLoading();
        new Thread(new MyRunnable(this)).start();
    }

    /**
     * 显示进度条
     */
    private void showLoading() {
        pb_splash_loading.setVisibility(View.VISIBLE);
    }

    /**
     * yx  2018/02/01
     * 显示软件更新对话框
     */
    protected void showNoticeDialog() {
        // 构造对话框
        new MaterialDialog.Builder(context)
                .title(getResources().getString(R.string.update_title))
                .cancelable(false)
                .content(getResources().getString(R.string.update_content))
                .negativeText(getResources().getString(R.string.update_negativeText))
                .positiveText(getResources().getString(R.string.update_positiveText))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        showDownloadDialog();
                    }
                }).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                ToastUtil.showShort(getResources().getString(R.string.update_negativeText));
                finish();
            }
        }).show();

    }

    /**
     * yx 2018/02/01
     * 显示软件下载对话框
     */
    private void showDownloadDialog() {
        // 构造软件下载对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.app_check_update, null);
        showProgress = (ShowProgress) v.findViewById(R.id.progBar);
        builder.setView(v).setCancelable(false);
        mDownloadDialog = builder.create();
        mDownloadDialog.show();
        // 下载文件
        downloadApk();
    }

    /**
     * yx
     * 下载apk文件
     */
    private void downloadApk() {
        // 启动新线程下载软件
        new downloadApkThread().start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CANCELED) {
            gainPermission();
        }
    }

    /**
     * 隐藏进度条
     */
    private void hideLoading() {
        pb_splash_loading.setVisibility(View.INVISIBLE);
    }

    /**
     * 静态内部类+弱引用
     */
    private static class MyPermissionListener implements PermissionUtil.OnPermissionListener {
        private final WeakReference<SplashActivity> mActivity;

        public MyPermissionListener(SplashActivity activity) {
            mActivity = new WeakReference<SplashActivity>(activity);
        }

        @Override
        public void onPermissionGranted() {
            Log.e(TAG, "权限获取成功");
            SplashActivity activity = mActivity.get();
            if (activity != null) {
                activity.init();
            }
        }

        @Override
        public void onPermissionDenied(String[] deniedPerssions, boolean alwaysDenied) {
            SplashActivity activity = mActivity.get();
            if (activity != null) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                intent.setData(uri);
                activity.startActivityForResult(intent, RESULT_CANCELED);
            }

        }
    }

    /**
     * @author fanjiajia
     * @date 2018/4/28
     * @desc 自定义MyRunnable 防止内存泄漏
     */
    private static class MyRunnable implements Runnable {

        private WeakReference<SplashActivity> mSplashActivity;

        public MyRunnable(SplashActivity splashActivity) {
            mSplashActivity = new WeakReference<SplashActivity>(splashActivity);
        }

        @Override
        public void run() {
            int xmlVersion = UpdateVersion.getXMLVersion(Constant.APP_NAME);// 服务器该软件的版本号
            int version = AppInfoUtil.getVersionCode(mSplashActivity.get());// 该软件版本号
            LogUtil.e(TAG, "xmlVersion=" + xmlVersion + "");
            LogUtil.e(TAG, "version=" + version + "");

            final SplashActivity activity = mSplashActivity.get();
            if (xmlVersion > version) {
                if (activity != null) {
                    LogUtil.e(TAG, "需更新");
                    activity.mHandler.post(new Runnable() {    // 这个Runnable并没有创建线程，而是发送了一个消息
                        @Override
                        public void run() {
                            // 关闭进度条
                            activity.hideLoading();
                            // 需要更新
                            activity.showNoticeDialog();
                        }
                    });
                }

            } else {
                if (activity != null) {
                    // 关闭进度条
                    activity.mHandler.post(new Runnable() {    // 这个Runnable并没有创建线程，而是发送了一个消息
                        @Override
                        public void run() {
                            activity.hideLoading();
                        }
                    });
                    // 不需要更新
                    LogUtil.e(TAG, "不需更新");
                    Intent intent = new Intent(activity, LoginActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }

            }
        }
    }

    /**
     * @author yx
     * @date 2018/02/01
     * 下载文件线程
     */
    private class downloadApkThread extends Thread {
        @Override
        public void run() {
            try {
                LogUtil.e(TAG,"开始下载");
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    // 获得存储卡的路径
                    String sdpath = Environment.getExternalStorageDirectory() + "/";
                    String savePath = sdpath + "download";
                    LogUtil.e(TAG, "savePath=" + savePath);
                    //用已知资源地址，连接网络获取apk文件大小，计算进度
                    URL url = new URL(UpdateVersion.getURL(Constant.APP_NAME));
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();
                    File file = new File(savePath);
                    // 判断文件目录是否存在
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    File apkFile = new File(savePath, Constant.APP_NAME);
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do {
                        //一次下载
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        // 设置进度条位置
                        showProgress.setProgress(progress);
                        if (numread <= 0) {
                            // 下载完成，安装APK

                            DeviceInfoUtil.installApk(context, savePath, Constant.APP_NAME);
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (true);
                    fos.close();
                    is.close();
                }
            } catch (FileNotFoundException e) {
                LogUtil.e(TAG,"FileNotFoundException");
                // 服务器更新文件不存在
                ToastUtil.showShort(getResources().getString(R.string.fileNotFound));
            } catch (MalformedURLException e) {
                LogUtil.e(TAG,"MalformedURLException");
                e.printStackTrace();
                ToastUtil.showShort("MalformedURLException");
            } catch (IOException e) {
                LogUtil.e(TAG,"IOException");
                e.printStackTrace();
                ToastUtil.showShort("IOException");
            }
            // 取消下载对话框显示
            mDownloadDialog.dismiss();
        }
    }
}
