package com.example.juhyun.haerak2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by juhyun on 2017. 11. 10..
 */

public class MylistLayout extends Fragment {

    private MyBucketListAdapter adapter;
    private String user;
    private DatabaseReference database, dataBase2;
    private ListView listView;
    private long currNum;

    private ValueEventListener getBucketList = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for(final DataSnapshot data : dataSnapshot.getChildren()){
                final String key = data.getKey();
                Bucket bucket = data.getValue(Bucket.class);

                if(bucket.getWriter().equals(user)){
                    dataBase2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            currNum = dataSnapshot.child(key).getChildrenCount();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    adapter.addBucket(key, bucket, user, currNum);
                }
            }

            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    View v;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("마이리스트");
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.mylist_layout, container, false);

        listView = (ListView)v.findViewById(R.id.my_bucketList);
        adapter = new MyBucketListAdapter();
        listView.setAdapter(adapter);

        database = FirebaseDatabase.getInstance().getReference("Buckets");
        dataBase2 = FirebaseDatabase.getInstance().getReference("Bucket-members");

        user = getArguments().getString("user");

        database.addListenerForSingleValueEvent(getBucketList);

        return v;
    }
}
