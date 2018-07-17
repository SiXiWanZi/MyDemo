package com.whut.demo.net;


import com.whut.demo.bean.ResultBean;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;

import okhttp3.Response;


/**
 * <pre>
 *  desc:
 *  Created by 忘尘无憾 on 2018/01/20.
 *  version:
 * </pre>
 */

public abstract class ResultCallBack extends Callback<ResultBean> {
    @Override
    public ResultBean parseNetworkResponse(Response response, int id) throws Exception {
        String str = response.body().string();
        JSONObject object = new JSONObject(str);
        ResultBean result = new ResultBean();
        result.setCode(object.getInt("code"));
        result.setData(object.getString("data"));
        result.setMsg(object.getString("msg"));
        return result;
    }
}
