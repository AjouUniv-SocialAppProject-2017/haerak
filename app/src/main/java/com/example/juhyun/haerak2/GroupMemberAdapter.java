package com.example.juhyun.haerak2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by SeoYeon Choi on 2017-12-05.
 */

public class GroupMemberAdapter extends BaseAdapter {

    private ArrayList<String> members;

    public GroupMemberAdapter(){
        members = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public Object getItem(int i) {
        return members.get(i);
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
            view = inflater.inflate(R.layout.group_member_layout, viewGroup, false);
        }

        TextView userText = (TextView) view.findViewById(R.id.member_nickname);
        ImageView image = (ImageView) view.findViewById(R.id.group_leader);

        String nickName = members.get(i);

        if(i==0){
            image.setImageResource(R.drawable.ic_crown);
        }
        userText.setText(nickName);

        return view;
    }

    public void addMember(String member){
        members.add(member);
    }
}
