package com.example.juhyun.haerak2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class RegisterActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private EditText userId, nickName, password, password2;
    private Button btnRegister;
    private RadioButton female, male;

    private ValueEventListener checkRegister = new ValueEventListener(){

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();

            while (child.hasNext()){
                if(userId.getText().toString().equals(child.next().getChildren())){
                    Toast.makeText(getApplicationContext(), "존재하는 아이디 입니다.", Toast.LENGTH_LONG).show();
                    mDatabase.removeEventListener(this);
                    return;
                }
            }
            makeNewUser();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        userId = (EditText) findViewById(R.id.idText);
        nickName = (EditText) findViewById(R.id.nicknameText);
        password = (EditText) findViewById(R.id.passwordText);
        password2 = (EditText) findViewById(R.id.password2Text);

        btnRegister = (Button) findViewById(R.id.registerButton);
        female = (RadioButton) findViewById(R.id.genderWoman);
        male = (RadioButton)findViewById(R.id.genderMan);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if(!password.equals(password2)){
//                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
//                }else{
                    mDatabase.addListenerForSingleValueEvent(checkRegister);
               // }

            }
        });

    }

    public void makeNewUser(){
        String gender = "F";

        if(female.isChecked()){
            gender = "F";
        }else if(male.isChecked()){
            gender = "M";
        }

        String key = mDatabase.child("User").push().getKey();

        mDatabase.child("User").child(key).child("id").setValue(userId.getText().toString());
        mDatabase.child("User").child(key).child("nick").setValue(nickName.getText().toString());
        mDatabase.child("User").child(key).child("passwd").setValue(password.getText().toString());
        mDatabase.child("User").child(key).child("gender").setValue(gender);

        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();

    }
}
