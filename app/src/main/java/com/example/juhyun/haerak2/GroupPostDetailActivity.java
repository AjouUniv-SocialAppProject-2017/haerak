package com.example.juhyun.haerak2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GroupPostDetailActivity extends AppCompatActivity {

    private BucketCommentAdapter adapter;
    private DatabaseReference database, database2;
    private TextView title, writer, content, likeNum, commentNum;
    private EditText commentText;
    private ListView commentList;
    private Button btn_addComment;
    private ImageView likePost;
    private String user, key, groupKey;
    private GroupPost post;
    private long num;

    private ValueEventListener getPostComments = new ValueEventListener(){

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            adapter = new BucketCommentAdapter();
            commentList.setAdapter(adapter);

            long comments = dataSnapshot.getChildrenCount();
            commentNum.setText(comments+"");

            for(DataSnapshot data : dataSnapshot.getChildren()){
                BucketComment comment = data.getValue(BucketComment.class);
                adapter.addComment(comment);
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private ValueEventListener getLikeNum = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            GroupPost groupPost = dataSnapshot.getValue(GroupPost.class);
            ArrayList<String> members = groupPost.getLikeMembers();

            if(groupPost.getLikeMembers() == null){
                num = 0;
            }else {
                num = groupPost.getLikeMembers().size();

                for(int i=0; i<members.size(); i++){
                    if(members.get(i).equals(user)){
                        likePost.setImageResource(R.drawable.ic_favorite_black_24dp);
                        likePost.setTag("like");
                    }
                }

            }
            likeNum.setText(num+"");
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_post_detail);

        post = (GroupPost)getIntent().getSerializableExtra("post");
        user = getIntent().getStringExtra("user");
        key = getIntent().getStringExtra("postKey");
        groupKey = getIntent().getStringExtra("groupKey");

        title = (TextView) findViewById(R.id.groupPostTitle);
        writer = (TextView)findViewById(R.id.groupPostWriter);
        content = (TextView) findViewById(R.id.groupPostContent);
        likeNum = (TextView) findViewById(R.id.postLikeNumber);
        commentNum = (TextView)findViewById(R.id.postCommentNum);
        commentText = (EditText) findViewById(R.id.addcommentText);
        commentList = (ListView)findViewById(R.id.groupPostComment);
        btn_addComment = (Button)findViewById(R.id.addcommentButton);
        likePost = (ImageView)findViewById(R.id.btnLikePost);

        title.setText(post.getTitle());
        writer.setText(post.getWriter());
        content.setText(post.getContent());

//        if(post.getLikeMembers() != null){
//            for (int i=0; i<post.getLikeMembers().size(); i++){
//                if(post.getLikeMembers().get(i).equals(user)){
//                    likePost.setImageResource(R.drawable.ic_favorite_black_24dp);
//                    likePost.setTag("like");
//                }
//            }
//        }


        database2 = FirebaseDatabase.getInstance().getReference("GroupPosts").child(groupKey).child(key);
        database2.addListenerForSingleValueEvent(getLikeNum);

        likePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(likePost.getTag().equals("notLike")){
                    likePost.setImageResource(R.drawable.ic_favorite_black_24dp);
                    likePost.setTag("like");

                    database2.child("likeMembers").child(num+"").setValue(user);
                    database2.addListenerForSingleValueEvent(getLikeNum);
                }else{
                    return;
                }
            }
        });

        database = FirebaseDatabase.getInstance().getReference("GroupPostComments").child(key);
        database.addListenerForSingleValueEvent(getPostComments);

        btn_addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BucketComment comment = new BucketComment();
                comment.setWriter(user);
                comment.setContent(commentText.getText().toString());

                long time = System.currentTimeMillis();
                Date date = new Date(time);
                SimpleDateFormat format = new SimpleDateFormat("yyyy/mm/dd hh:mm:ss");
                String dateFormat = format.format(date);

                comment.setDate(dateFormat);

                database.push().setValue(comment);
                database.addListenerForSingleValueEvent(getPostComments);

                commentText.setText("");
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
