/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.easying.vehiclecamera.contracts;



import android.app.Activity;
import android.content.Context;

/**
 * Created by yangjie11 on 2016/9/10.
 */

public interface MainActivityContract {
    interface View extends BaseView {
        Activity getParentActivity();
        Context getParentContext();
        void showToast(String content);
        void setViewListener(MainActivityContract.ViewListener listener);
        void showFragment(int type);
    }

    interface ViewListener {
        void onButtonClick(int key);
        void onBackButtonPressed();
        void onResume();
    }

    interface Presenter extends BasePresenter {
        void setAdapter();
    }
}
