package com.example.juhyun.haerak2;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AddBucketActivity extends AppCompatActivity {

    private ImageButton gocalButton, category;
    private TextView date_view;
    private Button cancel, save;
    private DatabaseReference databaseReference;
    private EditText title, limitNumber, content;
    private User user;
    private int REQUEST_CATEGORY, REQUEST_DATE;
    private String bucket_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bucket);

        user = (User) getIntent().getSerializableExtra("user");

        date_view = (TextView) findViewById(R.id.dateText);
        gocalButton = (ImageButton) findViewById(R.id.gocalButton);

        title = (EditText)findViewById(R.id.titleText);
        limitNumber = (EditText)findViewById(R.id.limitnumberText);
        content = (EditText)findViewById(R.id.contentText);

        category = (ImageButton)findViewById(R.id.categoryButton);
        cancel = (Button) findViewById(R.id.addCancelButton);
        save = (Button) findViewById(R.id.addbucketButton);
        bucket_category = "do";

        REQUEST_CATEGORY = 1;
        REQUEST_DATE = 2;

        //날짜 선택
        gocalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddBucketActivity.this, CalendarActivity.class);
                startActivityForResult(intent, REQUEST_DATE);
            }
        });

        //카테고리 선택
        ImageButton add_button = (ImageButton) findViewById(R.id.categoryButton);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddBucketActivity.this, SelectCategoryPop.class);
                startActivityForResult(intent, REQUEST_CATEGORY);
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(title.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "버킷의 제목을 입력하세요", Toast.LENGTH_LONG).show();
                }else if(content.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "버킷의 내용을 입력하세요", Toast.LENGTH_LONG).show();
                }else{
                    addBucket();

                    Intent intent = new Intent(AddBucketActivity.this, MainActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    finish();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void addBucket(){
        Bucket bucket = new Bucket();

        bucket.setTitle(title.getText().toString());
        bucket.setWriter(user.getNickName());
        bucket.setDate(date_view.getText().toString());
        bucket.setContent(content.getText().toString());
        bucket.setLimitNumber(Integer.parseInt(limitNumber.getText().toString()));
        bucket.setCategory(bucket_category);

        String bucket_key = databaseReference.child("Buckets").push().getKey();
        databaseReference.child("Buckets").child(bucket_key).setValue(bucket);

        databaseReference.child("Bucket-members").child(bucket_key).child("0").setValue(user.getNickName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CATEGORY){
            if(resultCode == RESULT_OK){
                String bucketCate = data.getStringExtra("category");

                switch (bucketCate){
                    case "1":
                        category.setImageResource(R.drawable.icon_do);
                        bucket_category = "do";
                        break;
                    case "2":
                        category.setImageResource(R.drawable.icon_eat);
                        bucket_category = "eat";
                        break;
                    case "3":
                        category.setImageResource(R.drawable.icon_watch);
                        bucket_category = "watch";
                        break;
                    case "4":
                        category.setImageResource(R.drawable.icon_want);
                        bucket_category = "have";
                        break;
                    case "5":
                        category.setImageResource(R.drawable.icon_go);
                        bucket_category = "go";
                        break;
                    default:
                        category.setImageResource(R.drawable.icon_do);
                        bucket_category = "do";
                        break;
                }

            }else{
                Toast.makeText(getApplicationContext(), "카테고리를 다시 선택해주세요.", Toast.LENGTH_LONG).show();
            }

        }else if(requestCode == REQUEST_DATE){
            if(resultCode == RESULT_OK){
                String date = data.getStringExtra("date");
                date_view.setText(date);

            }else if(resultCode == RESULT_CANCELED){
                Toast.makeText(getApplicationContext(), "날짜를 다시 선택해주세요.", Toast.LENGTH_LONG).show();
            }
        }


    }
}
