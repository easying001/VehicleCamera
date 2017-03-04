/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.easying.vehiclecamera.contracts;


import java.io.File;

/**
 * Created by yangjie11 on 2016/9/11.
 */

/* This contact will be applied to interact with UsbFileManager */

public interface UsbFileManagerContract {
    /* Interface to request Usb File Mananger to do something */
    interface DeviceManagement {
        void setCallback(DeviceStateCallback callback);
        void register();
        void unregister();
        void discoverDevice();
        void setupDevice();
    }
    /* Interface to notify the requester of the change of usb device state */
    interface DeviceStateCallback {
        void onDeviceNotFound();
        void onDeviceFound();
        void onDeviceSetup(File root);
    }
}
