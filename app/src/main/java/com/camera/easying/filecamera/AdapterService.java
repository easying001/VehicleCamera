package com.camera.easying.filecamera;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Driver.UsbCameraRecordState;
import Driver.UsbDriver.CommonUsbPort;
import Driver.UsbDriver.IUsbBasePort;
import Driver.UsbDriver.IUsbDriver;
import Driver.UsbDriver.IUsbStoragePort;
import Driver.UsbDriver.IUsbVideoPort;
import Driver.UsbId;
import Driver.UsbRequest;
import Driver.UsbSdCardSpace;
import Driver.UsbCameraDeviceParam;
import Driver.UsbVideoProber;
import Utils.HexDumpHelper;

/**
 * Created by think on 2016/10/21.
 */

public class AdapterService extends Service{
    private static final String ACTION_USB_STORAGE_PERMISSION = "com.filecamera.storage.USB_PERMISSION";
    private static final String ACTION_USB_CAMERA_PERMISSION = "com.filecamera.camera.USB_PERMISSION";
    private static final int AUTH_KEY_LENGTH = 5;
    private static AdapterService mAdapterService;
    private AdapterServiceBinder mBinder;
    private static ArrayList<OnDeviceListener> mOnDeviceListener;
    private IUsbDriver mUsbDriver;
    private UsbManager mUsbManager;
    private IUsbBasePort mUsbPort;
    private boolean mCleaningUp;
    private byte[] randomKey;
    private byte[] privateKey = {1,2,1,2,1};

    public  UsbCameraRecordState mUsbCameraRecordState;
    public  UsbCameraDeviceParam mUsbCameraDeviceParam;

    void Cleanup() {
        if (mCleaningUp) {
            return;
        }
        mCleaningUp = true;
        unregisterReceiver(usbReceiver);
        if (mBinder != null) {
            mBinder = null;
        }
    }
    public static class AdapterServiceBinder extends Binder {
        private AdapterService mService;

        public AdapterServiceBinder(AdapterService svc) {
            mService = svc;
        }

        public AdapterService getService() {
            if ((mService != null) && (mService.isAvailable())) {
                return mService;
            }
            return null;
        }

        public void registerCallback(OnDeviceListener listener) {
//            mOnDeviceListener = listener;
            if (mOnDeviceListener == null) {
                mOnDeviceListener = new ArrayList<OnDeviceListener>();
            }
            if (!mOnDeviceListener.contains(listener) && listener != null) {
                Log.d("UsbService", "add a listener and mOnDeviceListener.size = " + mOnDeviceListener.size());
                mOnDeviceListener.add(listener);
            }
        }

        public void unregisterCallback(OnDeviceListener listener) {
            if (mOnDeviceListener == null || listener == null) {
                return;
            }
            mOnDeviceListener.remove(listener);
        }

        public List<UsbDevice> getConnectedUsbDevice() {
            AdapterService service = getService();
            if (service != null) {
                return service.getConnectedUsbDevice();
            } else {
                return null;
            }
        }

        public IUsbDriver getLoadedDriver() {
            AdapterService service = getService();
            if (service != null) {
                return service.getLoadedDriver();
            } else {
                return null;
            }
        }

        public boolean isUsbCameraDeviceConnected() {
            AdapterService service = getService();
            if (service != null) {
                return service.isUsbCameraDeviceConnected();
            } else {
                return false;
            }
        }

        public boolean isUsbCameraOpened() {
            AdapterService service = getService();
            if (service != null) {
                return service.isUsbCameraOpened();
            } else {
                return false;
            }
        }

        public boolean isUsbFileDeviceConnected() {
            AdapterService service = getService();
            if (service != null) {
                return service.isUsbFileDeviceConnected();
            } else {
                return false;
            }
        }

        public void setUsbCameraMode() {
            AdapterService service = getService();
            if (service != null) {
                service.setUsbCameraMode();
            }
        }

        public void setUsbFileMode() {
            AdapterService service = getService();
            if (service != null) {
                service.setUsbFileMode();
            }
        }

        public UsbCameraRecordState getRecordState() {
            AdapterService service = getService();
            Log.d("UsbService", "getRecordState -----" +service.mUsbCameraRecordState);

            if (service != null) {
                if (service.mUsbCameraRecordState == null)
                {
                    service.mUsbCameraRecordState = service.getRecordState();
                }
                return service.mUsbCameraRecordState;
            } else {
                return null;
            }
        }

