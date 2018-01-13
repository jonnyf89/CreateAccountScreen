package com.ditkevinstreet.createaccountscreen;

import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ditkevinstreet.createaccountscreen.NotificationStuff.DataMessage;
import com.ditkevinstreet.createaccountscreen.NotificationStuff.DataSender;
import com.ditkevinstreet.createaccountscreen.NotificationStuff.MessageSender;
import com.ditkevinstreet.createaccountscreen.NotificationStuff.MyResponse;
import com.ditkevinstreet.createaccountscreen.NotificationStuff.Notification;
import com.ditkevinstreet.createaccountscreen.NotificationStuff.Sender;
import com.ditkevinstreet.createaccountscreen.Remote.APIDataService;
import com.ditkevinstreet.createaccountscreen.Remote.APIMessageService;
import com.ditkevinstreet.createaccountscreen.Remote.APIService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


import retrofit2.Callback;
import retrofit2.Call;
import retrofit2.Response;

import static android.app.PendingIntent.FLAG_ONE_SHOT;

/**
 * Created by Admin on 19/12/2017.
 */



public class AddReminder extends AppCompatActivity {
    private static final String TAG = "AddReminder";

    APIService mAPIService;
    APIDataService mAPIDataService;
    APIMessageService mAPIMessageService;


    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference myRef;
    private FirebaseUser user;
    private String userId;

    private TextView dateField, timeField;
    private EditText titleField, descriptionField;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    private String displayDateString, timeString, title, description;
    private Calendar calDate;
    private Reminder reminder;

    private Button btnSubmit, btnCancel;
    private ListView familyMembersList;
    private ArrayList<RecipientModel> mItems;
    private ArrayList<Child> childRecipients;


    // IDs for menu items
    private static final int MENU_DELETE = Menu.FIRST;
    private static final int MENU_DUMP = Menu.FIRST + 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_reminder);

        mAPIService = Common.getFCMClient();
        mAPIDataService = Common.getFCMDataClient();
        mAPIMessageService = Common.getFCMMessageClient();

        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        user = firebaseAuth.getCurrentUser();
        userId = user.getUid();

        Intent intent = getIntent();
        displayDateString = intent.getStringExtra("DISPLAYDATESTRING");
        calDate = (Calendar) intent.getSerializableExtra("CALDATE");

        mItems = new ArrayList<RecipientModel>();
        childRecipients = new ArrayList<Child>();
        RecipientModel self = new RecipientModel("Myself", 0);
        mItems.add(self);


        dateField = (TextView) findViewById(R.id.reminderDateField);
        timeField = (TextView) findViewById(R.id.reminderTimeField);
        titleField = (EditText) findViewById(R.id.reminderTitleField);
        descriptionField = (EditText) findViewById(R.id.reminderDescriptionField);
        familyMembersList = (ListView) findViewById(R.id.familyMembersList);
        btnSubmit = (Button) findViewById(R.id.btnCreate);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        final Date currentTime = Calendar.getInstance().getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentTime);

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        String hourString = Integer.toString(hour);
        if(hourString.length()==1){
            hourString = "0" + hourString;
        }
        int minute = cal.get(Calendar.MINUTE);
        String minuteString = Integer.toString(minute);
        if(minuteString.length()== 1){
            minuteString = "0" + minuteString;
        }
        calDate.set(Calendar.HOUR_OF_DAY, hour);
        calDate.set(Calendar.MINUTE, minute);
        timeString = hourString + ":" + minuteString;

        dateField.setText(displayDateString);
        dateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Calendar cal = Calendar.getInstance();
