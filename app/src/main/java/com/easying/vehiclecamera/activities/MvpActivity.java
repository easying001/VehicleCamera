package com.easying.vehiclecamera.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.apkfuns.logutils.LogUtils;
import com.easying.vehiclecamera.presenters.MvpPresenter;
import com.easying.vehiclecamera.views.MvpBaseView;

/**
 * Created by think on 2016/9/26.
 */

public abstract class MvpActivity<P extends MvpPresenter> extends FragmentActivity implements MvpBaseView {
    protected P presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.tag("Activity-Mvp").d("onCreate");
        presenter = createPresenter();
        if (presenter == null) {
            throw new NullPointerException("Presenter is Null");
        }
        presenter.attachView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.tag("Activity-Mvp").d("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.tag("Activity-Mvp").d("onStop");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.tag("Activity-Mvp").d("onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.tag("Activity-Mvp").d("onDestroy");
        presenter.detachView(false);
    }

    @Override
    public Activity getParentActivity() {
        return this;
    }

    public abstract P createPresenter();
}
