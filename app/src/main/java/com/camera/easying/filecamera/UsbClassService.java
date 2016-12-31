package com.camera.easying.filecamera;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by think on 2016/10/21.
 */

public abstract class UsbClassService extends Service {

    private boolean mCleaningUp = false;

    public interface IUsbClassServiceBinder extends IBinder {
        boolean cleanup();
    }
    protected IUsbClassServiceBinder mBinder;


    protected abstract IUsbClassServiceBinder initBinder();
    protected abstract boolean start();
    protected abstract boolean stop();

    protected boolean isAvailable() {
        return !mCleaningUp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = initBinder();

    }

    @Override
    public void onDestroy() {
        if(mBinder != null) {
            mBinder.cleanup();
            mBinder = null;
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
