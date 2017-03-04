/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.easying.vehiclecamera.models;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.apkfuns.logutils.LogUtils;
import com.serenegiant.usb.CameraDialog;
import com.serenegiant.usb.DeviceFilter;
import com.serenegiant.usb.USBMonitor;
import com.serenegiant.usb.UVCCamera;

import com.easying.vehiclecamera.contracts.UsbCameraManagerContract;

import com.easying.vehiclecamera.widgets.SimpleCameraTextureView;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by yangjie11 on 2016/9/10.
 */

public class UsbCameraManager implements UsbCameraManagerContract.UsbCameraManagement, CameraDialog.CameraDialogParent{
    private static final int CORE_POOL_SIZE = 1;		// initial/minimum threads
    private static final int MAX_POOL_SIZE = 4;			// maximum threads
    private static final int KEEP_ALIVE_TIME = 10;		// time periods while keep the idle thread

    private UsbManager mUsbManager = null;
    private static UsbCameraManager mInstance = null;
    private SurfaceView mPreviewSurfaceView;
    private SimpleCameraTextureView mPreviewTextView;
    public Surface mPreviewSurface;
    public USBMonitor mUSBMonitor;
    public UVCCamera mUVCCamera;
    private Context mContext;
    private boolean isActive, isPreview;
    private final Object mSync = new Object();
    private OnCameraStateChange mStateListener;
    private UsbAdapter mUsbAdapter;

    public interface OnCameraStateChange {
        void onPreviewStateChange(boolean isPreview);
        void onActiveStateChange(boolean isActive);
    }

    protected static final ThreadPoolExecutor EXECUTER
            = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME,
            TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    public UsbCameraManager() {

    }

    public void init(Context context, OnCameraStateChange stateListener) {
        mContext = context;
        mStateListener = stateListener;
        mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        LogUtils.tag("Model-CameraManager").d("new UsbMonitor");
        mUSBMonitor = new USBMonitor(mContext, mDeviceConnectListener);
        mUSBMonitor.register();
        mUsbAdapter = UsbAdapter.getInstance();
    }

    public void uninit() {
        LogUtils.tag("Model-CameraManager").d("uninit()");
        synchronized (mSync) {
            if (mUVCCamera != null) {
                mUVCCamera.destroy();
                // mUsbAdapter.stopDevice();
                mUVCCamera = null;
            }
            isActive = isPreview = false;
            if (mStateListener != null) {
                mStateListener.onActiveStateChange(false);
                mStateListener.onPreviewStateChange(false);
            }
        }

        mUSBMonitor.unregister();
        if (mUSBMonitor != null) {
            mUSBMonitor.destroy();
            mUSBMonitor = null;
        }
        mStateListener = null;
        if (mPreviewSurface != null) {
            mPreviewSurface.release();
            mPreviewSurface = null;
        }

        mPreviewSurfaceView = null;
        // Release resource for Usb Driver
//        try {
//            mControlPort.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public static UsbCameraManager getInstance() {
        if (null == mInstance) {
            synchronized(UsbCameraManager.class) {
                if (null == mInstance) {
                    mInstance = new UsbCameraManager();
                }
            }
        }
        return mInstance;
    }

    public UVCCamera getUVCCamera() {
        return mUVCCamera;
    }


    public boolean getActiveState() {
        synchronized (mSync) {
            return isActive;
        }
    }

    public boolean getPreviewState() {
        synchronized (mSync) {
            return isPreview;
        }
    }

//    public List<IUsbDriver> findUsbDrivers() {
//        List<IUsbDriver> drivers = UsbVideoProber.getDefaultProber().findAllDrivers(mUsbManager);
//        mUsbVideoPorts.clear();
//        for (final IUsbDriver driver : drivers) {
//            final List<IUsbBasePort> ports = driver.getPorts();
//            Log.d(TAG, String.format("+ %s: %s port%s",
//                    driver, Integer.valueOf(ports.size()), ports.size() == 1 ? "" : "s"));
//            mUsbVideoPorts.addAll(ports);
//        }
//        return drivers;
//    }

//    public void updateDeviceList() {
//        final List<IUsbDriver> drivers =
//                UsbVideoProber.getDefaultProber().findAllDrivers(mUsbManager);
//
//        mUsbVideoPorts.clear();
//        for (final IUsbDriver driver : drivers) {
//            final List<IUsbBasePort> ports = driver.getPorts();
//            Log.d(TAG, String.format("+ %s: %s port%s",
//                    driver, Integer.valueOf(ports.size()), ports.size() == 1 ? "" : "s"));
//            mUsbVideoPorts.addAll(ports);
//        }
//        //return result;
//    }


    public void destoryUVCCamera() {
        synchronized (mSync) {
            mUVCCamera.destroy();
            mUVCCamera = null;
            isActive = isPreview = false;
        }
    }

