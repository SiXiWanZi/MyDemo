package com.whut.demo.widget;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.whut.demo.R;


/**
 * <pre>
 *  desc:
 *  Created by 忘尘无憾 on 2018/01/20.
 *  version:
 * </pre>
 */

public class ProgressDialog extends android.app.ProgressDialog {
    public ProgressDialog(Context context) {
        super(context);
    }

    public ProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(getContext());
    }

    private void init(Context context) {
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_progess);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }

    @Override
    public void show() {
        super.show();
    }
}
