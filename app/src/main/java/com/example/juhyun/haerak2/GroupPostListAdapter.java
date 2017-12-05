package com.example.juhyun.haerak2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by SeoYeon Choi on 2017-12-06.
 */

public class GroupPostListAdapter extends BaseAdapter {

    private ArrayList<GroupPost> postList;
    private ArrayList<String> keyList;
    private String user;

    public GroupPostListAdapter(){
        postList = new ArrayList<>();
        keyList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return postList.size();
    }

    @Override
    public Object getItem(int i) {
        return postList.get(i);
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
            view = inflater.inflate(R.layout.group_post_list_layout, viewGroup, false);
        }

        TextView title = (TextView) view.findViewById(R.id.post_title_text);
        TextView likes = (TextView) view.findViewById(R.id.post_like_num);

        GroupPost post = postList.get(i);

        title.setText(post.getTitle());
        Log.d("dddddddddd", post.getTitle());
        view.setTag(keyList.get(i));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = (String) view.getTag();

                Intent intent = new Intent(context, GroupPostDetailActivity.class);
                intent.putExtra("key", key);
                intent.putExtra("user", user);
                context.startActivity(intent);
            }
        });

        return view;
    }

    public void addPost(String key, GroupPost post, String user){
        keyList.add(key);
        postList.add(post);
        this.user = user;
    }
}
