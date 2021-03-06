package com.example.juhyun.haerak2;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BucketDetailActivity extends AppCompatActivity {

    private DatabaseReference dataBase , dataBase2, dataBase3, dataBase4;
    private TextView nickName, title, date, content, currNum, limitNum;
    private String key, user, category, photoUrl;
    private Button join, addComment, makeGroup;
    private long memberNum;
    private EditText comments;
    private ListView commentList;
    private BucketCommentAdapter adapter;
    private ArrayList<String> members;
    private ImageButton location;


    private ValueEventListener getBucketCurrMembers = new ValueEventListener(){

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
           memberNum = dataSnapshot.getChildrenCount();
           currNum.setText(memberNum+"");

           for(DataSnapshot data : dataSnapshot.getChildren()){
               String member = data.getValue(String.class);
               members.add(member);

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
        makeGroup = (Button) findViewById(R.id.makeGroupButton);
        location = (ImageButton) findViewById(R.id.bucket_location);

        commentList = (ListView)findViewById(R.id.bucket_comments);
        members = new ArrayList<>();

        user = getIntent().getStringExtra("user");

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BucketDetailActivity.this, MapActivity.class);
                intent.putExtra("location", getIntent().getStringExtra("location"));
                intent.putExtra("latitude", getIntent().getDoubleExtra("latitude", 0.0));
                intent.putExtra("longitude", getIntent().getDoubleExtra("longitude", 0.0));
                startActivity(intent);
            }
        });

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
                category = bucket.getCategory();
                photoUrl = bucket.getPhotoUrl();

                if(bucket.getWriter().equals(user)){
                    makeGroup.setVisibility(View.VISIBLE);
                    makeGroup.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                    join.setVisibility(View.INVISIBLE);
                    join.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0));

                    if(bucket.getIsGroup().equals("true"))
                    {
                        makeGroup.setText("그룹확정");
                        makeGroup.setClickable(false);
                    }
                }else{
                    makeGroup.setVisibility(View.INVISIBLE);
                    join.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dataBase2 = FirebaseDatabase.getInstance().getReference("Bucket-members").child(key);
        dataBase2.addListenerForSingleValueEvent(getBucketCurrMembers);

        //참여하기 버튼 클릭 리스너
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //인원이 다 찼을 경우,
                if(user.equals("")){
                    Toast.makeText(getApplicationContext(), "로그인 후 이용가능합니다!", Toast.LENGTH_LONG).show();
                }else if(currNum.getText().toString().equals(limitNum.getText().toString())){
                    Toast.makeText(getApplicationContext(), "인원초과로 참여할 수 없습니다.", Toast.LENGTH_LONG).show();
                }else{
                    dataBase2.child(memberNum+"").setValue(user);
                    dataBase2.addListenerForSingleValueEvent(getBucketCurrMembers);
                }

            }
        });

        dataBase4 = FirebaseDatabase.getInstance().getReference();

        //그룹생성 버튼 클릭 리스너
        makeGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder confirm = new AlertDialog.Builder(BucketDetailActivity.this);
                confirm.setMessage("현재 인원으로 그룹을 생성하시겠습니까?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //그룹 생성 Yes
                        BucketGroup group = new BucketGroup();
                        group.setBucketId(key);
                        group.setLeader(user);
                        group.setTitle(title.getText().toString());
                        group.setContent(content.getText().toString());
                        group.setCategory(category);
                        group.setProgressRate(10);
                        group.setLimitNumber(Integer.parseInt(limitNum.getText().toString()));
                        group.setMembers(members);
                        group.setPhotoUrl(photoUrl);

                        String group_key = dataBase4.child("BucketGroups").push().getKey();
                        dataBase4.child("BucketGroups").child(group_key).setValue(group);

                        makeGroup.setText("그룹확정");
                        makeGroup.setClickable(false);
                        dataBase.child("isGroup").setValue("true");
                        
                        Toast.makeText(getApplicationContext(), "그룹 생성!", Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //그룹생성 No
                        return;
                    }
                });
                AlertDialog alert = confirm.create();
                alert.show();
            }
        });


        //버킷 댓글 로드
        dataBase3 = FirebaseDatabase.getInstance().getReference("BucketComments").child(key);
        dataBase3.addListenerForSingleValueEvent(getBucketComments);

        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.equals("")){
                    Toast.makeText(getApplicationContext(), "로그인 후 이용가능합니다!", Toast.LENGTH_LONG).show();
                    comments.setText("");
                }else{
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

                    comments.setText("");
                }
            }
        });

    }
}
