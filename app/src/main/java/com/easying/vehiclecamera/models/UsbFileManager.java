/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.easying.vehiclecamera.models;

import com.apkfuns.logutils.LogUtils;
import com.easying.vehiclecamera.contracts.UsbFileManagerContract;

import android.content.Context;

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
        LogUtils.tag("Model-StorageManager").d("construct UsbFileManager");
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
        LogUtils.tag("Model-StorageManager").d("UsbFileManager.init()");
    }

    public void uninit() {
        mDeviceState = STATE_IDLE;
    }


    public int getmDeviceState() {
        return mDeviceState;
    }

}
