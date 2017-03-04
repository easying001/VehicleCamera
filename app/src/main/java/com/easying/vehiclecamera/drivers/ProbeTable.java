package com.easying.vehiclecamera.drivers;

import android.util.Pair;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import com.easying.vehiclecamera.drivers.UsbDriver.IUsbDriver;

/**
 * Created by think on 2016/9/24.
 */

public class ProbeTable {
    /* Maps <vid, pid> pairs to corresponding usb video driver */
    private final Map<Pair<Integer, Integer>, Class<? extends IUsbDriver>> mProbeTable =
            new LinkedHashMap<Pair<Integer, Integer>, Class<? extends IUsbDriver>>();
    /* Add or update a (vid,pid) pair into the table */
    public ProbeTable addProduct(int vendorId, int productId, Class<? extends IUsbDriver> driverClass) {
        mProbeTable.put(Pair.create(vendorId, productId), driverClass);
        return this;
    }
    /* By calling getSupportedDevices, to add all supported products into table */
    public ProbeTable addDriver(Class<? extends IUsbDriver> driverClass) {
        final Method method;
         try {
             method = driverClass.getMethod("getSupportedDevices");
         } catch (SecurityException e) {
             throw new RuntimeException(e);
         } catch (NoSuchMethodException e) {
             throw new RuntimeException(e);
         }

        final Map<Integer, int[]> devices;
        try {
            devices = (Map<Integer, int[]>) method.invoke(null);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        for (Map.Entry<Integer, int[]> entry : devices.entrySet()) {
            final int vendorId = entry.getKey().intValue();
            for (int productId : entry.getValue()) {
                addProduct(vendorId, productId, driverClass);
            }
        }
        return this;
    }
    /* returns the driver from given vid pid pair */
    public Class<? extends IUsbDriver> findDriver(int vendorId, int productId) {
        final Pair<Integer, Integer> pair = Pair.create(vendorId, productId);
        return mProbeTable.get(pair);
    }


}
