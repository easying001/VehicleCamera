/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.camera.easying.filecamera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import Utils.FileHelper;
import Views.MvpUsbFileFragment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import Presenters.MvpMainActivityPresenter;
import Views.MvpActivity;

/**
 * Created by yangjie11 on 2016/9/10.
 */

public class MainActivity extends MvpActivity<MvpMainActivityPresenter> implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ImageButton mStartPreviewButton;
    private ImageButton mOpenFileSystemButton;
    private ImageButton mSettingButton;

    private String mConfigFilePath = "/data/local/tmp";
    private static ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("UsbActivity", "MainActivity.onCreate");
        setContentView(R.layout.home_screen);
        mStartPreviewButton = (ImageButton) findViewById(R.id.ib_open_camera);
        mOpenFileSystemButton = (ImageButton) findViewById(R.id.ib_open_file);
        mSettingButton = (ImageButton) findViewById(R.id.ib_setting);

        mStartPreviewButton.setOnClickListener(this);
        mOpenFileSystemButton.setOnClickListener(this);
        mSettingButton.setOnClickListener(this);
        presenter.init();

        registerReceiver();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    public void selectButton(int id) {
        if (id == R.id.ib_open_camera) {
            mOpenFileSystemButton.setSelected(false);
            mSettingButton.setSelected(false);
            mStartPreviewButton.setSelected(true);
        } else if (id == R.id.ib_open_file) {
            mOpenFileSystemButton.setSelected(true);
            mSettingButton.setSelected(false);
            mStartPreviewButton.setSelected(false);
        } else if (id ==R.id.ib_setting) {
            mOpenFileSystemButton.setSelected(false);
            mSettingButton.setSelected(true);
            mStartPreviewButton.setSelected(false);
        }
    }
    @Override
    public void onClick(View v) {
        selectButton(v.getId());
        switch(v.getId()) {
            case R.id.ib_open_file:
                presenter.showFileFragment();
                break;
            case R.id.ib_open_camera:
                presenter.showUsbCameraFragment();
                break;
            case R.id.ib_setting:
                presenter.showSettingFragment();
                break;
            default:
                break;
        }
    }

    @Override
    public Activity getParentActivity() {
        return this;
    }

    @Override
    public MvpMainActivityPresenter createPresenter() {
        return new MvpMainActivityPresenter();
    }
    @Override
    public void showToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(this, "请稍等","正在努力为您加载设备...");
            mProgressDialog.setCancelable(false);
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }

    }
    public void dismissProgressDialog() {
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        }
    }

    public void setProgressDialog(String content) {
        if (mProgressDialog != null) {
            mProgressDialog.setMessage(content);
        }
    }

    @Override
    public void onBackPressed() {

    }

    private void registerReceiver() {
        MediaMountedBroadcastReceiver mMediaMounterReceiver = new MediaMountedBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addDataScheme("file");
        registerReceiver(mMediaMounterReceiver, filter);
    }

    private class MediaMountedBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
                String mUsbRootPath = intent.getData().getPath() + "/sonix";
                FileHelper.write("/default.txt", mUsbRootPath);
                // mHandler.obtainMessage(MSG_UPDATE_VIEWPAGER).sendToTarget();
                Log.d("UsbActivity", "write usb storage path to file = " + mUsbRootPath);
            }
        }
    }
}
