package com.whut.demo.bean;

/**
 * <pre>
 *  desc: 后台返回前台的实体类
 *  Created by 忘尘无憾 on 2018/01/20.
 *  version:
 * </pre>
 */

public class ResultBean {
    int code;// 返回码 200成功，119失败，110后台异常。
    String msg;// 返回信息
    String data;// 返回数据

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
