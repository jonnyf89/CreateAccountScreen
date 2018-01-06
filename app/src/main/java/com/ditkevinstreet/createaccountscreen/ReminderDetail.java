package com.ditkevinstreet.createaccountscreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.UUID;


/**
 * Created by Admin on 05/01/2018.
 */



public class ReminderDetail extends AppCompatActivity {
    private TextView TitleField, DateField, TimeField, DescriptionField;
    private String time;
    private int minute;
    private String minuteString;
    private int hour;
    private String hourString;
    private String date;
    private int day;
    private String dayString;
    private int month;
    private String monthString;
    private int year;
    private String creatorUserId;
    private String title;
    private String description;
    private boolean creatorWantsNotification;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_detail);

        Bundle b = this.getIntent().getExtras();
        if(b!=null) {
            String reminderId = getIntent().getStringExtra("ID");

            minute = getIntent().getIntExtra("MINUTE", 61);
            minuteString = Integer.toString(minute);
            if(minuteString.length()==1){
                minuteString = "0" + minuteString;
            }

            hour = getIntent().getIntExtra("HOUR", 26);
            hourString = Integer.toString(hour);
            if(hourString.length()==1){
                hourString = "0" + hourString;
            }

            day = getIntent().getIntExtra("DAY", 32);
            dayString = Integer.toString(day);
            if(dayString.length()==1){
                dayString = "0" + dayString;
            }

            month = getIntent().getIntExtra("MONTH", 13);
            monthString = Integer.toString(month);
            if(monthString.length()==1){
                monthString = "0" + monthString;
            }

            year = getIntent().getIntExtra("YEAR", 0);
            creatorUserId = getIntent().getStringExtra("CREATORUSERID");
            title = getIntent().getStringExtra("TITLE");
            description = getIntent().getStringExtra("DESCRIPTION");
            creatorWantsNotification = getIntent().getBooleanExtra("CREATORWANTSUSERID", false);


            time = hourString + ":" + minuteString;
            date = dayString + "/" + monthString + "/" + year;



        }
        TitleField = (TextView) findViewById(R.id.reminderDetailTitleField);
        TitleField.setText(title);
        DateField = (TextView) findViewById(R.id.reminderDetailDateField);
        DateField.setText(date);
        TimeField = (TextView) findViewById(R.id.reminderDetailTimeField);
        TimeField.setText(time);
        DescriptionField = (TextView) findViewById(R.id.reminderDetailDescriptionField);
        DescriptionField.setText(description);
    }
}
