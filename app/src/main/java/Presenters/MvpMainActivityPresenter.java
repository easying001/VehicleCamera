package Presenters;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.usb.UsbDevice;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.camera.easying.filecamera.AdapterService;
import com.camera.easying.filecamera.MainActivity;
import com.camera.easying.filecamera.OnDeviceListener;
import com.camera.easying.filecamera.R;
import com.camera.easying.filecamera.UsbFragmentManager;

import Driver.UsbDriver.IUsbDriver;
import Driver.UsbId;
import Models.UsbAdapter;
import Views.MvpUsbCameraFragment;
import Views.MvpUsbFileFragment;
import Views.MvpUsbSettingFragment;


/**
 * Created by think on 2016/9/26.
 */
/* MainActivity presenter maintain the logic to manipulate fragment and
    discovery Usb Device attached and mount the corresponding driver
 */
public class MvpMainActivityPresenter extends MvpBasePresenter<MainActivity> {

    private static final int USB_MODE_MSC = 0;
    private static final int USB_MODE_UVC = 1;
    private UsbFragmentManager mFragmentManager;
    private MvpUsbCameraFragment mUsbCameraFragment;
    private MvpUsbSettingFragment mUsbSettingFragment;
    private MvpUsbFileFragment mUsbFileFragment;

    private UsbAdapter mUsbAdapter;
    private Handler mHandler = new Handler();
    private UsbDeviceModeListener mUsbModeListener;

    private interface UsbDeviceModeListener{
        void onDeviceModeChanged(int mode);
    }
    public MvpMainActivityPresenter() {

    }

    public void init() {
        mUsbAdapter = UsbAdapter.getInstance();
        mUsbAdapter.init(getView().getApplicationContext(), new UsbAdapter.OnServiceListener() {
            @Override
            public void onServiceConnected() {
                Log.d("UsbPresenter", "register device status callback");
                mUsbAdapter.registerCallback(mDeviceStatusCallback);
                showUsbCameraFragment();
                getView().selectButton(R.id.ib_open_camera);
            }
        });


    }

    public OnDeviceListener mDeviceStatusCallback = new OnDeviceListener() {
        @Override
        public void onDeviceAttacted(UsbDevice device) {

        }

        @Override
        public void onDeviceSetup(UsbDevice device) {
            Log.d("UsbFragment", "on usb device setup pid = " + device.getProductId());
            if (device.getProductId() == UsbId.SONIX_BULK) {
                if (mUsbModeListener != null) {
                    mUsbModeListener.onDeviceModeChanged(USB_MODE_UVC);
                    //mUsbModeListener = null;
                }
            } else if(device.getProductId() == UsbId.SONIX_STORAGE){
                if (mUsbModeListener != null) {
                    mUsbModeListener.onDeviceModeChanged(USB_MODE_MSC);
                    //mUsbModeListener = null;
                }
            }
        }

        @Override
        public void onDeviceDeattched(UsbDevice device) {

        }
    };

    @Override
    public void attachView(MainActivity view) {
        super.attachView(view);
        mFragmentManager = UsbFragmentManager.getInstance();
        Log.d("UsbMain", "mFragmentManager = " + mFragmentManager);
        Log.d("UsbMain", "MvpMainActivityPresenter.getView() = " + getView());
        mFragmentManager.init(getView().getParentActivity());
        mUsbCameraFragment = MvpUsbCameraFragment.newInstance();
        mUsbSettingFragment = MvpUsbSettingFragment.newInstance();
        mUsbFileFragment = MvpUsbFileFragment.newInstance();

    }

    public boolean isUsbCameraDeviceLoaded() {
        UsbDevice device = mUsbAdapter.getLoadedDevice();
        if (device != null) {
            return device.getProductId() == UsbId.SONIX_BULK;
        } else {
            return false;
        }
    }

    public boolean isUsbStorageDeviceLoaded() {
        UsbDevice device = mUsbAdapter.getLoadedDevice();
        if (device != null) {
            return device.getProductId() == UsbId.SONIX_STORAGE;
        } else {
            return false;
        }
    }

