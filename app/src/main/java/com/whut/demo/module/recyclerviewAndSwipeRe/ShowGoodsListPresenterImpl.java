package com.whut.demo.module.recyclerviewAndSwipeRe;

import android.text.TextUtils;

import com.whut.demo.App;
import com.whut.demo.R;
import com.whut.demo.bean.ResultBean;
import com.whut.demo.net.MyNetApi;
import com.whut.demo.utils.GsonUtil;
import com.whut.demo.utils.LogUtil;

import java.util.Arrays;
import java.util.List;

import okhttp3.Call;

/**
 * <pre>
 *     author : 杨丽金
 *     time   : 2018/07/12
 *     desc   :
 * </pre>
 */
public class ShowGoodsListPresenterImpl extends ShowGoodsListConstract.ShowGoodsListPresenter {
    private static final String TAG = "ShowGoodsListPresenterI";
    private static final int PAGE_SIZE = 4;

    /*
     * Presenter中持有View和Model的引用：通过attachView()传入View的引用；通过createModel()传入Model实例
     */
    @Override
    protected ShowGoodsListConstract.ShowGoodsListModel createModel() {
        return new ShowGoodsListModelImpl();
    }

    /**
     * 加载更多时：得到第page页的数据（从1开始）
     *
     * @param page：页码
     */
    @Override
    public void getPageData_loadMore(final int page) {
        if (view.isRefreshing()) {
            // 如果正在下拉刷新，则直接返回
            return;
        }
        if (view.isLoadMore()) {
            // 如果正在下拉加载数据，则“本次不操作”
            return;
        }
        // 设置isLoadMore变量
        view.setIsLoadMore(true);
//        view.showLoading();
        model.getPageDataList(page, new MyNetApi.OnCallBackListener() {
            @Override
            public void callSuccessResult(ResultBean response) {
                // 1，进度条取消
                view.hideLoading();
                List<Goods> list = GsonUtil.GsonToList(response.getData(), Goods.class);
                Goods[] a = list.toArray(new Goods[0]);
                LogUtil.e(TAG, String.format("当前page=%s，loadMore查询后台返回的数据:%s",page+"", Arrays.toString(a)));
                // 2，设置Adapter中loadState的值
                if (list.size() < PAGE_SIZE) {
                    // 说明后面没有数据了
                    view.setRecyclerLasted();
                } else {
                    // 说明后面还需加载
                    view.setRecyclerLoading();
                }
                // 3，数据拼接在现有数据源后面（该步骤中有notifyDataSetChanged操作，
                // 要放在上面setLoadState操作之后：这样才可把上面状态的改变反映到布局上）
                view.recyclerAppendData(list);
                // 4，当前页数加1
                view.setcurPage(page+1);
                // 5,设置isLoadMore变量
                view.setIsLoadMore(false);
            }

            @Override
            public void callFailResult(ResultBean response) {
                // 1,
                view.hideLoading();
                // 2,
                view.showMsg(TextUtils.isEmpty(response.getMsg()) ? App.getContext().getResources().getString(R.string.operate_failed) : response.getMsg());
                // 3，设置Adapter中loadState的值，说明加载失败
                view.setRecyclerLoadingError();
                // 4,设置isLoadMore变量
                view.setIsLoadMore(false);
            }

            @Override
            public void callError(Call call, Exception e, int id) {
                view.hideLoading();
                view.showMsg(App.getContext().getResources().getString(R.string.network_error));
                // 3，设置Adapter中loadState的值，说明加载失败
                view.setRecyclerLoadingError();
                // 4,设置isLoadMore变量
                view.setIsLoadMore(false);
            }

            @Override
            public void callExceptionResult(ResultBean response) {
                view.hideLoading();
                view.showMsg(App.getContext().getResources().getString(R.string.exception_tip));
                // 3，设置Adapter中loadState的值，说明加载失败
                view.setRecyclerLoadingError();
                // 4,设置isLoadMore变量
                view.setIsLoadMore(false);
            }
        });

    }

    /**
     * 刷新操作和加载更多操作不同时执行，
     * 若正在执行刷新操作：则不执行加载更多操作；并且把RecyclerView的底布局去掉
     * 若正在执行加载更多操作：则不执行刷新
     */
    @Override
    public void getPageData_update() {
        LogUtil.e(TAG,"开始执行getPageData_update()");
        if (view.isLoadMore()) {
            // 如果正在上拉加载，则直接返回
            /*
            * 若是SwipeRefreshLayout的onRefresh监听事件监听到下拉动作时，会自动将刷新状态设置为true，
            * 故：若此处不设置为false ，在界面上会看到不会消失的进度条，其实并没有实质性的数据库请求
             */
            view.setRefreshing(false);
            return;
        }
        /*
         * 移除RecyclerView的尾布局【此处的想法是：刷新时把RecyclerView的底布局移除，但是。。没有成功实现】
         */
        view.removeFooter();
        // 联网加载数据，加载完后将数据显示在RecyclerView上
        view.setRefreshing(true);// SwipeRefreshLayout的setRefreshing()设置为true后出现进度条
//        view.showLoading();
        model.getPageDataList(1, new MyNetApi.OnCallBackListener() {
            @Override
            public void callSuccessResult(ResultBean response) {
                // 1，进度条取消
                view.hideLoading();
                List<Goods> list = GsonUtil.GsonToList(response.getData(), Goods.class);
                Goods[] a = list.toArray(new Goods[0]);
                LogUtil.e(TAG, String.format("当前page=%s，update查询后台返回的数据:%s",1+"", Arrays.toString(a)));
                // 2，设置Adapter中loadState的值
                if (list.size() < PAGE_SIZE) {
                    // 说明后面没有数据了
                    view.setRecyclerLasted();
                } else {
                    // 说明后面还需加载
                    view.setRecyclerLoading();
                }
                // 3，替换现有数据源
                view.recyclerUpdateData(list);
                // 4，当前页数加1
                view.setcurPage(1);
                // 5,设置isRefreshing变量
                view.setRefreshing(false);
            }

            @Override
            public void callFailResult(ResultBean response) {
                // 1,
                view.hideLoading();
                // 2,
                view.showMsg(TextUtils.isEmpty(response.getMsg()) ? App.getContext().getResources().getString(R.string.operate_failed) : response.getMsg());
                // 3，设置Adapter中loadState的值，说明加载失败
                view.setRecyclerLoadingError();
                // 4,设置isRefreshing变量
                view.setRefreshing(false);
            }

            @Override
            public void callError(Call call, Exception e, int id) {
                view.hideLoading();
                view.showMsg(App.getContext().getResources().getString(R.string.network_error));
                // 3，设置Adapter中loadState的值，说明加载失败
                view.setRecyclerLoadingError();
                // 4,设置isRefreshing变量
                view.setRefreshing(false);
            }

            @Override
            public void callExceptionResult(ResultBean response) {
                view.hideLoading();
                view.showMsg(App.getContext().getResources().getString(R.string.exception_tip));
                // 3，设置Adapter中loadState的值，说明加载失败
                view.setRecyclerLoadingError();
                // 4,设置isRefreshing变量
                view.setRefreshing(false);
            }
        });
    }

}