package com.example.juhyun.haerak2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by SeoYeon Choi on 2017-11-26.
 */

public class BucketCommentAdapter extends BaseAdapter{

    private ArrayList<BucketComment> commentList;

    public BucketCommentAdapter(){
        commentList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Object getItem(int i) {
        return commentList.get(i);
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
            view = inflater.inflate(R.layout.comment_layout, viewGroup, false);
        }

        TextView writer = (TextView) view.findViewById(R.id.comment_writer);
        TextView date = (TextView) view.findViewById(R.id.comment_date);
        TextView content = (TextView) view.findViewById(R.id.comment_content);

        BucketComment comment = commentList.get(i);

        writer.setText(comment.getWriter());
        date.setText(comment.getDate());
        content.setText(comment.getContent());

        return view;
    }

    public void addComment(BucketComment comment){
        commentList.add(comment);
    }
}
