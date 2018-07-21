package com.whut.demo.module.nomalRecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.whut.demo.R;
import com.whut.demo.base.MyBaseActivity;
import com.whut.demo.bean.ResultBean;
import com.whut.demo.config.ConfigURL;
import com.whut.demo.net.NetApi;
import com.whut.demo.utils.GsonUtil;
import com.whut.demo.utils.LogUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * <pre>
 *     author : 杨丽金
 *     time   : 2018/07/21
 *     desc   : 用于演示RecyclerView的上拉加载，和刷新（通过界面上的按钮进行刷新）功能
 * </pre>
 */
public class LoadMoreAndRefreshRecyclerActivity extends MyBaseActivity{
    @BindView(R.id.rv_LoadMoreAndRefreshRecycler_recycler)
    RecyclerView rv_LoadMoreAndRefreshRecycler_recycler;
    @BindView(R.id.btn_LoadMoreAndRefreshRecycler_refresh)
    Button btn_LoadMoreAndRefreshRecycler_refresh;

    private RecyclerAdapter mAdapter;
    // 标志是否正在上拉加载{true:正在上拉加载；false：没有}
    private boolean isLoadMore = false;
    // 表示是否正在下拉刷新{true:正在刷新；false“没有}
    private boolean isRefreshing = false;
    // 当前是第几页：从0开始（0表示：当前没有数据）
    private int currentPage = 0;

    private static final String TAG = "LoadMoreAndRefreshRecyc";

    @Override
    public void initData(Bundle savedInstanceState) {
initRecyclerView();
    }

    private void initRecyclerView() {
        // 设置布局管理器
        rv_LoadMoreAndRefreshRecycler_recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        // 设置adapter
        mAdapter = new RecyclerAdapter(new ArrayList<Info>());
        rv_LoadMoreAndRefreshRecycler_recycler.setAdapter(mAdapter);
        // 监听RecyclerView的滑动事件
        rv_LoadMoreAndRefreshRecycler_recycler.addOnScrollListener(new MyOnScroolListener(this));
    }

    /**
     * 静态内部类+弱引用
     * 防止内存泄漏
     */
    static class MyOnScroolListener extends RecyclerView.OnScrollListener {
        WeakReference<LoadMoreAndRefreshRecyclerActivity> mActivity;

        public MyOnScroolListener(LoadMoreAndRefreshRecyclerActivity activity) {
            this.mActivity = new WeakReference<LoadMoreAndRefreshRecyclerActivity>(activity);
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LoadMoreAndRefreshRecyclerActivity activity = mActivity.get();
            if (activity != null) {
                RecyclerView.LayoutManager manager = activity.rv_LoadMoreAndRefreshRecycler_recycler.getLayoutManager();
                RecyclerAdapter adapter = (RecyclerAdapter) activity.rv_LoadMoreAndRefreshRecycler_recycler.getAdapter();
                if (null == manager) {
                    throw new RuntimeException("you should call setLayoutManager() first!!");
                }
                if (manager instanceof LinearLayoutManager) {
                    // 返回最后一个完全可见的item项的position值
                    int lastCompletelyVisibleItemPosition =
                            ((LinearLayoutManager) manager).findLastCompletelyVisibleItemPosition();
//                    lastCompletelyVisibleItemPosition+1 == adapter.getItemCount():表示已经滑动到底部
//                    adapter.getLoadState()==MyAdapter.STATE_LOADING：表示正在加载中
                    if (lastCompletelyVisibleItemPosition + 1 == adapter.getItemCount() &&
                            adapter.getLoadState() == RecyclerAdapter.STATE_LOADING) {
                        // 加载下页数据
                        activity.getData_loadMore();
                    }
                }
            }


        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_loadmore_refresh_recycler;
    }

