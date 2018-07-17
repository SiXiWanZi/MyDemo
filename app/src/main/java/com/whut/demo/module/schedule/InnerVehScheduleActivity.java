package com.whut.demo.module.schedule;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.whut.demo.R;
import com.whut.demo.base.MyBaseActivity;
import com.whut.demo.bean.Company;
import com.whut.demo.bean.ResultBean;
import com.whut.demo.config.ConfigURL;
import com.whut.demo.net.NetApi;
import com.whut.demo.utils.GsonUtil;
import com.whut.demo.utils.LogUtil;
import com.whut.demo.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * <pre>
 *     author : 杨丽金
 *     time   : 2018/07/06
 *     desc   : 内转车排班
 * </pre>
 */
public class InnerVehScheduleActivity extends MyBaseActivity {
    private static final String TAG = "InnerVehScheduleActivit";
    // 早班1
    private static final int SCHEDULE_ORDER_MORNING = 1;
    // 晚班5
    private static final int SCHEDULE_ORDER_NIGHT = 5;
    // 班次日期
    @BindView(R.id.sp_innervehschedule_scheduleDate)
    Spinner sp_innervehschedule_scheduleDate;
    // 班次
    @BindView(R.id.rg_innervehschedule_scheduleOrder)
    RadioGroup rg_innervehschedule_scheduleOrder;
    // 班次：早
    @BindView(R.id.rb_innervehschedule_scheduleOrder_morning)
    RadioButton rb_innervehschedule_scheduleOrder_morning;
    // 班次：晚
    @BindView(R.id.rb_innervehschedule_scheduleOrder_night)
    RadioButton rb_innervehschedule_scheduleOrder_night;
    // 可用内转车列表
    @BindView(R.id.rv_innervehschedule_availableInnerVeh)
    RecyclerView rv_innervehschedule_availableInnerVeh;
    // 提交按钮
    @BindView(R.id.tv_innervehschedule_submit)
    TextView tv_innervehschedule_submit;
    AvailableInnerVehAdapter mainAdapter;
    private int curCheckedScheduledOrder = SCHEDULE_ORDER_MORNING;

    @Override
    public void initData(Bundle savedInstanceState) {
        initScheduleDate();
        initScheduleOrder();
        initRecyclerView();
    }

    /**
     * 初始化班次日期
     */
    private void initScheduleDate() {
        // 建立数据源
        String[] mItems = getScheduleDate().toArray(new String[0]);
        LogUtil.e(TAG, Arrays.toString(mItems));
        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        sp_innervehschedule_scheduleDate.setAdapter(adapter);

    }

    /**
     * 初始化班次
     */
    private void initScheduleOrder() {
        curCheckedScheduledOrder = SCHEDULE_ORDER_MORNING;
        // 得到符合记录的内转车
//        GetInnerVehAndRefreshRecycleView(SCHEDULE_ORDER_MORNING);
    }

    /**
     * 初始化列表
     */
    private void initRecyclerView() {
        //设置布局管理器
        rv_innervehschedule_availableInnerVeh.setLayoutManager(new LinearLayoutManager(this));
        //初始化和设置Adapter
        mainAdapter = new AvailableInnerVehAdapter(new ArrayList<InnerVehScheduleRecord>());
        rv_innervehschedule_availableInnerVeh.setAdapter(mainAdapter);
    }

    private List<String> getScheduleDate() {
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
        return list;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_innervehschedule;
    }

