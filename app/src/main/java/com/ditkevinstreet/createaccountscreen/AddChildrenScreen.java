package com.ditkevinstreet.createaccountscreen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Admin on 22/10/2017.
 */

public class AddChildrenScreen extends AppCompatActivity {
    private static final String TAG = "AddChildrenScreen";

    private Button mAddChild, mReturn;
    private EditText mName, mEmail;
    private RadioGroup mGenderRadio;

    private String userID;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_child_screen);

        mAddChild = (Button) findViewById(R.id.btn_done);
        mReturn = (Button) findViewById(R.id.btn_done_adding_children);

        mName = (EditText) findViewById(R.id.childs_name_field);
        mEmail = (EditText) findViewById(R.id.childs_email_field);
        mGenderRadio = (RadioGroup) findViewById(R.id.radio_gender);

        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = firebaseAuth.getCurrentUser();

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


        mAddChild.setOnClickListener(new View.OnClickListener() {//wait, is this what we want? in this model the childs account is being created by the parent, not just a reference to the email, it could work though, let the parent create the accout and then the child can just set a password
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: AddChild pressed");
                String name = mName.getText().toString();
                int selectedId = mGenderRadio.getCheckedRadioButtonId();
                String email = mEmail.getText().toString();
                String gender;
                if(selectedId == 1){
                    gender = "male";
                }else if(selectedId == 2){
                    gender = "female";
                }else{
                    gender = "unspecified";
                }
                //this is just copied from userInfo.java, doesnt necessarily apply here
                Log.d(TAG, "onClick: Attempting to add to database: \n"
                        + "name: " + name + "\n"
                        + "gender: " + gender + "\n"
                        + "email: " + email + "\n"
                );
                //exception handling
                if(!name.equals("") && !email.equals("")){
                    Child child = new Child(name, gender, email);
                    myRef.child("users").child("children").child(userID).setValue(child);
                    toastMessage("Information saved");
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
