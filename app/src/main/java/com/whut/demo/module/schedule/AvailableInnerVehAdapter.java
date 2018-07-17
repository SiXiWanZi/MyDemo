package com.whut.demo.module.schedule;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whut.demo.R;

import java.util.List;

/**
 * <pre>
 *     author : 杨丽金
 *     time   : 2018/07/06
 *     desc   : 可用内转车列表适配器
 * </pre>
 */
public class AvailableInnerVehAdapter extends RecyclerView.Adapter<AvailableInnerVehAdapter.ViewHolder>{
    public interface OnItemClickListener{
        // checkbox点击事件
        void onMyCheckBoxChanged( int position,boolean checked);
    }

    private OnItemClickListener mOnItemClickListener;

    /**
     * 设置事件监听器
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    // 数据源
    private List<InnerVehScheduleRecord> mData;

    public AvailableInnerVehAdapter(List<InnerVehScheduleRecord> mList) {
        this.mData = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_innervehlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        InnerVehScheduleRecord record = mData.get(position);
        // 是否排班
        if (record.ISSCHEDULED()==0) {
            viewHolder.cb_innervehschedule_isScheduled.setChecked(false);

        }else{
            viewHolder.cb_innervehschedule_isScheduled.setChecked(true);
        }
        // 车牌号
        viewHolder.tv_innervehschedule_cph.setText(record.getCPH());
        // checkBox点击事件
       /* viewHolder.cb_innervehschedule_isScheduled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onMyCheckBoxChanged(position,isChecked);
                }
            }
        });*/
       // 不可监听CheckBox的onCheckedChanged，要监听setOnClickListener
        viewHolder.cb_innervehschedule_isScheduled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.cb_innervehschedule_isScheduled.isChecked()){
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onMyCheckBoxChanged(position,true);
                    }
                }else {
                    if (mOnItemClickListener != null){
                        mOnItemClickListener.onMyCheckBoxChanged(position,false);
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public InnerVehScheduleRecord getItem(int position) {
        return mData.get(position);
    }

    /**
     * 得到数据源
     */
    public List<InnerVehScheduleRecord> getDataSource(){
        return mData;
    }

    /**
     * 更新数据
     * @param data
     */
    public void upData(List<InnerVehScheduleRecord> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    //初始化子项控件
    static class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatCheckBox cb_innervehschedule_isScheduled;
        TextView tv_innervehschedule_cph;
        public ViewHolder(View itemView) {
            super(itemView);
            this.cb_innervehschedule_isScheduled=itemView.findViewById(R.id.cb_innervehschedule_isScheduled);
            this.tv_innervehschedule_cph = itemView.findViewById(R.id.tv_innervehschedule_cph);
        }
    }
}
