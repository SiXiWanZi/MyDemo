package com.whut.demo.module.recyclerviewAndSwipeRe;

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
 * <pre>
 *     author : 杨丽金
 *     time   : 2018/07/12
 *     desc   :
 * </pre>
 */
public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int STATE_LOADING = 1;// 正在加载中
    public static final int STATE_LASTED = 2;// 没有更多数据
    public static final int STATE_ERROR = 3;// 加载失败
    // 底布局
    private static final int TYPE_FOOTER = 0;
    // Item布局
    private static final int TYPE_ITEM = 1;
    private static final String STR_LOADING = "正在加载中";// 正在加载中
    private static final String STR_LASTED = "没有更多数据";// 没有更多数据
    private static final String STR_ERROR = "加载失败";// 加载失败
    // 数据源
    private List<Goods> mDatas;
    // 控制底布局中文字哪种该显示
    private int loadState;// 不给该值设置默认值
    private OnLoadMoreListener mOnLoadMoreListener;

    /**
     * 写个构造函数，传入数据源
     *
     * @param mData
     */
    public MyAdapter(List<Goods> mData) {
        this.mDatas = mData;
    }

    public int getLoadState() {
        return loadState;
    }

    public void setLoadState(int loadState) {
        this.loadState = loadState;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    /**
     * 必须实现
     *
     * @param viewGroup
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.footer_goodslist, viewGroup, false);
            return new FooterViewHolder(view);
        } else if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_goodslist, viewGroup, false);
            return new ItemViewHolder(view);
        }
        return null;
    }

    /**
     * 必须实现，给控件绑定数据
     *
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ItemViewHolder) {
            ItemViewHolder holder = (ItemViewHolder) viewHolder;
            // item数据绑定
            Goods goods = mDatas.get(position);
            holder.tv_showgoodslist_item_name.setText(String.format("序号：%s；名称：%s",
                    goods.getROWNUM(), goods.getNAME()));
            holder.tv_showgoodslist_item_description.setText(goods.getDESCRIPTION());
        } else if (viewHolder instanceof FooterViewHolder) {
            FooterViewHolder holder = (FooterViewHolder) viewHolder;
            // 数据绑定
            switch (loadState) {
                case STATE_LOADING:
                    // 正在加载
                    holder.tv_showgoodslist_footer_content.setText(STR_LOADING);
                    holder.itemView.setOnClickListener(null);
                    holder.pb_showgoodslist_footer_loading.setVisibility(View.VISIBLE);
                    break;
                case STATE_LASTED:
                    // 没有更多数据
                    holder.tv_showgoodslist_footer_content.setText(STR_LASTED);
                    holder.itemView.setOnClickListener(null);
                    holder.pb_showgoodslist_footer_loading.setVisibility(View.INVISIBLE);

                    break;
                case STATE_ERROR:
                    /*
                     * 加载失败，
                     * 设置点击事件，点击后重新加载
                      */
                    holder.tv_showgoodslist_footer_content.setText(STR_ERROR);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // 当点击时加载更多
                            if (mOnLoadMoreListener != null) {
                                mOnLoadMoreListener.loadMore();
                            }
                        }
                    });
                    holder.pb_showgoodslist_footer_loading.setVisibility(View.INVISIBLE);
                default:
                    // 文字显示“什么也不显示”
                    holder.tv_showgoodslist_footer_content.setText("");
                    // 隐藏进度条
                    holder.pb_showgoodslist_footer_loading.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            // 最后一项，就是底布局
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    /**
     * 必须实现
     *
     * @return
     */
    @Override
    public int getItemCount() {
//        return mDatas.size()==0?0: mDatas.size() + 1;
        return mDatas.size() + 1;
    }

    /**
     * 更新数据源
     *
     * @param list 新的数据源
     */
    public void updateData(List<Goods> list) {
        this.mDatas = list;
        notifyDataSetChanged();
    }

    /**
     * 将数据追加在现有数据源上
     *
     * @param list 追加数据
     */
    public void appendData(List<Goods> list) {
        this.mDatas.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 增加数据
     *
     * @param position
     */
    public void addData(int position, Goods goods) {

        mDatas.add(position, goods);
        notifyItemInserted(position);
    }

    /*
     * Adapter 中一般还提供一下几个方法
     */

    /**
     * 删除数据
     *
     * @param position
     */
    public void removeData(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public interface OnLoadMoreListener {
        void loadMore();
    }

    /**
     * 不同的布局有不同的ViewHolder
     * item对应的ViewHolder
     */
    static class ItemViewHolder extends RecyclerView.ViewHolder {
        // 商品名称
        @BindView(R.id.tv_showgoodslist_item_name)
        TextView tv_showgoodslist_item_name;
        // 商品描述
        @BindView(R.id.tv_showgoodslist_item_description)
        TextView tv_showgoodslist_item_description;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 底布局ViewHolder
     */
    static class FooterViewHolder extends RecyclerView.ViewHolder {
        // 进度条
        @BindView(R.id.pb_showgoodslist_footer_loading)
        ProgressBar pb_showgoodslist_footer_loading;
        // 提示信息
        @BindView(R.id.tv_showgoodslist_footer_content)
        TextView tv_showgoodslist_footer_content;

        public FooterViewHolder(View footerView) {
            super(footerView);
            ButterKnife.bind(this, footerView);
        }
    }

}
