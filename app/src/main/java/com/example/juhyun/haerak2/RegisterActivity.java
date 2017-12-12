package com.example.juhyun.haerak2;

import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.EventListener;
import java.util.Iterator;

public class RegisterActivity extends AppCompatActivity{

    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private EditText userId, nickName, password, password2;
    private Button btnRegister;
    private RadioButton female, male;

    private ValueEventListener checkRegister = new ValueEventListener(){

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                String key = snapshot.getKey();
                User user = snapshot.getValue(User.class);

                if(nickName.getText().toString().equals(user.getNickName())){
                    Toast.makeText(getApplicationContext(), "이미 존재하는 닉네임 입니다.", Toast.LENGTH_LONG).show();
                    mDatabase.removeEventListener(this);
                    return;
                }
            }

            signUp();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mDatabase = FirebaseDatabase.getInstance().getReference("User");

        userId = (EditText) findViewById(R.id.idText);
        nickName = (EditText) findViewById(R.id.nicknameText);
        password = (EditText) findViewById(R.id.passwordText);
        password2 = (EditText) findViewById(R.id.password2Text);

        btnRegister = (Button) findViewById(R.id.registerButton);
        female = (RadioButton) findViewById(R.id.genderWoman);
        male = (RadioButton)findViewById(R.id.genderMan);

        auth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userId.getText().toString().equals("") || nickName.getText().toString().equals("") || password.getText().toString().equals("") ){
                    Toast.makeText(getApplicationContext(), "각 항목을 빠짐없이 기입해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }else if(!(password.getText().toString()).equals(password2.getText().toString())){
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                }else{
                    mDatabase.addListenerForSingleValueEvent(checkRegister);
                }

            }
        });

    }

    private void signUp(){
        auth.createUserWithEmailAndPassword(userId.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String gender = "F";

                            if(female.isChecked()){
                                gender = "F";
                            }else if(male.isChecked()){
                                gender = "M";
                            }
                            FirebaseUser user = task.getResult().getUser();
                            String key = user.getUid();

                            User userModel = new User();
                            userModel.setUserId(user.getEmail());
                            userModel.setNickName(nickName.getText().toString());
                            userModel.setPassWord(password.getText().toString());
                            userModel.setGender(gender);

                            mDatabase.child(key).setValue(userModel);

                            Toast.makeText(getApplicationContext(), "가입 성공!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);

                        }else{
                            Toast.makeText(getApplicationContext(), "가입 실패", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

}
