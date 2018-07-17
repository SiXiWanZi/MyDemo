package com.whut.demo.module.recyclerviewAndSwipeRe;

import com.whut.demo.base.BaseModel;
import com.whut.demo.base.BasePresenter;
import com.whut.demo.base.BaseView;
import com.whut.demo.net.MyNetApi;

import java.util.List;

/**
 * <pre>
 *     author : 杨丽金
 *     time   : 2018/07/12
 *     desc   :
 * </pre>
 */
public interface ShowGoodsListConstract {
    interface ShowGoodsListModel extends BaseModel {

        // 得到某一页上的数据
        void getPageDataList(int page, MyNetApi.OnCallBackListener onCallBackListener);
    }

    interface ShowGoodsListView extends BaseView {
        // 向RecyclerView数据源中追加数据
        void recyclerAppendData(List<Goods> list);

        // 设置Adapter中loadSate变量为STATE_LASTED
        void setRecyclerLasted();
        // 设置Adapter中loadSate变量为STATE_LOADING
        void setRecyclerLoading();
        // 设置Adapter中loadSate变量为STATE_ERROR
        void setRecyclerLoadingError();


        // 更新RecyclerView数据源
        void recyclerUpdateData(List<Goods> list);

        // 设置变量isLoadMore的值
        void setIsLoadMore(boolean b);
        // 设置isRefreshing的值
        void setRefreshing(boolean b);

        boolean isRefreshing();

        boolean isLoadMore();

        void removeFooter();

        // 设置RecyclerView的当前页数
        void setcurPage(int i);
    }

    abstract class ShowGoodsListPresenter extends BasePresenter<ShowGoodsListView, ShowGoodsListModel>{

        // 得到第page页的数据
        public abstract void getPageData_loadMore(int page);

        // 得到RecyclerView的刷新数据（第一页数据）
        public abstract void getPageData_update();
    }


}
