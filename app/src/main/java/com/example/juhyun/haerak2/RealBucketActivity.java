package com.example.juhyun.haerak2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.util.List;

public class RealBucketActivity extends AppCompatActivity {

    private String key, user, bucketKey;
    private DatabaseReference database;
    private GroupMemberAdapter adapter;
    private TextView title, content;
    private ListView memberList;
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

        key = getIntent().getStringExtra("key");
        user = getIntent().getStringExtra("user");
        title = (TextView) findViewById(R.id.infotab_buckettitle);
        content = (TextView)findViewById(R.id.infotab_bucketcontent);
        categoryImage = (ImageView)findViewById(R.id.infotab_categoryimageview);
        memberList = (ListView) findViewById(R.id.infotab_memberlistview);

        adapter = new GroupMemberAdapter();
        memberList.setAdapter(adapter);

        database = FirebaseDatabase.getInstance().getReference("BucketGroups").child(key);
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                BucketGroup group = dataSnapshot.getValue(BucketGroup.class);
                ArrayList<String> members = group.getMembers();

                bucketKey = group.getBucketId();
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

    }
}
