package Views;

import Widgets.SimpleCameraTextureView;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.camera.easying.filecamera.R;
import com.camera.easying.filecamera.UsbFragmentManager;
import com.serenegiant.usb.UVCCamera;

import Presenters.MvpPresenter;
import Presenters.MvpUsbCameraPresenter;

/**
 * Created by think on 2016/9/25.
 */

public class MvpUsbCameraFragment extends MvpFragment<MvpUsbCameraPresenter> implements View.OnClickListener {
    private static final String TAG = MvpUsbCameraFragment.class.getSimpleName();
    private View mContentView;
    private ImageButton ib_take_pic;
    private ImageButton ib_start_record;
    private ImageButton ib_start_protected;
    private ImageButton ib_enable_mic;
    public ImageButton ib_start_protected_record;
    // private SurfaceView mSurfaceView;
    private SimpleCameraTextureView mUVCCameraView;
    private static ProgressDialog mUsbCameraProgressDialog;

    public static MvpUsbCameraFragment newInstance() {
        Bundle arguments = new Bundle();
        MvpUsbCameraFragment fragment = new MvpUsbCameraFragment();
        fragment.setArguments(arguments);
        fragment.setType(UsbFragmentManager.TYPE_USB_CAMERA);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "preseter = " + presenter + ", attachView = " + presenter.getView());
        //presenter.init();
    }


    @Override
    protected MvpUsbCameraPresenter createPresenter() {
        return new MvpUsbCameraPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        mContentView = inflater.inflate(R.layout.fragment_camera_preview, null);
        ib_take_pic = (ImageButton) mContentView.findViewById(R.id.ib_take_image);
        ib_start_record = (ImageButton) mContentView.findViewById(R.id.ib_start_record);
        ib_start_protected_record = (ImageButton) mContentView.findViewById(R.id.ib_start_protected_record);
        ib_enable_mic = (ImageButton) mContentView.findViewById(R.id.ib_enable_mic);

        ib_take_pic.setOnClickListener(this);
        ib_start_record.setOnClickListener(this);
        ib_start_protected_record.setOnClickListener(this);
        ib_enable_mic.setOnClickListener(this);

        // mSurfaceView = (SurfaceView) mContentView.findViewById(R.id.sv_camera_preview);
        mUVCCameraView = (SimpleCameraTextureView) mContentView.findViewById(R.id.tv_camera_preview);
        mUVCCameraView.setAspectRatio(UVCCamera.DEFAULT_PREVIEW_WIDTH/ (float)UVCCamera.DEFAULT_PREVIEW_HEIGHT);
        return mContentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.init();

    }

    @Override
    public void onResume() {
        super.onResume();
        // presenter.recoverPreview();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.uninit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {

        } else {
            presenter.updateCameraState();

        }
    }

    @Override
    public void showToast(String content) {
        Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.ib_start_record:
                presenter.startRecord();
                break;
            case R.id.ib_enable_mic:
                presenter.enableOrDisableMic();
                break;
            case R.id.ib_start_protected_record:
                presenter.startProtectedRecord();
                break;
            case R.id.ib_take_image:
                presenter.takeOnePic();
                break;
        }
    }
    public void showButtonIcon(boolean isStreaming) {
        if (isStreaming) {
            ib_start_record.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ib_start_record.setImageResource(R.drawable.fragment_camera_video_pause);
        } else {
            ib_start_record.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ib_start_record.setImageResource(R.drawable.fragment_camera_video_start);
        }
    }

    public void setMicButton(boolean mute) {
        if (mute) {
            ib_start_record.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ib_enable_mic.setImageResource(R.drawable.fragment_camera_disable_mic);
        } else {
            ib_start_record.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ib_enable_mic.setImageResource(R.drawable.fragment_camera_enable_mic);
        }
    }

    public void showProgressDialog() {
        if (mUsbCameraProgressDialog == null) {
            mUsbCameraProgressDialog = ProgressDialog.show(this.getActivity(), "Please Waiting","Detecting Usb Camera Device...");
            mUsbCameraProgressDialog.setCancelable(false);
            }
            if (!mUsbCameraProgressDialog.isShowing()) {
                mUsbCameraProgressDialog.show();
            }
    }

    public void dismissProgressDialog() {
        if (mUsbCameraProgressDialog != null) {
            if (mUsbCameraProgressDialog.isShowing()) {
                mUsbCameraProgressDialog.dismiss();
                mUsbCameraProgressDialog = null;
            }
        }
    }

    public void setProgressDialog(String content) {
        if (mUsbCameraProgressDialog != null) {
            mUsbCameraProgressDialog.setMessage(content);
        }
    }

//    public SurfaceView getmSurfaceView() {
//        return mSurfaceView;
//    }

    public SimpleCameraTextureView getUVCTextureView() {
        return mUVCCameraView;
    }

}
