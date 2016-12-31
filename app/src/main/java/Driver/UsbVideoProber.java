package Driver;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import Driver.UsbDriver.IUsbDriver;
import Driver.UsbDriver.SonixUsbStorageDriver;
import Driver.UsbDriver.SonixUsbVideoDriver;

/**
 * Created by think on 2016/9/24.
 */

public class UsbVideoProber {
    private final static String TAG = UsbVideoProber.class.getSimpleName();
    private static UsbVideoProber mInstance = null;
    private final ProbeTable mProbeTable;
    private List<IUsbDriver> mUsbDriverList;

    public UsbVideoProber(ProbeTable table) {
        mProbeTable = table;
    }

    public static UsbVideoProber getInstance() {
        if (mInstance == null) {
            synchronized (UsbVideoProber.class) {
                if (mInstance == null) {
                    mInstance = new UsbVideoProber(getDefaultProbeTable());
                }
            }
        }
        return mInstance;
    }

    public static ProbeTable getDefaultProbeTable() {
        final ProbeTable probeTable = new ProbeTable();
        probeTable.addDriver(SonixUsbVideoDriver.class);
        probeTable.addDriver(SonixUsbStorageDriver.class);
        return probeTable;
    }
    //
    public IUsbDriver getUsbDriver(UsbDevice device) {
        if (device == null) {
            return null;
        }
        if ((mUsbDriverList != null) && (mUsbDriverList.size() > 0)) {
            for(IUsbDriver driver : mUsbDriverList) {
                if (driver.getDevice().getProductId() == device.getProductId()
                        && (driver.getDevice().getDeviceId() == device.getDeviceId())) {
                   return driver;
                }
            }
        }
        return null;
    }

    //
    public IUsbDriver loadUsbDriver(UsbDevice device) {
        if (device != null) {
            return probeDevice(device);
        } else {
            return null;
        }
    }

    // Load the usb driver for specified device, if alreay loaded do nothing
    // otherwise, new driver instance and insert into list
    public void addUsbDriver(UsbDevice device) {
        if (isDriverLoaded(device)) {
            return;
        }
        IUsbDriver driver = probeDevice(device);
        if (driver != null) {
            mUsbDriverList.add(driver);
        } else {
            Log.d(TAG, "No suitable driver found for this device");
        }

    }
    // unload the usb driver for specified device
    // and remove this instance from list
    public void unloadUsbDriver(UsbDevice device) {
        if (isDriverLoaded(device)) {
            for(IUsbDriver driver : mUsbDriverList) {
                if (driver.getDevice().getProductId() == device.getProductId()
                        && (driver.getDevice().getDeviceId() == device.getDeviceId())) {
                    mUsbDriverList.remove(driver);
                    Log.d(TAG, "Remove driver for this device");
                }
            }
        }
    }

    public boolean isDriverLoaded(UsbDevice device) {
        if (device == null) {
            return false;
        }
        if ((mUsbDriverList != null) && (mUsbDriverList.size() > 0)) {
            for(IUsbDriver driver : mUsbDriverList) {
                if (driver.getDevice().getProductId() == device.getProductId()
                        && (driver.getDevice().getDeviceId() == device.getDeviceId())) {
                    Log.d(TAG, "Driver for this device has been loaded");
                    return true;
                }
            }
        }

        return false;
    }

    public void findAllDrivers(final UsbManager usbManager) {
        for (final UsbDevice usbDevice : usbManager.getDeviceList().values()) {
            loadUsbDriver(usbDevice);
        }
    }

    public IUsbDriver probeDevice(final UsbDevice usbDevice) {
        final int vendorId = usbDevice.getVendorId();
        final int productId = usbDevice.getProductId();

        final Class<? extends IUsbDriver> driverClass =
                mProbeTable.findDriver(vendorId, productId);
        if (driverClass != null) {
            final IUsbDriver driver;
            try {
                final Constructor<? extends IUsbDriver> ctor =
                        driverClass.getConstructor(UsbDevice.class);
                driver = ctor.newInstance(usbDevice);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            return driver;
        }
        return null;
    }
}
