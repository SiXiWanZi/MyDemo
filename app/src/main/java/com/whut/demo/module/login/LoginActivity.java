package com.whut.demo.module.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.whut.demo.R;
import com.whut.demo.base.MyBaseActivity;
import com.whut.demo.bean.Company;
import com.whut.demo.bean.ResultBean;
import com.whut.demo.config.ConfigURL;
import com.whut.demo.module.mvp.InnerVehScheduleActivity;
import com.whut.demo.net.NetApi;
import com.whut.demo.utils.DeviceInfoUtil;
import com.whut.demo.utils.GsonUtil;
import com.whut.demo.utils.LogUtil;
import com.whut.demo.utils.LoginInfoUtil;
import com.whut.demo.utils.ToastUtil;
import com.whut.demo.utils.XTRZInfoUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * <pre>
 *     author : 杨丽金
 *     time   : 2018/06/26
 *     desc   :
 * </pre>
 */
public class LoginActivity extends MyBaseActivity {
    private static final String TAG = "LoginActivity";
    // 用户名
    @BindView(R.id.edt_login_yhzh)
    EditText edt_login_yhzh;
    // 密码
    @BindView(R.id.edt_login_psw)
    EditText edt_login_psw;
    // 登录Btn
    @BindView(R.id.btn_login_login)
    Button btn_login_login;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @OnClick(R.id.btn_login_login)
    public void submitLoginData() {
        // 校验数据
        String yhzh = edt_login_yhzh.getText().toString();// 用户名
        String password = edt_login_psw.getText().toString();// 密码
        if (yhzh == null || "".equals(yhzh.trim())) {
            ToastUtil.showShort("用户名不能为空");
            return;
        }
        if (password == null || "".equals(password.trim())) {
            ToastUtil.showShort("密码不能为空");
            return;
        }

        // 连接数据库查询是否可成功登录，并给出相应提示
        loginCheckByDatabase(yhzh, password);
    }

    private void loginCheckByDatabase(String yhzh, String password) {
        //封装网络url参数
        HashMap<String, String> params = new HashMap<>();
        params.put("LX", "Login");
        params.put("yhzh", yhzh);
        params.put("psw", password);
        params.put("ip", DeviceInfoUtil.getLocalHostIp());
        params.put("mac", DeviceInfoUtil.getMAC());
        //返回标记
        NetApi.init(context, true).query(ConfigURL.LOGIN, params, new NetApi.OnCallBackListener() {
            @Override
            public void callSuccessResult(ResultBean response) {
                loginSuccessOp(response);

            }

            @Override
            public void callFailResult(ResultBean response) {
                loginFailedOp(response);
            }
        });
    }

    /**
     * 登录成功操作
     *
     * @param response
     */
    private void loginSuccessOp(ResultBean response) {

        String data = response.getData();
        UserInfo userInfo = GsonUtil.GsonToBean(data, UserInfo.class);
        LogUtil.e(TAG,userInfo.toString());
        // 保存数据到本地xml文件
        LoginInfoUtil.setYHZH(edt_login_yhzh.getText().toString());// 用户登录账户名
        LoginInfoUtil.setYHBH(userInfo.yhbh);// 用户编号
        LoginInfoUtil.setYHMC(userInfo.yhmc);// 用户编号对应的名称

        // 所述公司名称
        Company.getInstance().setCompanyName(userInfo.bmmc);
        LogUtil.e(TAG,"全局变量中保存的："+Company.getInstance().getCompanyName());
        //保存mac
        LoginInfoUtil.setMAC(DeviceInfoUtil.getMAC());
        //保存IP
        LoginInfoUtil.setIP(DeviceInfoUtil.getLocalHostIp());

        // 此处初始化xtrzBean
        XTRZInfoUtil.init();
        LogUtil.e(TAG, XTRZInfoUtil.getXTRZInfo().toString());

        //跳转至主界面
        Intent intent = new Intent(LoginActivity.this, InnerVehScheduleActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 登录失败操作
     *
     * @param response
     */
    private void loginFailedOp(ResultBean response) {
        ToastUtil.showShort(response.getMsg());
    }

    private static class UserInfo {
        String yhbh;
        String yhmc;
        // 所述公司名称
        String bmmc;

        @Override
        public String toString() {
            return "UserInfo{" +
                    "yhbh='" + yhbh + '\'' +
                    ", yhmc='" + yhmc + '\'' +
                    ", companyName='" + bmmc + '\'' +
                    '}';
        }
    }
}
