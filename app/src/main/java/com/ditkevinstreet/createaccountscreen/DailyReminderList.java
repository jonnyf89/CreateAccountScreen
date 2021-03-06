package com.ditkevinstreet.createaccountscreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.security.Key;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Queue;
import java.util.UUID;

/**
 * Created by Admin on 02/01/2018.
 */

public class DailyReminderList extends AppCompatActivity {
    private static final String TAG = "DailyReminderList";

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference myRef;
    private FirebaseUser user;
    private String userId;

    private TextView dateField;
    private String displayDateString, realDateString;
    private ListView reminderList;
    private Calendar calDate;

    private ArrayList<ReminderListItemModel> reminderListItems;
    private ArrayList<Reminder> reminderItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_reminder_list);

        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        user = firebaseAuth.getCurrentUser();
        userId = user.getUid();

        dateField = (TextView) findViewById(R.id.dateField);
        reminderList = (ListView) findViewById(R.id.daily_reminder_list_view);

        final Intent intent = getIntent();
        realDateString = intent.getStringExtra("REALDATESTRING");
        displayDateString = intent.getStringExtra("DISPLAYDATESTRING");
        calDate = (Calendar) intent.getSerializableExtra("CALDATE");

        dateField.setText(displayDateString);

        reminderListItems = new ArrayList<ReminderListItemModel>();
        reminderItems = new ArrayList<Reminder>();


//        //myRef.orderByChild("userId").equalTo(userId).addChildEventListener(new ChildEventListener() {
//        myRef.child("reminders").orderByKey().equalTo(userId).addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//                toastMessage(dataSnapshot.toString());
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                toastMessage(dataSnapshot.toString());
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                toastMessage(dataSnapshot.toString());
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                toastMessage(dataSnapshot.toString());
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });


        //Query query = myRef.child("parents").orderByChild("email").equalTo(parentsEmail);
//
//        Query query2 = myRef.child("parents").orderByChild("userId").equalTo("oFY3FpL8h6W8Cwu6BROeq6MZhEy2");
//        query2.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d(TAG, "onDataChange: ");
//                if (!dataSnapshot.exists()) {
//                    Log.d(TAG, "onDataChange: Datasnapshot not found");
//                    toastMessage("Child Datasnapshot not found");
//                } else {
//                    toastMessage(dataSnapshot.toString());
//                    toastMessage("Found");
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//        Query query3 = myRef.child("parents");
//        query3.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d(TAG, "onDataChange: ");
//                if (!dataSnapshot.exists()) {
//                    Log.d(TAG, "onDataChange: Datasnapshot not found");
//                    toastMessage("Parent Datasnapshot not found");
//                } else {//TODO if you find the datasnapshot, interate through all the parents, searching for the child with user id x
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