    @Override
    public void setListener() {
        // Spinner下拉事件
        sp_innervehschedule_scheduleDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                ToastUtil.showShort(sp_innervehschedule_scheduleDate.getAdapter().getItem(pos).toString());
                // 得到符合记录的内转车
                GetInnerVehAndRefreshRecycleView(curCheckedScheduledOrder);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        // 班次单选按钮
        rg_innervehschedule_scheduleOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_innervehschedule_scheduleOrder_morning:
                        ToastUtil.showShort("早班");
                        curCheckedScheduledOrder = SCHEDULE_ORDER_MORNING;
                        // 得到符合记录的内转车
                        GetInnerVehAndRefreshRecycleView(curCheckedScheduledOrder);
                        break;
                    case R.id.rb_innervehschedule_scheduleOrder_night:
                        ToastUtil.showShort("晚班");
                        curCheckedScheduledOrder = SCHEDULE_ORDER_NIGHT;
                        GetInnerVehAndRefreshRecycleView(curCheckedScheduledOrder);
                        break;
                }
            }
        });
        // RecyclerView Item点击事件
        mainAdapter.setOnItemClickListener(new AvailableInnerVehAdapter.OnItemClickListener() {
            @Override
            public void onMyCheckBoxChanged(int position, boolean checked) {
                // 设置该车是否被选中
                mainAdapter.getItem(position).setISSCHEDULED(checked ? 1 : 0);
            }
        });
        tv_innervehschedule_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitScheduleReords();
            }
        });
    }

    /**
     * 得到符合条件的内转车，并显示在界面上
     */
    private void GetInnerVehAndRefreshRecycleView(int scheduleOrder) {
//封装网络url参数
        HashMap<String, String> params = new HashMap<>();
        params.put("LX", "GetInnerVeh");
        params.put("scheduleDate", sp_innervehschedule_scheduleDate.getSelectedItem().toString());// 排班日期
        params.put("scheduleOrder", scheduleOrder + "");// 排班班次
        params.put("ssgs", Company.getInstance().getCompanyName());// 所属公司
        LogUtil.e(TAG, "scheduleDate:" + sp_innervehschedule_scheduleDate.getSelectedItem().toString());
        LogUtil.e(TAG, "scheduleOrder:" + scheduleOrder + "");
        //返回标记
        NetApi.init(context, true).query(ConfigURL.INNER_VEHICLE_SHCEDULE_RECORD, params, new NetApi.OnCallBackListener() {
            @Override
            public void callSuccessResult(ResultBean response) {
                querySuccessOp(response);
            }

            @Override
            public void callFailResult(ResultBean response) {
                queryFailedOp(response);
            }
        });

    }

    /**
     * 提交排班记录
     */
    private void submitScheduleReords() {
        // 所属公司
        String ssgs = Company.getInstance().getCompanyName();
        // 排班时间：若选择的是早班，则
        String scheduleDate = sp_innervehschedule_scheduleDate.getSelectedItem().toString();
        // 排班班次
        int scheduleOrder = curCheckedScheduledOrder;
        List<InnerVehScheduleRecord> list = mainAdapter.getDataSource();
        List<String> cphs = new ArrayList<>();
        for (InnerVehScheduleRecord record : list) {
            if (record.ISSCHEDULED() == 1) {
                cphs.add(record.getCPH());
            }
        }
        if(cphs==null || cphs.size()==0){
            ToastUtil.showShort("请选择排班车辆");
            return;
        }
        LogUtil.e(TAG, "传到后台的车皮号：" + Arrays.toString(cphs.toArray(new String[0])));
        LogUtil.e(TAG, "车皮号序列化：" + GsonUtil.ObToStr(cphs));
        //封装网络url参数
        HashMap<String, String> params = new HashMap<>();
        params.put("LX", "SubmitInnerVehSchedule");
        params.put("scheduleDate", sp_innervehschedule_scheduleDate.getSelectedItem().toString());// 排班日期
        params.put("scheduleOrder", scheduleOrder + "");// 排班班次
        params.put("ssgs", Company.getInstance().getCompanyName());// 所属公司
        params.put("cphs", GsonUtil.ObToStr(cphs));// 车牌号序列
        LogUtil.e(TAG, "scheduleDate:" + sp_innervehschedule_scheduleDate.getSelectedItem().toString());
        LogUtil.e(TAG, "scheduleOrder:" + scheduleOrder + "");
        //返回标记
        NetApi.init(context, true).execute(ConfigURL.INNER_VEHICLE_SHCEDULE_RECORD, params, new NetApi.OnCallBackListener() {
            @Override
            public void callSuccessResult(ResultBean response) {
                submitSuccessOp(response);
            }

            @Override
            public void callFailResult(ResultBean response) {
                submitFailedOp(response);
            }
        });
    }

    /**
     * 查询可用内转车，查询成功
     *
     * @param response
     */
    private void querySuccessOp(ResultBean response) {
        LogUtil.e(TAG, "response:" + response.toString());
        // 将返回结果解析
        String data = response.getData();
        if (TextUtils.isEmpty(data)) {
            return;
        } else {
            List<InnerVehScheduleRecord> list = GsonUtil.GsonToList(data, InnerVehScheduleRecord.class);
            InnerVehScheduleRecord[] a=list.toArray(new InnerVehScheduleRecord[0]);
            LogUtil.e(TAG,"查询后台返回的数据:"+Arrays.toString(a));
            mainAdapter.upData(list);
        }
    }

    /**
     * 查询可用内转车，查询成功
     *
     * @param response
     */
    private void queryFailedOp(ResultBean response) {
        LogUtil.e(TAG, "response:" + response.toString());
    }

    private void submitSuccessOp(ResultBean response) {
//        ToastUtil.showShort("操作成功");
        Snackbar.make(tv_innervehschedule_submit, "操作成功", Snackbar.LENGTH_LONG)
                .show();
    }

    private void submitFailedOp(ResultBean response) {
//        ToastUtil.showShort(response.getMsg());
        String msg=response.getMsg();
        if (!TextUtils.isEmpty(msg)) {
            Snackbar.make(tv_innervehschedule_submit, msg, Snackbar.LENGTH_LONG)
                    .show();
        }

    }

}
