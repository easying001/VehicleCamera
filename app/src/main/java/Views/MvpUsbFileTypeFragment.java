package Views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.camera.easying.filecamera.FileListAdapter;
import com.camera.easying.filecamera.ListFile;
import com.camera.easying.filecamera.R;
import com.camera.easying.filecamera.UsbFragmentManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import Presenters.MvpUsbFileTypePresenter;
import Utils.DateHelper;

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
