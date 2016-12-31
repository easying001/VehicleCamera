package Presenters;

import android.util.Log;

import Driver.UsbCameraRecordState;
import Driver.UsbSdCardSpace;
import Driver.UsbCameraDeviceParam;
import Models.UsbAdapter;
import Views.MvpUsbSettingFragment;

/**
 * Created by think on 2016/9/27.
 */

public class MvpUsbSettingPresenter extends MvpBasePresenter<MvpUsbSettingFragment> {
    private UsbAdapter mUsbAdapter;
    public UsbCameraRecordState mRecordState;
    public UsbCameraDeviceParam mDeviceParam;
    public MvpUsbSettingPresenter() {

    }

    public void init() {
        if (isViewAttached()) {
            mUsbAdapter = UsbAdapter.getInstance();

            if (mUsbAdapter != null) {
                mRecordState = mUsbAdapter.getRecordState();

            }
        }

    }

    public void updateSettings() {
        if (isViewAttached()) {
            if (mUsbAdapter != null) {
                mDeviceParam = getDeviceParam();
                mRecordState = getRecordState();
            }
        }
    }

    public int getMicLevel() {
        if (mRecordState != null) {
            return mRecordState.volumn_level;
        } else {
            return 0;
        }
    }

    public int getSensorLevel() {
        if (mDeviceParam != null) {
            return mDeviceParam.gSensorSensitivity;
        } else {
            return 0;
        }
    }

    public String getDeviceVersion() {

        if (mUsbAdapter != null) {
            Log.d("UsbPresenter", "mUsbAdapter = " + mUsbAdapter);
            return mUsbAdapter.getDevVersion();
        } else {
            return null;
        }

    }

    public long getDeviceTime() {
        if (mUsbAdapter != null) {
            return mUsbAdapter.getDevTime();
        } else {
            return 0;
        }
    }

    public void setDeviceTime(long time, byte zone) {
        if (mUsbAdapter != null) {
            mUsbAdapter.setDevTime(time, zone);
        }
    }

    public UsbCameraDeviceParam getDeviceParam() {
        if (mUsbAdapter != null) {
            return mUsbAdapter.getDevParam();
        } else {
            return null;
        }
    }

    public UsbCameraRecordState getRecordState() {
        if (mUsbAdapter != null) {
            return mUsbAdapter.getRecordState();
        } else {
            return null;
        }
    }

    public void setRecordState(int state) {
        if (mUsbAdapter != null) {
            mUsbAdapter.setRecordState(state);
        }
    }

    public void setTakePicture(int pic_num) {
        if (mUsbAdapter != null) {
            mUsbAdapter.setTakePic(pic_num);
        }
    }

    public UsbSdCardSpace getSDCardRemaining() {
        if (mUsbAdapter != null) {
            return mUsbAdapter.getSDCardSpace();
        } else {
            return null;
        }
    }

    public void setGSensorSensitivity(int sensitivity) {
        if (mUsbAdapter != null) {
            mUsbAdapter.setGsensorSensitivity(sensitivity);
        }
    }

    public void startProtectedRecord(int seconds) {
        if (mUsbAdapter != null) {
            mUsbAdapter.startProtectedRecord(seconds);
        }
    }

    public void resetFactoryDefault() {
        if (mUsbAdapter != null) {
            mUsbAdapter.resetFactoryDefault();
        }
    }

    public void formatSDCard() {
        if (mUsbAdapter != null) {
            mUsbAdapter.formatSDCard();
        }
    }

    public void setMic(int level) {
        if (mUsbAdapter != null) {
            mUsbAdapter.setMic(level);
        }
    }
}
