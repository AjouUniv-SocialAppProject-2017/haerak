package com.example.juhyun.haerak2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TabHost;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by juhyun on 2017. 11. 10..
 */

public class GrouplistLayout extends Fragment {

    private TabHost tabhost;
    private ListView fixedList, unfixedList;
    private GroupListAdapter adapter;
    private MyBucketListAdapter adapter2;
    private DatabaseReference database, database2 ,database3;
    private String user;
    private HashMap<String, Long> memberCount;
    private ArrayList<String> unfixedBuckets;

    private ValueEventListener getMemberCount = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for(DataSnapshot data: dataSnapshot.getChildren()){
                String key = data.getKey();
                long count = data.getChildrenCount();

                memberCount.put(key, count);

                for(DataSnapshot dd: data.getChildren()){
                    if(dd.getValue(String.class).equals(user)){
                        unfixedBuckets.add(key);
                    }
                }

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    private ValueEventListener getBucketGroups = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            for(DataSnapshot data : dataSnapshot.getChildren()){
                String key = data.getKey();
                BucketGroup group = data.getValue(BucketGroup.class);
                String bucketId = group.getBucketId();
                DataSnapshot dt = data.child("members");

                for(int i=0; i < dt.getChildrenCount(); i++){
                    if(dt.child(i+"").getValue(String.class).equals(user)){
                        long currNum = memberCount.get(bucketId).longValue();
                        adapter.addGroup(key, group, user, currNum);
                    }
                }

            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private ValueEventListener getBuckets = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Bucket bucket = dataSnapshot.getValue(Bucket.class);
            String key = dataSnapshot.getKey();
            long currNum = memberCount.get(key).longValue();

            adapter2.addBucket(key, bucket, user, currNum);

            adapter2.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    View v;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("그룹리스트");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.grouplist_layout, container, false);

        tabhost = (TabHost) v.findViewById(R.id.tabhost1);
        tabhost.setup();

        TabHost.TabSpec tab1 = tabhost.newTabSpec("Tab Spec Fixed");
        tab1.setContent(R.id.tab_fixed);
        tab1.setIndicator("그룹");
        tabhost.addTab(tab1);

        TabHost.TabSpec tab2 = tabhost.newTabSpec("Tab Spec Unfixed");
        tab2.setContent(R.id.tab_unfixed);
        tab2.setIndicator("참여");
        tabhost.addTab(tab2);

        fixedList = (ListView) v.findViewById(R.id.groupList);
        adapter = new GroupListAdapter();
        fixedList.setAdapter(adapter);

        unfixedList =(ListView) v.findViewById(R.id.unfixedBucketList);
        adapter2 = new MyBucketListAdapter();
        unfixedList.setAdapter(adapter2);

        unfixedBuckets = new ArrayList<>();
        memberCount = new HashMap<>();

        database = FirebaseDatabase.getInstance().getReference("BucketGroups");
        database2 = FirebaseDatabase.getInstance().getReference("Bucket-members");
        database3 = FirebaseDatabase.getInstance().getReference("Buckets");

        database2.addListenerForSingleValueEvent(getMemberCount);

        user = getArguments().getString("user");

        database.addListenerForSingleValueEvent(getBucketGroups);

        if(unfixedBuckets != null){
            Log.d("ddddddddddd", unfixedBuckets.size()+"");
            for(int i=0; i<unfixedBuckets.size(); i++){
                database3.child(unfixedBuckets.get(i)).addListenerForSingleValueEvent(getBuckets);
            }
        }


        return v;
    }
}
