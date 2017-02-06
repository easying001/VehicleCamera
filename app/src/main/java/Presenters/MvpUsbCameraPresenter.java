package Presenters;

import Driver.UsbCameraDeviceParam;
import android.hardware.usb.UsbDevice;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.camera.easying.filecamera.UsbFragmentManager;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Driver.UsbCameraRecordState;
import Driver.UsbDriver.IUsbDriver;
import Driver.UsbDriver.IUsbStoragePort;
import Driver.UsbId;
import Models.UsbAdapter;
import Models.UsbCameraManager;
import Views.MvpUsbCameraFragment;
import Views.UsbCameraDeviceDialog;
import com.camera.easying.filecamera.R;

/**
 * Created by think on 2016/9/25.
 */

public class MvpUsbCameraPresenter extends MvpBasePresenter<MvpUsbCameraFragment> {
    private static final String TAG = MvpUsbCameraPresenter.class.getSimpleName();
    private static final int MSG_START_PREVIEW = 1;
    private static final int MSG_START_DIALOG = 2;
    private static final int MSG_PREVIEW_STATE_ON = 3;
    private UsbCameraManager mUsbCameraManager;

    private UsbAdapter mUsbAdapter;
    private UsbCameraDeviceParam mDeviceParam;
    private UsbCameraRecordState mRecordState;
    private EventHandler mHandler = new EventHandler();


    private boolean enableMicFlag = false;
    public MvpUsbCameraPresenter() {

    }

    private UsbCameraManager.OnCameraStateChange mCameraStateCallback = new UsbCameraManager.OnCameraStateChange() {
        @Override
        public void onPreviewStateChange(boolean isPreview) {
            if (isPreview) {
                Log.d(TAG, "Camera preview is started");
                mHandler.obtainMessage(MSG_PREVIEW_STATE_ON).sendToTarget();

            }
        }

        @Override
        public void onActiveStateChange(boolean isActive) {

        }
    };

