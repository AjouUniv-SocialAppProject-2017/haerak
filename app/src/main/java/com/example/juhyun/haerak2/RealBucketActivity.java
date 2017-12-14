package com.example.juhyun.haerak2;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

    private String key, user, leader;
    private DatabaseReference database, database2;
    private GroupMemberAdapter adapter;
    private GroupPostListAdapter postAdapter;
    private TextView title, content, progressRate;
    private ListView memberList, postList;
    private ImageView categoryImage, progressImage;

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
        progressRate = (TextView)findViewById(R.id.rate_progress);
        progressImage = (ImageView)findViewById(R.id.img_progress);

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
                leader = group.getLeader();

                for (String mem : members){
                    adapter.addMember(mem);
                    adapter.notifyDataSetChanged();
                }

                setProgressBar(group.getProgressRate());

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

        progressImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.equals(leader)) {
                    AlertDialog.Builder ad = new AlertDialog.Builder(RealBucketActivity.this);
                    ad.setTitle("달성률 설정");
                    ad.setMessage("버킷을 얼마나 이루었나요?");

                    final EditText et = new EditText(RealBucketActivity.this);
                    ad.setView(et);

                    ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int value = Integer.parseInt(et.getText().toString());

                            setProgressBar(value);
                            database.child("progressRate").setValue(value);

                            dialogInterface.dismiss();
                        }
                    });

                    ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    ad.show();
                }
            }
        });

    }

    public void setProgressBar(int rate)
    {
        switch (rate){
            case 0:
                progressImage.setImageResource(R.drawable.perc10);
                progressRate.setText("10%");
                break;
            case 10:
                progressImage.setImageResource(R.drawable.perc10);
                progressRate.setText("10%");
                break;
            case 20:
                progressImage.setImageResource(R.drawable.perc20);
                progressRate.setText("20%");
                break;
            case 30:
                progressImage.setImageResource(R.drawable.perc30);
                progressRate.setText("30%");
                break;
            case 40:
                progressImage.setImageResource(R.drawable.perc40);
                progressRate.setText("40%");
                break;
            case 50:
                progressImage.setImageResource(R.drawable.perc50);
                progressRate.setText("50%");
                break;
            case 60:
                progressImage.setImageResource(R.drawable.perc60);
                progressRate.setText("60%");
                break;
            case 70:
                progressImage.setImageResource(R.drawable.perc70);
                progressRate.setText("70%");
                break;
            case 80:
                progressImage.setImageResource(R.drawable.perc80);
                progressRate.setText("80%");
                break;
            case 90:
                progressImage.setImageResource(R.drawable.perc90);
                progressRate.setText("90%");
                break;
            case 100:
                progressImage.setImageResource(R.drawable.perc100);
                progressRate.setText("100%");
                break;
        }
    }

}
