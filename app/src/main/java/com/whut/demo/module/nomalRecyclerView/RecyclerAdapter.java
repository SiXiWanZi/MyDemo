package com.whut.demo.module.nomalRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.whut.demo.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：鲁翩
 * 时间：2018/01/26
 * 功能：将Activity中数据通过适配器给控件
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int STATE_LOADING = 1;// 正在加载中
    public static final int STATE_LASTED = 2;// 没有更多数据
    public static final int STATE_ERROR = 3;// 加载失败
    public static final int STATE_OTHER = 4;// 什么都不显示
    // 每页个数
    public static final int PAGE_SIZE = 4;
    //普通布局的type
    static final int TYPE_ITEM = 0;
    //脚布局
    static final int TYPE_FOOTER = 1;
    private static final String STR_LOADING = "正在加载中";// 正在加载中
    private static final String STR_LASTED = "没有更多数据";// 没有更多数据
    private static final String STR_ERROR = "加载失败";// 加载失败
    // 标志底布局应该显示哪种(默认STATE_OTHER：什么都不显示)
    private int loadState = STATE_OTHER;
    // 数据源
    private List<Info> mDatas;
    private OnMyItemClickListener mOnMyItemClickListener;

    /**
     * 构造函数中传入数据源，无需传入上下文
     *
     * @param mDatas
     */
    public RecyclerAdapter(List<Info> mDatas) {
        this.mDatas = mDatas;
    }

    public int getLoadState() {
        return loadState;
    }

    public void setLoadState(int loadState) {
        this.loadState = loadState;
        notifyItemChanged(getItemCount() - 1);
    }

    public void setOnMyItemClickListener(OnMyItemClickListener lis) {
        this.mOnMyItemClickListener = lis;
    }

    //实例化viewholder,设置item布局
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        } else if (viewType == TYPE_FOOTER) {
            //脚布局
            View view = View.inflate(parent.getContext(), R.layout.item_end, null);
            FootViewHolder footViewHolder = new FootViewHolder(view);
            return footViewHolder;
        }
        return null;
    }

    //绑定数据
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            /*
            * 为Item绑定数据
             */
            //instanceof判断其左边对象是否为其右边类的实例，返回boolean类型的数据
            // 1，绑定数据
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            final Info info = mDatas.get(position);
            myViewHolder.tv_LoadMoreAndRefreshRecycler_name.setText(info.getName());
            myViewHolder.tv_LoadMoreAndRefreshRecycler_age.setText(info.getAge());
        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (loadState) {
                case STATE_LOADING:
                    /*
                     * 正在加载：
                     * 文字显示“加载中”，显示进度条
                      */
                    footViewHolder.tv_LoadMoreAndRefreshRecycler_footer_content.setText(STR_LOADING);
                    footViewHolder.itemView.setOnClickListener(null);
                    footViewHolder.pb_LoadMoreAndRefreshRecycler_footer_loading.setVisibility(View.VISIBLE);
                    if (mOnMyItemClickListener != null) {
                        mOnMyItemClickListener.onLoadMore();
                    }
                    break;
                case STATE_LASTED:
                    /*
                     * 没有更多数据
                     * 文字显示“没有更多数据”，隐藏进度条
                      */
                    footViewHolder.tv_LoadMoreAndRefreshRecycler_footer_content.setText(STR_LASTED);
                    footViewHolder.itemView.setOnClickListener(null);
                    footViewHolder.pb_LoadMoreAndRefreshRecycler_footer_loading.setVisibility(View.INVISIBLE);

                    break;
                case STATE_ERROR:
                    /*
                     * 加载失败，
                     * 文字显示“加载失败”，隐藏进度条
                      */
                    footViewHolder.tv_LoadMoreAndRefreshRecycler_footer_content.setText(STR_ERROR);
                    footViewHolder.pb_LoadMoreAndRefreshRecycler_footer_loading.setVisibility(View.INVISIBLE);
                    break;
                default:
                    /*
                    * 文字隐藏、进度条隐藏
                     */
                    footViewHolder.tv_LoadMoreAndRefreshRecycler_footer_content.setText("");
                    // 隐藏进度条
                    footViewHolder.pb_LoadMoreAndRefreshRecycler_footer_loading.setVisibility(View.INVISIBLE);
                    break;
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        //如果position加1正好等于所有item的总和,说明是最后一个item,将它设置为脚布局
        //返回的count也要加1,因为添加了一个脚布局
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        // 无论什么时候（加载成功，还是加载失败），始终都要添加一个底布局
        return mDatas.size() + 1;
    }


    public void setData(List<Info> data) {
        this.mDatas.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 更新数据源
     *
     * @param data
     */
    public void upData(List<Info> data) {
        this.mDatas = data;
        notifyDataSetChanged();
    }

    /**
     * 将数据追加在现有数据源上
     *
     * @param list 追加数据
     */
    public void appendData(List<Info> list) {
        this.mDatas.addAll(list);
        notifyDataSetChanged();
    }

    public interface OnMyItemClickListener {
        // Item条目点击事件
        void onMyItemClick(Info info, int position);

        // 加载更多事件
        void onLoadMore();
    }

    /**
     * 脚布局的ViewHolder
     */
    static class FootViewHolder extends RecyclerView.ViewHolder {
        // 进度条
        @BindView(R.id.pb_LoadMoreAndRefreshRecycler_footer_loading)
        ProgressBar pb_LoadMoreAndRefreshRecycler_footer_loading;
        // 提示信息
        @BindView(R.id.tv_LoadMoreAndRefreshRecycler_footer_content)
        TextView tv_LoadMoreAndRefreshRecycler_footer_content;

        public FootViewHolder(View footerView) {
            super(footerView);
            ButterKnife.bind(this, footerView);
        }
    }

    /**
     * 正常布局的ViewHolder
     */
    static class MyViewHolder extends RecyclerView.ViewHolder {
        // 姓名
        @BindView(R.id.tv_LoadMoreAndRefreshRecycler_name)
        TextView tv_LoadMoreAndRefreshRecycler_name;
        // 年龄
        @BindView(R.id.tv_LoadMoreAndRefreshRecycler_age)
        TextView tv_LoadMoreAndRefreshRecycler_age;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}