    public class EventHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_START_PREVIEW:
                    showCameraPreview();
                    break;
                case MSG_START_DIALOG:
                    break;
                case MSG_PREVIEW_STATE_ON:
                    updateCameraState();
                    break;
                default:
                    break;
            }

        }
    }

    public void init() {
        if (mUsbCameraManager == null) {
            mUsbCameraManager = UsbCameraManager.getInstance();
        }

        if (mUsbAdapter == null) {
            mUsbAdapter = UsbAdapter.getInstance();
        }

        if (isViewAttached()) {
            Log.d(TAG, "init UsbCameraManager");
            mUsbCameraManager.init(getView().getParentActivity(), mCameraStateCallback);

            // new UsbCameraScanDevice().execute("scan usb device");
            // getView().showProgressDialog();
//            Log.d(TAG, "setPreviewSurfaceView = " + getView().getmSurfaceView());
//            mUsbCameraManager.setPreviewSurfaceView(getView().getmSurfaceView());
            mUsbCameraManager.setPreviewTextureView(getView().getUVCTextureView());
            startPreview();
        }
    }

    public void updateCameraState() {
        if (isViewAttached()) {
            if (mUsbAdapter != null) {

                mRecordState = mUsbAdapter.getRecordState();
                Log.d(TAG, "sync up with usb camera for state, mic_level = " + mRecordState.volumn_level + ",state = "
                        + mRecordState.status);
                if (mRecordState.volumn_level <= 1) {
                    getView().setMicButton(true);
                } else {
                    getView().setMicButton(false);
                }

                if (mRecordState.status == 1) {
                    getView().showButtonIcon(true);
                } else {
                    getView().showButtonIcon(false);
                }
            }
        }
    }

    public void recoverPreview() {
        startPreview();
    }

    public void uninit() {
        if (isViewAttached()) {
            Log.d(TAG, "uninit UsbCameraManager");
            mUsbCameraManager.uninit();
        }
        mUsbCameraManager = null;

    }

    private void startPreview() {
        if (!mUsbCameraManager.getPreviewState()) {
            Log.d(TAG, "startPreview(): isPreview = false");
            IUsbDriver driver = mUsbAdapter.getLoadedDriver();
            if (driver != null) {
                Log.d(TAG, "startPreview: driver = " + driver);
                UsbCameraManager.getInstance().mUSBMonitor.requestPermission(driver.getDevice(),
                        driver.getPorts().get(0).getConnection());
            } else {
                Log.d(TAG, "startPreview: driver = null");
                mUsbAdapter.discoverAllDevice();
            }
        }
    }
    public void showCameraPreview() {
        if(isViewAttached()) {
            if (mUsbAdapter.isUsbCameraDeviceConnected()) {
                if (!mUsbCameraManager.getPreviewState()) {
                    //getView().showButtonIcon(true);
                    Log.d(TAG, "Camera Device Connected, start preview,connection=" + mUsbAdapter.getLoadedDriver().getPorts().get(0).getConnection());
                    UsbCameraManager.getInstance().mUSBMonitor.requestPermission(mUsbAdapter.getLoadedDriver().getDevice(),
                            mUsbAdapter.getLoadedDriver().getPorts().get(0).getConnection());

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "Camera Device Connected, start preview again");
                            UsbCameraManager.getInstance().mUSBMonitor.requestPermission(mUsbAdapter.getLoadedDriver().getDevice(),
                                    mUsbAdapter.getLoadedDriver().getPorts().get(0).getConnection());
                        }
                    }, 2000);
                }
            } else {
                Log.d(TAG, "Camera Device Not Connected");
                UsbCameraDeviceDialog mUsbCameraDialog = UsbCameraDeviceDialog.newInstance();
                UsbFragmentManager.getInstance().showAlertDialog(mUsbCameraDialog);
                //mUsbCameraDialog.setTargetFragment(getView()., 0);
            }
        }
    }

    // Toggle record start or stop
    public void startRecord() {
        if (mUsbAdapter != null && mUsbAdapter.isUsbCameraOpened()) {
            // UsbCameraRecordState recordState = mUsbAdapter.getRecordState();
            Log.d(TAG, "recordState = " + mRecordState.status);
            if (mRecordState.status == 1) {
                mUsbAdapter.setRecordState(0);
                getView().showButtonIcon(false);
                mRecordState.status = 0;
            } else {
                mUsbAdapter.setRecordState(1);
                getView().showButtonIcon(true);
                mRecordState.status = 1;
            }
        }
    }

    public void startProtectedRecord() {
        if (mUsbAdapter != null && mUsbAdapter.isUsbCameraOpened()) {
            getView().showToast("开始录制保护视频60秒");
            //mUsbAdapter.startProtectedRecord(60);

            getView().ib_start_protected_record.setImageResource(R.drawable.fragment_camera_lock_video);
            getView().ib_start_protected_record.setEnabled(false);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getView().ib_start_protected_record.setImageResource(R.drawable.fragment_camera_unlock_video);
                    getView().ib_start_protected_record.setEnabled(true);
                }
            }, 60000);
            mUsbAdapter.startProtectedRecord(60);
        }
    }

    // toggle enable or disable mic
    public void enableOrDisableMic() {
        if (mUsbAdapter != null && mUsbAdapter.isUsbCameraOpened()) {
            // UsbCameraRecordState recordState = mUsbAdapter.getRecordState();
            Log.d(TAG, "recordState mic level = " + mRecordState.volumn_level);
            if (mRecordState.volumn_level <= 1) {
                mUsbAdapter.setMic(99);
                getView().setMicButton(false);
                mRecordState.volumn_level = 99;
            } else {
                mUsbAdapter.setMic(0);
                getView().setMicButton(true);
                mRecordState.volumn_level = 0;
            }
        }
    }

    public void takeOnePic() {
        if (mUsbAdapter != null && mUsbAdapter.isUsbCameraOpened()) {
            getView().showToast("拍摄一张照片");
            mUsbAdapter.setTakePic(1);

        }
    }

//    private class UsbCameraScanDevice extends AsyncTask<String, String, String> {
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
//            Log.d(TAG, "UsbCameraScanDevice - doInBackground()");
//            int downCounter = 10;
//            while(downCounter-- > 0) {
//
//                if (mUsbAdapter.isUsbCameraDeviceConnected() == true) {
//                    mHandler.obtainMessage(MSG_START_PREVIEW).sendToTarget();
//                    break;
//                }
//                try {
//                    Thread.sleep(500);
//                    publishProgress("Starting UVC Previewing");
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }
//            mHandler.obtainMessage(MSG_START_DIALOG).sendToTarget();
//            return null;
//        }
//
//        @Override
//        protected void onProgressUpdate(String... values) {
//            getView().setProgressDialog(values[0]);
//        }
//    }
}
