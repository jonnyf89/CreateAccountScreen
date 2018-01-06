package com.ditkevinstreet.createaccountscreen;

import android.app.DatePickerDialog;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Admin on 19/12/2017.
 */



public class AddReminder extends AppCompatActivity {
    private static final String TAG = "AddReminder";

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference myRef;
    private FirebaseUser user;
    private String userId;

    private TextView dateField, timeField;
    private EditText titleField, descriptionField;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    private String dateString, timeString, title, description;
//    private Date date;
    private Calendar calDate;
    private Reminder reminder;

    private Button btnSubmit, btnCancel;
    private ListView familyMembersList;
//    private ArrayList<FamilyMemberListItem> mItems;
    private ArrayList<RecipientModel> mItems;
    private ArrayList<Child> childRecipients;
    RecipientModel[] modelItems;//TODO remove


    // IDs for menu items
    private static final int MENU_DELETE = Menu.FIRST;
    private static final int MENU_DUMP = Menu.FIRST + 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_reminder);

        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        user = firebaseAuth.getCurrentUser();
        userId = user.getUid();

        Intent intent = getIntent();
        dateString = intent.getStringExtra("DATESTRING");
//        date = (Date)intent.getSerializableExtra("DATE");
        calDate = (Calendar) intent.getSerializableExtra("CALDATE");


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

        int hour = cal.get(Calendar.HOUR);
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

        dateField.setText(dateString);
        dateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Calendar cal = Calendar.getInstance();
////                cal.setTime(date);
//                cal = calDate;
                int year = calDate.get(Calendar.YEAR);
                int month = calDate.get(Calendar.MONTH);
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
                Log.d(TAG, "onDateSet: date: " + dayOfMonth + "/" + month + "/" + year);//TODO make sure month is correct
                month = month + 1;//TODO this increases date by 1 every time user presses the date field, it is needed however
                calDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calDate.set(Calendar.MONTH, month);
                calDate.set(Calendar.YEAR, year);
                String dayString = Integer.toString(dayOfMonth);
                if(dayString.length()==1){
                    dayString="0"+dayString;
                }
                String monthString = Integer.toString(month);
                if(monthString.length()==1){
                    monthString="0"+monthString;
                }
                dateString = dayString + "/" + monthString + "/" + year;
                dateField.setText(dateString);

            }
        };
        timeField.setText(timeString);
        timeField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(currentTime);
                int hour = cal.get(Calendar.HOUR);
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
                calDate.set(Calendar.HOUR, hourOfDay);
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
//                    mItems = new ArrayList<FamilyMemberListItem>();
                    mItems = new ArrayList<RecipientModel>();
                    childRecipients = new ArrayList<Child>();
                    RecipientModel self = new RecipientModel("Myself", 0);
                    mItems.add(self);

//                    modelItems = new RecipientModel[1];
                    for (DataSnapshot child : childrenChildren) {
                        Child c = child.getValue(Child.class);
                        String childName = c.getFirstName();
                        RecipientModel recipientModel = new RecipientModel(childName, 0);
//                        FamilyMemberListItem item = new FamilyMemberListItem(childName);
//                        modelItems[0] = new RecipientModel(c.getFirstName(), 0);
//                        mItems.add(item);
                        mItems.add(recipientModel);
                        childRecipients.add(c);//TODO this is not right, its adding them all regardless of whether they are ticked
                    }
//                    modelItems = new RecipientModel[5];
//                    modelItems[0] = new RecipientModel("pizza", 0);
//                    modelItems[1] = new RecipientModel("burger", 1);
//                    modelItems[2] = new RecipientModel("olives", 1);
//                    modelItems[3] = new RecipientModel("orange", 0);
//                    modelItems[4] = new RecipientModel("tomato", 1);
//                    RecipientAdapter adapter = new RecipientAdapter(AddReminder.this, modelItems);
                    RecipientAdapter adapter = new RecipientAdapter(AddReminder.this, mItems);

                    familyMembersList.setAdapter(adapter);
//                    FamilyMemberListAdapter mAdapter = new FamilyMemberListAdapter(mItems, getApplicationContext());
//                    familyMembersList.setAdapter(mAdapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
                    reminder = new Reminder(calDate, userId, /*childRecipients,*/ title, description);
                    //TODO now add boolean value for creator wants reminder
                    if(mItems.get(0).getValue() == 1){
                        reminder.setCreatorWantsNotification(true);
                    }else{
                        reminder.setCreatorWantsNotification(false);
                    }
                    myRef.child("parents").child(userId).child("reminders").child(reminder.getId().toString()).setValue(reminder);
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
                                    for(int i = 0; i < childRecipients.size(); i++){
                                        if(childName.equals(childRecipients.get(i).getFirstName())){
                                            myRef.child("parents").child(userId).child("children")
                                                    .child(child.getKey()).child("reminders").child(reminder.getId().toString()).setValue(reminder);
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
