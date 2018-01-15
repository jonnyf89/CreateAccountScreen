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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Admin on 21/10/2017.
 */

public class RegisterParent2 extends AppCompatActivity {

    private static final String TAG = "RegisterParent2";

    //declaring buttons and editTexts
    private Button btnGoToAddChildren;
    private EditText mFirstName, mLastName;

    //declaring Firebase stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener mAuthListener;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_parent2);

        //buttons
        btnGoToAddChildren = (Button) findViewById(R.id.btnGoToAddChildren);
        //EditTexts
        mFirstName = (EditText) findViewById(R.id.first_name_field);
        mLastName = (EditText) findViewById(R.id.last_name_field);
        //firebase stuff
        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        //FirebaseUser user = firebaseAuth.getCurrentUser();
        //userID = user.getUid();// this line crashes app, moved it to inside the AuthStateListener



        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    toastMessage("Successfully signed in with: " + user.getEmail() + " please provide the following details");
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Not signed in. Please wait a few seconds");
                }

            }
        };

        btnGoToAddChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: btnGoToAddChildren pressed");//**************Dont declare these Strings here, put them up above and add them to the entity diagram
                String firstName = mFirstName.getText().toString();
                String lastName = mLastName.getText().toString();
                String email = firebaseAuth.getCurrentUser().getEmail();
                //TODO this isnt being stepped into
                if(!firstName.equals("") && !lastName.equals("")) {
                    Intent intent = new Intent(RegisterParent2.this, RegisterParent3AddChildren.class);
                    Parent.packageIntent(intent, firstName, lastName, email);
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
        //firebaseAuth.signOut();
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
