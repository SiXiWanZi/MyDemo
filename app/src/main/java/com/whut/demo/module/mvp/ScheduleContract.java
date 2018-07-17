package com.whut.demo.module.mvp;

import com.whut.demo.base.BaseModel;
import com.whut.demo.base.BasePresenter;
import com.whut.demo.base.BaseView;
import com.whut.demo.module.schedule.InnerVehScheduleRecord;
import com.whut.demo.net.MyNetApi;

import java.util.List;

/**
 * <pre>
 *     author : 杨丽金
 *     time   : 2018/07/08
 *     desc   : 内转车排班页面对应的契约类，
 *     在该契约中将BaseModel、BaseView、BasePresenter对应的子类放入其中。
 *     将这些子类放在一个文件中，是因为它们都是和这个页面相关的，放在一起方便查看修改
 * </pre>
 */
public interface ScheduleContract{
    interface ScheduleModel extends BaseModel {

        void getScheleDateArr(ScheduleModelImpl.OnGetScheleDateCallback onGetScheleDateCallback);

        void getInnerVehDataList(String scheduledDate, int scheduledOrder,String ssgs, MyNetApi.OnCallBackListener listener);

        void submitRecordData(String scheduleDate, int scheduledOrder, String ssgs, String cphs, MyNetApi.OnCallBackListener onCallBackListener);
    }

    interface ScheduleView extends BaseView {
        void setSpinnerItems(List<String> list);

        void updateRecyclerViewData(List<InnerVehScheduleRecord> list);
    }

    /**
     * 1，该类的定义中使用了泛型：Presenter是Model和View沟通的桥梁，故Presenter中需要同时持有Model和View的引用。
     * 但在底层接口中无法确定它们的具体类型，只确定它们是BaseView、BaseModel的子类。
     * 所以：使用泛型巧妙的解决了问题
     */
    abstract class SchedulePresenter extends BasePresenter<ScheduleView,ScheduleModel> {


        // 初始化时：排班日期（昨天、今天、明天）
        public abstract void getScheduleDate();


        public abstract void getInnerVehData(String scheduledDate, int scheduledOrder,String ssgs);

        public abstract void submitRecord(String s, int curCheckedScheduledOrder, String ssgs, String s1);
    }
}
