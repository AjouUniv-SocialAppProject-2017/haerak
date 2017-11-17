package com.example.juhyun.haerak2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CalendarView;

/**
 * Created by juhyun on 2017. 11. 16..
 */

public class CalendarActivity extends AppCompatActivity {

    private static final String TAG = "CalendarActivity";

    private CalendarView mCalendarView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = year + "년" + (month+1) + "월" + dayOfMonth + "일";
                Log.d(TAG, "onSelectedDayChange: date" + date);

                Intent intent = new Intent(CalendarActivity.this, AddBucketActivity.class);
                intent.putExtra("date",  date);
                startActivity(intent);
            }
        });

    }
}
