package com.ditkevinstreet.createaccountscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Admin on 10/11/2017.
 */

public class CalendarView extends AppCompatActivity {
    private static final String TAG = "CalendarView";

    //declaring Firebase stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private String userId, realDateString, displayDateString;

    private android.widget.CalendarView mCalendarView;
    private Button btnSignOut, btnAddReminder, btnViewDay;
    private ListView ReminderListView;
    private ArrayList<ReminderListItemModel> reminderItems;

//    private Date date;
    private Calendar calDate;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_view);

//        date = new Date();
        calDate = Calendar.getInstance();
//        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

//        dateString = formatter.format(date);
        displayDateString = formatter.format(calDate.getTime());
        //dateString = date.toString();

        //firebase stuff
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();

        btnSignOut = (Button) findViewById(R.id.btnSignOut);
        btnAddReminder = (Button) findViewById(R.id.btnAddReminder);
        btnViewDay = (Button) findViewById(R.id.btnViewDay);
        mCalendarView = (android.widget.CalendarView) findViewById(R.id.calendarView);
        ReminderListView = (ListView) findViewById(R.id.ReminderListView);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth mAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    toastMessage("Successfully signed in with: " + user.getEmail() + " please provide the following details");
                    userId = user.getUid();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Not signed in. Please wait a few seconds");
                }

            }
        };

        mCalendarView.setOnDateChangeListener(new android.widget.CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull android.widget.CalendarView view, int year, int month, int dayOfMonth) {
                int displayMonth = month + 1;
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String dayString = Integer.toString(dayOfMonth);
                if(dayString.length()==1){
                    dayString = "0" + dayString;
                }
                String monthString = Integer.toString(month);
                if(monthString.length()==1){
                    monthString = "0" + monthString;
                }
                String displayMonthString = Integer.toString(displayMonth);
                if(displayMonthString.length()==1){
                    displayMonthString = "0" + displayMonthString;
                }

                realDateString = dayString + "/" + monthString + "/" + year;
                displayDateString = dayString + "/" + displayMonthString + "/" + year;
//                date = cal.getTime();
                calDate = cal;

                //Date date = mCalendarView.getDate();
            }
        });


        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent toWelcome = new Intent(CalendarView.this, WelcomeScreen.class);
                startActivity(toWelcome);
            }
        });

        btnAddReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarView.this, AddReminder.class);
                intent.putExtra("REALDATESTRING", realDateString);
                intent.putExtra("DISPLAYDATESTRING", displayDateString);
//                intent.putExtra("DATE", date);
                intent.putExtra("CALDATE", calDate);

                startActivity(intent);

            }
        });
        btnViewDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarView.this, DailyReminderList.class);
                intent.putExtra("REALDATESTRING", realDateString);
                intent.putExtra("DISPLAYDATESTRING", displayDateString);
                intent.putExtra("CALDATE", calDate);
                startActivity(intent);
            }
        });


//        Query query = myRef
//                .child("parents").child(userId).child("reminders").orderByChild("reminders");
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (!dataSnapshot.exists()) {
//                    Log.d(TAG, "onDataChange: Datasnapshot not found");
//                    toastMessage("Datasnapshot not found");
//                } else {
//                    DataSnapshot childrenSnapshot = dataSnapshot.child(userId).child("reminders");
//                    Iterable<DataSnapshot> childrenChildren = childrenSnapshot.getChildren();
//                    for (DataSnapshot child : childrenChildren) {
//                        Reminder r = child.getValue(Reminder.class);
//                        //String reminderTime = r.getTime();
//
//                    }
//                }
//                ReminderListAdapter adapter = new ReminderListAdapter(CalendarView.this, reminderItems);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
