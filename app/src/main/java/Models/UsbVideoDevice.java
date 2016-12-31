package Models;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

/**
 * Created by think on 2016/9/23.
 */

public class UsbVideoDevice implements IDeviceDriver{
    private UsbManager usbManager;
    private UsbDeviceConnection deviceConnection;
    private UsbDevice usbDevice;
    private UsbInterface usbInterface;
    private UsbEndpoint inEndpoint;
    private UsbEndpoint outEndpoint;


    @Override
    public void open() {
        deviceConnection = usbManager.openDevice(usbDevice);
        if (deviceConnection == null) {
            Log.d("UsbVideoDevice", "deviceConnection = null");
        }
        boolean claim = deviceConnection.claimInterface(usbInterface, true);
        if (!claim) {
            Log.e("UsbVideoDevice", "could not claim interface!");
            return;
        }

        UsbCommunication communication;

    }

    @Override
    public void close() {

    }

    @Override
    public void read() {

    }

    @Override
    public void write() {

    }
}


