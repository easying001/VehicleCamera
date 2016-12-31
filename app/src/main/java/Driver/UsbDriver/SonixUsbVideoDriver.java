package Driver.UsbDriver;

import android.hardware.usb.UsbDevice;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import Driver.UsbId;

/**
 * Created by think on 2016/9/24.
 */

public class SonixUsbVideoDriver implements IUsbDriver {
    private  UsbDevice mDevice;
    private IUsbVideoPort mPort;
    // A driver contains a reference to UsbDevice and the ports used to communication
    public SonixUsbVideoDriver(UsbDevice device) {
        mDevice = device;
        mPort = new VideoControlPort(device, 0);
    }
    @Override
    public UsbDevice getDevice() {
        return mDevice;
    }

    @Override
    public List<IUsbBasePort> getPorts() {
        return Collections.singletonList((IUsbBasePort) mPort);
    }


    public static Map<Integer, int[]> getSupportedDevices() {
        final Map<Integer, int[]> supportDevices = new LinkedHashMap<Integer, int[]>();
        supportDevices.put(Integer.valueOf(UsbId.VENDOR_SONIX),
            new int[] {
                    UsbId.SONIX_BULK
            });
        return supportDevices;
    }

}
