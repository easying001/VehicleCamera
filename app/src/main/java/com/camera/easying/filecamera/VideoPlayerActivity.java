/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.camera.easying.filecamera;

import java.io.File;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by yangjie11 on 2016/12/31.
 */

public class VideoPlayerActivity extends Activity implements EasyVideoCallback {
    private static final String TAG = VideoPlayerActivity.class.getSimpleName();
    private EasyVideoPlayer mVideoPlayer;
    private List<ListFile> mPlayList;
    private String mPlayFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.easy_video_player);
        mVideoPlayer = (EasyVideoPlayer) findViewById(R.id.video_player);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mPlayFile = bundle.getString("playFile");
        mPlayList = (List<ListFile>) bundle.getSerializable("playList");
        Log.d(TAG, "play file = " + mPlayFile);
        Log.d(TAG, "play list size = " + mPlayList.size());

        if (mVideoPlayer != null) {
            mVideoPlayer.setSubmitText("");
            mVideoPlayer.setCallback(this);
            mVideoPlayer.setAutoPlay(true);
            mVideoPlayer.setAutoFullscreen(true);
            mVideoPlayer.setBottomLabelText(mPlayFile);
            mVideoPlayer.setSource(Uri.fromFile(new File(mPlayFile)));
        } else {
            Log.d(TAG, "mVideoPlayer is null");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayFile = "";
        mPlayList = null;
    }

    @Override
    public void onStarted(EasyVideoPlayer player) {
        Log.d(TAG, "on video started");

    }

    @Override
    public void onPaused(EasyVideoPlayer player) {
        Log.d(TAG, "on video paused");
    }

    @Override
    public void onPreparing(EasyVideoPlayer player) {

    }

    @Override
    public void onPrepared(EasyVideoPlayer player) {

    }

    @Override
    public void onBuffering(int percent) {

    }

    @Override
    public void onError(EasyVideoPlayer player, Exception e) {
        Log.d(TAG, "on video play error");
    }

    @Override
    public void onCompletion(EasyVideoPlayer player) {
        Log.d(TAG, "on video completed");
    }

    @Override
    public void onRetry(EasyVideoPlayer player, Uri source) {

    }

    @Override
    public void onSubmit(EasyVideoPlayer player, Uri source) {
        Log.d(TAG, "on video submited uri = " + source);
    }

    @Override
    public void onNext(EasyVideoPlayer player, Uri source) {
        Log.d(TAG, "on video skip next");
        int index = findIndexFromList(mPlayFile);
        Log.d(TAG, "find index = " + index + ",size = " + mPlayList.size());
        if (++index < mPlayList.size()) {
            mPlayFile = mPlayList.get(index).getFile().getAbsolutePath();
        }

        if (mVideoPlayer != null) {
            mVideoPlayer.setBottomLabelText(mPlayFile);
            mVideoPlayer.setSource(Uri.fromFile(new File(mPlayFile)));
        } else {
            Log.d(TAG, "mVideoPlayer is null");
        }

    }

    @Override
    public void onPrev(EasyVideoPlayer player, Uri source) {
        Log.d(TAG, "on video skip prev");
        int index = findIndexFromList(mPlayFile);
        Log.d(TAG, "find index = " + index + ",size = " + mPlayList.size());
        if (index > 0) {
            index--;
            mPlayFile = mPlayList.get(index).getFile().getAbsolutePath();
        }

        if (mVideoPlayer != null) {
            mVideoPlayer.setBottomLabelText(mPlayFile);
            mVideoPlayer.setSource(Uri.fromFile(new File(mPlayFile)));
        } else {
            Log.d(TAG, "mVideoPlayer is null");
        }

    }

    private int findIndexFromList(String file) {
        int index = 0;
        if (mPlayList != null) {
            Iterator<ListFile> iterator = mPlayList.iterator();
            while(iterator.hasNext()) {
                ListFile listFile = iterator.next();
                if (listFile.getFile().getAbsolutePath().equals(file)) {
                    break;
                }
                index++;
            }
        }
        return index;
    }
}
