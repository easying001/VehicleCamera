package com.easying.vehiclecamera.activities;

/**
 * Created by wangruiping on 17/1/16.
 */

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

import java.util.Iterator;
import java.util.List;

import com.apkfuns.logutils.LogUtils;
import com.easying.vehiclecamera.R;
import com.easying.vehiclecamera.entities.ListFile;

public class PhotoShowActivity extends Activity implements ViewFactory, OnTouchListener{

    private static final String TAG = PhotoShowActivity.class.getSimpleName();

    /**
     * ImagaSwitcher 的引用
     */
    private ImageSwitcher mImageSwitcher;
    /**
     * 图片id数组
     */
    //private int[] imgIds;
    /**
     * 当前选中的图片id序号
     */
    private int currentPosition;
    /**
     * 按下点的X坐标
     */
    private float downX;
    /**
     * 装载点点的容器
     */
    private LinearLayout linearLayout;
    /**
     * 点点数组
     */
    private ImageView[] tips;

    private String mPlayFile;
    private List<ListFile> mPlayList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_photo);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mPlayFile = bundle.getString("filepath");
        mPlayList = (List<ListFile>) bundle.getSerializable("filelist");
        LogUtils.tag("Activity-Photo").d("file path= " + mPlayFile);
        LogUtils.tag("Activity-Photo").d("file list size = " + mPlayList.size());

        //实例化ImageSwitcher
        mImageSwitcher  = (ImageSwitcher) findViewById(R.id.imageSwitcher1);
        //设置Factory
        mImageSwitcher.setFactory(this);
        //设置OnTouchListener，我们通过Touch事件来切换图片
        mImageSwitcher.setOnTouchListener(this);

        linearLayout = (LinearLayout) findViewById(R.id.viewGroup);

        tips = new ImageView[mPlayList.size()];
        for(int i=0; i<mPlayList.size(); i++){
            ImageView mImageView = new ImageView(this);
            tips[i] = mImageView;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
            layoutParams.rightMargin = 3;
            layoutParams.leftMargin = 3;

            //mImageView.setBackgroundResource(R.drawable.page_indicator_unfocused);
            linearLayout.addView(mImageView, layoutParams);
        }

        //这个我是从上一个界面传过来的，上一个界面是一个GridView
//        currentPosition = getIntent().getIntExtra("position", 0);

        currentPosition = findIndexFromList(mPlayFile);
        mImageSwitcher.setImageURI(Uri.parse(mPlayList.get(currentPosition).getFile().getAbsolutePath()));


        //mImageSwitcher.setImageURI(mPlayList.get(currentPosition).getFile().toURI());

        //setImageBackground(currentPosition);

    }


/*    private void setImageBackground(int selectItems){
        for(int i=0; i<tips.length; i++){
            if(i == selectItems){
                tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
            }else{
                tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }
        }
    }*/

    @Override
    public View makeView() {
        final ImageView i = new ImageView(this);
        i.setBackgroundColor(0xff000000);
        i.setScaleType(ImageView.ScaleType.CENTER_CROP);
        i.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        return i ;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:{
                //手指按下的X坐标
                downX = event.getX();
                break;
            }
            case MotionEvent.ACTION_UP:{
                float lastX = event.getX();
                //抬起的时候的X坐标大于按下的时候就显示上一张图片
                if(lastX > downX){
                    if(currentPosition > 0){
                        //设置动画，这里的动画比较简单，不明白的去网上看看相关内容
                        mImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.left_in));
                        mImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.right_out));
                        currentPosition --;
                        mImageSwitcher.setImageURI(Uri.parse(mPlayList.get(currentPosition).getFile().getAbsolutePath()));
                        //setImageBackground(currentPosition);
                    }else{
                        Toast.makeText(getApplication(), "已经是第一张", Toast.LENGTH_SHORT).show();
                    }
                }

                if(lastX < downX){
                    if(currentPosition < mPlayList.size() - 1){
                        mImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.right_in));
                        mImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.left_out));
                        currentPosition ++ ;
                        mImageSwitcher.setImageURI(Uri.parse(mPlayList.get(currentPosition).getFile().getAbsolutePath()));
                        //setImageBackground(currentPosition);
                    }else{
                        Toast.makeText(getApplication(), "到了最后一张", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            break;
        }

        return true;
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