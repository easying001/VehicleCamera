package Models;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.os.IBinder;
import android.util.Log;

import com.camera.easying.filecamera.AdapterService;
import com.camera.easying.filecamera.OnDeviceListener;

import Driver.UsbCameraRecordState;
import Driver.UsbDriver.IUsbDriver;
import Driver.UsbSdCardSpace;
import Driver.UsbCameraDeviceParam;

/**
 * Created by think on 2016/10/22.
 */

public class UsbAdapter {
    private static UsbAdapter mInstance;
    private AdapterServiceConnection adapterServiceConnection;
    private Context mContext;
    private AdapterService.AdapterServiceBinder mBinder;
    private OnDeviceListener mListener;
    private OnServiceListener mServiceListener;
    public UsbAdapter() {

    }

    public interface OnServiceListener {
        void onServiceConnected();
    }

    public void init(Context cx, OnServiceListener callback) {
        mContext = cx;
        mServiceListener = callback;
        startService(AdapterService.class);
    }

    public void registerCallback(OnDeviceListener callback) {
        if (mBinder != null) {
            mBinder.registerCallback(callback);
        }
    }

    public void unregisterCallback(OnDeviceListener callback) {
        if (mBinder != null) {
            mBinder.unregisterCallback(callback);
        }
    }

    public static UsbAdapter getInstance() {
        if (null == mInstance) {
            synchronized(UsbAdapter.class) {
                if (null == mInstance) {
                    mInstance = new UsbAdapter();
                }
            }
        }
        mInstance.getDevParam();
        return mInstance;
    }

    public boolean isUsbCameraDeviceConnected() {
        if (mBinder != null) {
            return mBinder.isUsbCameraDeviceConnected();
        } else {
            return false;
        }
    }

    public boolean isUsbCameraOpened() {
        if (mBinder != null) {
            return mBinder.isUsbCameraOpened();
        } else {
            return false;
        }
    }

    public boolean isUsbFileDeviceConnected() {
        if (mBinder != null) {
            return mBinder.isUsbFileDeviceConnected();
        } else {
            return false;
        }
    }

    public void setUsbFileMode() {
        if (mBinder != null) {
            mBinder.setUsbFileMode();
        }
    }

    public void setUsbCameraMode() {
        if (mBinder != null) {
            mBinder.setUsbCameraMode();
        }
    }

    public void setupDevice(UsbDevice device) {
        if (mBinder != null) {
            mBinder.setupDevice(device);
        }
    }

    public String getDevVersion() {
        if (mBinder != null) {
            return mBinder.getDevVersion();
        } else {
            return null;
        }
    }

    public long getDevTime() {
        if (mBinder != null) {
            return mBinder.getDevTime();
        } else {
            return 0;
        }
    }


    public void setDevTime(long time, byte zone) {
        if (mBinder != null) {
            mBinder.setDevTime(time, zone);
        }
    }

    public UsbCameraDeviceParam getDevParam() {
        if (mBinder != null) {
            return mBinder.getDevParam();
        } else {
            return null;
        }
    }

    public int getVideoParam() {
        if (mBinder != null) {
            return mBinder.getVideoParam();
        } else {
            return 0;
        }
    }

    public UsbCameraRecordState getRecordState() {
        if (mBinder != null) {
            return mBinder.getRecordState();
        } else {
            return null;
        }
    }

    public void setRecordState(int state) {
        if (mBinder != null) {
            mBinder.setRecordState(state);
        }
    }

    public void setTakePic(int pic_num) {
        if (mBinder != null) {
            mBinder.setTakePic(pic_num);
        }
    }

    public UsbSdCardSpace getSDCardSpace() {
        if (mBinder != null) {
            return mBinder.getSDCardSpace();
        } else {
            return null;
        }
    }

    public void setMic(int level) {
        if (mBinder != null) {
            mBinder.setMic(level);
        }
    }

    public void setGsensorSensitivity(int sensitivity) {
        if (mBinder != null) {
            mBinder.setGsensorSensitivity(sensitivity);
        }
    }

    public void startProtectedRecord(int seconds) {
        if (mBinder != null) {
            mBinder.startProtectedRecord(seconds);
        }
    }

    public void resetFactoryDefault() {
        if (mBinder != null) {
            mBinder.resetFactoryDefault();
        }
    }

    public void formatSDCard() {
        if (mBinder != null) {
            mBinder.formatSdCard();
        }
    }

    public void setAuthChallenge() {
        if (mBinder != null) {
            mBinder.setAuthRandom();
        }
    }

    public boolean getAuthResponse() {
        if (mBinder != null) {
            return mBinder.getAuthResponse();
        } else {
            return false;
        }
    }

    public boolean isPortOpened() {
        if (mBinder != null) {
            return mBinder.isPortOpened();
        } else {
            return false;
        }
    }

    public void setUsbConnection(UsbDeviceConnection connection) {
        if (mBinder != null) {
            mBinder.setUsbConnection(connection);
        }
    }

    public void discoverAllDevice() {
        if (mBinder != null) {
            mBinder.discoverAllDevice();
        }
    }

    public IUsbDriver getLoadedDriver() {
        if (mBinder != null) {
            return mBinder.getLoadedDriver();
        } else {
            return null;
        }
    }

    public UsbDevice getLoadedDevice() {
        UsbDevice device = null;
        if (mBinder != null) {
            IUsbDriver usbDriver = mBinder.getLoadedDriver();
            if (usbDriver != null) {
                device = usbDriver.getDevice();
            }
        }

        return device;
    }

    public void stopDevice() {
        if (mBinder != null) {
            mBinder.stopDevice();
        }
    }


    private void startService(Class<?> serviceClass) {
        Log.d("UsbActivity", "startService ");
        Intent intent = new Intent(mContext, serviceClass);
        adapterServiceConnection = new AdapterServiceConnection();
        mContext.bindService(intent, adapterServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public class AdapterServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AdapterService.AdapterServiceBinder binder = (AdapterService.AdapterServiceBinder) iBinder;
            Log.d("UsbActivity", "Bind Service = " + iBinder);
            if (binder != null) {
                mBinder = binder;
                mBinder.registerCallback(mListener);
                if (mServiceListener != null) {
                    mServiceListener.onServiceConnected();
                }
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBinder = null;
        }
    }


}
