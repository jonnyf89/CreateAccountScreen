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

import java.util.ArrayList;

import static com.ditkevinstreet.createaccountscreen.Parent.EMAIL;
import static com.ditkevinstreet.createaccountscreen.Parent.FIRSTNAME;
import static com.ditkevinstreet.createaccountscreen.Parent.LASTNAME;

/**
 * Created by Admin on 23/10/2017.
 */

public class RegisterParent3AddChildren extends AppCompatActivity {

    private static final String TAG = "AddChildScreenSimplifie";

    private Button btnAddChild, btnFinish;
    private EditText childsEmailField;

    private String userID;
    private String childEmail;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Parent parent;
    private ArrayList<Child> childrenList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_parent3_add_children);
        Log.d(TAG, "onCreate: Entered onCreate");

        btnAddChild = (Button) findViewById(R.id.btnAddChild);
        btnFinish = (Button) findViewById(R.id.btn_finish);
        childsEmailField = (EditText) findViewById(R.id.childsEmailField);
        childrenList = new ArrayList<Child>();


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
        // Read from the database //TODO do i need this?
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

        Bundle b = this.getIntent().getExtras();
        if(b!=null){
            String firstName = getIntent().getStringExtra(FIRSTNAME);
            String lastName = getIntent().getStringExtra(LASTNAME);
            String email = getIntent().getStringExtra(EMAIL);

            parent = new Parent(firstName, lastName);
            parent.setEmail(email);
            parent.setDeviceToken(Common.currentToken);

        }

        btnAddChild.setOnClickListener(new View.OnClickListener() {//wait, is this what we want? in this model the childs account is being created by the parent, not just a reference to the email, it could work though, let the parent create the accout and then the child can just set a password
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: AddChild pressed");
                childEmail = childsEmailField.getText().toString().toLowerCase();

                //exception handling
                if(!childEmail.equals("")){
                    Child child = new Child(childEmail, parent);
                    childrenList.add(child);
                    childsEmailField.setText(null);

                }else{
                    toastMessage("You have not provided an email address");
                }
            }
        });
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myRef.child("parents").child(userID).setValue(parent);
                myRef.child("parents").child(userID).child("children").setValue(childrenList);

                Intent finishSetUp = new Intent(RegisterParent3AddChildren.this, CalendarView.class);
                toastMessage("Registered successfully");
                startActivity(finishSetUp);
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


