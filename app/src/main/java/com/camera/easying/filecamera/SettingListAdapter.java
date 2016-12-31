package com.camera.easying.filecamera;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Driver.UsbCameraDeviceParam;
import Driver.UsbCameraRecordState;
import Presenters.MvpPresenter;
import Presenters.MvpUsbSettingPresenter;
import Views.MvpSettiingItem;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import Views.MvpStatusButton;

/**
 * Created by think on 2016/10/31.
 */

public class SettingListAdapter extends BaseAdapter {
    private ArrayList<MvpSettiingItem> mList;
    private Context mContext;
    private MvpStatusButton.OnStatusButtonClickListener mClickListener;


    public SettingListAdapter(Context context, ArrayList<MvpSettiingItem> list, MvpStatusButton
            .OnStatusButtonClickListener listener) {
        mContext = context;
        mClickListener = listener;
        mList = list;
    }

    private class ViewHolder {
        TextView settingName;
        MvpStatusButton switchBtn;
        ImageView arrowIcon;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public MvpSettiingItem getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Log.d("UsbFragment", "position = " + i + "view = " + view);
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.fragment_setting_list_item, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.settingName = (TextView) view.findViewById(R.id.tv_setting_item_name);
            viewHolder.switchBtn = (MvpStatusButton) view.findViewById(R.id.sb_setting_button);
            viewHolder.arrowIcon = (ImageView) view.findViewById(R.id.iv_direction);
            view.setTag(viewHolder);
        }

        ViewHolder cache = (ViewHolder) view.getTag();
        MvpSettiingItem item = mList.get(i);

        if (item.mName.equals("Mic")) {
            cache.arrowIcon.setVisibility(View.INVISIBLE);
            cache.switchBtn.setVisibility(View.VISIBLE);
            cache.switchBtn.setAllButtonText("打开", "关闭");
            cache.switchBtn.setAllBtnClickListener(mOnBtnMicClickListener);
            cache.switchBtn.setName("Mic");
            if (item.mValue <= 1) {
                cache.switchBtn.setBtnLowChecked();
            } else {
                cache.switchBtn.setBtnHighChecked();
            }
        } else if (item.mName.equals("Sensor")){
            cache.arrowIcon.setVisibility(View.INVISIBLE);
            cache.switchBtn.setVisibility(View.VISIBLE);
            cache.switchBtn.setAllButtonText("灵敏度高","灵敏度中","灵敏度低");
            cache.switchBtn.setAllBtnClickListener(mOnBtnMicClickListener);
            cache.switchBtn.setName("Sensor");
            if (item.mValue <= 500) {
                cache.switchBtn.setBtnLowChecked();
            } else if (item.mValue >500 && item.mValue <= 1000){
                cache.switchBtn.setBtnMiddleChecked();
            } else if (item.mValue > 1000) {
                cache.switchBtn.setBtnHighChecked();
            }

        } else {
            cache.arrowIcon.setVisibility(View.VISIBLE);
            cache.switchBtn.setVisibility(View.INVISIBLE);
        }
        cache.settingName.setText(item.mTitle);

//        switch(i) {
//            case 0:
//
//                cache.arrowIcon.setVisibility(View.INVISIBLE);
//                cache.switchBtn.setVisibility(View.VISIBLE);
//                cache.switchBtn.setAllButtonText("打开", "关闭");
//                cache.switchBtn.setAllBtnClickListener(mOnBtnMicClickListener);
//                cache.switchBtn.setName("Mic");
//                if (mRecordState != null) {
//                    Log.d("UsbFragment", "mic level = " + mRecordState.volumn_level);
//                    if (mRecordState.volumn_level <= 1) {
//                        cache.switchBtn.setBtnLowChecked();
//                    } else {
//                        cache.switchBtn.setBtnHighChecked();
//                    }
//                }
//                break;
//            case 1:
//                cache.arrowIcon.setVisibility(View.INVISIBLE);
//                cache.switchBtn.setVisibility(View.VISIBLE);
//                cache.switchBtn.setAllButtonText("灵敏度高","灵敏度中","灵敏度低");
//                cache.switchBtn.setAllBtnClickListener(mOnBtnMicClickListener);
//                cache.switchBtn.setName("Sensor");
//                if (mDeviceParam != null) {
//                    Log.d("UsbFragment", "gSensor = " + mDeviceParam.gSensorSensitivity);
//                    if (mDeviceParam.gSensorSensitivity <= 500) {
//                        cache.switchBtn.setBtnLowChecked();
//                    } else if (mDeviceParam.gSensorSensitivity >500 && mDeviceParam.gSensorSensitivity <= 1000){
//                        cache.switchBtn.setBtnMiddleChecked();
//                    } else if (mDeviceParam.gSensorSensitivity > 1000) {
//                        cache.switchBtn.setBtnHighChecked();
//                    }
//                }
//                break;
//            default:
//                cache.arrowIcon.setVisibility(View.VISIBLE);
//                cache.switchBtn.setVisibility(View.INVISIBLE);
//                break;
//        }
//        cache.settingName.setText(settingName[(int) getItemId(i)]);
//        //cache.settingName.setTextColor(mContext.getResources().getColor(R.color.cl_press_b_tab_press));

        return view;
    }

    private MvpStatusButton.OnStatusButtonClickListener mOnBtnMicClickListener = new MvpStatusButton.OnStatusButtonClickListener() {
        @Override
        public void onClick(MvpStatusButton sButton, MvpStatusButton.StatusButtonChild child) {
            if (mClickListener != null) {
                mClickListener.onClick(sButton, child);
            }
        }
    };
}
