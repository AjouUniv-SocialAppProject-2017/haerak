package com.example.juhyun.haerak2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText idText = (EditText) findViewById(R.id.idText);
        EditText nicknameText = (EditText) findViewById(R.id.nicknameText);
        EditText passwordText = (EditText) findViewById(R.id.passwordText);
        //비밀번호확인 부분

        Button registerButton = (Button) findViewById(R.id.registerButton);

    }
}
