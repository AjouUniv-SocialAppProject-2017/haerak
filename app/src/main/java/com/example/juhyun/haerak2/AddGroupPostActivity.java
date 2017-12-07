package com.example.juhyun.haerak2;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddGroupPostActivity extends AppCompatActivity {

    private String groupKey, user;
    private EditText title, content;
    private Button save, cancel;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group_post);

        title = (EditText) findViewById(R.id.post_title);
        content = (EditText)findViewById(R.id.post_content);
        save = (Button)findViewById(R.id.btn_post_save);
        cancel = (Button)findViewById(R.id.btn_post_cancel);

        user = getIntent().getStringExtra("user");
        groupKey = getIntent().getStringExtra("groupKey");

        database = FirebaseDatabase.getInstance().getReference();

        //게시물 저장
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(title.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "제목을 입력해주세요!", Toast.LENGTH_LONG).show();
                }else if(content.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "내용을 입력해주세요!", Toast.LENGTH_LONG).show();
                }else{
                    GroupPost post = new GroupPost();
                    post.setTitle(title.getText().toString());
                    post.setContent(content.getText().toString());
                    post.setWriter(user);

                    String key = database.child("GroupPosts").child(groupKey).push().getKey();
                    database.child("GroupPosts").child(groupKey).child(key).setValue(post);
                    Toast.makeText(getApplicationContext(), "게시물 등록!", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(AddGroupPostActivity.this, RealBucketActivity.class);
                    intent.putExtra("user", user);
                    intent.putExtra("key", groupKey);
                    startActivity(intent);
                    finish();
                }
            }
        });

        //게시물 작성 취소
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                AlertDialog.Builder confirm = new AlertDialog.Builder(AddGroupPostActivity.this);
                confirm.setMessage("게시물 작성을 취소하시겠습니까?").setCancelable(false).setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       onBackPressed();
                    }
                }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
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

    }
}
