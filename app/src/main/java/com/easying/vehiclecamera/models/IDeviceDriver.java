package com.easying.vehiclecamera.models;

/**
 * Created by think on 2016/9/23.
 */

public interface IDeviceDriver {
    void open();
    void close();
    void read();
    void write();
}
