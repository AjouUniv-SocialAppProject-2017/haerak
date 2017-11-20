package com.example.juhyun.haerak2;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AddBucketActivity extends AppCompatActivity {

    ImageButton gocalButton;
    TextView date_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bucket);

        date_view = (TextView) findViewById(R.id.dateText);
        gocalButton = (ImageButton) findViewById(R.id.gocalButton);

        Intent incomingintent = getIntent();
        String date = incomingintent.getStringExtra("date");
        date_view.setText(date);

        gocalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddBucketActivity.this, CalendarActivity.class);
                startActivity(intent);

            }
        });

        ImageButton add_button = (ImageButton) findViewById(R.id.addbucketButton);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddBucketActivity.this, SelectCategoryPop.class));
            }
        });
    }

}