    public void showFileFragment() {
        // Switch to Usb Storage Fragment
        // Make sure has been switch to usb file mode
        if (isUsbCameraDeviceLoaded()) {
            getView().showProgressDialog();
            //mUsbAdapter.setUsbFileMode();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d("UsbActivity", "dismiss progress dialog");
                    getView().dismissProgressDialog();
                    mFragmentManager.showFragment(mUsbFileFragment);
                }
            }, 200);
//            mUsbModeListener = new UsbDeviceModeListener() {
//                @Override
//                public void onDeviceModeChanged(int mode) {
//                    if (mode == USB_MODE_MSC) {
//                        getView().\dismissProgressDialog();
//                        mFragmentManager.showFragment(mUsbFileFragment);
//                    }
//                }
//            };
        } else if (isUsbStorageDeviceLoaded()) {
            mFragmentManager.showFragment(mUsbFileFragment);
        }
    }


    public void showUsbCameraFragment() {
        if (mUsbAdapter != null) {
            // In case of MSC mode, to switch to Camera mode, we need to request the permission and send switch command
            // than set switch command directly
            if (isUsbStorageDeviceLoaded()) {
                Log.d("UsbFragment", "Usb MSC mode, starting to switch mode");
                IUsbDriver usbDriver = mUsbAdapter.getLoadedDriver();
                getView().showProgressDialog();
                mUsbModeListener = new UsbDeviceModeListener() {
                    @Override
                    public void onDeviceModeChanged(int mode) {
                        if (mode == USB_MODE_UVC) {
                            Log.d("UsbFragment", "finish switch mode, dismiss dialog");
                            getView().dismissProgressDialog();
                            mFragmentManager.showFragment(mUsbCameraFragment);
                        }
                    }
                };
                Log.d("UsbFragment", "show progress dialog mUsbModeListener = " + mUsbModeListener);
                mUsbAdapter.setupDevice(usbDriver.getDevice());
            } else if (isUsbCameraDeviceLoaded() && !mUsbAdapter.isPortOpened()) {
                Log.d("UsbFragment", "Already in UVC mode, waiting for open port");
                mUsbAdapter.discoverAllDevice();
                mUsbModeListener = new UsbDeviceModeListener() {
                    @Override
                    public void onDeviceModeChanged(int mode) {
                        if (mode == USB_MODE_UVC) {
                            mFragmentManager.showFragment(mUsbCameraFragment);
                        }
                    }
                };
            } else {
                mFragmentManager.showFragment(mUsbCameraFragment);
            }
        }
    }

    public void showSettingFragment() {
        if (mUsbAdapter != null) {
            // In case of MSC mode, to switch to Camera mode, we need to request the permission and send switch command
            // than set switch command directly
            if (isUsbStorageDeviceLoaded()) {
                IUsbDriver usbDriver = mUsbAdapter.getLoadedDriver();
                getView().showProgressDialog();
                mUsbModeListener = new UsbDeviceModeListener() {
                    @Override
                    public void onDeviceModeChanged(int mode) {
                        if (mode == USB_MODE_UVC) {
                            getView().dismissProgressDialog();
                            mFragmentManager.showFragment(mUsbSettingFragment);
                        }
                    }
                };
                mUsbAdapter.setupDevice(usbDriver.getDevice());
            } else if (isUsbCameraDeviceLoaded() && !mUsbAdapter.isPortOpened()) {
                mUsbAdapter.discoverAllDevice();
                mUsbModeListener = new UsbDeviceModeListener() {
                    @Override
                    public void onDeviceModeChanged(int mode) {
                        if (mode == USB_MODE_UVC) {
                            mFragmentManager.showFragment(mUsbSettingFragment);
                        }
                    }
                };
            } else {
                mFragmentManager.showFragment(mUsbSettingFragment);
            }
        }
    }

