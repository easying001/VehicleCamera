package Driver.UsbDriver;

import android.hardware.usb.UsbDevice;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import Driver.UsbId;

/**
 * Created by think on 2016/10/17.
 */

public class SonixUsbStorageDriver implements IUsbDriver {
    private UsbDevice mDevice;
    private IUsbStoragePort mPorts;

    public SonixUsbStorageDriver(UsbDevice device) {
        mDevice = device;
        mPorts = new Driver.UsbDriver.StorageControlPort(device, 0);
    }

    @Override
    public UsbDevice getDevice() {
        return mDevice;
    }

    @Override
    public List<IUsbBasePort> getPorts() {
        return Collections.singletonList((IUsbBasePort) mPorts);
    }

    public static Map<Integer, int[]> getSupportedDevices() {
        final Map<Integer, int[]> supportDevices = new LinkedHashMap<Integer, int[]>();
        supportDevices.put(Integer.valueOf(UsbId.VENDOR_SONIX),
                new int[] {
                        UsbId.SONIX_STORAGE
                });
        return supportDevices;
    }
}
