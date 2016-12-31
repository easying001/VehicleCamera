package Driver.UsbDriver;

import android.hardware.usb.UsbDevice;

import java.util.List;

/**
 * Created by think on 2016/9/24.
 */

public interface IUsbDriver {
    UsbDevice getDevice();
    List<IUsbBasePort> getPorts();
}
