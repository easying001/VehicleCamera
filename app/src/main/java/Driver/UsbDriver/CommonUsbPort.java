package Driver.UsbDriver;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;

import java.io.IOException;

/**
 * Created by think on 2016/10/17.
 */
public abstract class CommonUsbPort implements IUsbBasePort {
    public static final int STATE_IDLE = 0;
    public static final int STATE_OPENED = 1;
    protected UsbDevice mUsbDevice = null;
    protected int mPortNumber = 0;
    protected UsbDeviceConnection mConnection = null;
    protected int mPortState = STATE_IDLE;

    public CommonUsbPort(UsbDevice device, int portNumber) {
        mUsbDevice = device;
        mPortNumber = portNumber;
        mPortState = STATE_IDLE;
    }


    public void setPortState(int state) {
        mPortState = state;
    }
    public int getPortState() {
        return mPortState;
    }
    public final UsbDevice getDevice() {
        return mUsbDevice;
    }

    @Override
    public int getPortNumber() {
        return mPortNumber;
    }



    public UsbDeviceConnection getConnection() {
        return mConnection;
    }

    @Override
    abstract public void open(UsbManager usbManager) throws IOException;

    @Override
    abstract public void close() throws IOException;

}
