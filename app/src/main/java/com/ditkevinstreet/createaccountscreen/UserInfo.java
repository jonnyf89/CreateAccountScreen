package com.ditkevinstreet.createaccountscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Admin on 21/10/2017.
 */

public class UserInfo extends AppCompatActivity {

    private static final String TAG = "UserInfo";

    //declaring buttons and editTexts
    private Button mDone;
    private EditText mFirstName, mLastName, mNickname;
    private String userID;

    //declaring Firebase stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener mAuthListener;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_page_layout);

        //buttons
        mDone = (Button) findViewById(R.id.btn_done);
        //EditTexts
        mFirstName = (EditText) findViewById(R.id.first_name_field);
        mLastName = (EditText) findViewById(R.id.last_name_field);
        mNickname = (EditText) findViewById(R.id.nickname_field);
        //firebase stuff
        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        //userID = user.getUid();// this line crashes app, moved it to inside the AuthStateListener


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    toastMessage("Successfully signed in with: " + user.getEmail() + " please provide the following details");
                    userID = user.getUid();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Not signed in. Please wait a few seconds");
                }

            }
        };


        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d(TAG, "onDataChange: Added information to the DB: \n" + dataSnapshot.getValue());

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        /*mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Done pressed");
                String firstName = mFirstName.getText().toString();
                String lastName = mLastName.getText().toString();
                String nickname = mNickname.getText().toString();

                Log.d(TAG, "onClick: Attempting to add to database: \n"
                + "first name: " + firstName + "\n"
                + "last name: " + lastName + "\n"
                + "nickname: " + nickname + "\n"
                );
                //exception handling
                if(!firstName.equals("") && !lastName.equals("")){
                    UserInformation userInformation = new UserInformation(firstName, lastName, nickname);
                    myRef.child("users").child(userID).setValue(userInformation);
                    toastMessage("Information saved");
                }else{
                    toastMessage("You must provide a first and last name");
                }
            }
        });*/
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Done pressed");//**************Dont declare these Strings here, put them up above and add them to the entity diagram
                String firstName = mFirstName.getText().toString();
                String lastName = mLastName.getText().toString();
                String nickname = mNickname.getText().toString();
                FirebaseUser user = firebaseAuth.getCurrentUser();

                Log.d(TAG, "onClick: Attempting to add to database: \n"
                        + "first name: " + firstName + "\n"
                        + "last name: " + lastName + "\n"
                        + "nickname: " + nickname + "\n"
                );
                //exception handling
                if(!firstName.equals("") && !lastName.equals("")){
                    Parent parent = new Parent(firstName, lastName, nickname);
                    myRef.child("users").child("parents").child(userID).setValue(parent);
                    toastMessage("Information saved");
                    Intent intent = new Intent(UserInfo.this, AddChildScreenSimplified.class);
                    startActivity(intent);
                }else{
                    toastMessage("You must provide a first and last name");
                }
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
        firebaseAuth.signOut();
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
