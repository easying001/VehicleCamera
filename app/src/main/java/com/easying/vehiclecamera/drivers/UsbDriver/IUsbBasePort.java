package com.easying.vehiclecamera.drivers.UsbDriver;

import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;

import java.io.IOException;

/**
 * Created by think on 2016/10/17.
 */

public interface IUsbBasePort {
    // public IUsbDriver getDriver();
    int getPortNumber();
    String getSerial();
    void open(UsbManager usbManager) throws IOException;
    void close() throws IOException;
//    public void claimControlInterface(int inf);
//    public void releaseControlInterface(int inf);
int getPortState();
    UsbDeviceConnection getConnection();
}
