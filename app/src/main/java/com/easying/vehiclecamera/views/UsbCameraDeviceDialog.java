package com.easying.vehiclecamera.views;

import android.content.DialogInterface;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.easying.vehiclecamera.R;
import com.easying.vehiclecamera.adapters.UsbCameraDeviceListAdapter;

import java.util.List;

import com.easying.vehiclecamera.models.UsbCameraManager;

/**
 * Created by think on 2016/9/17.
 */

public class UsbCameraDeviceDialog extends BaseDialog {
    private View mContentView;
    private ListView mDeviceListView;
    private UsbCameraDeviceListAdapter mDeviceListAdapter;
    private TextView mTvNoDevice;
    UsbCameraDeviceDialog.FragmentInterface mListerner;

    public interface FragmentInterface {
        void showUsbCameraFragment();
    }

    public static UsbCameraDeviceDialog newInstance() {

        UsbCameraDeviceDialog dialog = new UsbCameraDeviceDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title" , "Select USB Camera Device: ");
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public View InitView() {
        mContentView = LayoutInflater.from(getActivity()).inflate(R.layout.usb_camera_dialog, null);
        mTvNoDevice = (TextView) mContentView.findViewById(R.id.tv_no_device);
        mDeviceListView = (ListView) mContentView.findViewById(R.id.lv_usb_camera_devices);
        mTvNoDevice.setVisibility(View.INVISIBLE);

        return mContentView;
    }

    @Override
    public void onClickListener(int key) {
        // UsbCameraManager.getInstance().updateDeviceList();
        List<UsbDevice> deviceList = UsbCameraManager.getInstance().getUsbCameraDeviceList();
        // confirm button
        if (key == DialogInterface.BUTTON_POSITIVE) {
            if (deviceList.size() == 0) {

            } else {
                final Object item = mDeviceListView.getItemAtPosition(0);
                if (item instanceof UsbDevice) {
                    //UsbCameraManager.getInstance().mUSBMonitor.requestPermission((UsbDevice)item);
                }
            }
        } else if (key == DialogInterface.BUTTON_NEGATIVE) {

        } else if (key == DialogInterface.BUTTON_NEUTRAL) {
            //List<UsbDevice> deviceList = UsbCameraManager.getInstance().getUsbCameraDeviceList();
            if (deviceList.size() == 0) {
                mTvNoDevice.setVisibility(View.VISIBLE);
            } else {
                mTvNoDevice.setVisibility(View.INVISIBLE);
                mDeviceListAdapter = new UsbCameraDeviceListAdapter(getActivity(), deviceList);
                mDeviceListView.setAdapter(mDeviceListAdapter);
            }
        }
    }

}