    private final USBMonitor.OnDeviceConnectListener mDeviceConnectListener = new USBMonitor.OnDeviceConnectListener() {
        @Override
        public void onAttach(UsbDevice device) {

        }

        @Override
        public void onDettach(UsbDevice device) {
        }

        @Override
        public void onConnect(UsbDevice device, final USBMonitor.UsbControlBlock ctrlBlock, boolean createNew) {
            LogUtils.tag("Model-CameraManager").d("onConnect");
            synchronized(mSync) {
                if (mUVCCamera != null) {
                    mUVCCamera.destroy();

                }
            }
            EXECUTER.execute(new Runnable() {
                @Override
                public void run() {
                    synchronized(mSync) {
                        mUVCCamera = new UVCCamera();
                        mUVCCamera.open(ctrlBlock);
                        // UsbAdapter.getInstance().setUsbConnection(ctrlBlock.getUsbDeviceConnection());
                        //Log.d("UsbCamera", "SupportedSize: " + mUsbAdapter.getVideoParam());
                        if (mPreviewSurface != null) {
                            mPreviewSurface.release();
                            mPreviewSurface = null;
                        }

                        try {
                            mUVCCamera.setPreviewSize(UVCCamera.DEFAULT_PREVIEW_WIDTH, UVCCamera
                                    .DEFAULT_PREVIEW_HEIGHT, UVCCamera.FRAME_FORMAT_MJPEG);
                        } catch (final IllegalArgumentException e) {
                            try {
                                mUVCCamera.setPreviewSize(UVCCamera.DEFAULT_PREVIEW_WIDTH, UVCCamera
                                        .DEFAULT_PREVIEW_HEIGHT,UVCCamera.DEFAULT_PREVIEW_MODE);
                            } catch (final IllegalArgumentException e1) {
                                mUVCCamera.destroy();
                                mUVCCamera = null;
                            }
                        }
                        final SurfaceTexture st = mPreviewTextView.getSurfaceTexture();

                        if ((mUVCCamera != null) && (st != null)) {
                            LogUtils.tag("Model-CameraManager").d("surface Textview = " + st);
                            mPreviewSurface = new Surface(st);
                            mUVCCamera.setPreviewDisplay(mPreviewSurface);
                            mUsbAdapter.setAuthChallenge();
                            if (mUsbAdapter.getAuthResponse()) {
                                mUVCCamera.startPreview();
                                isPreview = true;
                                if (mStateListener != null) {
                                    mStateListener.onPreviewStateChange(true);
                                }
                            }
                        }
                        isActive = true;
                    }
                }
            });
        }

        @Override
        public void onDisconnect(UsbDevice device, USBMonitor.UsbControlBlock ctrlBlock) {
            uninit();
        }


        @Override
        public void onCancel() {
        }
    };

    @Override
    public void setPreviewSurfaceView(SurfaceView view) {
        mPreviewSurfaceView = view;
        mPreviewSurfaceView.getHolder().addCallback(mSurfaceCallback);
    }

    @Override
    public void setPreviewTextureView(SimpleCameraTextureView textureView) {
        mPreviewTextView = textureView;

    }

    private final SurfaceHolder.Callback mSurfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            LogUtils.tag("Model-CameraManager").d("surfaceCreated");

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            LogUtils.tag("Model-CameraManager").d("surfaceChanged" + ",format = " + format + ",width = " + width + ", height = " + height);

            if ((width != 1280) && (height != 720)) {
                holder.setFixedSize(1280, 720);
                return;
            }

            if (format != PixelFormat.RGBA_8888) {
                holder.setFormat(PixelFormat.RGBA_8888);
                return;
            }

            // Log.d("UsbCamera", "surfaceChanged" + ",format = " + format + ",width = " + width + ", height = " +
            //        height);
            mPreviewSurface = holder.getSurface();
            synchronized(mSync) {
                if (isActive && !isPreview) {
                    // mUsbAdapter.setAuthChallenge();

                    mUVCCamera.setPreviewDisplay(mPreviewSurface);
                    mUVCCamera.startPreview();
                    isPreview = true;
                    if (mStateListener != null) {
                        mStateListener.onPreviewStateChange(true);
                    }
                }
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            LogUtils.tag("Model-CameraManager").d("surfaceDestroyed");
            synchronized(mSync) {
                if (mUVCCamera != null) {
                    mUVCCamera.stopPreview();
                    // mUsbAdapter.stopDevice();
                }
                isPreview = false;
                if (mStateListener != null) {
                    mStateListener.onPreviewStateChange(false);
                }
            }
            mPreviewSurface = null;
        }
    };

    public List<UsbDevice> getUsbCameraDeviceList() {
        final List<DeviceFilter> filter = DeviceFilter.getDeviceFilters(mContext, com.serenegiant.uvccamera.R.xml.device_filter);
        mUSBMonitor.setDeviceFilter(filter);
        return mUSBMonitor.getDeviceList();
    }

    @Override
    public USBMonitor getUSBMonitor() {
        return mUSBMonitor;
    }
}
