package com.easying.vehiclecamera;

import com.easying.vehiclecamera.services.AdapterService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by think on 2016/10/21.
 */

public class StartupReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
            Intent serviceIntent = new Intent(context, AdapterService.class);
            context.startService(serviceIntent);
        }

    }
}
