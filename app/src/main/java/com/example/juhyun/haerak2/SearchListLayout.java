package com.example.juhyun.haerak2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by SeoYeon Choi on 2017-11-20.
 */

public class SearchListLayout extends Fragment{

    private DatabaseReference dataBase;
    private GridView gridView;
    private BucketListAdapter adapter;

    private ValueEventListener getbucketList = new ValueEventListener(){

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            ArrayList<Bucket> bucketList = new ArrayList<>();

            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                String key = snapshot.getKey();
                Bucket bucket = snapshot.getValue(Bucket.class);

                if(bucket.getTitle().contains(getArguments().getString("search"))){
                    adapter.addBucket(key, bucket.getTitle(), bucket.getWriter(), bucket.getLimitNumber());
                }

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_list_layout, container, false);

        dataBase = FirebaseDatabase.getInstance().getReference("Buckets");

        gridView = (GridView)view.findViewById(R.id.search_bucketList);
        adapter = new BucketListAdapter();
        gridView.setAdapter(adapter);

        dataBase.addListenerForSingleValueEvent(getbucketList);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
