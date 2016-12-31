package com.camera.easying.filecamera;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by think on 2016/9/17.
 */

public abstract class MyListAdapter<T> extends BaseAdapter {
    protected List<T> mList;
    protected Context mContext;
    protected final LayoutInflater mInflater;

    public MyListAdapter(Context context, List<T> list) {
        mContext = context;
        mList = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (mList != null) {
            return mList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        return mList == null ? null : mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public abstract View getView(int i, View view, ViewGroup viewGroup);

    public void setData(List<T> list) {
        this.mList = list;
        notifyDataSetChanged();
    }
}
