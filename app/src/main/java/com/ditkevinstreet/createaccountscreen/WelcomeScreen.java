package com.ditkevinstreet.createaccountscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Admin on 01/12/2017.
 */

public class WelcomeScreen extends AppCompatActivity{
    private static final String TAG = "WelcomeScreen";

    private Button btnLogIn;
    private Button btnParentRegister;
    private Button btnChildRegister;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            toastMessage("Already signed in as " + mAuth.getCurrentUser().getEmail());
            startActivity(new Intent(WelcomeScreen.this, CalendarView.class));

        }

        setContentView(R.layout.welcome_page);

        btnLogIn = (Button) findViewById(R.id.btnLogIn);
        btnParentRegister = (Button) findViewById(R.id.btnParentRegister);
        btnChildRegister = (Button) findViewById(R.id.btnChildRegister);


        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toLogIn = new Intent(WelcomeScreen.this, LogInScreen.class);
                startActivity(toLogIn);
            }
        });

        btnParentRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toParentReg = new Intent(WelcomeScreen.this, MainActivity.class);
                startActivity(toParentReg);
            }
        });

        btnChildRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toChildReg = new Intent(WelcomeScreen.this, RegisterChild1.class);
                startActivity(toChildReg);
            }
        });




    }
    private void toastMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
