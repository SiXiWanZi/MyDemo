package com.whut.demo.module.mvp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.whut.demo.App;
import com.whut.demo.R;
import com.whut.demo.bean.ResultBean;
import com.whut.demo.module.schedule.InnerVehScheduleRecord;
import com.whut.demo.net.MyNetApi;
import com.whut.demo.utils.GsonUtil;
import com.whut.demo.utils.LogUtil;

import java.util.Arrays;
import java.util.List;

import okhttp3.Call;

/**
 * <pre>
 *     author : 杨丽金
 *     time   : 2018/07/08
 *     desc   :
 * </pre>
 */
public class SchedulePresenterImpl extends ScheduleContract.SchedulePresenter {
    // Presenter中持有View、Model的引用model,view

    public static final int GET_INNER_VEH_DATA = 1;
    public static final String INNER_VEH_DATA = "INNER_VEH_DATA";
    private static final String TAG = "SchedulePresenterImpl";
    /*
     * 在Presenter写业务逻辑，一般会开启子线程获取网络上的数据，故更新UI需要和Handler配合
     */
    private Handler mHandler;

    /**
     * 重写构造函数
     * 1，实例化Handler
     * 2，通过super调用父类构造器，实例化Model对象
     */
    public SchedulePresenterImpl() {
        super();
        mHandler = new MyHandler();
    }

    /**
     * 得到排班日期
     */
    @Override
    public void getScheduleDate() {
        // 建立数据源
        model.getScheleDateArr(new ScheduleModelImpl.OnGetScheleDateCallback() {

            @Override
            public void onSuccess(List<String> list) {
                if (view != null) {
                    // 设置Spinner列表
                    view.setSpinnerItems(list);
                }
            }

            @Override
            public void onFailed() {
                view.showMsg("获取排班日期失败");
            }
        });

    }

    /**
     * 得到该公司可用的内转车
     *
     * @param scheduledDate：排班日期
     * @param scheduledOrder：排班时班次
     * @param ssgs:所属公司
     */
    @Override
    public void getInnerVehData(String scheduledDate, int scheduledOrder, String ssgs) {
        view.showLoading();
        // 连接数据库查询可用的内转车
        model.getInnerVehDataList(scheduledDate, scheduledOrder, ssgs, new MyNetApi.OnCallBackListener() {
            @Override
            public void callSuccessResult(ResultBean response) {
                // 将返回结果解析
                view.hideLoading();
                String data = response.getData();
                if (TextUtils.isEmpty(data)) {
                    return;
                } else {
                    Message msg = new Message();
                    msg.what = GET_INNER_VEH_DATA;
                    Bundle bundle = new Bundle();
                    bundle.putString(INNER_VEH_DATA, data);
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                }
            }

            @Override
            public void callFailResult(ResultBean response) {
                view.hideLoading();
                view.showMsg(TextUtils.isEmpty(response.getMsg()) ? App.getContext().getResources().getString(R.string.operate_failed) : response.getMsg());

            }

            @Override
            public void callError(Call call, Exception e, int id) {
                view.hideLoading();
                view.showMsg(App.getContext().getResources().getString(R.string.network_error));
            }

            @Override
            public void callExceptionResult(ResultBean response) {
                view.hideLoading();
                view.showMsg(App.getContext().getResources().getString(R.string.exception_tip));

            }
        });
    }

    @Override
    public void submitRecord(String scheduleDate, int ScheduledOrder, String ssgs, String cphs) {
        view.showLoading();
        // 连接数据库提交排班记录
        model.submitRecordData(scheduleDate, ScheduledOrder, ssgs, cphs, new MyNetApi.OnCallBackListener() {
            @Override
            public void callSuccessResult(ResultBean response) {
                // 将返回结果解析
                view.hideLoading();
                view.showMsg(TextUtils.isEmpty(response.getMsg()) ?
                        App.getContext().getResources().getString(R.string.operate_success) : response.getMsg());
            }

            @Override
            public void callFailResult(ResultBean response) {
                view.hideLoading();
                view.showMsg(TextUtils.isEmpty(response.getMsg()) ? App.getContext().getResources().getString(R.string.operate_failed) : response.getMsg());

            }

            @Override
            public void callError(Call call, Exception e, int id) {
                view.hideLoading();
                view.showMsg(App.getContext().getResources().getString(R.string.network_error));
            }

            @Override
            public void callExceptionResult(ResultBean response) {
                view.hideLoading();
                view.showMsg(App.getContext().getResources().getString(R.string.exception_tip));

            }
        });
    }

    @Override
    public void detachView() {

        super.detachView();
        // 移除Handler中的消息
        LogUtil.e(TAG, "mHandler.remove");
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 重写createModel()方法，在其中创建model实例
     *
     * @return
     */
    @Override
    protected ScheduleContract.ScheduleModel createModel() {
        return new ScheduleModelImpl();
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET_INNER_VEH_DATA:
                    Bundle bundle = msg.getData();
                    String data = bundle.getString(INNER_VEH_DATA);
                    List<InnerVehScheduleRecord> list = GsonUtil.GsonToList(data, InnerVehScheduleRecord.class);
                    InnerVehScheduleRecord[] a = list.toArray(new InnerVehScheduleRecord[0]);
                    LogUtil.e(TAG, "查询后台返回的数据:" + Arrays.toString(a));
                    view.updateRecyclerViewData(list);
                    break;

            }
        }
    }
}
