package com.example.juhyun.haerak2;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RealBucketActivity extends AppCompatActivity {

    private String key, user;
    private DatabaseReference database, database2;
    private GroupMemberAdapter adapter;
    private GroupPostListAdapter postAdapter;
    private TextView title, content;
    private ListView memberList, postList;
    private ImageView categoryImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_bucket);

        TabHost th = (TabHost) findViewById(R.id.group_tab);
        th.setup();

        TabHost.TabSpec infosp = th.newTabSpec("info_tab");
        infosp.setIndicator(getString(R.string.info_tab));
        infosp.setContent(R.id.info_tab);
        th.addTab(infosp);

        TabHost.TabSpec communitysp = th.newTabSpec("community_tab");
        communitysp.setIndicator(getString(R.string.community_tab));
        communitysp.setContent(R.id.community_tab);
        th.addTab(communitysp);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab2);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddGroupPostActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("groupKey", key);
                startActivity(intent);
            }
        });

        key = getIntent().getStringExtra("key");
        user = getIntent().getStringExtra("user");
        title = (TextView) findViewById(R.id.infotab_buckettitle);
        content = (TextView)findViewById(R.id.infotab_bucketcontent);
        categoryImage = (ImageView)findViewById(R.id.infotab_categoryimageview);
        memberList = (ListView) findViewById(R.id.infotab_memberlistview);
        postList = (ListView)findViewById(R.id.join_bucketList);

        adapter = new GroupMemberAdapter();
        memberList.setAdapter(adapter);

        postAdapter = new GroupPostListAdapter();
        postList.setAdapter(postAdapter);

        database = FirebaseDatabase.getInstance().getReference("BucketGroups").child(key);

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                BucketGroup group = dataSnapshot.getValue(BucketGroup.class);
                ArrayList<String> members = group.getMembers();

                title.setText(group.getTitle());
                content.setText(group.getContent());

                for (String mem : members){
                    adapter.addMember(mem);
                }

                switch (group.getCategory()){
                    case "do":
                        categoryImage.setImageResource(R.drawable.icon_do);
                        break;
                    case "eat":
                        categoryImage.setImageResource(R.drawable.icon_eat);
                        break;
                    case "watch":
                        categoryImage.setImageResource(R.drawable.icon_watch);
                        break;
                    case "have":
                        categoryImage.setImageResource(R.drawable.icon_want);
                        break;
                    case "go":
                        categoryImage.setImageResource(R.drawable.icon_go);
                        break;
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        database2 = FirebaseDatabase.getInstance().getReference("GroupPosts").child(key);
        database2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    String postKey = data.getKey();
                    DataSnapshot dd = data.child("likeMembers");

                    GroupPost post = new GroupPost();
                    post.setTitle(data.child("title").getValue(String.class));
                    post.setContent(data.child("content").getValue(String.class));
                    post.setWriter(data.child("writer").getValue(String.class));

                    ArrayList<String> members = new ArrayList<>();
                    for(DataSnapshot name : dd.getChildren()){
                        members.add(name.getValue(String.class));
                    }

                    post.setLikeMembers(members);

                    postAdapter.addPost(key, postKey, post, user);
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
