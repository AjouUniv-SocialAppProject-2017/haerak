package com.example.juhyun.haerak2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private EditText userId, password;
    private Button loginButton;
    private TextView registerButton, browseButton;

    private ValueEventListener checkLogin = new ValueEventListener(){

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                User user = snapshot.getValue(User.class);

                if(user.getUserId().equals(userId.getText().toString())){
                    if(user.getPassWord().equals(password.getText().toString())){
                        login(user);
                        return;
                    }else{
                        Toast.makeText(getApplicationContext(), "패스워드가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                        databaseReference.removeEventListener(this);
                        return;
                    }

                }else{
                    Toast.makeText(getApplicationContext(), "아이디가 존재하지 않습니다.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userId = (EditText) findViewById(R.id.idText);
        password = (EditText) findViewById(R.id.passwordText);
        loginButton = (Button) findViewById(R.id.loginButton);
        registerButton = (TextView) findViewById(R.id.registerButton);
        browseButton = (TextView) findViewById(R.id.browseButton);

        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        browseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(mainIntent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                databaseReference.addListenerForSingleValueEvent(checkLogin);
            }
        });

    }

    public void login(User user){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }
}
