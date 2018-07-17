package com.whut.demo.net;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;

import com.whut.demo.R;
import com.whut.demo.bean.ResultBean;
import com.whut.demo.config.ConfigURL;
import com.whut.demo.utils.GsonUtil;
import com.whut.demo.utils.LogUtil;
import com.whut.demo.utils.RegexUtil;
import com.whut.demo.utils.ToastUtil;
import com.whut.demo.utils.XTRZInfoUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.HashMap;

import okhttp3.Call;

/**
 * <pre>
 *  desc: 网络操作类
 *  Created by 忘尘无憾 on 2018/01/20.
 *  version:
 * </pre>
 */

public class NetApi {
    private Context context;// 上下文
    private ProgressDialog dialog;// 进度弹框

    /**
     * 返回一个NetApi对象，默认没有加载框
     *
     * @param context
     * @return
     */
    public static NetApi init(Context context) {
        return new NetApi(context, false);
    }

    /**
     * 返回一个NetApi对象
     *
     * @param context
     * @param isShow  true有加载框 false无加载框
     * @return
     */
    public static NetApi init(Context context, boolean isShow) {
        return new NetApi(context, isShow);
    }

    private NetApi() {

    }

    /**
     * 构造函数，根据isShow判断是否有加载框
     *
     * @param context
     * @param isShow
     */
    private NetApi(Context context, boolean isShow) {
        this.context = context;
        if (isShow) {
            if (dialog == null) {
                dialog = new ProgressDialog(context, R.style.CustomDialog);
                dialog.show();
            }
        }
    }

    public static abstract class OnCallBackListener {
        public abstract void callSuccessResult(ResultBean response);

        public abstract void callFailResult(ResultBean response);

        public void callError(Call call, Exception e, int id) {

        }
        public void callExceptionResult(Context context,ResultBean response) {
            String msg = response.getMsg();
            if (RegexUtil.isNotNullOrEmpty(msg)) {
                ToastUtil.showShort(msg);
            } else {
               //ToastUtil.showShort(context.getResources().getString(R.string.exception_tip));
                //Toast.makeText(AppCache.getContext(),"网络哈哈哈哈发",Toast.LENGTH_SHORT).show();
                ToastUtil.showShort(R.string.exception_tip);
            }
        }

    }

    /**
     * 查询操作
     *
     * @param urlName            文件名
     * @param onCallBackListener 网络操作回调
     */
    public void query(String urlName, @NonNull OnCallBackListener onCallBackListener) {
        invokePost(urlName, null, onCallBackListener, 1);
    }

    /**
     * 查询操作
     *
     * @param urlName            文件名
     * @param params             参数
     * @param onCallBackListener 网络操作回调
     */
    public void query(String urlName, HashMap<String, String> params, @NonNull OnCallBackListener onCallBackListener) {
        invokePost(urlName, params, onCallBackListener, 1);
    }

    /**
     * 增删改操作，默认传递系统日志
     *
     * @param urlName            文件名
     * @param onCallBackListener 网络操作回调
     */
    public void execute(String urlName, @NonNull OnCallBackListener onCallBackListener) {
        invokePost(urlName, null, onCallBackListener, 0);
    }

    /**
     * 增删改操作，默认传递系统日志
     *
     * @param urlName            文件名
     * @param params             参数
     * @param onCallBackListener 网络操作回调
     */
    public void execute(String urlName, HashMap<String, String> params, @NonNull OnCallBackListener onCallBackListener) {
        invokePost(urlName, params, onCallBackListener, 0);
    }

    /**
     * 增删改操作
     *
     * @param urlName            文件名
     * @param params             参数
     * @param flag               0表示加入系统日志参数，其他数字表示未加入系统日志参数<br/>
     *                           比如登录操作时，需自己添加系统日志所需字段
     * @param onCallBackListener 网络操作回调
     */
    public void execute(String urlName, HashMap<String, String> params, int flag, @NonNull OnCallBackListener onCallBackListener) {
        invokePost(urlName, params, onCallBackListener, flag);
    }

    private void invokePost(String urlName, HashMap<String, String> params, @NonNull final OnCallBackListener onCallBack, int flag) {

        //0代表进行增删改操作
        if (flag == 0) {
            params.put("XTRZ", GsonUtil.ObToStr(XTRZInfoUtil.getXTRZInfo()));
            LogUtil.e(XTRZInfoUtil.getXTRZInfo().toString());
        }

        LogUtil.e(ConfigURL.INNER_VEHICLE_EXTERNAL_BASE_URL + urlName);
        OkHttpUtils
                .post()
                .url(ConfigURL.INNER_VEHICLE_EXTERNAL_BASE_URL + urlName)
                .params(params == null ? new HashMap<String, String>() : params)
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtil.e(e.toString() + "--id:" + id);
                        ToastUtil.showShort(context.getResources().getString(R.string.network_error));
                        dismissDialog();
                        onCallBack.callError(call, e, id);
                    }

                    @Override
                    public void onResponse(ResultBean response, int id) {
                        LogUtil.e("返回内容" + response.getData());
                        dismissDialog();
                        switch (response.getCode()) {
                            case 200://返回成功
                                onCallBack.callSuccessResult(response);
                                break;
                            case 119://返回失败
                                onCallBack.callFailResult(response);
                                break;
                            case 110://后台产生异常
                                /*String msg = response.getMsg();
                                if (RegexUtil.isNotNullOrEmpty(msg)) {
                                    ToastUtil.showShort(msg);
                                } else {
                                    ToastUtil.showShort(context.getResources().getString(R.string.exception_tip));
                                }*/
                                onCallBack.callExceptionResult(context,response);
                                break;
                        }
                    }
                });
    }

    /**
     * 关闭加载框
     */
    private void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    private void showDialog() {
        if (dialog != null) {
            dialog.show();
        }
    }
}
