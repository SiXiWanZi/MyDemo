package com.whut.demo.module.recyclerviewAndSwipeRe;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.whut.demo.R;
import com.whut.demo.base.MyBaseActivity;
import com.whut.demo.utils.LogUtil;
import com.whut.demo.utils.ToastUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * <pre>
 *     author : 杨丽金
 *     time   : 2018/07/12
 *     desc   : 该Activity作为View层，实现ShowGoodsListView接口
 * </pre>
 */
public class ShowGoodsListActivity extends MyBaseActivity implements ShowGoodsListConstract.ShowGoodsListView {
    // Recyclerview：展示商品列表
    @BindView(R.id.rv_showgoodslist_goodsList)
    RecyclerView rv_showgoodslist_goodsList;
    @BindView(R.id.srl_showgoodslist_refreshLayout)
    SwipeRefreshLayout srl_showgoodslist_refreshLayout;
    @BindView(R.id.pb_showgoodslist_loading)
    ProgressBar pb_showgoodslist_loading;
    
    // View中持有Presenter的引用
    private ShowGoodsListConstract.ShowGoodsListPresenter mPresenter;
    // RecyclerView的Adapter
    private MyAdapter mainAdapter;

    // 当前是第几页：从0开始（0表示：当前没有数据）
    private int curPage=0;
    // 是否正在加载更多 true：正在加载更多；false：未加载更多
    private boolean isLoadMore=false;

    private static final String TAG = "ShowGoodsListActivity";


    @Override
    public void initData(Bundle savedInstanceState) {
        // 创建Presenter实例
        mPresenter = new ShowGoodsListPresenterImpl();
        mPresenter.attachView(this);
        initSwipeRefreshLayout();
        initRecyclerView();
    }

    private void initSwipeRefreshLayout() {
        srl_showgoodslist_refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        srl_showgoodslist_refreshLayout.setOnRefreshListener(new MyRefreshListener(this));
    }

    private void initRecyclerView() {
        //设置布局管理器
        rv_showgoodslist_goodsList.setLayoutManager(new LinearLayoutManager(this));
        //初始化和设置Adapter
        mainAdapter = new MyAdapter(new ArrayList<Goods>());
        rv_showgoodslist_goodsList.setAdapter(mainAdapter);
        // 获取第一页数据
        mPresenter.getPageData_update();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_showgoodslist;
    }

    @Override
    public void setListener() {
        // RecyclerView的加载更多方法
        mainAdapter.setOnLoadMoreListener(new MyOnLoadMoreListener(this));
        // 监听RecyclerView的滑动事件
        rv_showgoodslist_goodsList.addOnScrollListener(new MyOnScroolListener(this));
    }

    @Override
    public void recyclerAppendData(List<Goods> list) {
        mainAdapter.appendData(list);
    }

    @Override
    public void setRecyclerLasted() {
        mainAdapter.setLoadState(MyAdapter.STATE_LASTED);
    }

    @Override
    public void setRecyclerLoading() {
        mainAdapter.setLoadState(MyAdapter.STATE_LOADING);
    }

    @Override
    public void setRecyclerLoadingError() {
        mainAdapter.setLoadState(MyAdapter.STATE_ERROR);
    }

    @Override
    public void recyclerUpdateData(List<Goods> list) {
        mainAdapter.updateData(list);
    }

    @Override
    public void setIsLoadMore(boolean b) {
        isLoadMore=b;
    }

    @Override
    public void setRefreshing(boolean b) {
        if (isRefreshing() != b) {
            srl_showgoodslist_refreshLayout.setRefreshing(b);
        }
    }

    @Override
    public boolean isRefreshing() {
        return srl_showgoodslist_refreshLayout.isRefreshing();
    }

    @Override
    public boolean isLoadMore() {
        return isLoadMore;
    }

    @Override
    public void removeFooter() {
        /*
         * RecyclerView的尾布局【此处的想法是：刷新时把RecyclerView的底布局移除，但是。。没有成功实现】
         */
    }

    @Override
    public void setcurPage(int i) {
        curPage=i;
    }

    @Override
    public void showMsg(String msg) {
        ToastUtil.showShort(msg);
    }

    @Override
    public void showLoading() {
        pb_showgoodslist_loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        pb_showgoodslist_loading.setVisibility(View.INVISIBLE);
    }

    /**
     * 静态内部类+弱引用
     */
    static class MyOnLoadMoreListener implements MyAdapter.OnLoadMoreListener {

        private WeakReference<ShowGoodsListActivity> mActivity;

        public MyOnLoadMoreListener(ShowGoodsListActivity activity) {
            mActivity = new WeakReference<ShowGoodsListActivity>(activity);
        }

        /**
         * RecyclerView加载更多
         */
        @Override
        public void loadMore() {
            ShowGoodsListActivity activity = mActivity.get();
            if (activity != null) {
                // 加载下一页数据
                activity.mPresenter.getPageData_loadMore(activity.curPage + 1);
            }
        }
    }

    /**
     * 静态内部类+弱引用
     * 防止内存泄漏
     */
    static class MyOnScroolListener extends RecyclerView.OnScrollListener{
        WeakReference<ShowGoodsListActivity> mActivity;

        public MyOnScroolListener(ShowGoodsListActivity activity){
            this.mActivity = new WeakReference<ShowGoodsListActivity>(activity);
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            ShowGoodsListActivity activity = mActivity.get();
            if (activity != null) {
                RecyclerView.LayoutManager manager = activity.rv_showgoodslist_goodsList.getLayoutManager();
                MyAdapter adapter = (MyAdapter) activity.rv_showgoodslist_goodsList.getAdapter();
                if (null == manager) {
                    throw new RuntimeException("you should call setLayoutManager() first!!");
                }
                if (manager instanceof LinearLayoutManager) {
                    // 返回最后一个完全可见的item项的position值
                    int lastCompletelyVisibleItemPosition =
                            ((LinearLayoutManager) manager).findLastCompletelyVisibleItemPosition();

//                    lastCompletelyVisibleItemPosition+1 == adapter.getItemCount():表示已经滑动到底部
//                    adapter.getLoadState()==MyAdapter.STATE_LOADING：表示正在加载中
                    if (lastCompletelyVisibleItemPosition +1 == adapter.getItemCount() &&
                            adapter.getLoadState()==MyAdapter.STATE_LOADING) {
                        // 加载下页数据
                        activity.mPresenter.getPageData_loadMore(activity.curPage+1);

                    }
                }
            }


        }
    }

    /**
     * 监听SwipeRefresh的刷新事件
     */
    static class MyRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        WeakReference<ShowGoodsListActivity> mActivity;

        public MyRefreshListener(ShowGoodsListActivity activity){
            this.mActivity = new WeakReference<ShowGoodsListActivity>(activity);
        }
        @Override
        public void onRefresh() {
            ShowGoodsListActivity activity = mActivity.get();
            if (activity != null) {
                LogUtil.e(TAG,"检测到onRefresh（）时间");
                // 获取第一页数据
                activity.mPresenter.getPageData_update();
            }
        }
    }

}
