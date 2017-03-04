/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.easying.vehiclecamera.contracts;

import android.app.Activity;
import android.view.SurfaceView;

/**
 * Created by yangjie11 on 2016/9/7.
 */

public interface UsbCameraContract {
    interface View extends BaseView {
        Activity getParentActivity();
        SurfaceView getSurfaceView();
        void showCameraPreview();
        void showToast(String content);
        void setViewListener(UsbCameraContract.ViewListener listener);
    }

    interface ViewListener {
        void onResume();
        void onActivityCreated();
        void onViewCreated();
        void onClick(int key);
    }
    interface Presenter extends BasePresenter {
        void openCamera();
        void closeCamera();
    }
}
