package Presenters;
import android.os.Environment;
import android.util.Log;

import com.camera.easying.filecamera.FileListAdapter;
import com.camera.easying.filecamera.ListFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import Models.UsbFileManager;
import Utils.DateHelper;
import Views.MvpUsbFileTypeFragment;

/**
 * Created by think on 2016/11/2.
 */

public class MvpUsbFileTypePresenter extends MvpBasePresenter<MvpUsbFileTypeFragment> {

    private FileListAdapter mFileListAdapter;
    private LinkedHashMap<String, List<ListFile>> fileMap;
    private String suffix;
    private String suffix1;
    private String mess_storage_path = "/mnt/udisk/sonix";
    private String photoList = "";
    private String mRootPath = "";
    private UsbFileManager mUsbFileManager;

    public void init(String path) {
        if (isViewAttached()) {
            String fileType = getView().getFileType();
            mess_storage_path = path;
            initData(fileType);
        }
    }

    private void initData(String type) {
        if (type != null) {
            if (type.equalsIgnoreCase("picture")) {
                suffix = ".jpg";
                suffix1 = ".jpg";
            } else if (type.equalsIgnoreCase("record")) {
                suffix = ".avi";
                suffix1 = ".mp4";
            } else if (type.equalsIgnoreCase("protect")) {
                suffix = ".avi";
                suffix1 = ".avi";
            } else if (type.equalsIgnoreCase("phone_record")) {
                suffix = ".avi";
                suffix1 = ".avi";
            }
        }

        Log.d("UsbFileType", "root path =" + mess_storage_path);
        this.mRootPath = mess_storage_path + "/" + type;
        update();
    }

    public void update() {
        Log.d("UsbFileType", "Usb File mRootPath = " + mRootPath);
        if (mRootPath == null) {
            return;
        }
        fileMap = getFileDirectory(new File(mRootPath));

        if (fileMap == null || fileMap.size() == 0) {

            getView().showEmptyPage();
        } else {
            Log.d("UsbFileType", "fileMap size = " + fileMap.size());
            getView().showFilesPage();
        }

        mFileListAdapter = new FileListAdapter(getView().getContext(), fileMap, suffix, suffix1);
        getView().setFileListAdapter(mFileListAdapter);

    }

    private LinkedHashMap<String, List<ListFile>> getFileDirectory(File rootFile) {
        LinkedHashMap<String, List<ListFile>> map = null;
        try {
            List<File> fileListFiles = new ArrayList<>();
            File[] files = rootFile.listFiles();
            if (files == null) {
                Log.d("UsbFileType", "no file found in USB storage");
                return null;
            }
            for (File file : files) {
                if (file.isDirectory()) {

                } else if (file.isFile()) {
                    if (file.getName().endsWith(suffix) || file.getName().endsWith(suffix1)) {
                        fileListFiles.add(file);
                        Log.d("UsbFileType", "add a file with suffix into list");
                    }
                }
            }

            Collections.sort(fileListFiles, new Comparator<File>() {
                @Override
                public int compare(File lhs, File rhs) {
                    long ll = lhs.lastModified();
                    long lr = rhs.lastModified();
                    if (ll > lr) {
                        return 1;
                    } else if (ll < lr) {
                        return -1;
                    }
                    return 0;
                }
            });

            map = new LinkedHashMap<>();
            for (int i = 0; i < fileListFiles.size(); i++) {
                File f = fileListFiles.get(i);
                String fileday = DateHelper.getStringTime(DateHelper.YYYY_MM_DD, f.lastModified());
                List<ListFile> l = map.get(fileday);
                if (l == null) {
                    l = new ArrayList<>();
                }
                l.add(new ListFile(f, getView().getFileType()));
                map.put(fileday, l);
                Log.d("UsbFileType", "put files of day " + fileday + " into map");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


}
