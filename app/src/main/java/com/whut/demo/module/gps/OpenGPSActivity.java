package com.whut.demo.module.gps;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.whut.demo.R;
import com.whut.demo.utils.DeviceInfoUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OpenGPSActivity extends Activity {

    @BindView(R.id.btn_opengps_sure)
    Button btn_opengps_sure;
    @BindView(R.id.btn_opengps_cancel)
    Button btn_opengps_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_gps);
        ButterKnife.bind(this);
        OpenGpsActivityCollector.addActivity(this);
        setListenner();
    }

    private void setListenner() {
        btn_opengps_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeviceInfoUtil.openGPSSetting(getApplicationContext());
                finish();
            }
        });
        btn_opengps_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OpenGpsActivityCollector.removeActivity(this);
    }
}
