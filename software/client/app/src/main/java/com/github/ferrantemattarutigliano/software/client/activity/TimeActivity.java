package com.github.ferrantemattarutigliano.software.client.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import com.github.ferrantemattarutigliano.software.client.R;

import java.sql.Time;
import java.util.Calendar;

public class TimeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        final TimePicker timePicker = findViewById(R.id.time_picker);
        Button acceptButton = findViewById(R.id.button_time_picker_accept);
        Button backButton = findViewById(R.id.button_time_picker_back);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Time time = getShowedTime(timePicker);
                Intent intent = new Intent();
                intent.putExtra("time", time);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private Time getShowedTime(TimePicker timePicker){
        Calendar calendar = Calendar.getInstance();
        if(Build.VERSION.SDK_INT < 23){
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
            calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
        } else{
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
            calendar.set(Calendar.MINUTE, timePicker.getMinute());
        }
        return new Time(calendar.getTimeInMillis());
    }
}