        public UsbCameraDeviceParam getDevParam() {
            AdapterService service = getService();
            Log.d("UsbService", "getDevParam -----" +service.mUsbCameraDeviceParam);

            if (service != null) {
                if (service.mUsbCameraDeviceParam == null)
                {
                    service.mUsbCameraDeviceParam = service.getDevParam();
                }
                return service.mUsbCameraDeviceParam;
            } else {
                return null;
            }
        }

        public int getVideoParam() {
            AdapterService service = getService();
            if (service != null) {
                return service.getVideoParam();
            } else {
                return 0;
            }
        }

        public void setDevTime(long time, byte timeZone) {
            AdapterService service = getService();
            if (service != null) {
                service.setDevTime(time, timeZone);
            }
        }

        public long getDevTime() {
            AdapterService service = getService();
            if (service != null) {
                return service.getDevTime();
            } else {
                return 0;
            }
        }

        public String getDevVersion() {
            AdapterService service = getService();
            Log.d("UsbService", "service = " + service);
            if (service != null) {
                return service.getDevVersion();
            } else {
                return null;
            }
        }

        public UsbSdCardSpace getSDCardSpace() {
            AdapterService service = getService();
            if (service != null) {
                return service.getSDCardSpace();
            } else {
                return null;
            }
        }

        public void setTakePic(int number) {
            AdapterService service = getService();
            if (service != null) {
                service.setTakePic(number);
            }
        }

        public void setRecordState(int state) {
            AdapterService service = getService();
            if (service != null) {
                service.setRecordState(state);
            }
        }

        public void setUsbConnection(UsbDeviceConnection connection) {
            AdapterService service = getService();
            if (service != null) {
                service.setUsbConnection(connection);
            }
        }

        public void setMic(int level) {
            AdapterService service = getService();
            if (service != null) {
                service.setMic(level);
                service.mUsbCameraRecordState.volumn_level = (byte)level;
            }
        }

        public void setGsensorSensitivity(int sensitivity) {
            AdapterService service = getService();
            if (service != null) {
                service.setGsensorSensitivity(sensitivity);
                service.mUsbCameraDeviceParam.gSensorSensitivity = sensitivity;
            }
        }

        public void startProtectedRecord(int seconds) {
            AdapterService service = getService();
            if (service != null) {
                service.startProtectedRecord(seconds);
            }
        }

        public void resetFactoryDefault() {
            AdapterService service = getService();
            if (service != null) {
                service.resetFactoryDefault();
            }
        }

        public void formatSdCard() {
            AdapterService service = getService();
            if (service != null) {
                service.formatSdCard();
            }
        }

        public boolean isPortOpened() {
            AdapterService service = getService();
            if (service != null) {
                return service.isUsbPortOpened();
            } else {
                return false;
            }
        }

        public void setupDevice(UsbDevice device) {
            AdapterService service = getService();
            if (service != null) {
                service.setupDevice(device);
            }
        }

        public void discoverAllDevice() {
            AdapterService service = getService();
            if (service != null) {
                service.discoverAllDevice();
            }
        }

        public void setAuthRandom() {
            AdapterService service = getService();
            if (service != null) {
                service.setAuthRandom();
            }
        }

        public boolean getAuthResponse() {
            AdapterService service = getService();
            if (service != null) {
                return service.getAuthResponse();
            } else {
                return false;
            }
        }
    }



    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new AdapterServiceBinder(this);
        Log.d("UsbService", "onCreate");
        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        registerUsbListener();
        registerPowerOffListener();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("UsbService", "onDestroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("UsbService", "onStartCommand");
        discoverAllDevice();
        return super.onStartCommand(intent, flags, startId);
    }

