package com.ditkevinstreet.createaccountscreen;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.UUID;


/**
 * Created by Admin on 05/01/2018.
 */



public class ReminderDetail extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener mAuthListener;


    private TextView TitleField, DateField, TimeField, DescriptionField, reminderDetailAlarmField;
    private Button btnBackToCalendar, btnSetAlarm, btnCancelAlarm;
    private String time, minuteString, hourString, date, dayString, displayMonthString,
            creatorUserId, userId, title, description, reminderId;
    private int minute, hour, day, month, displayMonth, year;
    private boolean creatorWantsNotification;
    private AlarmManager alarmManager;
    private Calendar calendarForAlarm;

    Intent alarmIntent;
    private PendingIntent alarmPendingIntent;
    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_detail);

        //firebase stuff
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        calendarForAlarm = Calendar.getInstance();
        this.mContext = this;
        alarmIntent = new Intent(this.mContext, AlarmReceiver.class);



        btnBackToCalendar = (Button) findViewById(R.id.btnBackToCalendar);
        btnSetAlarm = (Button) findViewById(R.id.btnSetAlarm);
        btnCancelAlarm = (Button) findViewById(R.id.btnCancelAlarm);
        reminderDetailAlarmField = (TextView) findViewById(R.id.reminderDetailAlarmField);

        Bundle b = this.getIntent().getExtras();
        if(b!=null) {
            if(!b.containsKey("REMINDERID")){
//            if(getIntent().getStringExtra("REMINDERID").equals(null)){

                reminderId = getIntent().getStringExtra("ID");

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
                displayMonth = month + 1;//months in the database start from 0, January = 0, so whenever a month is being pulled from the databse and displayed, it needs to be converted to the gregorian format by adding 1
                displayMonthString = Integer.toString(displayMonth);
                if(displayMonthString.length()==1){
                    displayMonthString = "0" + displayMonthString;
                }

                year = getIntent().getIntExtra("YEAR", 0);
                creatorUserId = getIntent().getStringExtra("CREATORUSERID");
                title = getIntent().getStringExtra("TITLE");
                description = getIntent().getStringExtra("DESCRIPTION");
                creatorWantsNotification = getIntent().getBooleanExtra("CREATORWANTSNOTIFICATION", false);

                time = hourString + ":" + minuteString;
                date = dayString + "/" + displayMonthString + "/" + year;
                TitleField = (TextView) findViewById(R.id.reminderDetailTitleField);
                TitleField.setText(title);
                DateField = (TextView) findViewById(R.id.reminderDetailDateField);
                DateField.setText(date);
                TimeField = (TextView) findViewById(R.id.reminderDetailTimeField);
                TimeField.setText(time);
                DescriptionField = (TextView) findViewById(R.id.reminderDetailDescriptionField);
                DescriptionField.setText(description);
            }else{
                reminderId = getIntent().getStringExtra("REMINDERID");
                Query query = myRef.child("reminders").child(userId).orderByKey().equalTo(reminderId);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String title = (String)dataSnapshot.child(reminderId).child("title").getValue();
                        String description = (String)dataSnapshot.child(reminderId).child("description").getValue();
                        Long dayLong = (Long)dataSnapshot.child(reminderId).child("day").getValue();
                        day = Long.valueOf(dayLong).intValue();
                        String dayString = Integer.toString(day);
                        if(dayString.length()==1){
                            dayString = "0" + dayString;
                        }
                        Long monthLong = (Long)dataSnapshot.child(reminderId).child("month").getValue();
                        month = Long.valueOf(monthLong).intValue();
                        int displayMonth = month + 1;
                        String displayMonthString = Integer.toString(displayMonth);
                        if(displayMonthString.length()==1){
                            displayMonthString = "0" + displayMonthString;
                        }
                        Long yearLong = (Long)dataSnapshot.child(reminderId).child("year").getValue();
                        year = Long.valueOf(yearLong).intValue();
                        String yearString = Integer.toString(year);
                        if(yearString.length()==1){
                            yearString = "0" + yearString;
                        }
                        Long hourLong = (Long)dataSnapshot.child(reminderId).child("hour").getValue();
                        hour = Long.valueOf(hourLong).intValue();
                        String hourString = Integer.toString(hour);
                        if(hourString.length()==1){
                            hourString = "0" + hourString;
                        }
                        Long minuteLong = (Long)dataSnapshot.child(reminderId).child("minute").getValue();
                        minute = Long.valueOf(minuteLong).intValue();
                        String minuteString = Integer.toString(minute);
                        if(minuteString.length()==1){
                            minuteString = "0" + minuteString;
                        }
                        time = hourString + ":" + minuteString;
                        date = dayString + "/" + displayMonthString + "/" + year;

                        time = hourString + ":" + minuteString;
                        date = dayString + "/" + displayMonthString + "/" + year;
                        TitleField = (TextView) findViewById(R.id.reminderDetailTitleField);
                        TitleField.setText(title);
                        DateField = (TextView) findViewById(R.id.reminderDetailDateField);
                        DateField.setText(date);
                        TimeField = (TextView) findViewById(R.id.reminderDetailTimeField);
                        TimeField.setText(time);
                        DescriptionField = (TextView) findViewById(R.id.reminderDetailDescriptionField);
                        DescriptionField.setText(description);
                        creatorUserId = (String)dataSnapshot.child(reminderId).child("creatorUserId").getValue();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("ReminderDetail", "onCancelled");
                    }
                });
            }


        }else{
            toastMessage("Reminder data not found");
        }

        btnSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Clicked on setAlarm", "not in Receiver yet" );
            //create intent to alarm receiver class
//                alarmIntent.putExtra("REMINDERID", reminderId);
                alarmIntent.putExtra("TITLE", title);
                alarmIntent.putExtra("DESCRIPTION", description);
                alarmIntent.putExtra("DAY", day);
                alarmIntent.putExtra("MONTH", month);
                alarmIntent.putExtra("YEAR", year);
                alarmIntent.putExtra("HOUR", hour);
                alarmIntent.putExtra("MINUTE", minute);
                alarmIntent.putExtra("CREATORUSERID", creatorUserId);
                alarmIntent.putExtra("CREATORWANTSNOTIFICATION", creatorWantsNotification);

                //put in extra string into alarmIntent, telling clock whether alarm is being set or silenced
                alarmIntent.putExtra("extra", "alarm on");


                alarmPendingIntent = PendingIntent.getBroadcast(ReminderDetail.this,
                        1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT );

                //set up calendar for alarmManager
                calendarForAlarm.set(year, month, day, hour, minute);

                //set up alarm manager
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendarForAlarm.getTimeInMillis(), alarmPendingIntent);
                reminderDetailAlarmField.setText("SET");
                reminderDetailAlarmField.setTextColor(Color.GREEN);
                toastMessage("Alarm set");

            }
        });

        btnCancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                alarmManager.cancel(alarmPendingIntent);

                //put extra string into alarmIntent to tell the clock we pressed cancel alarm button
                alarmIntent.putExtra("extra", "alarm off");

                sendBroadcast(alarmIntent);

                reminderDetailAlarmField.setText("NOT SET");
                reminderDetailAlarmField.setTextColor(Color.RED);
                toastMessage("Alarm cancelled");
            }
        });

        btnBackToCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReminderDetail.this, CalendarView.class);
                startActivity(intent);
            }
        });


    }
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
