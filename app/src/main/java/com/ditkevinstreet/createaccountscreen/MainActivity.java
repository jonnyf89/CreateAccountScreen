package com.ditkevinstreet.createaccountscreen;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
/*
this is the parent register activity
 */

public class MainActivity extends AppCompatActivity{
    private static final String TAG = "MainActivity";

    //attributes
    private String email;
    private String password;
    private String conpass;
    private String userID;

    //UI references
    private EditText emailField, passwordField, confirmPasswordField;
    private Button btnSubmit;
    //private ProgressDialog progress;

    //Firebase stuff
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private FirebaseDatabase mFirebaseDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // progress = new ProgressDialog(this);

        emailField = (EditText) findViewById(R.id.email_field);
        passwordField = (EditText) findViewById(R.id.password_field);
        confirmPasswordField = (EditText) findViewById(R.id.confirm_password_field);
        btnSubmit = (Button) findViewById(R.id.btnCreate);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();





        firebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    toastMessage("Successfully signed in with: " + user.getEmail());
                    userID = user.getUid();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Not signed in.");
                }

            }
        };
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();

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

    public void registerUser() {
        email = emailField.getText().toString().trim().toLowerCase();
        password = passwordField.getText().toString().trim();
        conpass = confirmPasswordField.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            toastMessage("Please enter email");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            toastMessage("Please choose password");
            return;
        }
        if(!password.equals(conpass)){
            toastMessage("The password fields do not match");
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            toastMessage("Registered Successfully");
                            //CreateParent();
                            //toastMessage("Created Parent");
                            Intent intent = new Intent(MainActivity.this, UserInfo.class);
                            startActivity(intent);

                        }else{
                            Toast.makeText(MainActivity.this, "Could not Register", Toast.LENGTH_SHORT).show();
                        }
                       // progress.dismiss();
                    }
                });
    }
    /*public void CreateParent(){
        Parent parent = new Parent();
        myRef.child("parents").child(userID).setValue(parent)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            toastMessage("Parent Object created");
                        }else{
                            toastMessage("Failed to create Parent Object");
                        }
                    }
                });
    }*/


    /*public void onClick(View view) {

        registerUser();
        Intent intent = new Intent(MainActivity.this, UserInfo.class);
        startActivity(intent);
    }*/

}

