package com.easying.vehiclecamera.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.easying.vehiclecamera.R;
import com.easying.vehiclecamera.adapters.SettingListAdapter;
import com.easying.vehiclecamera.models.UsbFragmentManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.easying.vehiclecamera.drivers.UsbCameraDeviceParam;
import com.easying.vehiclecamera.drivers.UsbCameraRecordState;
import com.easying.vehiclecamera.drivers.UsbSdCardSpace;
import com.easying.vehiclecamera.models.UsbCameraManager;
import com.easying.vehiclecamera.presenters.MvpUsbSettingPresenter;
import com.easying.vehiclecamera.views.MvpSettiingItem;
import com.easying.vehiclecamera.widgets.MvpStatusButton;

/**
 * Created by think on 2016/9/27.
 */

public class MvpUsbSettingFragment extends MvpFragment<MvpUsbSettingPresenter>
        implements View.OnClickListener, AdapterView.OnItemClickListener {
    private View mContentView;
    private Button mGetDevVer;
    private Button mGetDevParam;
//    private Button mSetVideoParam;
//    private Button mGetVideoParam;
    private Button mGetDevTime;
    private Button mTakePic;
//    private Button mSetRecordState;
    private Button mGetRecordState;
    private Button mGetSDCardRemaining;
//    private Button mSwitchModeMsc;
    private Button mSetDevTime;
    private Button mStartRecord;
    private Button mStopRecord;
    private Button mEnableMic;
    private Button mDisableMic;
    private Button mStartProtectedRecord;
    private Button mResetFactory;
    private Button mFormatSdCard;
    private Button mGetSensorSensitivity;
    private Button mSetSensorSensitivityHigh;
    private Button mSetSensorSensitivityLow;
    private Button mSetSensorSensitivityMedium;
    private Button mNativeStartPreview;
    private Button mNativeStopPreview;

    private MvpStatusButton mBtnGSensor;
    private MvpStatusButton mBtnMic;
    private ListView mListView;
    private SettingListAdapter mAdapter;
    ArrayList<MvpSettiingItem> mSettingList = new ArrayList<MvpSettiingItem>();



    public static MvpUsbSettingFragment newInstance() {
        Bundle arguments = new Bundle();
        MvpUsbSettingFragment fragment = new MvpUsbSettingFragment();
        fragment.setArguments(arguments);
        fragment.setType(UsbFragmentManager.TYPE_USB_SETTING);
        return fragment;
    }

    @Override
    protected MvpUsbSettingPresenter createPresenter() {
        return new MvpUsbSettingPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //mContentView = inflater.inflate(R.layout.fragment_usb_setting, null);
        mContentView = inflater.inflate(R.layout.fragment_usb_camera_setting, null);
        mListView = (ListView) mContentView.findViewById(R.id.lv_usb_camera_setting);

        int resId = R.array.camera_setting_name;
        String[] title = getResources().getStringArray(resId);
        if (mSettingList != null) {
            mSettingList.clear();
        }
        mSettingList.add(new MvpSettiingItem(title[0], "Mic", 0));
        mSettingList.add(new MvpSettiingItem(title[1], "Sensor", 0));
        mSettingList.add(new MvpSettiingItem(title[2], "FactorySetting", 0));
        mSettingList.add(new MvpSettiingItem(title[3], "FormatSdcard", 0));
        mSettingList.add(new MvpSettiingItem(title[4], "About", 0));

/*
        mGetDevVer = (Button) mContentView.findViewById(R.id.bt_get_device_verison);
        mGetDevTime = (Button) mContentView.findViewById(R.id.bt_get_device_time);
        mSetDevTime = (Button) mContentView.findViewById(R.id.bt_set_device_time);
        mGetDevParam = (Button) mContentView.findViewById(R.id.bt_get_device_param);
//        mGetVideoParam = (Button) mContentView.findViewById(R.id.bt_get_video_param);
//        mSetVideoParam = (Button) mContentView.findViewById(R.id.bt_set_video_param);
//        mSetRecordState = (Button) mContentView.findViewById(R.id.bt_record_set_state);
        mGetRecordState = (Button) mContentView.findViewById(R.id.bt_record_get_state);
        mTakePic = (Button) mContentView.findViewById(R.id.bt_record_take_pic);
        mGetSDCardRemaining = (Button) mContentView.findViewById(R.id.bt_get_sdcard_remaining);
//        mSwitchModeMsc = (Button) mContentView.findViewById(R.id.bt_set_usb_mode);
        mEnableMic = (Button) mContentView.findViewById(R.id.bt_record_enable_mic);
        mDisableMic = (Button) mContentView.findViewById(R.id.bt_record_disable_mic);
        mStartProtectedRecord = (Button) mContentView.findViewById(R.id.bt_record_protected);
        mStartRecord = (Button) mContentView.findViewById(R.id.bt_record_start);
        mStopRecord = (Button) mContentView.findViewById(R.id.bt_record_stop);
        mFormatSdCard = (Button) mContentView.findViewById(R.id.bt_format_sd_card);
        mResetFactory = (Button) mContentView.findViewById(R.id.bt_reset_factory_default);
        mSetSensorSensitivityHigh = (Button) mContentView.findViewById(R.id.bt_set_gsensor_sensitivity_high);
        mSetSensorSensitivityLow = (Button) mContentView.findViewById(R.id.bt_set_gsensor_sensitivity_low);
        mSetSensorSensitivityMedium = (Button) mContentView.findViewById(R.id.bt_set_gsensor_sensitivity_medium);
        mNativeStartPreview = (Button) mContentView.findViewById(R.id.bt_native_start_preview);
        mNativeStopPreview = (Button) mContentView.findViewById(R.id.bt_native_stop_preview);

        //mSetUsbMode = (Button) mContentView.findViewById(R.id.)
        mGetDevVer.setOnClickListener(this);
        mGetDevParam.setOnClickListener(this);
//        mGetVideoParam.setOnClickListener(this);
//        mSetVideoParam.setOnClickListener(this);
        mGetDevTime.setOnClickListener(this);
        mGetRecordState.setOnClickListener(this);
        mTakePic.setOnClickListener(this);
//        mSetRecordState.setOnClickListener(this);
        mGetSDCardRemaining.setOnClickListener(this);
//        mSwitchModeMsc.setOnClickListener(this);
        mSetDevTime.setOnClickListener(this);
        mEnableMic.setOnClickListener(this);
        mDisableMic.setOnClickListener(this);
        mStartProtectedRecord.setOnClickListener(this);
        mStopRecord.setOnClickListener(this);
        mStartRecord.setOnClickListener(this);
        mFormatSdCard.setOnClickListener(this);
        mResetFactory.setOnClickListener(this);
        mSetSensorSensitivityHigh.setOnClickListener(this);
        mSetSensorSensitivityLow.setOnClickListener(this);
        mSetSensorSensitivityMedium.setOnClickListener(this);
        mNativeStopPreview.setOnClickListener(this);
        mNativeStartPreview.setOnClickListener(this);
*/
        presenter.init();
        presenter.updateSettings();
        mSettingList.get(0).mValue = presenter.getMicLevel();
        mSettingList.get(1).mValue = presenter.getSensorLevel();

        mAdapter = new SettingListAdapter(this.getActivity().getApplicationContext(), mSettingList,
                mOnStatusButtonClicked);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);


        return mContentView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {

        } else {
            LogUtils.tag("Fragment-Setting").d("Show fragment and send command to sync up usb settings");
            presenter.updateSettings();
            mSettingList.get(0).mValue = presenter.getMicLevel();
            mSettingList.get(1).mValue = presenter.getSensorLevel();
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void showToast(String content) {
        Toast.makeText(getParentActivity(), content, Toast.LENGTH_SHORT).show();
    }

    private MvpStatusButton.OnStatusButtonClickListener mOnStatusButtonClicked = new MvpStatusButton.OnStatusButtonClickListener() {
        @Override
        public void onClick(MvpStatusButton sButton, MvpStatusButton.StatusButtonChild child) {
            if (sButton.getName() == "Mic") {
                if (child == MvpStatusButton.StatusButtonChild.HIGH) {
                    sButton.setBtnHighChecked();
                    Toast.makeText(getView().getContext(), "录像时打开麦克风", Toast.LENGTH_SHORT).show();
                    presenter.setMic(100);
                } else if (child == MvpStatusButton.StatusButtonChild.MIDDLE) {

                } else if (child == MvpStatusButton.StatusButtonChild.LOW) {
                    sButton.setBtnLowChecked();
                    presenter.setMic(0);
                    Toast.makeText(getView().getContext(), "录像时关闭麦克风", Toast.LENGTH_SHORT).show();
                }
            } else if (sButton.getName() == "Sensor") {
                if (child == MvpStatusButton.StatusButtonChild.HIGH) {
                    sButton.setBtnHighChecked();
                    Toast.makeText(getView().getContext(), "设置灵敏度高", Toast.LENGTH_SHORT).show();
                    presenter.setGSensorSensitivity(1600);
                } else if (child == MvpStatusButton.StatusButtonChild.MIDDLE) {
                    sButton.setBtnMiddleChecked();
                    Toast.makeText(getView().getContext(), "设置灵敏度中", Toast.LENGTH_SHORT).show();
                    presenter.setGSensorSensitivity(1000);
                } else if (child == MvpStatusButton.StatusButtonChild.LOW) {
                    sButton.setBtnLowChecked();
                    Toast.makeText(getView().getContext(), "设置灵敏度低", Toast.LENGTH_SHORT).show();
                    presenter.setGSensorSensitivity(500);
                }
            }
        }
    };

    @Override
    public void onClick(View view) {
       switch(view.getId()) {
           case R.id.bt_native_start_preview:
               UsbCameraManager.getInstance().mUVCCamera.setPreviewDisplay(UsbCameraManager.getInstance().mPreviewSurface);
               UsbCameraManager.getInstance().mUVCCamera.startPreview();
               break;
           case R.id.bt_native_stop_preview:
               UsbCameraManager.getInstance().mUVCCamera.stopPreview();
               break;
           case R.id.bt_get_device_verison:
                showToast(presenter.getDeviceVersion());
                break;
           case R.id.bt_get_device_param:
               UsbCameraDeviceParam deviceParam = new UsbCameraDeviceParam();
               deviceParam = presenter.getDeviceParam();
               showToast("Year = "+ deviceParam.year + ",GsensorSensitivity = " + deviceParam.gSensorSensitivity);
               break;
//           case R.id.bt_get_video_param:
//               break;
//           case R.id.bt_set_video_param:
//
//               break;
           case R.id.bt_get_device_time: {
               SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
               Calendar cal = Calendar.getInstance();
               TimeZone tz = cal.getTimeZone();
               dateFormat.setTimeZone(tz);

               String devTime = dateFormat.format(new Date(presenter.getDeviceTime() * 1000));
               showToast(devTime);
           }
               break;
           case R.id.bt_set_device_time:
                long time = System.currentTimeMillis()/1000;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar cal = Calendar.getInstance();
                TimeZone tz = cal.getTimeZone();
                dateFormat.setTimeZone(tz);

                String devTime = dateFormat.format(time * 1000);
                showToast("set time = " + devTime + ",longtime = " + time);
                presenter.setDeviceTime(time, (byte)0x05);
               break;
           case R.id.bt_record_get_state:
               UsbCameraRecordState state = new UsbCameraRecordState();
               state = presenter.getRecordState();
               showToast("state=" + state.status + ",FPS=" + state.record_video_fps + ",mic level=" + state.volumn_level);
               break;
//           case R.id.bt_record_set_state:
//               if (presenter.getRecordState().status == UsbRequest.USB_CAMERA_RECORD_STATE_STOPPED) {
//                   showToast("Set Record State to 1");
//                   presenter.setRecordState(1); //  start record
//               } else {
//                   showToast("Set Record State to 0");
//                   presenter.setRecordState(0); //  stop record
//               }
//               break;
           case R.id.bt_record_take_pic:
               showToast("Take 1 Picture");
               presenter.setTakePicture(1);
               break;
           case R.id.bt_get_sdcard_remaining:
               UsbSdCardSpace space = presenter.getSDCardRemaining();
               showToast("Remain space in percentage = " + space.remaining + ",Capacity = " + space.capacity);
               break;
           case R.id.bt_format_sd_card:
               showToast("Start Formatting SD Card");
               presenter.formatSDCard();
               break;
           case R.id.bt_reset_factory_default:
               showToast("Start Formatting SD Card");
               presenter.resetFactoryDefault();
               break;
           case R.id.bt_record_start:
               showToast("Start Recording");
               presenter.setRecordState(1);
               break;
           case R.id.bt_record_stop:
               showToast("Stop Recording");
               presenter.setRecordState(0);
               break;
           case R.id.bt_record_protected:
               showToast("Start Protected Recording for 60 Seconds");
               presenter.startProtectedRecord(60);
               break;
           case R.id.bt_record_enable_mic:
               showToast("Unmute Mic");
               presenter.setMic(100);
               break;
           case R.id.bt_record_disable_mic:
               showToast("Mute Mic");
               presenter.setMic(0);
               break;
           case R.id.bt_set_gsensor_sensitivity_high:
               showToast("Set GSensor Sensitivity to High");
               presenter.setGSensorSensitivity(1600);
               break;
           case R.id.bt_set_gsensor_sensitivity_medium:
               showToast("Set GSensor Sensitivity to Medium");
               presenter.setGSensorSensitivity(1000);
               break;
           case R.id.bt_set_gsensor_sensitivity_low:
               showToast("Set GSensor Sensitivity to Low");
               presenter.setGSensorSensitivity(500);
               break;
           default:
               break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
        int itemId = (int) adapterView.getItemIdAtPosition(pos);
        switch(itemId) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                new AlertDialog.Builder(getView().getContext()).setTitle("系统提示").setMessage("是否恢复到出厂设置？系统将重启")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                presenter.resetFactoryDefault();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
                break;
            case 3:
                new AlertDialog.Builder(getView().getContext()).setTitle("系统提示").setMessage("是否格式化SDCARD ? 数据内容将无法恢复")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                presenter.formatSDCard();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
                break;
            case 4:
                new AlertDialog.Builder(getView().getContext()).setTitle("系统提示").
                        setMessage("摄像头固件版本号为：" + presenter.getDeviceVersion()).show();
                break;
            default:
                break;
        }
    }

}