    public void registerUsbListener() {
        IntentFilter filter = new IntentFilter(ACTION_USB_CAMERA_PERMISSION);
        filter.addAction(ACTION_USB_STORAGE_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(usbReceiver, filter);
    }

    public void registerPowerOffListener() {
        Log.d("UsbService", "registerPowerOffListener");
        IntentFilter filter = new IntentFilter(Intent.ACTION_SHUTDOWN);
        filter.addAction("android.intent.action.QUICKBOOT_POWEROFF");
        registerReceiver(mPowerStatsReceiver, filter);
    }

    public void unregisterPowerOffListener() {
        unregisterReceiver(mPowerStatsReceiver);
    }

    private BroadcastReceiver mPowerStatsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_SHUTDOWN)) {
                Log.d("UsbService", "on reception of showdown command");
                if (mUsbDriver != null && mUsbDriver.getDevice().getProductId() == UsbId.SONIX_BULK) {
                    setRecordState(0);
                }
            } else if (intent.getAction().equalsIgnoreCase("android.intent.action.QUICKBOOT_POWEROFF")) {
                Log.d("UsbService", "on reception of showdown command");
            }
        }
    };

    private boolean isAvailable() {
        return !mCleaningUp;
    }

    private boolean isUsbPortOpened() {
        if (mUsbPort != null) {
            return mUsbPort.getPortState() == CommonUsbPort.STATE_OPENED;
        } else {
            return false;
        }
    }

    // In case of poweron, usb camera has been connected with HU, this service will lauch automatically,
    // and our serice will perform an active scanning to find out the attached usb device and load the corresponding
    // driver to open this device
    private void discoverAllDevice() {
        if (mUsbDriver != null) {
            Log.d("UsbSerice", "Usb Driver Instance is already existed");
            return;
        }

        for (final UsbDevice usbDevice : mUsbManager.getDeviceList().values()) {
            mUsbDriver = UsbVideoProber.getInstance().loadUsbDriver(usbDevice);
            if (mUsbDriver != null) {
                setupDevice(usbDevice);
                break;
            }
        }
    }

    private void closeDevice(UsbDevice device) {
        IUsbDriver driver = UsbVideoProber.getInstance().getUsbDriver(device);
        if (driver == null) {
            return ;
        }
        IUsbBasePort port = driver.getPorts().get(0);
        try {
            port.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        UsbVideoProber.getInstance().unloadUsbDriver(device);
    }

    private void stopDevice(UsbDevice device) {
        if (mUsbDriver == null) {
            return;
        } else if(mUsbDriver.getDevice().getProductId() != device.getProductId()){
            Log.d("UsbService", "Other usb device plug out");
        } else {
            IUsbBasePort port = mUsbDriver.getPorts().get(0);
            try {
                port.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mUsbDriver = null;
            mUsbPort = null;
            Log.d("UsbService", "clear mUsbDriver");
        }
    }

    public void setupDevice(UsbDevice device) {
        if (mUsbDriver == null) {
            Log.d("UsbService", "Usb Driver is NOT loaded");
            return;
        } else if (mUsbDriver.getDevice().getProductId() != device.getProductId()) {
            Log.d("UsbService", "Usb Driver is not matched with device,reload driver...");
            mUsbDriver = UsbVideoProber.getInstance().loadUsbDriver(device);
            initDevice(device);
        } else {
            Log.d("UsbService", "driver is loaded and start init device");
            initDevice(device);
        }

    }

    private void initDevice(UsbDevice device) {
        if (mUsbManager.hasPermission(device)) {
            Log.d("UsbService", "permission has been granted, just setup device");
            mUsbPort = mUsbDriver.getPorts().get(0);
            try {
                mUsbPort.open(mUsbManager);
                if (device.getProductId() == UsbId.SONIX_BULK) {
                    long time = System.currentTimeMillis() / 1000;
                    setDevTime(time, (byte) 0x08);
                    //showRecordState();
                } else if (device.getProductId() == UsbId.SONIX_STORAGE) {
                    setUsbCameraMode();
                }
                Log.d("UsbService", "open device mUsbPort = " + mUsbPort);
            } catch (IOException e) {
                e.printStackTrace();
            }
//            if (mOnDeviceListener != null) {
//                mOnDeviceListener.onDeviceMounted(device);
//                mOnDeviceListener = null;
//            }
            if (mOnDeviceListener == null) {
                Log.d("UsbService", "mOnDeviceListener.size = 0");
            } else {
                Log.d("UsbService", "mOnDeviceListener.size = " + mOnDeviceListener.size());
            }
            if (mOnDeviceListener != null && mOnDeviceListener.size() > 0) {
                for (OnDeviceListener callBack : mOnDeviceListener) {
                    callBack.onDeviceSetup(device);
                }
            }
        } else {

            if (device.getProductId() == UsbId.SONIX_BULK) {
                // first request permission from user to communicate with the
                // underlying UsbDevice

                PendingIntent permissionIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(
                        ACTION_USB_CAMERA_PERMISSION), 0);
                if (!mUsbManager.hasPermission(device)) {
                    Log.d("UsbService", "request permission for device" + device.getProductId());
                    mUsbManager.requestPermission(device, permissionIntent);
                } else {
                    Log.d("UsbService", "permission already granted for : " + device.getProductId());
                }
            } else if (device.getProductId() == UsbId.SONIX_STORAGE) {
                Log.d("UsbService", "request permission for device" + device.getProductId());
                PendingIntent permissionIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(
                        ACTION_USB_STORAGE_PERMISSION), 0);

                if (!mUsbManager.hasPermission(device)) {
                    Log.d("UsbService", "request permission for device" + device.getProductId());
                    mUsbManager.requestPermission(device, permissionIntent);
                } else {
                    Log.d("UsbService", "permission already granted for : " + device.getProductId());
                }
            }
        }
    }

    private void discoverDevice(UsbDevice device) {
        UsbVideoProber.getInstance().loadUsbDriver(device);
        IUsbDriver driver = UsbVideoProber.getInstance().getUsbDriver(device);
        if (driver == null) {
            return;
        }
        if (mUsbManager.hasPermission(device)) {
            mUsbPort = driver.getPorts().get(0);
            try {
                mUsbPort.open(mUsbManager);
                if (device.getProductId() == UsbId.SONIX_BULK) {
                    long time = System.currentTimeMillis() / 1000;
                    setDevTime(time, (byte) 0x08);
                    showRecordState();
                } else if (device.getProductId() == UsbId.SONIX_STORAGE) {
                    setUsbCameraMode();
                }
                Log.d("UsbService", "open device mUsbPort = " + mUsbPort);
            } catch (IOException e) {
                e.printStackTrace();
            }
//            if (mOnDeviceListener != null) {
//                mOnDeviceListener.onDeviceMounted(device);
//                mOnDeviceListener = null;
//            }
            if (mOnDeviceListener == null) {
                Log.d("UsbService", "mOnDeviceListener.size = 0");
            } else {
                Log.d("UsbService", "mOnDeviceListener.size = " + mOnDeviceListener.size());
            }
            if (mOnDeviceListener != null && mOnDeviceListener.size() > 0) {
                for (OnDeviceListener callBack : mOnDeviceListener) {
                    callBack.onDeviceSetup(device);
                }
            }
        } else {
            if (device.getProductId() == UsbId.SONIX_BULK) {
                // first request permission from user to communicate with the
                // underlying UsbDevice
                PendingIntent permissionIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(
                        ACTION_USB_CAMERA_PERMISSION), 0);

                if (!mUsbManager.hasPermission(device)) {
                    Log.d("UsbService", "request permission for device" + device.getProductId());
                    mUsbManager.requestPermission(device, permissionIntent);
                } else {
                    Log.d("UsbService", "permission already granted for : " + device.getProductId());
                }
            } else if (device.getDeviceId() == UsbId.SONIX_STORAGE) {
                PendingIntent permissionIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(
                        ACTION_USB_STORAGE_PERMISSION), 0);
                if (!mUsbManager.hasPermission(device)) {
                    Log.d("UsbService", "request permission for device" + device.getProductId());
                    mUsbManager.requestPermission(device, permissionIntent);
                } else {
                    Log.d("UsbService", "permission already granted for : " + device.getProductId());
                }
            }
        }

    }

    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            Log.d("UsbService", "usbReceiver action = " + action);
            // In case of user grant the permission to access usb camera device
            // we need to open an usb port to this device
            if (ACTION_USB_CAMERA_PERMISSION.equals(action) || ACTION_USB_STORAGE_PERMISSION.equals(action)) {

                if (mUsbDriver == null) {
                    mUsbDriver = UsbVideoProber.getInstance().loadUsbDriver(device);
                }
                mUsbPort = mUsbDriver.getPorts().get(0);
                try {
                    mUsbPort.open(mUsbManager);
                    if (device.getProductId() == UsbId.SONIX_BULK) {
                        long time = System.currentTimeMillis() / 1000;
                        setDevTime(time, (byte) 0x08);
                        showRecordState();

                    } else if (device.getProductId() == UsbId.SONIX_STORAGE) {
                        setUsbCameraMode();
                    }
                    Log.d("UsbService", "open device mUsbPort = " + mUsbPort);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                if (mOnDeviceListener != null) {
//                    mOnDeviceListener.onDeviceMounted(device);
//                    mOnDeviceListener = null;
//                }
                if (mOnDeviceListener == null) {
                    Log.d("UsbService", "mOnDeviceListener.size = 0");
                } else {
                    Log.d("UsbService", "mOnDeviceListener.size = " + mOnDeviceListener.size());
                }
                if (mOnDeviceListener != null && mOnDeviceListener.size() > 0) {
                    for (OnDeviceListener callBack : mOnDeviceListener) {
                        callBack.onDeviceSetup(device);
                    }
                }
            } else if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                Toast.makeText(getApplicationContext(), "BroadcastReceiver: Usb Device Attached,PID = 0x" +
                                Integer.toHexString(device.getProductId()) + ", VID = 0x" + Integer.toHexString(device.getVendorId())
                        , Toast.LENGTH_SHORT).show();
                // Usb Device plugged in, load the driver and save the driver instance;
                mUsbDriver = UsbVideoProber.getInstance().loadUsbDriver(device);
                if (mUsbDriver != null) {
                    if (mOnDeviceListener != null && mOnDeviceListener.size() > 0) {
                        for (OnDeviceListener callBack : mOnDeviceListener) {
                            callBack.onDeviceAttacted(device);
                        }
                    }
                    Log.d("UsbService", "load driver for device :" + mUsbDriver.getDevice().getProductId());
                } else {
                    Log.d("UsbService", "no suitable driver matched");
                }
                Log.d("UsbService", "device.getPID = " + device.getProductId());
                if (device.getProductId() != UsbId.SONIX_STORAGE) {
                    setupDevice(device);
                }

            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                Toast.makeText(getApplicationContext(), "BroadcastReceiver: Usb Device Detached,PID = 0x" +
                        Integer.toHexString(device.getProductId()) + ", VID = 0x" + Integer.toHexString(device.getVendorId())
                        , Toast.LENGTH_SHORT).show();
                stopDevice(device);
                if (mOnDeviceListener != null && mOnDeviceListener.size() > 0) {
                    for (OnDeviceListener callBack : mOnDeviceListener) {
                        callBack.onDeviceDeattched(device);
                    }
                }
            }
        }

    };

    private void showRecordState() {
        UsbCameraRecordState recordState = getRecordState();
        if (recordState.status == 0) {
            Toast.makeText(getApplicationContext(), "录像已经停止", Toast.LENGTH_SHORT).show();
        } else if (recordState.status == 1) {
            Toast.makeText(getApplicationContext(), "录像已经开始", Toast.LENGTH_SHORT).show();

        } else if (recordState.status == 2) {
            Toast.makeText(getApplicationContext(), "SD-CARD不存在", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isUVCDevice(UsbDevice device) {
        if (device != null) {
            return device.getProductId() == UsbId.SONIX_BULK;
        } else {
            return false;
        }
    }

    private boolean isMSCDevice(UsbDevice device) {
        if (device != null) {
            return device.getProductId() == UsbId.SONIX_STORAGE;
        } else {
            return false;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.d("UsbService", "onBind");
        discoverAllDevice();
        return mBinder;
    }

    private List<UsbDevice> getConnectedUsbDevice() {
        List<UsbDevice> devices = new ArrayList<UsbDevice>();

        for (final UsbDevice usbDevice : mUsbManager.getDeviceList().values()) {
            if (usbDevice != null) {
                devices.add(usbDevice);
            }
        }
        return devices;
    }

    boolean isUsbCameraOpened() {
        if (mUsbPort != null) {
            if (mUsbPort instanceof IUsbVideoPort) {
                return true;
            }
        }
        return false;
    }

    boolean isUsbCameraDeviceConnected() {
        List<UsbDevice> deviceList = getConnectedUsbDevice();
        for (UsbDevice device : deviceList) {
            if (device.getProductId() == UsbId.SONIX_BULK) {
                return true;
            }
        }

        return false;
    }

    boolean isUsbFileDeviceConnected() {
        List<UsbDevice> deviceList = getConnectedUsbDevice();
        for (UsbDevice device : deviceList) {
            if (device.getProductId() == UsbId.SONIX_STORAGE) {
                return true;
            }
        }

        return false;
    }

    IUsbDriver getLoadedDriver() {
        return mUsbDriver;
    }

    void setUsbCameraMode() {
                Log.d("UsbService", "change to USB Camera Mode");
                byte[] data = new byte[0x0C];
                data[0] = 0x00;
                data[1] = 0x00;
                data[2] = 0x00;
                data[3] = 0x00;
                data[4] = 0x0E;

                if (mUsbPort != null) {
                    ((IUsbStoragePort) mUsbPort).switchMode(data);
                }
    }

    void setUsbFileMode() {
        byte[] data = new byte[0x0C];
        data[0] = 0x00;
        data[1] = 0x00;
        data[2] = 0x00;
        data[3] = 0x00;
        data[4] = 0x08;

        if (mUsbPort != null) {
            ((IUsbVideoPort)mUsbPort).switchMode(data);
        }
    }

    UsbCameraRecordState getRecordState() {
        UsbCameraRecordState recordState = new UsbCameraRecordState();
        byte[] data = new byte[0x40];
        int index = 1;
        data[0] = UsbRequest.USB_SUB_CMD_GET_RECORD_STATE;
        if (mUsbPort != null) {
            ((IUsbVideoPort)mUsbPort).getRecordState(data);
            recordState.status = data[index++];
            recordState.running_status = data[index++];
            recordState.volumn_level = data[index++];
            recordState.record_video_length_in_minutes = data[index++];
            recordState.record_video_fps = data[index++];
            recordState.record_video_bitrate = HexDumpHelper.toInt(data, index);
            index += 4;
            recordState.record_video_resolution = data[index++];

        }

        return recordState;
    }

    int getVideoParam() {
        int ret = 0;
       byte[] data = new byte[0x40];
        data[0] = UsbRequest.USB_SUB_CMD_GET_VIDEO_PARAM;
        if (mUsbPort != null) {
            ((IUsbVideoPort) mUsbPort).getDeviceVideoParam(data);
            ret = (int)(data[4]);
        }
        return ret;
    }

    UsbCameraDeviceParam getDevParam() {
        UsbCameraDeviceParam param = new UsbCameraDeviceParam();
        byte[] data = new byte[0x40];
        int index = 1;
        data[0] = UsbRequest.USB_SUB_CMD_GET_DEV_PARAM;
        if (mUsbPort != null) {
            ((IUsbVideoPort)mUsbPort).getDeviceParam(data);
            param.year = HexDumpHelper.toShort(data, index++);
            index++;
            param.month = data[index++];
            param.day = data[index++];
            param.hour = data[index++];
            param.minute = data[index++];
            param.second = data[index++];
            param.timezone = HexDumpHelper.toInt(data, index);
            index += 4;
            param.batteryLevel = HexDumpHelper.toInt(data, index);
            index += 4;
            param.powerFreq = HexDumpHelper.toInt(data, index);
            index += 4;
            param.gSensorSensitivity = HexDumpHelper.toInt(data, index);
            index += 4;

        }

        return param;
    }

    long getDevTime() {
        long timeInSeconds = 0;
        byte[] data = new byte[0x40];
        data[0] = UsbRequest.USB_SUB_CMD_GET_DEV_TIME;
        if (mUsbPort != null) {
            ((IUsbVideoPort)mUsbPort).getDeviceTime(data);
            timeInSeconds = HexDumpHelper.toLong(data, 1);
            Log.d("UsbControl", "data[1] = " + Integer.toHexString(data[1]));
            Log.d("UsbControl", "data[2] = " + Integer.toHexString(data[2]));
            Log.d("UsbControl", "data[3] = " + Integer.toHexString(data[3]));
            Log.d("UsbControl", "data[4] = " + Integer.toHexString(data[4]));
            Log.d("UsbControl", "data[5] = " + Integer.toHexString(data[5]));
            Log.d("UsbControl", "data[6] = " + Integer.toHexString(data[6]));
            Log.d("UsbControl", "data[7] = " + Integer.toHexString(data[7]));
            Log.d("UsbControl", "data[8] = " + Integer.toHexString(data[8]));
            Log.d("UsbControl", "time = " + Long.toHexString(timeInSeconds));
        }

        return timeInSeconds;

    }

    void setDevTime(long time, byte timeZone) {
        time += 8 * 60 * 60;
        byte[] data = new byte[0x40];
        data[0] = UsbRequest.USB_SUB_CMD_SET_DEV_TIME;
        data[1] = (byte) (time & 0x00000000000000FFL);
        data[2] = (byte) ((time & 0x000000000000FF00L) >> 8);
        data[3] = (byte) ((time & 0x0000000000FF0000L) >> 16);
        data[4] = (byte) ((time & 0x00000000FF000000L) >> 24);
        data[5] = (byte) ((time & 0x000000FF00000000L) >> 32);
        data[6] = (byte) ((time & 0x0000FF0000000000L) >> 40);
        data[7] = (byte) ((time & 0x00FF000000000000L) >> 48);
        data[8] = (byte) ((time & 0xFF00000000000000L) >> 56);

        data[9] = timeZone;
        Log.d("UsbControl", "data[1] = " + Integer.toHexString(data[1]));
        Log.d("UsbControl", "data[2] = " + Integer.toHexString(data[2]));
        Log.d("UsbControl", "data[3] = " + Integer.toHexString(data[3]));
        Log.d("UsbControl", "data[4] = " + Integer.toHexString(data[4]));
        Log.d("UsbControl", "data[5] = " + Integer.toHexString(data[5]));
        Log.d("UsbControl", "data[6] = " + Integer.toHexString(data[6]));
        Log.d("UsbControl", "data[7] = " + Integer.toHexString(data[7]));
        Log.d("UsbControl", "data[8] = " + Integer.toHexString(data[8]));
        if (mUsbPort != null) {
            ((IUsbVideoPort)mUsbPort).setDeviceTime(data);
        }
    }

    String getDevVersion() {
        String retString = "Get Device Version Error";
        byte[] data = new byte[0x40];
        data[0] = UsbRequest.USB_SUB_CMD_GET_DEV_VERSION;
        Log.d("UsbService", "mUsbPort = " + mUsbPort);
        if (mUsbPort != null) {
            ((IUsbVideoPort)mUsbPort).getDeviceVersion(data);
            retString = new String(data);
        } else {
            retString = "Usb Camera Not Connected";
        }

        return retString;
    }

    UsbSdCardSpace getSDCardSpace() {
        UsbSdCardSpace space = new UsbSdCardSpace();
        byte[] data = new byte[0x40];
        data[0] = UsbRequest.USB_SUB_CMD_GET_SDCARD_REMAINING;
        if (mUsbPort != null) {
            ((IUsbVideoPort)mUsbPort).getSDCardSpace(data);
            space.remaining = HexDumpHelper.toInt(data, 1);
            space.capacity = HexDumpHelper.toInt(data, 5);
            //Log.d("UsbControl", "time = " + Integer.toHexString((int)timeInSeconds));
        }

        return space;
    }

    void setTakePic(int number) {
        byte[] data = new byte[0x40];
        data[0] = UsbRequest.USB_SUB_CMD_SET_RECORD_TAKEPIC;
        data[1] = (byte) (number & 0x000000FF);
        data[2] = (byte) ((number & 0x0000FF00) >> 8);
        data[3] = (byte) ((number & 0x00FF0000) >> 16);
        data[4] = (byte) ((number & 0xFF000000) >> 24);
        Log.d("UsbControl", "data[1] = " + data[1]);
        Log.d("UsbControl", "data[2] = " + data[2]);
        Log.d("UsbControl", "data[3] = " + data[3]);
        Log.d("UsbControl", "data[4] = " + data[4]);
        if (mUsbPort != null) {
            ((IUsbVideoPort)mUsbPort).setTakePic(data);
        }
    }

    void setRecordState(int state) {
        byte[] data = new byte[0x40];
        data[0] = UsbRequest.USB_SUB_CMD_SET_RECORD_STATE;
        data[1] = (byte) (state & 0x000000FF);
        data[2] = (byte) ((state & 0x0000FF00) >> 8);
        data[3] = (byte) ((state & 0x00FF0000) >> 16);
        data[4] = (byte) ((state & 0xFF000000) >> 24);
        Log.d("UsbControl", "data[1] = " + data[1]);
        Log.d("UsbControl", "data[2] = " + data[2]);
        Log.d("UsbControl", "data[3] = " + data[3]);
        Log.d("UsbControl", "data[4] = " + data[4]);
        if (mUsbPort != null) {
            ((IUsbVideoPort)mUsbPort).setRecordState(data);
        }
    }

    void setMic(int level) {
        byte[] data = new byte[0x40];
        data[0] = UsbRequest.USB_SUB_CMD_SET_RECORD_MIC_LEVEL;
        data[1] = (byte) (level & 0x000000FF);
        data[2] = (byte) ((level & 0x0000FF00) >> 8);
        data[3] = (byte) ((level & 0x00FF0000) >> 16);
        data[4] = (byte) ((level & 0xFF000000) >> 24);

        if (mUsbPort != null) {
            ((IUsbVideoPort)mUsbPort).setMic(data);
        }
    }

    void setGsensorSensitivity(int sensitivity) {
        byte[] data = new byte[0x40];
        data[0] = UsbRequest.USB_SUB_CMD_SET_GSENSOR_SENSITIVITY;
        data[1] = (byte) (sensitivity & 0x000000FF);
        data[2] = (byte) ((sensitivity & 0x0000FF00) >> 8);
        data[3] = (byte) ((sensitivity & 0x00FF0000) >> 16);
        data[4] = (byte) ((sensitivity & 0xFF000000) >> 24);

        if (mUsbPort != null) {
            ((IUsbVideoPort)mUsbPort).setGsensorSensitivity(data);
        }
    }

    void startProtectedRecord(int seconds) {
        byte[] data = new byte[0x40];
        data[0] = UsbRequest.USB_SUB_CMD_SET_RECORD_PROTECTED_RECORD;
        data[1] = (byte) (seconds & 0x000000FF);
        data[2] = (byte) ((seconds & 0x0000FF00) >> 8);
        data[3] = (byte) ((seconds & 0x00FF0000) >> 16);
        data[4] = (byte) ((seconds & 0xFF000000) >> 24);

        if (mUsbPort != null) {
            ((IUsbVideoPort)mUsbPort).setProtectedVideoTime(data);
        }
    }

    void resetFactoryDefault() {
        byte[] data = new byte[0x40];
        data[0] = UsbRequest.USB_SUB_CMD_SET_RESET_FACTORY;

        if (mUsbPort != null) {
            ((IUsbVideoPort)mUsbPort).setFactoryDefault(data);
        }
    }

    void formatSdCard() {
        byte[] data = new byte[0x40];
        data[0] = UsbRequest.USB_SUB_CMD_SET_SDCARD_FORMAT;

        if (mUsbPort != null) {
            ((IUsbVideoPort)mUsbPort).setFormatSdCard(data);
        }
    }



    void setUsbConnection(UsbDeviceConnection connection) {
//        try {
//            mUsbPort.open(connection);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    void setAuthRandom() {
        byte[] data = new byte[0x40];
        data[0] = UsbRequest.USB_SUB_CMD_SET_AUTH_CHALLENGE;
        randomKey = getRandomString(AUTH_KEY_LENGTH);
        for(int i = 0; i < AUTH_KEY_LENGTH; i++) {
            Log.d("UsbControl", "randomKey[] = " + randomKey[i]);
            data[i+1] = randomKey[i];
        }

        if (mUsbPort != null) {
            ((IUsbVideoPort)mUsbPort).setAuthChallenge(data);
        }
    }

    boolean getAuthResponse() {
        byte[] data = new byte[0x40];
        data[0] = UsbRequest.USB_SUB_CMD_GET_AUTH_CHALLENGE;
        if (mUsbPort != null) {
            ((IUsbVideoPort)mUsbPort).getAuthChallenge(data);
            Log.d("UsbControl", "rsp data[0] = 0x" + Integer.toHexString(data[0]));
            Log.d("UsbControl", "rsp data[1] = 0x" + Integer.toHexString(data[1]));
            Log.d("UsbControl", "rsp data[2] = 0x" + Integer.toHexString(data[2]));
            Log.d("UsbControl", "rsp data[3] = 0x" + Integer.toHexString(data[3]));
            Log.d("UsbControl", "rsp data[4] = 0x" + Integer.toHexString(data[4]));
            Log.d("UsbControl", "rsp data[5] = 0x" + Integer.toHexString(data[5]));
            return decodeAndVerify(data, 1);
        } else {
            return false;
        }
    }

    public byte[] getRandomString(int length) { //length表示生成字符串的长度
        byte[] randomNum = new byte[length];
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            randomNum[i] = (byte) base.charAt(number);
        }

        return randomNum;
    }

    public boolean decodeAndVerify(byte[] response, int offset) {
        for (int i = 0; i < AUTH_KEY_LENGTH; i++) {
            byte tmp = (byte) (randomKey[i] << privateKey[i]);
            Log.d("UsbControl", "response[] = " + response[i + offset] + ",random[] << key[] = " + tmp);
            if (tmp != response[i + offset]) {
                return false;
            }
        }
        return true;
    }

}
