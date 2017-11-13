package com.ditkevinstreet.createaccountscreen;

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
 * Created by Admin on 23/10/2017.
 */

public class AddChildScreenSimplified extends AppCompatActivity {

    private static final String TAG = "AddChildScreenSimplifie";

    private Button btnAddChild, btnBack;
    private EditText emailField;

    private String userID;
    private String childEmail;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_child_screen_simplified);
        Log.d(TAG, "onCreate: Entered onCreate");

        btnAddChild = (Button) findViewById(R.id.btn_done);
        btnBack = (Button) findViewById(R.id.btn_go_back);
        emailField = (EditText) findViewById(R.id.childs_email_field);

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
                    toastMessage("Signed in as: " + user.getEmail());
                    userID = user.getUid();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Not signed in.");
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

        btnAddChild.setOnClickListener(new View.OnClickListener() {//wait, is this what we want? in this model the childs account is being created by the parent, not just a reference to the email, it could work though, let the parent create the accout and then the child can just set a password
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: AddChild pressed");
                 childEmail = emailField.getText().toString();
                Log.d(TAG, "onClick: Attempting to add child to parent database object");

                //excetion handling
                if(!childEmail.equals("")){
                    //here is the tricky part, will not be the same as before
                    //this below might work, it also might not because the 'children' is an array and here its a string
                    //ok this does work, the only issue is that the parent object hasnt been created yet so make it so
                    //parent adds details, then presses done, which saves it to the DB, then takes them to the child screen
                    myRef.child("users").child("parents").child(userID).child("children").setValue(childEmail);
                    toastMessage("Child added");

                }else{
                    toastMessage("You have not provided an email address");
                }
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
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


