package com.camera.easying.filecamera;

import android.os.Binder;

/**
 * Created by think on 2016/10/21.
 */

public class CameraService extends UsbClassService {
    @Override
    protected IUsbClassServiceBinder initBinder() {
        return new UsbCameraBinder(this);
    }

    @Override
    protected boolean start() {
        return false;
    }

    @Override
    protected boolean stop() {
        return false;
    }

    private static class UsbCameraBinder extends Binder implements IUsbClassServiceBinder{

        private CameraService mService;

        UsbCameraBinder(CameraService svc) {
            mService = svc;
        }

        private CameraService getService() {
            if ((mService != null) && (mService.isAvailable())) {
                return mService;
            }
            return null;
        }

        @Override
        public boolean cleanup() {
            mService = null;
            return true;
        }



    }
}
