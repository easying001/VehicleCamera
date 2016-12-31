/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package Models;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import Contracts.UsbFileManagerContract;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;

/**
 * Created by yangjie11 on 2016/9/10.
 */

public class UsbFileManager {
    private static final String ACTION_USB_PERMISSION = "com.github.mjdev.libaums.USB_PERMISSION";
    private static final String TAG = "UsbFile";//UsbFileManager.class.getSimpleName();
    public static final int STATE_IDLE = 0;
    public static final int STATE_DISCOVERED = 1;
    public static final int STATE_GRANTED = 2;
    public static final int STATE_SETUPED = 3;

    private Context mContext;
    private UsbFileManagerContract.DeviceStateCallback mDeviceStateCallback;
    private static UsbFileManager mInstance;
    private int mDeviceState = STATE_IDLE;

    public UsbFileManager() {
        Log.d("UsbFile", "construct UsbFileManager");
    }

    public static UsbFileManager getInstance() {
        if (null == mInstance) {
            synchronized(UsbFileManager.class) {
                if (null == mInstance) {
                    mInstance = new UsbFileManager();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context, UsbFileManagerContract.DeviceStateCallback callback) {
        mContext = context;
        Log.d("UsbFile", "UsbFileManager.init()");
    }

    public void uninit() {
        mDeviceState = STATE_IDLE;

    }


    public int getmDeviceState() {
        return mDeviceState;
    }

}
