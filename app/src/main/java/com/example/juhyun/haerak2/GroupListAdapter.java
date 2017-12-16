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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;

/**
 * Created by SeoYeon Choi on 2017-12-03.
 */

public class GroupListAdapter extends BaseAdapter {

    private ArrayList<BucketGroup> groupList;
    private ArrayList<String> keyList;
    private ArrayList<Long> countList;
    private String user;

    public GroupListAdapter(){
        groupList = new ArrayList<>();
        keyList = new ArrayList<>();
        countList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return groupList.size();
    }

    @Override
    public Object getItem(int i) {
        return groupList.get(i);
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
            view = inflater.inflate(R.layout.bucket_layout_menu, viewGroup, false);
        }

        TextView title = (TextView) view.findViewById(R.id.bucket_title_text);
        TextView category = (TextView) view.findViewById(R.id.bucket_category);
        TextView current = (TextView) view.findViewById(R.id.bucket_currNum);
        TextView limit = (TextView) view.findViewById(R.id.bucket_limitNum);
        final LinearLayout layout = (LinearLayout) view.findViewById(R.id.bucket_back);

        BucketGroup bucket = groupList.get(i);

        title.setText(bucket.getTitle());
        category.setText(bucket.getCategory());
        current.setText(countList.get(i)+"");
        limit.setText(bucket.getLimitNumber()+"");
        view.setTag(keyList.get(i));


        Glide.with(context)
                .load(bucket.getPhotoUrl())
                .override(300,300)
                .centerCrop()
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        layout.setBackground(resource);
                    }
                });

        if(bucket!=null){
            if(bucket.getCategory().equals("do")){
                category.setBackgroundColor(0xff66ccff);
            }else if(bucket.getCategory().equals("eat")){
                category.setBackgroundColor(0xffff6600);
            }else if(bucket.getCategory().equals("watch")){
                category.setBackgroundColor(0xffffcc00);
            }else if(bucket.getCategory().equals("have")){
                category.setBackgroundColor(0xffac39ac);
            }else{
                category.setBackgroundColor(0xff00cc44);
            }
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = (String) view.getTag();

                Intent intent = new Intent(context, RealBucketActivity.class);
                intent.putExtra("key", key);
                intent.putExtra("user", user);
                context.startActivity(intent);
            }
        });

        return view;
    }

    public void addGroup(String key, BucketGroup group, String user, long currNum) {
        keyList.add(key);
        groupList.add(group);
        this.user = user;
        countList.add(currNum);
    }
}
