package com.example.juhyun.haerak2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TabHost;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CategorySearchActivity extends AppCompatActivity {

    private DatabaseReference database;
    private BucketListAdapter adapter1, adapter2, adapter3, adapter4, adapter5;
    private GridView doBucketList, eatBucketList, watchBucketList, haveBucketList, goBucketList;
    private String user;

    private ValueEventListener getBucketsByCategory = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for(DataSnapshot data: dataSnapshot.getChildren()){
                String key = data.getKey();
                Bucket bucket = data.getValue(Bucket.class);

                if(bucket.getCategory().equals("do")){
                    adapter1.addBucket(key, bucket, user);
                }else if(bucket.getCategory().equals("eat")){
                    adapter2.addBucket(key, bucket, user);
                }else if(bucket.getCategory().equals("watch")){
                    adapter3.addBucket(key, bucket, user);
                }else if(bucket.getCategory().equals("have")){
                    adapter4.addBucket(key, bucket, user);
                }else if(bucket.getCategory().equals("go")){
                    adapter5.addBucket(key, bucket, user);
                }

            }
            adapter1.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_search);

        TabHost th = (TabHost) findViewById(R.id.categorytabhost);
        th.setup();

        TabHost.TabSpec dosp = th.newTabSpec("do_tab");
        dosp.setIndicator(getString(R.string.do_tab));
        dosp.setContent(R.id.do_tab);
        th.addTab(dosp);

        TabHost.TabSpec eatsp = th.newTabSpec("eat_tab");
        eatsp.setIndicator(getString(R.string.eat_tab));
        eatsp.setContent(R.id.eat_tab);
        th.addTab(eatsp);

        TabHost.TabSpec watchsp = th.newTabSpec("watch_tab");
        watchsp.setIndicator(getString(R.string.watch_tab));
        watchsp.setContent(R.id.watch_tab);
        th.addTab(watchsp);

        TabHost.TabSpec havesp = th.newTabSpec("have_tab");
        havesp.setIndicator(getString(R.string.have_tab));
        havesp.setContent(R.id.have_tab);
        th.addTab(havesp);

        TabHost.TabSpec gosp = th.newTabSpec("go_tab");
        gosp.setIndicator(getString(R.string.go_tab));
        gosp.setContent(R.id.go_tab);
        th.addTab(gosp);

        int index = getIntent().getIntExtra("tabIndex", 0);

        if(index == 0){
            th.setCurrentTab(0);
        }else if(index == 1) {
            th.setCurrentTab(1);
        }else if(index == 2){
            th.setCurrentTab(2);
        }else if(index == 3){
            th.setCurrentTab(3);
        }else if(index == 4){
            th.setCurrentTab(4);
        }

        user = getIntent().getStringExtra("user");

        doBucketList = (GridView) findViewById(R.id.do_bucketList);
        eatBucketList = (GridView) findViewById(R.id.eat_bucketList);
        watchBucketList = (GridView) findViewById(R.id.watch_bucketList);
        haveBucketList = (GridView) findViewById(R.id.have_bucketList);
        goBucketList = (GridView) findViewById(R.id.go_bucketList);

        adapter1 = new BucketListAdapter();
        adapter2 = new BucketListAdapter();
        adapter3 = new BucketListAdapter();
        adapter4 = new BucketListAdapter();
        adapter5 = new BucketListAdapter();

        doBucketList.setAdapter(adapter1);
        eatBucketList.setAdapter(adapter2);
        watchBucketList.setAdapter(adapter3);
        haveBucketList.setAdapter(adapter4);
        goBucketList.setAdapter(adapter5);

        database = FirebaseDatabase.getInstance().getReference("Buckets");
        database.addListenerForSingleValueEvent(getBucketsByCategory);
    }
}
