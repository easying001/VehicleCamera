package com.camera.easying.filecamera;

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
        Log.d("UsbActivity", "UsbHiddenActivity.onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("UsbActivity", "UsbHiddenActivity.onResume");
        finish();
    }
}
