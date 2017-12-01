package com.example.juhyun.haerak2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BucketDetailActivity extends AppCompatActivity {

    private DatabaseReference dataBase , dataBase2, dataBase3;
    private TextView nickName, title, date, content, currNum, limitNum;
    private String key, user;
    private Button join, addComment;
    private long memberNum;
    private EditText comments;
    private ListView commentList;
    private BucketCommentAdapter adapter;

    private ValueEventListener getBucketCurrMembers = new ValueEventListener(){

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
           memberNum = dataSnapshot.getChildrenCount();
           currNum.setText(memberNum+"");

           for(DataSnapshot data : dataSnapshot.getChildren()){
               String member = data.getValue(String.class);

               if(user.equals(member)){
                   join.setText("참여완료");
                   join.setClickable(false);
               }
           }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private ValueEventListener getBucketComments = new ValueEventListener(){

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            adapter = new BucketCommentAdapter();
            commentList.setAdapter(adapter);

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket_detail);

        key = getIntent().getStringExtra("key");
        nickName = (TextView)findViewById(R.id.bucket_head);
        title = (TextView)findViewById(R.id.bucket_title);
        date = (TextView)findViewById(R.id.bucket_date);
        content = (TextView)findViewById(R.id.bucket_content);
        currNum = (TextView)findViewById(R.id.bucket_presentnumber);
        limitNum = (TextView)findViewById(R.id.bucket_totalnumber);
        comments = (EditText)findViewById(R.id.addcommentText);

        join = (Button)findViewById(R.id.addbucketgroupButton);
        addComment = (Button)findViewById(R.id.addcommentButton);

        commentList = (ListView)findViewById(R.id.bucket_comments);

        user = getIntent().getStringExtra("user");

        //버킷 상세정보 로드
        dataBase = FirebaseDatabase.getInstance().getReference("Buckets").child(key);
        dataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Bucket bucket = dataSnapshot.getValue(Bucket.class);
                nickName.setText(bucket.getWriter());
                title.setText(bucket.getTitle());
                date.setText(bucket.getDate());
                content.setText(bucket.getContent());
                limitNum.setText(bucket.getLimitNumber() + "");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dataBase2 = FirebaseDatabase.getInstance().getReference("Bucket-members").child(key);
        dataBase2.addListenerForSingleValueEvent(getBucketCurrMembers);


        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //인원이 다 찼을 경우,
                if(currNum.getText().toString().equals(limitNum.getText().toString())){
                    Toast.makeText(getApplicationContext(), "인원초과로 참여할 수 없습니다.", Toast.LENGTH_LONG).show();
                }else{
                    dataBase2.child(memberNum+"").setValue(user);
                    dataBase2.addListenerForSingleValueEvent(getBucketCurrMembers);
                }

            }
        });


        //버킷 댓글 로드
        dataBase3 = FirebaseDatabase.getInstance().getReference("BucketComments").child(key);
        dataBase3.addListenerForSingleValueEvent(getBucketComments);

        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BucketComment comment = new BucketComment();
                comment.setWriter(user);
                comment.setContent(comments.getText().toString());

                long time = System.currentTimeMillis();
                Date date = new Date(time);
                SimpleDateFormat format = new SimpleDateFormat("yyyy/mm/dd hh:mm:ss");
                String dateFormat = format.format(date);

                comment.setDate(dateFormat);

                dataBase3.push().setValue(comment);
                dataBase3.addListenerForSingleValueEvent(getBucketComments);
            }
        });

    }
}
