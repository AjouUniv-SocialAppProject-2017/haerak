package com.example.juhyun.haerak2;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddBucketActivity extends AppCompatActivity {

    private ImageButton gocalButton, category, bucketImageButton;
    private TextView date_view, imageName;
    private Button cancel, save;
    private DatabaseReference databaseReference;
    private StorageReference storageRef;
    private EditText title, limitNumber, content, place;
    private User user;
    private Uri uri;
    private String bucket_category;
    private double latitude, longitude;
    private boolean isLocationTrue = true;
    private static final int REQUEST_CATEGORY = 1;
    private static final int REQUEST_DATE = 2;
    private static final int REQUEST_GALLERY = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bucket);

        user = (User) getIntent().getSerializableExtra("user");

        date_view = (TextView) findViewById(R.id.dateText);
        gocalButton = (ImageButton) findViewById(R.id.gocalButton);
        place = (EditText) findViewById(R.id.placeText);

        title = (EditText)findViewById(R.id.titleText);
        limitNumber = (EditText)findViewById(R.id.limitnumberText);
        content = (EditText)findViewById(R.id.contentText);
        imageName = (TextView)findViewById(R.id.imageName);
        category = (ImageButton)findViewById(R.id.categoryButton);
        bucketImageButton = (ImageButton)findViewById(R.id.bucket_image);
        cancel = (Button) findViewById(R.id.addCancelButton);
        save = (Button) findViewById(R.id.addbucketButton);
        bucket_category = "do";

        storageRef = FirebaseStorage.getInstance().getReference();

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

        bucketImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_GALLERY);
            }
        });


        databaseReference = FirebaseDatabase.getInstance().getReference();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePlaceLanLon();

                if(title.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "버킷의 제목을 입력하세요", Toast.LENGTH_LONG).show();
                }else if(content.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "버킷의 내용을 입력하세요", Toast.LENGTH_LONG).show();
                }else if(!isLocationTrue){
                    Toast.makeText(getApplicationContext(), "주소를 정확히 입력해주세요", Toast.LENGTH_LONG).show();
                }else{

                    addBucket();
                    uploadFile();

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
        bucket.setLocation(place.getText().toString());
        bucket.setLatitude(latitude);
        bucket.setLongitude(longitude);

        String bucket_key = databaseReference.child("Buckets").push().getKey();
        databaseReference.child("Buckets").child(bucket_key).setValue(bucket);

        databaseReference.child("Bucket-members").child(bucket_key).child("0").setValue(user.getNickName());

    }

    public void uploadFile()
    {
        StorageReference filePath = storageRef.child("Photos").child(uri.getLastPathSegment());
        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String photoUrl = taskSnapshot.getDownloadUrl().toString();
                Toast.makeText(getApplicationContext(), photoUrl, Toast.LENGTH_LONG).show();
            }
        });
    }

    //주소를 경도, 위도로 변환
    public void changePlaceLanLon(){
        Geocoder geocoder = new Geocoder(this);
        List<Address> list = null;

        String str = place.getText().toString();
        try {
            list = geocoder.getFromLocationName(
                    str, // 지역 이름
                    10); // 읽을 개수
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
        }

        if (list != null) {
            if (list.size() == 0) {
                isLocationTrue = false;
                //Toast.makeText(getApplicationContext(), "해당되는 주소가 없습니다.", Toast.LENGTH_LONG).show();
            } else {
                isLocationTrue = true;
                latitude = list.get(0).getLatitude();
                longitude = list.get(0).getLongitude();
            }
        }

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
        }else if(requestCode == REQUEST_GALLERY && resultCode == RESULT_OK){
            uri = data.getData();
            imageName.setText(uri.getEncodedPath());
        }

    }
}
