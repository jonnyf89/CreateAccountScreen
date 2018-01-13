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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Admin on 19/12/2017.
 */

public class RegisterChild2 extends AppCompatActivity {

    private static final String TAG = "RegisterChild2";

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference myRef;
    private FirebaseUser user;

    private EditText firstNameField, lastNameField, passwordField, confPasswordField;
    private TextView parentsEmailField, childsEmailField;

    private Button btnSubmit, btnBack;

    private String parentsEmail, childsEmail, firstName, lastName, password, confPassword, userId, parentsUserId, childKey;

    private Object childSnap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_child_step_2);

        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();


        firstNameField = (EditText) findViewById(R.id.first_name_field);
        lastNameField = (EditText) findViewById(R.id.last_name_field);
        passwordField = (EditText) findViewById(R.id.password_field);
        confPasswordField = (EditText) findViewById(R.id.confirm_password_field);

        parentsEmailField = (TextView) findViewById(R.id.parentsEmailField);
        childsEmailField = (TextView) findViewById(R.id.childsEmailField);

        btnSubmit = (Button) findViewById(R.id.btnCreate);
        btnBack = (Button) findViewById(R.id.btnBack);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            parentsEmail = extras.getString("PARENTSEMAIL");
            childsEmail = extras.getString("CHILDSEMAIL");
        } else {
            toastMessage("No extras");
        }
        Query query = myRef
                .child("parents").orderByChild("email").equalTo(parentsEmail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: ");
                if (!dataSnapshot.exists()) {
                    toastMessage("Parent not found");
                    parentsEmailField.setText("Parent not found, please go back");
                    parentsEmailField.setTextColor(getColor(R.color.errorMessageRed));
                }
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    if (singleSnapshot.exists()) {
                        //Parent parent = (Parent) singleSnapshot.getValue();
                        toastMessage("Parent found");
                        parentsEmailField.setText(parentsEmail);
                        parentsUserId = singleSnapshot.getKey().toString();
                        Query query = myRef
                                .child("parents").child(parentsUserId).child("children").orderByChild("email").equalTo(childsEmail);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.d(TAG, "onDataChange: ");
                                if (!dataSnapshot.exists()) {
                                    toastMessage("Child not found");
                                    childsEmailField.setText("Email not found, please go back");
                                    childsEmailField.setTextColor(getColor(R.color.errorMessageRed));
                                }
                                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                    if (singleSnapshot.exists()) {
                                        childSnap = singleSnapshot;
                                        childKey = singleSnapshot.getKey().toString();
                                        toastMessage("Child found");
                                        childsEmailField.setText(childsEmail);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.d(TAG, "onCancelled: ");
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: ");
            }


        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstName = firstNameField.getText().toString();
                lastName = lastNameField.getText().toString();
                password = passwordField.getText().toString();
                confPassword = confPasswordField.getText().toString();

                if((!firstName.equals(""))&&(!lastName.equals(""))&&(!password.equals(""))&&(!confPassword.equals(""))){
                    registerChild(childsEmail, password, confPassword);

                }else if(firstName.equals("")){
                    toastMessage("You have not entered your first name");
                }else if(lastName.equals("")){
                    toastMessage("You have not entered your last name");
                }else if(password.equals("")){
                    toastMessage("You have not selected a password");
                }else if(confPassword.equals("")){
                    toastMessage("You need to confirm your password");
                }else{
                    Log.d(TAG, "onClick: reached somewhere we shouldnt have");
                }


            }
        });

    }
    public void registerChild(String email, String password, String confPassword) {
        if (password.equals(confPassword)) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegisterChild2.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                toastMessage("Registered successfully");
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                userId = user.getUid();
                                myRef.child("parents").child(parentsUserId).child("children").child(childKey).child("firstName").setValue(firstName);
                                myRef.child("parents").child(parentsUserId).child("children").child(childKey).child("lastName").setValue(lastName);
                                myRef.child("parents").child(parentsUserId).child("children").child(childKey).child("userId").setValue(userId);
                                myRef.child("parents").child(parentsUserId).child("children").child(childKey).child("deviceToken").setValue(Common.currentToken);
                                myRef.child("parents").child(parentsUserId).child("children").child(childKey).child("parent").child("userId").setValue(parentsUserId);
                                Intent intent = new Intent(RegisterChild2.this, CalendarView.class);
                                startActivity(intent);
                            }
                        }
                    });
        } else {
            toastMessage("Provided passwords don't match");
        }
    }
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
