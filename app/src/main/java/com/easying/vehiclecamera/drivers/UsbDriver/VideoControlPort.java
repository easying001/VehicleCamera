package com.easying.vehiclecamera.drivers.UsbDriver;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

import java.io.IOException;

import com.easying.vehiclecamera.drivers.UsbRequest;

/**
 * Created by think on 2016/10/17.
 */

public class VideoControlPort extends CommonUsbPort implements IUsbVideoPort{
    private UsbInterface mControlInterface;
    private UsbInterface mDataInterface;

    private UsbEndpoint mControlEndpoint;
    private UsbEndpoint mReadEndpoint;
    private UsbEndpoint mWriteEndpoint;

    public VideoControlPort(UsbDevice device, int port) {
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
        mDataInterface = mUsbDevice.getInterface(1);
        mConnection = usbManager.openDevice(mUsbDevice);
        // mConnection.claimInterface(mDataInterface, true);
        setPortState(STATE_OPENED);
        Log.d("UsbPort", "open usb video port!!!");
    }

//    public void claimControlInterface(int inf) {
//        if (inf == 0) {
//            mConnection.claimInterface(mControlInterface, true);
//        } else if (inf == 1) {
//            mConnection.claimInterface(mDataInterface, true);
//        }
//    }

//    public void releaseControlInterface(int inf) {
//        if (inf == 0) {
//            mConnection.releaseInterface(mControlInterface);
//        } else if (inf == 1) {
//            mConnection.releaseInterface(mDataInterface);
//        }
//
//    }

    @Override
    public void close() throws IOException {
        if (mConnection != null) {
            mConnection.close();
        }
        mConnection = null;
        setPortState(STATE_IDLE);
        Log.d("UsbPort", "close usb video port!!!");
    }



    private int sendControlMessage(int requestType, int request, int value, int index, byte[] buf) {
        int ret = 0;
        Log.d("UsbCommunication", "start sending usb control message");
        // With the latest UVC firmware, DO NOT release data interface and claim data interface
        // Only just claim control interface and release control interface, otherwise, preview will stop
        //mConnection.releaseInterface(mDataInterface);
        mConnection.claimInterface(mControlInterface, true);
        ret = mConnection.controlTransfer(
                requestType, request, value, index, buf, buf != null ? buf.length : 0, 5000);
        mConnection.releaseInterface(mControlInterface);
        Log.d("UsbCommunication", "finish sending usb control message");
        //mConnection.claimInterface(mDataInterface, true);

        return ret;
    }

    private int usbGetCur(int controlSelector, byte[] data) {
        return sendControlMessage(UsbRequest.USB_GET_REQUEST, 0x81,controlSelector ,UsbRequest.USB_UVC_UNIT_ID, data);
    }

    private int usbSetCur(int controlSelector, byte[] data) {

        return sendControlMessage(UsbRequest.USB_SET_REQUEST, 0x01,controlSelector, UsbRequest.USB_UVC_UNIT_ID, data);
    }

    public void setTakePic(byte[] data) {
        usbSetCur(0x0300, data);
    }

    public void getRecordState(byte[] data) {
        usbSetCur(0x0300, data);
        usbGetCur(0x0300, data);
    }

    public void setRecordState(byte[] data) {
        usbSetCur(0x0300, data);
    }

    public void getDeviceVersion(byte[] data) {
        usbSetCur(0x0100, data);
        usbGetCur(0x0100, data);

    }

    public void getDeviceTime(byte[] data) {
        usbSetCur(0x0100, data);
        usbGetCur(0x0100, data);
    }

    public void getDeviceParam(byte[] data) {
        usbSetCur(0x0100, data);
        usbGetCur(0x0100, data);
    }

    public void getDeviceVideoParam(byte[] data) {
        usbSetCur(0x0200, data);
        usbGetCur(0x0200, data);
        Log.d("UsbPort", "data[4] = " + data[4]);
    }

    public void getSDCardSpace(byte[] data) {
        usbSetCur(0x0400, data);
        usbGetCur(0x0400, data);
    }

    @Override
    public void switchMode(byte[] data) {
        sendControlMessage(0x40, 0x01, 0x0000, 0x0002, data);
    }

    @Override
    public void setMic(byte[] data) {
        usbSetCur(0x0300, data);
    }

    @Override
    public void setProtectedVideoTime(byte[] data) {
        usbSetCur(0x0300, data);
    }

    @Override
    public void setDeviceTime(byte[] data) {
        usbSetCur(0x0100, data);
    }

    @Override
    public void setGsensorSensitivity(byte[] data) {
        usbSetCur(0x0100, data);
    }

    @Override
    public void setFormatSdCard(byte[] data) {
        usbSetCur(0x0400, data);
    }

    @Override
    public void setFactoryDefault(byte[] data) {
        usbSetCur(0x0100, data);
    }

    @Override
    public void setAuthChallenge(byte[] data) {
        usbSetCur(0x0100, data);
    }
    @Override
    public void getAuthChallenge(byte[] data) {
        usbSetCur(0x0100, data);
        usbGetCur(0x0100, data);
    }


//        @Override
//        public void getDevVersion() {
//            byte[] data = new byte[0x40];
//            data[0] = 0x00;
//
//            int len = mConnection.controlTransfer(0x21, 0x01, 0x0400, 0x0400, data, 0x40, 5000);
//            Log.d("UsbConnection", "len = " + len + ",value = " + data[0]);
//            len = mConnection.controlTransfer(0xa1, 0x81, 0x0100, 0x0400, data, 0x40, 5000);
//            Log.d("UsbConnection", "len = " + len + ",value = " + data[0] + data[1] + data[2] + data[3] );
//        }
}
