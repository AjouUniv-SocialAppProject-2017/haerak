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

public class AddBucketActivity extends AppCompatActivity {

    private ImageButton gocalButton, category;
    private TextView date_view;
    private Button cancel, save;
    private DatabaseReference databaseReference;
    private EditText title, limitNumber, content;
    private User user;

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

        Intent incomingintent = getIntent();
        final String date = incomingintent.getStringExtra("date");
        date_view.setText(date);

        gocalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddBucketActivity.this, CalendarActivity.class);
                startActivity(intent);

            }
        });

        ImageButton add_button = (ImageButton) findViewById(R.id.categoryButton);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddBucketActivity.this, SelectCategoryPop.class));
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
                }
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
        bucket.setCategory("do");

        databaseReference.child("Buckets").push().setValue(bucket);

    }

}
