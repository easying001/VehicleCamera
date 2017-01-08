package Views;

import Utils.FileHelper;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.camera.easying.filecamera.R;
import com.camera.easying.filecamera.UsbFragmentManager;
import com.camera.easying.filecamera.UsbFragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import Presenters.MvpUsbFilePreseter;

/**
 * Created by think on 2016/9/29.
 */

public class MvpUsbFileFragment extends MvpFragment<MvpUsbFilePreseter> implements
        View.OnClickListener, AdapterView.OnItemClickListener {
    private static final String TAG = MvpUsbFileFragment.class.getSimpleName();
    public static final String GET_FILE_PATH_ACTION = "Views.MvpUsbFileFragment.GetFilePath";
    private static final int MSG_UPDATE_VIEWPAGER = 1;
    private View mContentView;
    private ViewPager mViewPager;
    private ImageButton mTabPicture;
    private ImageButton mTabRecord;
    private ImageButton mTabProtect;
    private ImageView mTabLine;
    private TabLayout mTabLayout;
    private String mUsbRootPath = "";
    private RefreshHandler mHandler = new RefreshHandler();

    private UsbFragmentStatePagerAdapter mFragmentAdapter;
    private MvpUsbFileTypeFragment mFilePictureFragment;
    private MvpUsbFileTypeFragment mFileVideoFragment;
    private MvpUsbFileTypeFragment mFileProtectionFragment;
    private List<android.support.v4.app.Fragment> mFragmentList = new ArrayList<>();
    private int bmpWidth;
    private int offset;
    private int CurrentIndex;

    private VideoFileBroadcastReceiver mFileControlReceiver;
    private MediaMountedBroadcastReceiver mMediaMounterReceiver;
    private static ProgressDialog mUsbFileProgressDialog;

    public static MvpUsbFileFragment newInstance() {
        Bundle arguments = new Bundle();
        MvpUsbFileFragment fragment = new MvpUsbFileFragment();
        fragment.setArguments(arguments);
        fragment.setType(UsbFragmentManager.TYPE_USB_FILE);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // register();
        Log.d(TAG, "preseter = " + presenter + ", attachView = " + presenter.getView());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_file_management, null);
        mViewPager = (ViewPager) mContentView.findViewById(R.id.vp_file_list);
        mTabLine = (ImageView) mContentView.findViewById(R.id.iv_top_tab_underline);
        // mTabLayout = (TabLayout) mContentView.findViewById(R.id.)
        initTabButton();
        initViewPager();
        initTabLine();
        presenter.init();

        return mContentView;
    }

    public void register() {
//        mFileControlReceiver = new VideoFileBroadcastReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(GET_FILE_PATH_ACTION);
//        getContext().registerReceiver(mFileControlReceiver, filter);

//        mMediaMounterReceiver = new MediaMountedBroadcastReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
//        filter.addDataScheme("file");
//        getContext().registerReceiver(mMediaMounterReceiver, filter);
    }

    private void unregister() {

    }
    private void initTabButton() {
        Log.d(TAG, "init top tab imagebuttons");
        mTabPicture = (ImageButton) mContentView.findViewById(R.id.ib_top_tab_picture);
        mTabRecord = (ImageButton) mContentView.findViewById(R.id.ib_top_tab_record);
        mTabProtect = (ImageButton) mContentView.findViewById(R.id.ib_top_tab_protect);
        mTabPicture.setOnClickListener(this);
        mTabRecord.setOnClickListener(this);
        mTabProtect.setOnClickListener(this);
    }

    public void initViewPager() {
        Log.d(TAG, "init viewpageer and associated fragments");
        mUsbRootPath = FileHelper.read("/default.txt");
        Log.d("UsbFile", "read default file for path = " + mUsbRootPath);

        mFilePictureFragment = MvpUsbFileTypeFragment.newInstance("picture", mUsbRootPath);
        mFileVideoFragment = MvpUsbFileTypeFragment.newInstance("record", mUsbRootPath);
        mFileProtectionFragment = MvpUsbFileTypeFragment.newInstance("protect", mUsbRootPath);
        if (mFragmentList != null) {
            mFragmentList.clear();
        }
        mFragmentList.add(mFilePictureFragment);
        mFragmentList.add(mFileVideoFragment);
        mFragmentList.add(mFileProtectionFragment);
        mFragmentAdapter = new UsbFragmentStatePagerAdapter(getChildFragmentManager(), mFragmentList);

        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setOnPageChangeListener(new MvpUsbFileFragment.MyOnPageChangeListener());
    }

    public void deinitViewPager() {
        Log.d(TAG, "uninit viewpager and fragments");
        //mFilePictureFragment = null;
        //mFileVideoFragment = null;
        //mFileProtectionFragment = null;
        //mFragmentList.clear();
        //mFragmentAdapter = null;
        //mViewPager.setAdapter(null);
    }


    private void initTabLine() {
        bmpWidth = BitmapFactory.decodeResource(getResources(), R.drawable.top_tab_underline).getWidth();// 获取图片宽度
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / 3 - bmpWidth) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        mTabLine.setImageMatrix(matrix);// 设置动画初始位置
        Log.d("UsbFile", "bmpWidth = " + bmpWidth + ",offset = " + offset);

    }



    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        int one = offset * 2 + bmpWidth;
        int two = one * 2;
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Log.d(TAG, "viewpager on page selected position = " + position);
            if (position == 0) {
                selectButton(R.id.ib_top_tab_picture);
            } else if (position == 1) {
                selectButton(R.id.ib_top_tab_record);
            } else if (position == 2) {
                selectButton(R.id.ib_top_tab_protect);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        deinitViewPager();
        presenter.uninit();
    }

    @Override
    protected MvpUsbFilePreseter createPresenter() {
        return new MvpUsbFilePreseter();
    }

    @Override
    public void showToast(String content) {

    }

    private class RefreshHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case MSG_UPDATE_VIEWPAGER:
                    initViewPager();
                    break;
                default:
                    break;
            }
        }
    }

    public void showProgressDialog() {
        if (mUsbFileProgressDialog == null) {
            mUsbFileProgressDialog = ProgressDialog.show(this.getActivity(), "Please Waiting","Detecting Usb Storage Device...");
            mUsbFileProgressDialog.setCancelable(true);
        }
        if (!mUsbFileProgressDialog.isShowing()) {
            mUsbFileProgressDialog.show();
        }

    }
    public void dismissProgressDialog() {
        if (mUsbFileProgressDialog != null) {
            if (mUsbFileProgressDialog.isShowing()) {
                mUsbFileProgressDialog.dismiss();
                mUsbFileProgressDialog = null;
            }
        }
    }

    public void setProgressDialog(String content) {
        if (mUsbFileProgressDialog != null) {
            mUsbFileProgressDialog.setMessage(content);
        }
    }

    public void showLoading() {

    }

    public void selectButton(int id) {
        if (id == R.id.ib_top_tab_picture) {
            mTabProtect.setSelected(false);
            mTabRecord.setSelected(false);
            mTabPicture.setSelected(true);
        } else if (id == R.id.ib_top_tab_record) {
            mTabRecord.setSelected(true);
            mTabPicture.setSelected(false);
            mTabProtect.setSelected(false);
        } else if (id ==R.id.ib_top_tab_protect) {
            mTabPicture.setSelected(false);
            mTabProtect.setSelected(true);
            mTabRecord.setSelected(false);
        }
    }

    @Override
    public void onClick(View view) {
        selectButton(view.getId());
        switch(view.getId()) {
            case R.id.ib_top_tab_picture:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.ib_top_tab_record:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.ib_top_tab_protect:
                mViewPager.setCurrentItem(2);
                break;
            default:
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        TextView fileNameTv = (TextView) view.findViewById(R.id.gv_folder_name);
//        Log.d("UsbFile", "File " + fileNameTv.getText() + " clicked");
    }

    private class VideoFileBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isNext = intent.getBooleanExtra("nextVideo", true);

        }
    }

    private class MediaMountedBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
                mUsbRootPath = intent.getData().getPath() + "/sonix";
                // mHandler.obtainMessage(MSG_UPDATE_VIEWPAGER).sendToTarget();
                Log.d("UsbFile", "usb storage path = " + mUsbRootPath);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregister();
    }
}
