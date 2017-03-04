package com.easying.vehiclecamera.adapters;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import com.easying.vehiclecamera.adapters.MyListAdapter;

/**
 * Created by think on 2016/9/17.
 */

public class UsbCameraDeviceListAdapter extends MyListAdapter<UsbDevice> {

    public UsbCameraDeviceListAdapter(Context context, List<UsbDevice> list) {
        super(context, list);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
//        if (view == null) {
//            view = mInflater.inflate(R.layout.device_list_item, viewGroup, false);
//        }
//
//        final UsbDevice device = (UsbDevice) getItem(i);
//        TextView tv_name = (TextView) view.findViewById(R.id.tv_device_name);
//        tv_name.setText(String.format("UVC Camera:(%x:%x:%s)", device.getVendorId(), device.getProductId(), device
//                .getDeviceName()));

        return view;
    }
}
