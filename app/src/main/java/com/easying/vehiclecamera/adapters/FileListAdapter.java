package com.easying.vehiclecamera.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.easying.vehiclecamera.R;
import com.easying.vehiclecamera.entities.ListFile;
import com.easying.vehiclecamera.activities.PhotoShowActivity;
import com.easying.vehiclecamera.activities.VideoPlayerActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.easying.vehiclecamera.utils.UIHelper;

/**
 * Created by think on 2016/11/4.
 */

public class FileListAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private LinkedHashMap<String, List<ListFile>> map;
    private List<String> postionKeys;
    private List<ListFile> allListFile = new ArrayList<>();
    private Context ctx;
    private String mSuffix;
    private String mSuffix1;


    public FileListAdapter(Context context, LinkedHashMap<String, List<ListFile>> map, String suffix, String suffix1) {
        ctx = context;
        this.map = map;
        this.mSuffix = suffix;
        this.mSuffix1 = suffix1;

        inflater = LayoutInflater.from(context);
        postionKeys = new ArrayList<>();
        if (map == null) {
            return;
        }

        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            postionKeys.add((String) entry.getKey());
            allListFile.addAll((List<ListFile>) entry.getValue());
        }
    }

    @Override
    public int getCount() {
        return map == null ? 0 : map.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.list_file_gridview, null, false);
        TextView tvCount = (TextView) convertView.findViewById(R.id.tvCount);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        final GridLayout gridList = (GridLayout) convertView.findViewById(R.id.gridList);
        gridList.setTag(position);
        String key = postionKeys.get(position);
        final List<ListFile> val = map.get(key);
        int count = val.size();

        tvDate.setText(key);
        if (mSuffix.equals(".avi") || mSuffix1.equals(".mp4")) {
            tvCount.setText(count + "个");
        } else {
            tvCount.setText(count + "张");
        }

        //photoList = "";
        for (int i = 0; i < val.size(); i++) {
            final File file = val.get(i).getFile();
            if (file.length() == 0) {
                continue;
            }
            final String path = file.getAbsolutePath();
            final ImageView img = new ImageView(ctx);
            final TextView text = new TextView(ctx);
/*            if(photoList.equals("")) {
                photoList += path;
            }else {
                photoList += "," + path;
            }*/
            LinearLayout gridItemLayout = new LinearLayout(ctx);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            gridItemLayout.setOrientation(LinearLayout.VERTICAL);
            gridItemLayout.setLayoutParams(lp);
            img.setTag(path);
            int maxWidth = ((int) UIHelper.width / 3) - UIHelper.dip2px(ctx, 8);
            int maxHeight = (int) (maxWidth * 0.7);
            LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(maxWidth, maxHeight);
            img.setLayoutParams(vp);
            if (mSuffix.equals(".avi") || mSuffix1.equals(".mp4")) {
                img.setImageResource(R.drawable.def_video_img);
            } else {
                img.setImageResource(R.drawable.def_photo_img);
            }
            img.setPadding(UIHelper.dip2px(ctx, 5), UIHelper.dip2px(ctx, 5), UIHelper.dip2px(ctx, 5), UIHelper.dip2px(ctx, 5));

            String filePath = file.getParent();
            String fileName = file.getName();
            if (mSuffix.equals(".avi") || mSuffix1.equals(".mp4")) {
                String[] tmpSplit = fileName.split("\\.");
                String tmpString = tmpSplit[0].substring(0, tmpSplit[0].length() - 4);
                String tmp = tmpString + "0000." + tmpSplit[1];

                //File f = new File(filePath + "/thumbnail/" + fileName.replace(suffix, ".jpg"));
                File f = new File(filePath + "/thumbnail/" + tmp.replace(mSuffix, ".jpg"));
                if (f.exists()) {
                    Picasso.with(ctx).load(f).resize(160, 90).into(img);
                }
            } else {
                if (file.exists()) {
                    Picasso.with(ctx).load(file).resize(160, 90).into(img);
                }
            }

            RelativeLayout.LayoutParams tp = new RelativeLayout.LayoutParams(maxWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);
            text.setLayoutParams(tp);
            text.setTextSize(10);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (path.endsWith(".avi") || path.endsWith(".mp4")) {
                        LogUtils.tag("Adapter-FileList").d("to show video");
                        File file = new File(path);
                        if(!file.exists()) {
                            //Toast.makeText(ctx, getString(R.string.file_not_exist_check_sdcard), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent intent = new Intent(ctx, VideoPlayerActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("playFile", path);
                        bundle.putSerializable("playList", (Serializable) val);
                        intent.putExtras(bundle);
                        // intent.putExtra("playFile", path);
                        //intent.putExtra("allListFile", (Serializable) val);
                        ctx.startActivity(intent);

//                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                        String strend="";
//                        if(path.toLowerCase().endsWith(".mp4")){
//                            strend="mp4";
//                        }
//                        else if(path.toLowerCase().endsWith(".3gp")){
//                            strend="3gp";
//                        }
//                        else if(path.toLowerCase().endsWith(".mov")){
//                            strend="mov";
//                        }
//                        else if(path.toLowerCase().endsWith(".wmv")){
//                            strend="wmv";
//                        }
//
//                        intent.setDataAndType(Uri.parse(path), "video/"+strend);
//                        ctx.startActivity(intent);
                    } else {
                        LogUtils.tag("Adapter-FileList").d("to phone show");

                        File file = new File(path);
                        if(!file.exists()) {
                            //Toast.makeText(getActivity(), getString(R.string.file_not_exist_check_sdcard), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent intent = new Intent(ctx, PhotoShowActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("filepath", path);
                        bundle.putSerializable("filelist", (Serializable) val);

                        intent.putExtras(bundle);
                        //intent.putExtra("filepath", path);
                        //intent.putExtra("filelist", (Serializable) val);
                        ctx.startActivity(intent);
                        LogUtils.tag("Adapter-FileList").d("to phone show ok");
                    }
                }
            });
            img.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
//                        deleteFile(file );
                    return true;
                }
            });
            text.setText(file.getName());
            gridItemLayout.addView(img);
            gridItemLayout.addView(text);
            gridList.addView(gridItemLayout);
        }
        return convertView;
    }
}
