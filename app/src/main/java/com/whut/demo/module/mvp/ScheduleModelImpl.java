package com.whut.demo.module.mvp;

import com.whut.demo.config.ConfigURL;
import com.whut.demo.net.MyNetApi;
import com.whut.demo.utils.LogUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * <pre>
 *     author : 杨丽金
 *     time   : 2018/07/08
 *     desc   :
 * </pre>
 */
public class ScheduleModelImpl implements ScheduleContract.ScheduleModel {
    private static final String TAG = "ScheduleModelImpl";

    @Override
    public void getScheleDateArr(OnGetScheleDateCallback listener) {
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            List<String> list = new ArrayList<>();
            // 当前日期
            list.add((sdf.format(date)));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            // 前一天
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            list.add(sdf.format(calendar.getTime()));
            // 后一天
            calendar.add(Calendar.DAY_OF_MONTH, +2);
            list.add(sdf.format(calendar.getTime()));
            for (String s : list) {
                LogUtil.e(TAG, "list" + s);
            }
            if (listener != null) {
                listener.onSuccess(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            listener.onFailed();
        }
    }

    @Override
    public void getInnerVehDataList(String scheduledDate, int scheduledOrder, String ssgs
            , MyNetApi.OnCallBackListener listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("LX", "GetInnerVeh");
        params.put("scheduleDate", scheduledDate);// 排班日期
        params.put("scheduleOrder", scheduledOrder + "");// 排班班次
        params.put("ssgs", ssgs);// 所属公司
        LogUtil.e(TAG, "scheduleDate:" + scheduledDate);
        LogUtil.e(TAG, "scheduleOrder:" + scheduledOrder + "");
        //返回标记
        MyNetApi.init().
                query(ConfigURL.INNER_VEHICLE_SHCEDULE_RECORD, params, listener);
    }

    @Override
    public void submitRecordData(String scheduleDate, int scheduledOrder, String ssgs, String cphs,
                                 MyNetApi.OnCallBackListener listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("LX", "SubmitInnerVehSchedule");
        params.put("scheduleDate", scheduleDate);// 排班日期
        params.put("scheduleOrder", scheduledOrder + "");// 排班班次
        params.put("ssgs", ssgs);// 所属公司
        params.put("cphs", cphs);// 车牌号序列
        //返回标记
        MyNetApi.init().
                execute(ConfigURL.INNER_VEHICLE_SHCEDULE_RECORD, params, listener);
    }


    /**
     * 得到排班日期数组回调接口
     */
    public interface OnGetScheleDateCallback {
        // 成功
        void onSuccess(List<String> list);

        // 失败
        void onFailed();
    }

}
