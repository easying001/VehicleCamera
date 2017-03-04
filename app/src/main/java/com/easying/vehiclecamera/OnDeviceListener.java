package com.easying.vehiclecamera;

import android.hardware.usb.UsbDevice;


/**
 * Created by think on 2016/10/23.
 */

public interface OnDeviceListener {
    void onDeviceAttacted(UsbDevice device);
    void onDeviceSetup(UsbDevice device);
    void onDeviceDeattched(UsbDevice device);
}
