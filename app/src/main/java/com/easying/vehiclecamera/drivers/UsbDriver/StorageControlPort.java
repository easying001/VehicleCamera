package com.easying.vehiclecamera.drivers.UsbDriver;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

import java.io.IOException;

import com.easying.vehiclecamera.drivers.UsbRequest;

/**
 * Created by think on 2016/10/17.
 */

public class StorageControlPort extends CommonUsbPort implements IUsbStoragePort {
    private UsbInterface mControlInterface;

    public StorageControlPort(UsbDevice device, int port) {
        super(device, port);
    }

    @Override
    public String getSerial() {
        return null;
    }

    @Override
    public void open(UsbManager usbManager) throws IOException {
        if (usbManager == null) {
            Log.d("UsbPort", "Failed to open usbPort since usbManager is null");
            return;
        }
        mControlInterface = mUsbDevice.getInterface(0);
        mConnection = usbManager.openDevice(mUsbDevice);
        //mConnection.claimInterface(mControlInterface, true);
        setPortState(STATE_OPENED);
        Log.d("UsbPort", "open usb storage port!!!");
    }

    @Override
    public void close() throws IOException {
        mConnection = null;
        setPortState(STATE_IDLE);
    }


    private int sendControlMessage(int requestType, int request, int value, int index, byte[] buf) {
        int ret = 0;
        //mConnection.claimInterface(mControlInterface, true);
        ret = mConnection.controlTransfer(
                requestType, request, value, index, buf, buf != null ? buf.length : 0, 5000);
        //mConnection.releaseInterface(mControlInterface);
        return ret;
    }

    private int usbGetCur(int controlSelector, byte[] data) {
        return sendControlMessage(UsbRequest.USB_GET_REQUEST, 0x81,controlSelector ,UsbRequest.USB_UVC_UNIT_ID, data);
    }

    private int usbSetCur(int controlSelector, byte[] data) {
        return sendControlMessage(UsbRequest.USB_SET_REQUEST, 0x01,controlSelector, UsbRequest.USB_UVC_UNIT_ID, data);
    }

    @Override
    public void switchMode(byte[] data) {
        Log.d("UsbControl", "send control message to switch to UVC mode, data.size = " + data.length);
        sendControlMessage(0x40, 0x01, 0x0000, 0x0002, data);
    }
}
