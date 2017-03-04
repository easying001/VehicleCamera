package com.easying.vehiclecamera.presenters;

import android.support.annotation.UiThread;

import com.easying.vehiclecamera.views.MvpView;

/**
 * Created by think on 2016/9/25.
 */

public interface MvpPresenter<V extends MvpView> {
    @UiThread
    void attachView(V view);

    @UiThread
    void detachView(boolean retainInstance);

}
