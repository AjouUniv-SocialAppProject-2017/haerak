package com.example.juhyun.haerak2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.ViewTarget;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by SeoYeon Choi on 2017-11-19.
 */

public class BucketListAdapter extends BaseAdapter{

    private ArrayList<Bucket> bucketList;
    private ArrayList<String> keyList;
    private String user;

    public BucketListAdapter(){
        bucketList = new ArrayList<>();
        keyList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return bucketList.size();
    }

    @Override
    public Object getItem(int i) {
        return bucketList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int pos = i;
        final Context context = viewGroup.getContext();

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.bucket_layout_main, viewGroup, false);
        }

        TextView title = (TextView) view.findViewById(R.id.bucketTitle);
        TextView writer = (TextView) view.findViewById(R.id.bucketWriter);
        final LinearLayout layout = (LinearLayout) view.findViewById(R.id.bucket_item);

        final Bucket bucket = bucketList.get(i);

        title.setText(bucket.getTitle());
        writer.setText(bucket.getCategory());
        view.setTag(keyList.get(i));

        Glide.with(context)
                .load(bucket.getPhotoUrl())
                .override(300,300)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        layout.setBackground(resource);
                    }
                });

        if(bucket!=null){
                if(bucket.getCategory().equals("do")){
                    writer.setBackgroundColor(0xff66ccff);
                }else if(bucket.getCategory().equals("eat")){
                    writer.setBackgroundColor(0xffff6600);
                }else if(bucket.getCategory().equals("watch")){
                    writer.setBackgroundColor(0xffffcc00);
                }else if(bucket.getCategory().equals("have")){
                    writer.setBackgroundColor(0xffac39ac);
                }else{
                    writer.setBackgroundColor(0xff00cc44);
                }
        }



        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = (String) view.getTag();

                Intent intent = new Intent(context, BucketDetailActivity.class);
                intent.putExtra("key", key);
                intent.putExtra("user", user);
                intent.putExtra("latitude", bucket.getLatitude());
                intent.putExtra("longitude", bucket.getLongitude());
                intent.putExtra("location", bucket.getLocation());
                context.startActivity(intent);
            }
        });

        return view;
    }

    public void addBucket(String key, Bucket bucket, String user) {
        bucketList.add(bucket);
        keyList.add(key);
        this.user = user;
    }

}
