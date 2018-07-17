package com.whut.demo.module.mvp;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.whut.demo.R;
import com.whut.demo.base.MyBaseActivity;
import com.whut.demo.bean.Company;
import com.whut.demo.module.schedule.AvailableInnerVehAdapter;
import com.whut.demo.module.schedule.InnerVehScheduleRecord;
import com.whut.demo.utils.GsonUtil;
import com.whut.demo.utils.LogUtil;
import com.whut.demo.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemSelected;

/**
 * <pre>
 *     author : 杨丽金
 *     time   : 2018/07/08
 *     desc   :
 * </pre>
 */
public class InnerVehScheduleActivity extends MyBaseActivity implements ScheduleContract.ScheduleView{
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
    // 进度条
    @BindView(R.id.pb_innervehschedule_loading)
    ProgressBar pb_innervehschedule_loading;
    // 列表适配器
    AvailableInnerVehAdapter mainAdapter;
    // 班次（默认为早班）
    private int curCheckedScheduledOrder = SCHEDULE_ORDER_MORNING;
    // 所属公司
    private String ssgs= Company.getInstance().getCompanyName();

    // View中持有Presenter的引用
    private ScheduleContract.SchedulePresenter mPresenter;
    @Override
    public void initData(Bundle savedInstanceState) {
        // 初始化
        mPresenter=new SchedulePresenterImpl();
        mPresenter.attachView(this);
        initScheduleDate();
        initScheduleOrder();
        initRecyclerView();
    }

    /**
     * 初始化RecyclerView的列表
     */
    private void initRecyclerView() {
        //设置布局管理器
        rv_innervehschedule_availableInnerVeh.setLayoutManager(new LinearLayoutManager(this));
        //初始化和设置Adapter
        mainAdapter = new AvailableInnerVehAdapter(new ArrayList<InnerVehScheduleRecord>());
        rv_innervehschedule_availableInnerVeh.setAdapter(mainAdapter);
    }

    @Override
    public void setListener() {
        mainAdapter.setOnItemClickListener(new AvailableInnerVehAdapter.OnItemClickListener() {
            @Override
            public void onMyCheckBoxChanged(int position, boolean checked) {
                // 设置该车是否被选中
                mainAdapter.getItem(position).setISSCHEDULED(checked ? 1 : 0);
            }
        });
    }

    /**
     * 初始化排班班次
     */
    private void initScheduleOrder() {
        // 默认选中早班
        rb_innervehschedule_scheduleOrder_morning.setChecked(true);
    }

    /**
     * 初始化排班日期
     */
    private void initScheduleDate() {
        mPresenter.getScheduleDate();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_innervehschedule;
    }

    /**
     * Spinner选择事件
     * @param position
     */
    @OnItemSelected(R.id.sp_innervehschedule_scheduleDate)//默认callback为ITEM_SELECTED
    void onItemSelected(int position) {
        showMsg(sp_innervehschedule_scheduleDate.getAdapter().getItem(position).toString());
        // 得到符合记录的内转车
        mPresenter.getInnerVehData(sp_innervehschedule_scheduleDate.getSelectedItem().toString(),
                curCheckedScheduledOrder,ssgs);
    }

    /**
     * 班次单选按钮点击事件
     * @param view
     * @param ischanged
     */
    @OnCheckedChanged({R.id.rb_innervehschedule_scheduleOrder_morning,R.id.rb_innervehschedule_scheduleOrder_night})
    public void OnCheckedChangeListener(CompoundButton view, boolean ischanged ) {
        switch (view.getId()) {
            case R.id.rb_innervehschedule_scheduleOrder_morning:
                if (ischanged){//注意：这里一定要有这个判断，只有对应该id的按钮被点击了，ischanged状态发生改变，才会执行下面的内容
                    //这里写你的按钮变化状态的UI及相关逻辑
                    showMsg("早班");
                    // 得到符合记录的内转车
                    curCheckedScheduledOrder = SCHEDULE_ORDER_MORNING;
                    mPresenter.getInnerVehData(sp_innervehschedule_scheduleDate.getSelectedItem().toString(),
                            curCheckedScheduledOrder,ssgs);
                }
                break;
            case R.id.rb_innervehschedule_scheduleOrder_night:
                if (ischanged) {
                    //这里写你的按钮变化状态的UI及相关逻辑
                    showMsg("晚班");
                    curCheckedScheduledOrder = SCHEDULE_ORDER_NIGHT;
                    mPresenter.getInnerVehData(sp_innervehschedule_scheduleDate.getSelectedItem().toString(),
                            curCheckedScheduledOrder,ssgs);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 提交排班记录
     */
    @OnClick(R.id.tv_innervehschedule_submit)
    public void submitScheduleRecord(){
        // 得到选中的车辆
        List<InnerVehScheduleRecord> list = mainAdapter.getDataSource();
        List<String> cphs = new ArrayList<>();
        for (InnerVehScheduleRecord record : list) {
            if (record.ISSCHEDULED() == 1) {
                cphs.add(record.getCPH());
            }
        }
        if(cphs==null || cphs.size()==0){
            showMsg("请选择排班车辆");
            return;
        }
        showMsg("提交");
        mPresenter.submitRecord(sp_innervehschedule_scheduleDate.getSelectedItem().toString(),
                curCheckedScheduledOrder,ssgs,GsonUtil.ObToStr(cphs));
    }

    @Override
    public void showMsg(String msg) {
        ToastUtil.showShort(msg);
    }

    @Override
    public void showLoading() {
        pb_innervehschedule_loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        pb_innervehschedule_loading.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setSpinnerItems(List<String> list) {
        String[] mItems = list.toArray(new String[0]);
        LogUtil.e(TAG, Arrays.toString(mItems));
        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        sp_innervehschedule_scheduleDate.setAdapter(adapter);
    }

    /**
     * 更新recyclerView数据源
     * @param list
     */
    @Override
    public void updateRecyclerViewData(List<InnerVehScheduleRecord> list) {
        mainAdapter.upData(list);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 将view引用置为null，防止内存泄漏。但销毁view后需要考虑：
        // 例如现在的场景是：Button点击后通过网络请求获取一段数据显示在TextView中。
        // 当Button点击后，数据返回前，退出页面，可能会因为调用view的方法抛出空指针异常，
        // 1）所以要注意判空。
        // 2）或者，在将view置为null的同时将handler中的任务取消，防止资源浪费
        mPresenter.detachView();
    }
}
