package com.easying.vehiclecamera.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by think on 2016/11/2.
 */

public class UsbFragmentStatePagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private String tabTitle[] = new String[]{"照片，视频，保护"};

    public UsbFragmentStatePagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        fragments = list;
    }
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle[position];
    }
}
