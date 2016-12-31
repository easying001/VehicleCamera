/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package Contracts;

import android.app.Activity;

import com.camera.easying.filecamera.UsbCameraDeviceListAdapter;

/**
 * Created by yangjie11 on 2016/9/13.
 */

public interface UsbSettingContract {
    interface View extends BaseView {
        Activity getParentActivity();
        void setViewListener(ViewListener listener);
        void setSpinnerAdapter(UsbCameraDeviceListAdapter adapter);
    }

    interface ViewListener {
        void onClick(int key);
    }

    interface Presenter extends BasePresenter {

    }
}