//    public void showCameraFragment() {
//        Log.d("UsbFragment", "mUsbCameraFragment = " + mUsbCameraFragment);
//        if (mUsbAdapter != null) {
//            IUsbDriver usbDriver = mUsbAdapter.getLoadedDriver();
//            if (usbDriver != null) {
//                if (usbDriver.getDevice().getProductId() == UsbId.SONIX_STORAGE) {
//                    // In MSC mode, show the progress dialog and perform mode switching
//                    getView().showProgressDialog();
//                    mHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            Log.d("UsbFragment", "dismiss progress dialog and show Camera Framgnet");
//                            // Dismiss the progress dialog until user grant the usb permission
//                            getView().dismissProgressDialog();
//                            mFragmentManager.showFragment(mUsbCameraFragment);
//                        }
//                    }, 10000);
////                    mUsbAdapter.registerCallback(new OnDeviceListener() {
////                        @Override
////                        public void onDeviceMounted(UsbDevice device) {
////                            // Dismiss the progress dialog until user grant the usb permission
////                            getView().dismissProgressDialog();
////                            Log.d("UsbPresenter", "onDeviceMounted : " + Integer.toHexString(device.getProductId()));
////                            mFragmentManager.showFragment(mUsbCameraFragment);
////                        }
////                    });
//                    // setup device will trigger request permission to set USB mode
//                    // once USB MSC device is opened ,the default operation is to swith to Camerma mode
//                    Log.d("UsbFragment", "setup and set device to Camera Mode");
//                    mUsbAdapter.setupDevice(usbDriver.getDevice());
//                    //mUsbAdapter.setUsbCameraMode();
//                } else if (usbDriver.getDevice().getProductId() == UsbId.SONIX_BULK && !mUsbAdapter.isPortOpened()) {
//                    // In UVC mode, request the usb access permission and load the driver
//                    mUsbAdapter.discoverAllDevice();
//                    mUsbAdapter.registerCallback(new OnDeviceListener() {
//                        @Override
//                        public void onDeviceMounted(UsbDevice device) {
//                            Log.d("UsbPresenter", "onDeviceMounted : " + Integer.toHexString(device.getProductId()));
//                            mFragmentManager.showFragment(mUsbCameraFragment);
//                        }
//                    });
//                } else {
//                    mFragmentManager.showFragment(mUsbCameraFragment);
//                }
//            }
//
//        }
//    }

//    public void showSettingFragment() {
//        Log.d("UsbActivity", "mUsbSettingFragment = " + mUsbSettingFragment);
//        if (mUsbAdapter != null) {
//            IUsbDriver usbDriver = mUsbAdapter.getLoadedDriver();
//            if (usbDriver != null) {
//                if (usbDriver.getDevice().getProductId() == UsbId.SONIX_STORAGE) {
//                    // In MSC mode, show the progress dialog and perform mode switching
//                    getView().showProgressDialog();
//                    mUsbAdapter.registerCallback(new OnDeviceListener() {
//                        @Override
//                        public void onDeviceMounted(UsbDevice device) {
//                            // Dismiss the progress dialog until user grant the usb permission
//                            getView().dismissProgressDialog();
//                            Log.d("UsbPresenter", "onDeviceMounted : " + Integer.toHexString(device.getProductId()));
//                            mFragmentManager.showFragment(mUsbSettingFragment);
//                        }
//                    });
//                    mUsbAdapter.setUsbCameraMode();
//                } else if (usbDriver.getDevice().getProductId() == UsbId.SONIX_BULK && !mUsbAdapter.isPortOpened()) {
//                    // In UVC mode, request the usb access permission and load the driver
//                    mUsbAdapter.discoverAllDevice();
//                    mUsbAdapter.registerCallback(new OnDeviceListener() {
//                        @Override
//                        public void onDeviceMounted(UsbDevice device) {
//                            Log.d("UsbPresenter", "onDeviceMounted : " + Integer.toHexString(device.getProductId()));
//                            mFragmentManager.showFragment(mUsbSettingFragment);
//                        }
//                    });
//                } else {
//                    mFragmentManager.showFragment(mUsbSettingFragment);
//                }
//            }
//
//        }
//    }

}
