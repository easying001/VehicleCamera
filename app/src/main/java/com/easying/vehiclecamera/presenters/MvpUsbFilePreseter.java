package com.easying.vehiclecamera.presenters;

import com.easying.vehiclecamera.models.UsbFileManager;
import com.easying.vehiclecamera.fragments.MvpUsbFileFragment;


/**
 * Created by think on 2016/9/29.
 */

public class MvpUsbFilePreseter extends MvpBasePresenter<MvpUsbFileFragment> {

    private UsbFileManager mUsbFileManager;

    public MvpUsbFilePreseter() {

    }

    public void init() {
//
//        if (mUsbFileManager == null) {
//            mUsbFileManager = mUsbFileManager.getInstance();
//        }
//
        if (isViewAttached()) {
            //mUsbFileManager.init(getView().getParentActivity(), this);
            //getView().showProgressDialog();
            //Log.d("UsbPresenter", "mUsbFileManager.init = " + mUsbFileManager);
            //mUsbFileManager.getMassStorageDevices();
        }
    }

    public void uninit() {
//        if (isViewAttached()) {
//            mUsbFileManager.uninit();
//        }
//        mUsbFileManager = null;
    }

    public void listUsbFiles() {
        if (isViewAttached()) {
//            if (mUsbFileManager.getmDeviceState())
//
//            if (mUsbFileManager.getPreviewState()) {
//                getView().showButtonIcon(true);
//            } else {
//                UsbCameraDeviceDialog mUsbCameraDialog = UsbCameraDeviceDialog.newInstance();
//                UsbFragmentManager.getInstance().showAlertDialog(mUsbCameraDialog);
//                mUsbCameraDialog.setTargetFragment(getView(), 0);
//            }
        }

    }


//    private class UsbFileScanDevice extends AsyncTask<String, String, String> {
//        @Override
//        protected void onPreExecute() {
//            getView().showProgressDialog();
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            getView().dismissProgressDialog();
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            Log.d("UsbTask", "UsbCameraScanDevice - doInBackground()");
//            int downCounter = 50;
//            // Scan usb device and find matched usb driver
//            List<IUsbDriver> usbDrivers = UsbCameraManager.getInstance().findUsbDrivers();
//            // traversing all the detected driver, in case of UVC device, start previewing
//            // in case of Usb Storage device, send the usb command to switch USB mode
//            for(IUsbDriver driver : usbDrivers) {
//                if ((driver.getDevice().getVendorId() == UsbId.VENDOR_SONIX) && (driver.getDevice().getProductId() == UsbId.SONIX_BULK)){
//                    publishProgress("Usb Camera Device Detected");
//                    UsbCameraManager.getInstance().mUSBMonitor.requestPermission(driver.getDevice());
//                    while(downCounter-- > 0) {
//                        try {
//                            Thread.sleep(100);
//                            publishProgress("Starting UVC Previewing");
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        if (UsbCameraManager.getInstance().getPreviewState() == true) {
//                            break;
//                        }
//                    }
//                    break;
//                } else { // TODO: switch Usb Mode to UVC
//                    publishProgress("Usb Storage Device Detected");
//                }
//            }
//            return null;
//        }
//
//        @Override
//        protected void onProgressUpdate(String... values) {
//            getView().setProgressDialog(values[0]);
//        }
//    }
}