//        DataSnapshot remindersSnapshot = dataSnapshot.child(userId).child("reminders");
//                    Iterable<DataSnapshot> remindersChildren = remindersSnapshot.getChildren();

        Query query = myRef.child("reminders").orderByKey().equalTo(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                DataSnapshot remindersSnapshot = dataSnapshot.child("reminders").child(userId);
                Iterable<DataSnapshot> remindersChildren = dataSnapshot.getChildren();

                for (DataSnapshot dataChild : remindersChildren) {
                    String reminderId = dataChild.child(userId).getKey();
                    HashMap<String, Object> allReminders = (HashMap<String, Object>)dataChild.getValue();

                    for (Object value : allReminders.values()) {
                        HashMap<String, Object> singleReminder = (HashMap<String, Object>)value;
                        Log.d(TAG, "onDataChange: ");
                        Long rDayLong = (Long)singleReminder.get("day");
                        int rDay = Long.valueOf(rDayLong).intValue();

                        Long rMonthLong = (Long)singleReminder.get("month");
                        int rMonth = Long.valueOf(rMonthLong).intValue();

                        Long rYearLong = (Long)singleReminder.get("year");
                        int rYear = Long.valueOf(rYearLong).intValue();

                        String rDayString = Integer.toString(rDay);
                        if(rDayString.length()==1){
                            rDayString = "0" + rDayString;
                        }

                        String rMonthString = Integer.toString(rMonth);
                        if(rMonthString.length() == 1){
                            rMonthString = "0" + rMonthString;
                        }

                        String rYearString = Integer.toString(rYear);

                        String rDateString = rDayString + "/" + rMonthString
                                + "/" + rYearString;

                        if(rDateString.equals(realDateString)){
                            String rTitle = (String) singleReminder.get("title");
                            String rDescription = (String) singleReminder.get("description");
                            String rCreatorUserId = (String) singleReminder.get("creatorUserId");
                            boolean rCreatorWantsNotification = (boolean)singleReminder.get("creatorWantsNotification");
                            String rId = (String)singleReminder.get("id");


                            Long rHourLong = (Long)singleReminder.get("hour");
                            int rHour = Long.valueOf(rHourLong).intValue();
                            String rHourString = Integer.toString(rHour);
                            if(rHourString.length()==1){
                                rHourString = "0" + rHourString;
                            }

                            Long rMinuteLong = (Long)singleReminder.get("minute");
                            int rMinute = Long.valueOf(rMinuteLong).intValue();
                            String rMinuteString = Integer.toString(rMinute);
                            if(rMinuteString.length()==1){
                                rMinuteString = "0" + rMinuteString;
                            }

                            String rTime = rHourString + ":" + rMinuteString;

                            Reminder reminder = new Reminder();
                            reminder.setTitle(rTitle);
                            reminder.setDescription(rDescription);
                            reminder.setDay(rDay);
                            reminder.setMonth(rMonth);
                            reminder.setYear(rYear);
                            reminder.setHour(rHour);
                            reminder.setMinute(rMinute);
                            reminder.setCreatorWantsNotification(rCreatorWantsNotification);
                            reminder.setId(rId);


                            ReminderListItemModel reminderListItemModel = new ReminderListItemModel(rTime, rTitle);
                            reminderListItems.add(reminderListItemModel);
                            reminderItems.add(reminder);
                        }
                        Log.d(TAG, "onDataChange: ");

                        //TODO we can now access the titles of all reminders for this user.
                        //TODO the next step is access the dates, and for the reminders on the selected date,
                        //TODO display them in the listview, this shouldnt require much as we should have
                        //TODO most of the code below, it should be the same process for parents and children
                        //TODO after this I need to add the alarms and then we are basically done

                    }
                }
                final ReminderListAdapter adapter = new ReminderListAdapter(DailyReminderList.this,
                        reminderListItems, reminderItems);
                reminderList.setAdapter(adapter);
                reminderList.setLongClickable(true);
                reminderList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d(TAG, "onItemLongClick: ");
                        Vibrator vibrator = (Vibrator) DailyReminderList.this.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(200);
                        Reminder toShow = adapter.getReminder(position);
                        Intent intent = new Intent(DailyReminderList.this, ReminderDetail.class);
                        String Id = toShow.getId();
                        int minute = toShow.getMinute();
                        int hour = toShow.getHour();
                        int day = toShow.getDay();
                        int month = toShow.getMonth();
                        int year = toShow.getYear();
                        String creatorUserId = toShow.getCreatorUserId();
                        String title = toShow.getTitle();
                        String description = toShow.getDescription();
                        boolean creatorWantsNotification = toShow.getCreatorWantsNotification();

                        intent.putExtra("ID", Id);
                        intent.putExtra("MINUTE", minute);
                        intent.putExtra("HOUR", hour);
                        intent.putExtra("DAY", day);
                        intent.putExtra("MONTH", month);
                        intent.putExtra("YEAR", year);
                        intent.putExtra("CREATORUSERID", creatorUserId);
                        intent.putExtra("TITLE", title);
                        intent.putExtra("DESCRIPTION", description);
                        intent.putExtra("CREATORWANTSNOTIFICATION", creatorWantsNotification);

                        startActivity(intent);

                        return false;
                    }
                });
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

