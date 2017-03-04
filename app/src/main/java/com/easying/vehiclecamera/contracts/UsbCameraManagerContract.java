/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.easying.vehiclecamera.contracts;

import com.easying.vehiclecamera.widgets.SimpleCameraTextureView;

import android.view.SurfaceView;

/**
 * Created by yangjie11 on 2016/9/12.
 */

public interface UsbCameraManagerContract {
    interface UsbCameraManagement {
        void setPreviewSurfaceView(SurfaceView surface);
        void setPreviewTextureView(SimpleCameraTextureView textureView);
    }

}
