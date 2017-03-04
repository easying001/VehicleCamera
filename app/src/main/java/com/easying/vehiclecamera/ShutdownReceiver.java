package com.easying.vehiclecamera;

import com.apkfuns.logutils.LogUtils;
import com.easying.vehiclecamera.services.AdapterService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by yangjie11 on 2016/12/8.
 */

public class ShutdownReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_SHUTDOWN)) {
            LogUtils.tag("Others-Receiver").d("showdown !!!!!!1");
            Toast.makeText(context, "shutting down system........", Toast.LENGTH_LONG).show();
            Intent serviceIntent = new Intent(context, AdapterService.class);
            context.stopService(serviceIntent);

        } else if (intent.getAction().equalsIgnoreCase("android.intent.action.QUICKBOOT_POWEROFF")) {
            LogUtils.tag("Others-Receiver").d("showdown !!!!!!2");
        }
    }
}