//        Query query = myRef.child("parents").orderByKey().equalTo(userId);
//        Query query = myRef.child("reminders").orderByKey().equalTo(userId);
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d(TAG, "onDataChange: ");
//                if (!dataSnapshot.exists()) {
//                    Log.d(TAG, "onDataChange: Datasnapshot not found");
//                    toastMessage("Parent Datasnapshot not found");
//                } else {
//                    Log.d(TAG, "onDataChange: User found");
//                    toastMessage("User found");
//                    DataSnapshot remindersSnapshot = dataSnapshot.child(userId).child("reminders");
//                    Iterable<DataSnapshot> remindersChildren = remindersSnapshot.getChildren();
//                   DataSnapshot remindersSnapshot = dataSnapshot.child("reminders").child(userId);
//                    Iterable<DataSnapshot> remindersChildren = dataSnapshot.getChildren();
//
//                    reminderListItems = new ArrayList<ReminderListItemModel>();
//                    reminderItems = new ArrayList<Reminder>();
//
////                    for (DataSnapshot child : remindersChildren) {
////                    for (DataSnapshot child : remindersChildren) {
//                    for (DataSnapshot reminderSnapshot: dataSnapshot.getChildren()){
////                        Reminder r = child.getValue(Reminder.class);
////                        int rDay = r.getDay();
//                        Reminder r = new Reminder();
//                        String title = (String) reminderSnapshot.child("title").getValue();
//                        r.setTitle(title);
////                        int Day = (int) reminderSnapshot.child("day").getValue();
////                        r.setDay(Day);
//                        String rDayString = Integer.toString(rDay);
//                        if(rDayString.length() == 1){
//                            rDayString = "0" + rDayString;
//                        }
//                        int rMonth = r.getMonth();
//                        String rMonthString = Integer.toString(rMonth);
//                        if(rMonthString.length() == 1){
//                            rMonthString = "0" + rMonthString;
//                        }
//                        int rYear = r.getYear();
//                        int rHour = r.getHour();
//                        String rHourString = Integer.toString(rHour);
//                        if(rHourString.length() == 1){
//                            rHourString = "0" + rHourString;
//                        }
//                        int rMinute =r.getMinute();
//                        String rMinuteString = Integer.toString(rMinute);
//                        if(rMinuteString.length() == 1){
//                            rMinuteString = "0" + rMinuteString;
//                        }
//                        String rDate = rDayString + "/" + rMonthString + "/" + rYear;
//                        String rTime = rHourString + ":" + rMinuteString;
//
//                        if(rDate.equals(dateString)) {
//                            ReminderListItemModel reminderListItemModel = new ReminderListItemModel(rDate, r.getTitle().toString());
//                            reminderListItemModel.setTime(rTime);
//                            reminderListItemModel.setTitle(r.getTitle());
//                            reminderListItems.add(reminderListItemModel);
//                            reminderItems.add(r);
//                        }
//                    }
                    /*final ReminderListAdapter adapter = new ReminderListAdapter(DailyReminderList.this,
                            reminderListItems, reminderItems);
                    reminderList.setAdapter(adapter);
                    reminderList.setLongClickable(true);
                    reminderList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            Log.d(TAG, "onItemLongClick: ");
                            Vibrator vibrator = (Vibrator) DailyReminderList.this.getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(200);

                            Reminder toShow = adapter.getReminder(position);
                            Intent intent = new Intent(DailyReminderList.this, ReminderDetail.class);
                            UUID Id = toShow.getId();
                            int minute = toShow.getMinute();
                            int hour = toShow.getHour();
                            int day = toShow.getDay();
                            int month = toShow.getMonth();
                            int year = toShow.getYear();
                            String creatorUserId = toShow.getCreatorUserId();
                            String title = toShow.getTitle();
                            String description = toShow.getDescription();
                            boolean creatorWantsNotification = toShow.getCreatorWantsNotification();

                            intent.putExtra("ID", Id);
                            intent.putExtra("MINUTE", minute);
                            intent.putExtra("HOUR", hour);
                            intent.putExtra("DAY", day);
                            intent.putExtra("MONTH", month);
                            intent.putExtra("YEAR", year);
                            intent.putExtra("CREATORUSERID", creatorUserId);
                            intent.putExtra("TITLE", title);
                            intent.putExtra("DESCRIPTION", description);
                            intent.putExtra("CREATORWANTSNOTIFICATION", creatorWantsNotification);

                            startActivity(intent);

                            return false;
                        }
                    });

                }*/




    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
