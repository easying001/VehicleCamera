package com.easying.vehiclecamera.fragments;

import com.apkfuns.logutils.LogUtils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by think on 2016/9/26.
 */

public class MvpBaseFragment extends Fragment {
    protected int fragmentType;

    public void setType(int type) {
        fragmentType = type;
    }

    public int getType() {
        return fragmentType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.tag("Fragment-Base").d("fragmentType = " + fragmentType + " onCreate()");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.tag("Fragment-Base").d("fragmentType = " + fragmentType + " onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.tag("Fragment-Base").d("fragmentType = " + fragmentType + " onPause()");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.tag("Fragment-Base").d("fragmentType = " + fragmentType + " onStart()");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.tag("Fragment-Base").d("fragmentType = " + fragmentType + " onStop()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.tag("Fragment-Base").d("fragmentType = " + fragmentType + " onDestroy()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.tag("Fragment-Base").d("fragmentType = " + fragmentType + " onDestroyView()");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtils.tag("Fragment-Base").d("fragmentType = " + fragmentType + " onHiddenChanged(): " + hidden);
    }
}
