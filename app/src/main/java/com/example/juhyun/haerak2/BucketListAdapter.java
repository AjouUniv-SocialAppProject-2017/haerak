package com.example.juhyun.haerak2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

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

        Bucket bucket = bucketList.get(i);

        title.setText(bucket.getTitle());
        writer.setText(bucket.getWriter());
        view.setTag(keyList.get(i));

            if(bucket!=null){
                if(bucket.getLimitNumber() == 1){
                    view.setBackgroundResource(R.drawable.back1);
                }else if(bucket.getLimitNumber() == 2){
                    view.setBackgroundResource(R.drawable.back2);
                }else if(bucket.getLimitNumber() == 3){
                    view.setBackgroundResource(R.drawable.back3);
                }else if(bucket.getLimitNumber() == 4){
                    view.setBackgroundResource(R.drawable.back4);
                }else {
                    view.setBackgroundResource(R.drawable.back5);
                }
            }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = (String) view.getTag();

                Intent intent = new Intent(context, BucketDetailActivity.class);
                intent.putExtra("key", key);
                intent.putExtra("user", user);
                context.startActivity(intent);
            }
        });

        return view;
    }

    public void addBucket(String key, Bucket bucket, String user){
        bucketList.add(bucket);
        keyList.add(key);
        this.user = user;
    }

}
