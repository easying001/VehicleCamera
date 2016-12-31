package Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.camera.easying.filecamera.R;

import java.util.zip.Inflater;

/**
 * Created by think on 2016/10/31.
 */

public class MvpStatusButton extends LinearLayout {
    private Context mContext;
    private RadioGroup mBtnGroup;
    private RadioButton mBtnLow;
    private RadioButton mBtnMiddle;
    private RadioButton mBtnHigh;
    private String mName;


    private OnClickListener clickListenerLow;
    private OnClickListener clickListenerMiddle;
    private OnClickListener clickListenerHigh;
    private OnStatusButtonClickListener allListener;
    private int btnFlag;
    public enum StatusButtonChild {
        HIGH, MIDDLE, LOW
    }

    public interface OnStatusButtonClickListener {
        void onClick(MvpStatusButton sButton, StatusButtonChild child);
    }

    public MvpStatusButton(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public MvpStatusButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public MvpStatusButton setName(String name) {
        mName = name;
        return this;
    }

    public String getName() {
        return mName;
    }

    public MvpStatusButton setAllBtnClickListener(OnStatusButtonClickListener allListener) {
        this.allListener = allListener;
        return this;
    }

    public MvpStatusButton setAllButtonText(String resIdLeft, String resIdRight) {
        mBtnHigh.setText(resIdLeft);
        mBtnMiddle.setVisibility(View.GONE);
        mBtnLow.setText(resIdRight);
        return this;
    }

    public MvpStatusButton setAllButtonText(String resIdLeft, String resIdMiddle, String resIdRight) {
        mBtnHigh.setText(resIdLeft);
        mBtnMiddle.setText(resIdMiddle);
        mBtnLow.setText(resIdRight);
        return this;
    }

    public MvpStatusButton setBtnHighChecked() {
        mBtnHigh.setChecked(true);
        btnFlag = 1;
        return this;
    }

    public MvpStatusButton setBtnMiddleChecked() {
        mBtnMiddle.setChecked(true);
        btnFlag = 2;
        return this;
    }

    public MvpStatusButton setBtnLowChecked() {
        mBtnLow.setChecked(true);
        btnFlag = 3;
        return this;
    }



    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.setting_status_button, this);
        mBtnGroup = (RadioGroup) findViewById(R.id.status_button_group);
        mBtnHigh = (RadioButton) mBtnGroup.getChildAt(0);
        mBtnMiddle = (RadioButton) mBtnGroup.getChildAt(1);
        mBtnLow = (RadioButton) mBtnGroup.getChildAt(2);

        mBtnGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == mBtnHigh.getId() && btnFlag != 1) {
                    btnFlag = 1;
                } else if (i == mBtnMiddle.getId() && btnFlag != 2) {
                    btnFlag = 2;
                } else if (i == mBtnLow.getId() && btnFlag != 3) {
                    btnFlag = 3;
                }
                if (allListener != null) {
                    switch (btnFlag) {
                        case 1:
                            allListener.onClick(MvpStatusButton.this, StatusButtonChild.HIGH);
//                            if (clickListenerHigh != null) {
//                                clickListenerHigh.onClick(mBtnHigh);
//                            }
                            break;
                        case 2:
                            allListener.onClick(MvpStatusButton.this, StatusButtonChild.MIDDLE);
//                            if (clickListenerMiddle != null) {
//                                clickListenerMiddle.onClick(mBtnMiddle);
//                            }
                            break;
                        case 3:
                            allListener.onClick(MvpStatusButton.this, StatusButtonChild.LOW);
//                            if (clickListenerLow != null) {
//                                clickListenerLow.onClick(mBtnLow);
//                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }
}
