package com.ditkevinstreet.createaccountscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Admin on 19/12/2017.
 */

public class RegisterChild1 extends AppCompatActivity {
    private static final String TAG = "RegisterChild1";

    private EditText parentsEmailField, childsEmailField;
    private Button btnSubmit, btnBack;

    private String parentsEmail, childsEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_child_step_1);


        parentsEmailField = (EditText) findViewById(R.id.parentsEmailField);
        childsEmailField = (EditText) findViewById(R.id.childsEmailField);

        btnSubmit = (Button) findViewById(R.id.btnCreate);
        btnBack = (Button) findViewById(R.id.btnBack);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentsEmail = parentsEmailField.getText().toString().toLowerCase();
                childsEmail = childsEmailField.getText().toString().toLowerCase();
                if((!parentsEmail.equals(""))&&(!childsEmail.equals(""))){
                    Intent intent = new Intent(RegisterChild1.this, RegisterChild2.class);
                    intent.putExtra("PARENTSEMAIL", parentsEmail);
                    intent.putExtra("CHILDSEMAIL", childsEmail);
                    startActivity(intent);
                }else if(parentsEmail.equals("")){
                    toastMessage("You have not provided your parents email");
                }else if(childsEmail.equals("")){
                    toastMessage("You have not provided your own email");
                }
            }
        });
    }
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
