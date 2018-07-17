package com.whut.demo.module.recyclerviewAndSwipeRe;

import com.whut.demo.config.ConfigURL;
import com.whut.demo.net.MyNetApi;

import java.util.HashMap;

/**
 * <pre>
 *     author : 杨丽金
 *     time   : 2018/07/12
 *     desc   :
 * </pre>
 */
public class ShowGoodsListModelImpl implements ShowGoodsListConstract.ShowGoodsListModel {
    private static final String TAG = "ShowGoodsListModelImpl";
    /**
     * 得到第page页上的数据
     * @param page
     * @param onCallBackListener
     */
    @Override
    public void getPageDataList(int page, MyNetApi.OnCallBackListener onCallBackListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("LX", "GetPageData");
        params.put("page", page+"");// 第几页
        //返回标记
        MyNetApi.init().
                query(ConfigURL.MY_TEST_URL, params, onCallBackListener);
    }
}
