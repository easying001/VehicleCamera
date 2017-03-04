package com.easying.vehiclecamera.drivers.UsbDriver;

/**
 * Created by think on 2016/10/17.
 */

public interface IUsbVideoPort extends IUsbBasePort {
    void getDeviceVideoParam(byte[] data);
    void getDeviceVersion(byte[] data);
    void getDeviceTime(byte[] data);
    void getDeviceParam(byte[] data);
    void setTakePic(byte[] data);
    void setRecordState(byte[] data);
    void getRecordState(byte[] data);
    void getSDCardSpace(byte[] data);
    void switchMode(byte[] data);
    void setMic(byte[] data);
    void setProtectedVideoTime(byte[] data);
    void setDeviceTime(byte[] data);
    void setGsensorSensitivity(byte[] data);
    void setFormatSdCard(byte[] data);
    void setFactoryDefault(byte[] data);
    void setAuthChallenge(byte[] data);
    void getAuthChallenge(byte[] data);

}
