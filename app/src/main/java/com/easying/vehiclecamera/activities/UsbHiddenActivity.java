package com.easying.vehiclecamera.activities;

import com.apkfuns.logutils.LogUtils;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by yangjie11 on 2016/12/17.
 */

public class UsbHiddenActivity extends Activity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.tag("Activity-Hiddle").d("onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.tag("Activity-Hiddle").d("onResume");
        finish();
    }
}
