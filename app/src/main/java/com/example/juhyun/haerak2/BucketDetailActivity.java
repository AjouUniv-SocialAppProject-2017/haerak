package com.example.juhyun.haerak2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BucketDetailActivity extends AppCompatActivity {

    private DatabaseReference dataBase;
    private TextView nickName, title, date, content, limitNum;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket_detail);

        key = getIntent().getStringExtra("key");
        nickName = (TextView)findViewById(R.id.bucket_head);
        title = (TextView)findViewById(R.id.bucket_title);
        date = (TextView)findViewById(R.id.bucket_date);
        content = (TextView)findViewById(R.id.bucket_content);
        limitNum = (TextView)findViewById(R.id.bucket_totalnumber);

        dataBase = FirebaseDatabase.getInstance().getReference("Buckets").child(key);
        dataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Bucket bucket = dataSnapshot.getValue(Bucket.class);
                nickName.setText(bucket.getWriter());
                title.setText(bucket.getTitle());
                date.setText(bucket.getDate());
                content.setText(bucket.getContent());
                limitNum.setText(bucket.getLimitNumber()+"");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
