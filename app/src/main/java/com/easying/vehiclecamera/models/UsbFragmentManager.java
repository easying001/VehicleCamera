package com.easying.vehiclecamera.models;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.apkfuns.logutils.LogUtils;
import com.easying.vehiclecamera.R;
import com.easying.vehiclecamera.views.BaseDialog;
import com.easying.vehiclecamera.fragments.MvpBaseFragment;

/**
 * Created by think on 2016/9/26.
 */

public class UsbFragmentManager {
    public static final int TYPE_USB_NONE = 0;
    public static final int TYPE_USB_CAMERA = 1;
    public static final int TYPE_USB_FILE = 2;
    public static final int TYPE_USB_SETTING = 3;
    public static final int TYPE_USB_FILE_TYPE = 4;
    private FragmentManager mFragmentManager;
    private MvpBaseFragment mCurrentFragment;
    private static UsbFragmentManager mInstance = null;
    private Context mContext;

    public static UsbFragmentManager getInstance() {
        if (null == mInstance) {
            synchronized(UsbFragmentManager.class) {
                if (null == mInstance) {
                    mInstance = new UsbFragmentManager();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context) {
        mContext = context;
        mFragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
    }

    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

    public void hideFragment(MvpBaseFragment fragment) {
        if (fragment != null) {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.hide(fragment);
            ft.commitAllowingStateLoss();
        }
    }

    public void showFragment(MvpBaseFragment fragment) {
        LogUtils.tag("Model-FragmentManager").d("show fragment = " + fragment);
        if (mCurrentFragment == null) {
            replaceFragment(fragment);
        } else if (fragment.getType() == TYPE_USB_SETTING && mCurrentFragment.getType() == TYPE_USB_CAMERA) {
            showOrHideFragment(fragment);
        } else if (fragment.getType() == TYPE_USB_CAMERA && mCurrentFragment.getType() == TYPE_USB_SETTING) {
            showOrHideFragment(fragment);
        } else {
            replaceFragment(fragment);
        }

        mCurrentFragment = fragment;
    }
    private void replaceFragment(Fragment fragment) {
        LogUtils.tag("Model-FragmentManager").d("replace fragment = " + fragment);
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commitAllowingStateLoss();

    }
    private void showOrHideFragment(Fragment fragment) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (mCurrentFragment != null)
            LogUtils.tag("Model-FragmentManager").d("previous = " + mCurrentFragment + ",current = " + fragment);
        if (mCurrentFragment != null) {
            LogUtils.tag("Model-FragmentManager").d("Hide fragment: " + mCurrentFragment);
            ft.hide(mCurrentFragment);
//            if (mCurrentFragment.getType() == TYPE_LAUNCH) {
//                ft.remove(mCurrentFragment);
//            } else {
//                ft.hide(mCurrentFragment);
//            }
        }

        if (fragment.isAdded()) {
            LogUtils.tag("Model-FragmentManager").d("Show fragment: " + fragment);
            ft.show(fragment);
        } else {
            LogUtils.tag("Model-FragmentManager").d("Add fragment: " + fragment);
            ft.add(R.id.fragment_container, fragment);
        }

        ft.commitAllowingStateLoss();
    }

    public void showAlertDialog(BaseDialog dialog) {
        //dialog.show(mFragmentManager, "");
    }
}
