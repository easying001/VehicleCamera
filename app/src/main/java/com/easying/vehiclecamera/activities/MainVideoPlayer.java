package com.easying.vehiclecamera.activities;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;
import com.apkfuns.logutils.LogUtils;
import com.easying.vehiclecamera.R;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by yangjie11 on 2016/12/20.
 */

public class MainVideoPlayer extends Activity implements EasyVideoCallback {
    private EasyVideoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_video_player);
        player = (EasyVideoPlayer) findViewById(R.id.main_player);
        assert player != null;
        player.setCallback(this);
        // All further configuration is done from the XML layout.
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.pause();
    }

    @Override
    public void onStarted(EasyVideoPlayer player) {
    }

    @Override
    public void onPaused(EasyVideoPlayer player) {
    }

    @Override
    public void onPreparing(EasyVideoPlayer player) {
        LogUtils.tag("Activity-Video").d("onPreparing");
    }

    @Override
    public void onPrepared(EasyVideoPlayer player) {
        LogUtils.tag("Activity-Video").d("onPrepared");
    }

    @Override
    public void onBuffering(int percent) {
        LogUtils.tag("Activity-Video").d("onBuffering = " + percent + "%");
    }

    @Override
    public void onError(EasyVideoPlayer player, Exception e) {
        LogUtils.tag("Activity-Video").d("onError(): " + e.getMessage());
//        new MaterialDialog.Builder(this)
//                .title(R.string.error)
//                .content(e.getMessage())
//                .positiveText(android.R.string.ok)
//                .show();
    }

    @Override
    public void onCompletion(EasyVideoPlayer player) {
        LogUtils.tag("Activity-Video").d("onCompletion()");
    }


    @Override
    public void onRetry(EasyVideoPlayer player, Uri source) {
        Toast.makeText(this, "Retry", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSubmit(EasyVideoPlayer player, Uri source) {
        Toast.makeText(this, "Submit", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNext(EasyVideoPlayer player, Uri source) {
        Toast.makeText(this, "Skip Next", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPrev(EasyVideoPlayer player, Uri source) {
        Toast.makeText(this, "Skip Prev", Toast.LENGTH_SHORT).show();
    }
}
