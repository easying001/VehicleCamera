package com.easying.vehiclecamera.views;

import com.apkfuns.logutils.LogUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by think on 2016/9/17.
 */
/* Two way to customize a dialog which extent from fragment,
 -  the first one is override onCreateDialog() method, by using Dialog.builder to construct a basic dialog
 -  the second way is override onCreateView() method, by inflate your own layout file to realise customization
 */
public abstract class BaseDialog extends DialogFragment {

    public abstract View InitView();
    public abstract void onClickListener(int key);

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog d = (AlertDialog) getDialog();

        if (d != null) {
            Button refreshButton = d.getButton(Dialog.BUTTON_NEUTRAL);
            refreshButton.setOnClickListener(mViewOnClick);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LogUtils.tag("Views-BaseDialog").d("onCreateDialog");

        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
        builder.setView(InitView());
        builder.setTitle(getArguments().getString("title"));
        builder.setPositiveButton("确定",mOnClick);
        builder.setNegativeButton("取消", mOnClick);
        builder.setNeutralButton("刷新", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog show = builder.create();
        //show.getWindow().setContentView(InitView());//自定义布局
        //show.setTitle(getArguments().getString("title"));
        //show.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);//宽高
        //show.getWindow().setGravity(Gravity.TOP);//位置  setLayout
        //show.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);//支持输入法show.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        return show;
    }

    public DialogInterface.OnClickListener mOnClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int which) {
            onClickListener(which);
        }
    };

    public View.OnClickListener mViewOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onClickListener(Dialog.BUTTON_NEUTRAL);
        }
    };



}
