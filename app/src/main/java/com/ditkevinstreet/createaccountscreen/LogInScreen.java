package com.ditkevinstreet.createaccountscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Admin on 01/12/2017.
 */

public class LogInScreen extends AppCompatActivity {

    private static final String TAG = "LogInScreen";

    //declaring UI elements
    private Button btnLogIn, btnBack;
    private EditText emailField, passwordField;
    private String emailString, passwordString;

    //declaring Firebase stuff
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_layout);
        final Bundle b = this.getIntent().getExtras();


        btnLogIn = (Button) findViewById(R.id.btnLogIn);
        btnBack = (Button) findViewById(R.id.btnBack);

        emailField = (EditText) findViewById(R.id.email_field);
        passwordField = (EditText) findViewById(R.id.password_field);

        mAuth = FirebaseAuth.getInstance();

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailString = emailField.getText().toString().toLowerCase();
                passwordString = passwordField.getText().toString();
                if(!emailString.equals("")&&!passwordString.equals("")){
                    mAuth.signInWithEmailAndPassword(emailString, passwordString)
                            .addOnCompleteListener(LogInScreen.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(!task.isSuccessful()){
                                        toastMessage("Log in failed");
                                    }else{
                                        toastMessage("Signed in as " + emailString);//this will display even if the above fails
                                        if(b==null) {
                                            Intent toCalendar = new Intent(LogInScreen.this, CalendarView.class);
                                            startActivity(toCalendar);
                                        }else{
                                            String reminderId = getIntent().getStringExtra("REMINDERID");
                                            Intent toDetail = new Intent(LogInScreen.this, ReminderDetail.class);
                                            toDetail.putExtra("REMINDERID", reminderId);
                                            startActivity(toDetail);
                                        }
                                    }
                                }
                            });
                }else{
                    toastMessage("You didn't fill in all the fields");
                }
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            toastMessage("Already signed in as " + mAuth.getCurrentUser().getEmail());
            startActivity(new Intent(LogInScreen.this, CalendarView.class));

        }
    }
    private void toastMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