////                cal.setTime(date);
//                cal = calDate;
                int year = calDate.get(Calendar.YEAR);
                int month = calDate.get(Calendar.MONTH);
                int displayMonth = month + 1;
                int day = calDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddReminder.this,
                        android.R.style.Theme_Holo_Light_DarkActionBar,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d(TAG, "onDateSet: date: " + dayOfMonth + "/" + (month) + "/" + year);//TODO make sure month is correct
                int displayMonth = (month + 1);//TODO this increases date by 1 every time user presses the date field, it is needed however
                calDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calDate.set(Calendar.MONTH, month);
                calDate.set(Calendar.YEAR, year);
                String dayString = Integer.toString(dayOfMonth);
                if(dayString.length()==1){
                    dayString="0"+dayString;
                }
                String monthString = Integer.toString(displayMonth);
                if(monthString.length()==1){
                    monthString="0"+monthString;
                }
                displayDateString = dayString + "/" + monthString + "/" + year;
                dateField.setText(displayDateString);

            }
        };
        timeField.setText(timeString);
        timeField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(currentTime);
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);
                TimePickerDialog dialog = new TimePickerDialog(AddReminder.this,
                        mTimeSetListener,
                        hour, minute, true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Log.d(TAG, "onTimeSet: time: " + hourOfDay + ":" + minute);
                calDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calDate.set(Calendar.MINUTE, minute);
                String hourString = Integer.toString(hourOfDay);
                if(hourString.length()==1){
                    hourString = "0" + hourString;
                }
                String minuteString = Integer.toString(minute);
                if(minuteString.length()==1){
                    minuteString = "0" + minuteString;
                }
                timeString = hourString + ":" + minuteString;
                timeField.setText(timeString);
            }
        };
        Query query = myRef
                .child("parents").orderByKey().equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: ");
                if (!dataSnapshot.exists()) {
                    Log.d(TAG, "onDataChange: Datasnapshot not found");
                    toastMessage("Datasnapshot not found");
                } else {
                    Log.d(TAG, "onDataChange: Parent found");
                    toastMessage("Parent found");
                    DataSnapshot childrenSnapshot = dataSnapshot.child(userId).child("children");
                    Iterable<DataSnapshot> childrenChildren = childrenSnapshot.getChildren();



                    for (DataSnapshot child : childrenChildren) {
                        Child c = child.getValue(Child.class);
                        String childName = c.getFirstName();
                        RecipientModel recipientModel = new RecipientModel(childName, 0);
                        mItems.add(recipientModel);
                        childRecipients.add(c);//TODO this is not right, its adding them all regardless of whether they are ticked
                    }

//                    RecipientAdapter adapter = new RecipientAdapter(AddReminder.this, mItems);
//
//                    familyMembersList.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        RecipientAdapter adapter = new RecipientAdapter(AddReminder.this, mItems);

        familyMembersList.setAdapter(adapter);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = titleField.getText().toString();
                description = descriptionField.getText().toString();

                if(title.equals("")){
                   toastMessage("A reminder must have a title.");
                }else{
                    for(int i = 0; i < mItems.size(); i++){
                        if(mItems.get(i).getValue()==0){
                            for(int n = 0; n <childRecipients.size(); n++){
                                if(childRecipients.get(n).getFirstName().equals(mItems.get(i).getName())){
                                    childRecipients.remove(n);
                                }
                            }
                        }
                    }
                    reminder = new Reminder(calDate, userId, title, description);
                    //TODO now add boolean value for creator wants reminder
                    if(mItems.get(0).getValue() == 1){
                        reminder.setCreatorWantsNotification(true);
                    }else{
                        reminder.setCreatorWantsNotification(false);
                    }
                    //Create entry in database of reminder for self
                    myRef.child("reminders").child(userId).child(reminder.getId().toString()).setValue(reminder,
                            new DatabaseReference.CompletionListener() {
                        public void onComplete(DatabaseError error, DatabaseReference ref) {
                            toastMessage("Reminder Created");
                        }
                    });
                    Query query = myRef
                            .child("parents").orderByKey().equalTo(userId);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                Log.d(TAG, "onDataChange: Datasnapshot not found");
                                toastMessage("Datasnapshot not found");
                            } else {
                                DataSnapshot childrenSnapshot = dataSnapshot.child(userId).child("children");
                                Iterable<DataSnapshot> childrenChildren = childrenSnapshot.getChildren();
                                for (DataSnapshot child : childrenChildren) {
                                    Child c = child.getValue(Child.class);
                                    String childName = c.getFirstName();
                                    String childDeviceToken = c.getDeviceToken();
                                    for(int i = 0; i < childRecipients.size(); i++){
                                        if(childName.equals(childRecipients.get(i).getFirstName())){
                                            String childUserId = childRecipients.get(i).getUserId();
                                            //Create entry in database of reminder for any selected children
                                            myRef.child("reminders").child(childUserId).child(reminder.getId().toString()).setValue(reminder);
//                                            DataMessage dataMessage = new DataMessage("reminderId", reminder.getId());

//                                            String notificationMessage = "Testing new method";
                                            Notification notification = new Notification(reminder.getId());

//                                            Intent reminderIntent = new Intent(getApplicationContext(), ReminderDetail.class);
//                                            reminderIntent.putExtra("DAY", calDate.get(Calendar.DAY_OF_MONTH));
//                                            reminderIntent.putExtra("MONTH", calDate.get(Calendar.MONTH));
//                                            reminderIntent.putExtra("YEAR", calDate.get(Calendar.YEAR));
//                                            reminderIntent.putExtra("HOUR", calDate.get(Calendar.HOUR_OF_DAY));
//                                            reminderIntent.putExtra("MINUTE", calDate.get(Calendar.MINUTE));
//                                            reminderIntent.putExtra("TITLE", title);
//                                            reminderIntent.putExtra("DESCRIPTION", description);
//                                            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, reminderIntent, FLAG_ONE_SHOT);


                                            Sender sender = new Sender(childDeviceToken, notification);
//                                            DataSender dataSender = new DataSender(childDeviceToken, dataMessage);

//                                            RemoteMessage remoteMessage = new RemoteMessage.Builder(childDeviceToken)
//                                                    .setMessageId("99")
//                                                    .addData("reminderId", reminder.getId())
//                                                    .build();
//                                            MessageSender messageSender = new MessageSender(childDeviceToken, remoteMessage);

                                            mAPIService.sendNotification(sender)
                                                    .enqueue(new Callback<MyResponse>(){
                                                        @Override
                                                        public void onResponse(Call<MyResponse> call,
                                                                               Response<MyResponse> response){if(response.body().success == 1){
                                                                toastMessage("Success");
                                                            }else{
                                                                toastMessage("Failed");
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<MyResponse> call, Throwable t) {
                                                            Log.e("ERROR",t.getMessage() );
                                                        }

                                                    });
//                                            mAPIDataService.sendDataMessage(dataSender)
//                                                                .enqueue(new Callback<MyResponse>(){
//                                                                    @Override
//                                                                    public void onResponse(Call<MyResponse> call,
//                                                                                           Response<MyResponse> response){if(response.body().success == 1){
//                                                                        toastMessage("Success");
//                                                                    }else{
//                                                                        toastMessage("Failed");
//                                                                    }
//                                                        }
//
//                                                        @Override
//                                                        public void onFailure(Call<MyResponse> call, Throwable t) {
//                                                            Log.e("ERROR",t.getMessage() );
//                                                        }
//
//                                                    });
//                                            mAPIMessageService.sendRemoteMessage(messageSender)
//                                                    .enqueue(new Callback<MyResponse>(){
//                                                        @Override
//                                                        public void onResponse(Call<MyResponse> call,
//                                                                               Response<MyResponse> response){if(response.body().success == 1){
//                                                            toastMessage("Success");
//                                                        }else{
//                                                            toastMessage("Failed");
//                                                        }
//                                                        }
//
//                                                        @Override
//                                                        public void onFailure(Call<MyResponse> call, Throwable t) {
//                                                            Log.e("ERROR",t.getMessage() );
//                                                        }
//
//                                                    });


                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    //TODO does this do anything?
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, "Delete all");
        menu.add(Menu.NONE, MENU_DUMP, Menu.NONE, "Dump to log");
        return true;
    }
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