    /**
     * 得到刷新数据
     * 第一页数据
     */
    private void getData_update() {
        if (isLoadMore) {
            // 正在上拉加载，退出
            LogUtil.e(TAG, "正在上拉加载，取消本次操作");
            return;
        }
        if (isRefreshing) {
            // 正在刷新
            LogUtil.e(TAG, "正在刷新，取消本次操作");
            return;
        }
        isRefreshing = true;
        // 刷新时将底布局设置为什么都不显示
        mAdapter.setLoadState(RecyclerAdapter.STATE_OTHER);
        final HashMap<String, String> params = new HashMap<>();
        params.put("LX", "Query");
        params.put("page", 1 + "");
        NetApi.init(context, true).query(ConfigURL.GET_QUERY_DATA, params, new NetApi.OnCallBackListener() {

            @Override
            public void callSuccessResult(ResultBean response) {
                // 刷新完毕
                isRefreshing = false;
                List<Info> list = GsonUtil.GsonToList(response.getData(), Info.class);
                Info[] a = list.toArray(new Info[0]);
                LogUtil.e(TAG, String.format("callSuccessResult()；page=%s;update返回的数据:%s", 1 + "", Arrays.toString(a)));
                // 1，设置Adapter中loadState的值
                if (list.size() < RecyclerAdapter.PAGE_SIZE) {
                    // 说明后面没有数据了
                    mAdapter.setLoadState(RecyclerAdapter.STATE_LASTED);
                    LogUtil.e(TAG, "后面无更多数据");
                } else {
                    // 说明后面还需加载
                    mAdapter.setLoadState(RecyclerAdapter.STATE_LOADING);
                    LogUtil.e(TAG, "后面仍需加载");
                }
                // 2，用新数据替换原来的数据源
                mAdapter.upData(list);
                // 3，当前页数设置为1
                currentPage = 1;
            }

            @Override
            public void callFailResult(ResultBean response) {
                LogUtil.e(TAG, "callFailResult（）");
                // 刷新完毕
                isRefreshing = false;
                // 设置Adapter中loadState的值，说明加载失败
                mAdapter.setLoadState(RecyclerAdapter.STATE_ERROR);
            }

            @Override
            public void callError(Call call, Exception e, int id) {
                LogUtil.e(TAG, "callError（）");
                super.callError(call, e, id);
                // 刷新完毕
                isRefreshing = false;
                // 设置Adapter中loadState的值，说明加载失败
                mAdapter.setLoadState(RecyclerAdapter.STATE_ERROR);
            }

            @Override
            public void callExceptionResult(Context context, ResultBean response) {
                super.callExceptionResult(context, response);
                LogUtil.e(TAG, "callExceptionResult（）");
                // 刷新完毕
                isRefreshing = false;
                // 设置Adapter中loadState的值，说明加载失败
                mAdapter.setLoadState(RecyclerAdapter.STATE_ERROR);
            }
        });
    }

    /**
     * 上拉加载更多数据
     * 上拉加载
     */
    private void getData_loadMore() {
        LogUtil.e(TAG, "正在执行getData_loadMore()");
        if (isLoadMore) {
            // 正在上拉加载，退出
            LogUtil.e(TAG, "正在上拉加载，取消本次操作");
            return;
        }
        if (isRefreshing) {
            // 正在刷新
            LogUtil.e(TAG, "正在刷新，取消本次操作");
            return;
        }
        final HashMap<String, String> params = new HashMap<>();
        params.put("LX", "Query");
        params.put("page", currentPage + 1 + "");
        NetApi.init(context, false).query(ConfigURL.GET_QUERY_DATA, params, new NetApi.OnCallBackListener() {

            @Override
            public void callSuccessResult(ResultBean response) {
                // 上拉加载完毕
                isLoadMore = false;
                List<Info> list = GsonUtil.GsonToList(response.getData(), Info.class);
                Info[] a = list.toArray(new Info[0]);
                LogUtil.e(TAG, String.format("callSuccessResult():page=%s;loadMore返回的数据:%s", currentPage + 1 + "", Arrays.toString(a)));
                // 1，设置Adapter中loadState的值
                if (list.size() < RecyclerAdapter.PAGE_SIZE) {
                    // 说明后面没有数据了
                    mAdapter.setLoadState(RecyclerAdapter.STATE_LASTED);
                } else {
                    // 说明后面还需加载
                    mAdapter.setLoadState(RecyclerAdapter.STATE_LOADING);
                }
                // 2，数据拼接在现有数据源后面
                mAdapter.appendData(list);
                // 3，当前页数加1
                currentPage++;
            }

            @Override
            public void callFailResult(ResultBean response) {
                LogUtil.e(TAG, "callFailResult（）");
                // 上拉加载完毕
                isLoadMore = false;
                // 设置Adapter中loadState的值，说明加载失败
                mAdapter.setLoadState(RecyclerAdapter.STATE_ERROR);
            }

            @Override
            public void callError(Call call, Exception e, int id) {
                LogUtil.e(TAG, "callError（）");
                super.callError(call, e, id);
                // 上拉加载完毕
                isLoadMore = false;
                // 设置Adapter中loadState的值，说明加载失败
                mAdapter.setLoadState(RecyclerAdapter.STATE_ERROR);
            }

            @Override
            public void callExceptionResult(Context context, ResultBean response) {
                LogUtil.e(TAG, "callExceptionResult（）");
                super.callExceptionResult(context, response);
                // 上拉加载完毕
                isLoadMore = false;
                // 设置Adapter中loadState的值，说明加载失败
                mAdapter.setLoadState(RecyclerAdapter.STATE_ERROR);
            }
        });
    }
}
