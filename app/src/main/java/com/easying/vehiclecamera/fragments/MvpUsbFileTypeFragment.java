package com.easying.vehiclecamera.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.easying.vehiclecamera.adapters.FileListAdapter;
import com.easying.vehiclecamera.R;
import com.easying.vehiclecamera.models.UsbFragmentManager;
import com.easying.vehiclecamera.presenters.MvpUsbFileTypePresenter;

/**
 * Created by think on 2016/11/2.
 */

public class MvpUsbFileTypeFragment extends MvpFragment<MvpUsbFileTypePresenter> {
    private View mConvertView;
    private ListView mFileList;
    private ImageView mNoFiles;
    private String mType = "";
    private String mRootPath = "/mnt/udisk/sonix";
    //private LinkedHashMap<String, List<ListFile>> fileMap;


    public static MvpUsbFileTypeFragment newInstance(String type, String path) {
        Bundle arguments = new Bundle();
        MvpUsbFileTypeFragment fragment = new MvpUsbFileTypeFragment();
        fragment.setArguments(arguments);
        fragment.setType(UsbFragmentManager.TYPE_USB_FILE_TYPE);
        fragment.setFileType(type);
        fragment.setRootPath(path);
        return fragment;
    }

    @Override
    protected MvpUsbFileTypePresenter createPresenter() {
        return new MvpUsbFileTypePresenter();
    }

    @Override
    public void showToast(String content) {

    }

    public void setFileType(String type) {
        try {
            this.mType = type;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setRootPath(String path) {
        if (path != null) {
            mRootPath = path;
        }

    }

    public String getFileType() {
        return this.mType;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mConvertView = inflater.inflate(R.layout.fragment_usb_file_pictures, null);
        mFileList = (ListView) mConvertView.findViewById(R.id.file_list_in_viewpager);
        mNoFiles = (ImageView) mConvertView.findViewById(R.id.iv_no_content);
        //mFileListAdapter = new FileListAdapter(getContext(), fileMap, this.suffix, this.suffix1);
        //mFileList.setAdapter(mFileListAdapter);
        presenter.init(mRootPath);
        return mConvertView;

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.update();
    }

    public void setFileListAdapter(FileListAdapter adapter) {
        mFileList.setAdapter(adapter);
    }

    public void showEmptyPage() {
        mNoFiles.setVisibility(View.VISIBLE);
        mFileList.setVisibility(View.GONE);
    }

    public void showFilesPage() {
        mNoFiles.setVisibility(View.GONE);
        mFileList.setVisibility(View.VISIBLE);
    }


}
