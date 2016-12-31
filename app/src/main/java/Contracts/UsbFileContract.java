/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package Contracts;


import android.app.Activity;

/**
 * Created by yangjie11 on 2016/9/7.
 */

public interface UsbFileContract {

    interface View extends BaseView {
        Activity getParentActivity();
        void showToast(String content);
        void showFragment();
        void setViewListener(ViewListener listener);
    }

    interface ViewListener {
        void onListItemClick(int pos);
        void onBackPressed();
        void onResume();
        void onActivityCreated();

    }

    interface Presenter extends BasePresenter {
        void showFragment();

    }

}
