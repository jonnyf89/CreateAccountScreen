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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 02/12/2017.
 *
 * TODO i dont think i use this
 */

public class RegisterChild extends AppCompatActivity {
    private static final String TAG = "RegisterChild";

    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;//not used?
    private FirebaseUser user;


    private EditText parentEmailField, firstNameField, lastNameField, emailField, passwordField, confPasswordField;
    private Button btnSubmit;

    private boolean parentExists, parentHasChild;
    private String parentsUserId, parentEmail, childKey, userId, email, password, confPassword, firstName, lastName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_child_page);

        firebaseAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();


        parentExists = false;


        //will hold a list of parents
        final List<Parent> parentsList = new ArrayList<Parent>();


        parentEmailField = (EditText) findViewById(R.id.parent_email_field);
        firstNameField = (EditText) findViewById(R.id.first_name_field);
        lastNameField = (EditText) findViewById(R.id.last_name_field);
        emailField = (EditText) findViewById(R.id.email_field);
        passwordField = (EditText) findViewById(R.id.password_field);
        confPasswordField = (EditText) findViewById(R.id.confirm_password_field);

        btnSubmit = (Button) findViewById(R.id.btnCreate);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fieldsFilled() == true) {
                    parentEmail = parentEmailField.getText().toString();
                    firstName = firstNameField.getText().toString();
                    lastName = lastNameField.getText().toString();
                    email = emailField.getText().toString();
                    password = passwordField.getText().toString();
                    confPassword = confPasswordField.getText().toString();

                    /*final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference parentsRef = database.getReference("parents");
                    //to write the child info I need the user ID of the parent
                    parentsRef.child("parents").addValueEventListener(new ValueEventListener() {
                        /*
                        this method is evoked everytime data in the database changes
                        also evoked when we connect the listener, to get an initial snapshot of data on DB.
                         *//*
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {//datasnapshot represents the data at a particular time
                            //get all of the children at this level
                            Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                            //shake hands with each of them
                            for(DataSnapshot child : children){
                                Parent parent = child.getValue(Parent.class);
                                parentsList.add(parent);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });*/
                    /*parentsRef.orderByChild("email").equalTo(email).addChildEventListener(new ChildEventListener(){
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                            System.out.println(dataSnapshot.getKey());
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });*/
                    CheckIfParentEmailExists(parentEmail, email);
//                    if(parentExists){
//                        Log.d(TAG, "onClick: Parent exists");
//                        registerUser(email, password, confPassword);
//                        user = firebaseAuth.getCurrentUser();
//                        userId = user.getUid();
//                        toastMessage("Child Registered");
//                        reference.child("parents").child(parentsUserId).child("children").child(childKey).child("firstName").setValue(firstName);
//                        reference.child("parents").child(parentsUserId).child("children").child(childKey).child("lastName").setValue(lastName);
//                        reference.child("parents").child(parentsUserId).child("children").child(childKey).child("userId").setValue(userId);


//
//                    }else if(!parentExists){
//                        Log.d(TAG, "onClick: Parent does not exist");
//
//                    }else{
//                        Log.d(TAG, "onClick: Error, parentsExists has no value");
//                    }
//                    //TODO if(parentEmail exists, and child email exists on it)
//                    //TODO registerUser(email, password, confPassword);
//                }else{
//                    toastMessage("You must fill all the fields");
//                }
                }
            }
        });
    }

    public boolean fieldsFilled() {
        boolean filled = false;
        if ((!parentEmailField.getText().toString().equals("")) &&
                (!firstNameField.getText().toString().equals("")) &&
                (!lastNameField.getText().toString().equals("")) &&
                (!emailField.getText().toString().equals("")) &&
                (!passwordField.getText().toString().equals("")) &&
                (!confPasswordField.getText().toString().equals(""))) {
            filled = true;
        }
        return filled;
    }

    public void registerUser(String email, String password, String confPassword) {
        if (password.equals(confPassword)) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegisterChild.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                toastMessage("Registered Successfully");
                            }
                        }
                    });
        } else {
            toastMessage("Provided passwords don't match");
        }
    }

    /*
    TODO at the moment the below method only works because the database is set to readable without authentication, might not be a good idea
     */
    public void CheckIfParentEmailExists(final String parentEmail, final String childEmail) {

        //DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child("parents").orderByChild("email").equalTo(parentEmail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: ");
                if (!dataSnapshot.exists()) {
                    toastMessage("Parent not found");
                    //parentExists = false;
                    //TODO give an error
                }
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    if (singleSnapshot.exists()) {
                        //Parent parent = (Parent) singleSnapshot.getValue();
                        toastMessage("Parent found");
                        parentExists = true;
                        parentsUserId = singleSnapshot.getKey().toString();
                        CheckParentForChild(childEmail);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: ");
            }
        });
    }

    public void CheckParentForChild(final String childEmail) {
        Query query = reference
                .child("parents").child(parentsUserId).child("children").orderByChild("email").equalTo(childEmail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: ");
                if (!dataSnapshot.exists()) {
                    toastMessage("Child not found");
                    parentHasChild = false;
                    //TODO give an error
                }
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    if (singleSnapshot.exists()) {
                        Object child = singleSnapshot;
                        childKey = singleSnapshot.getKey().toString();
                        toastMessage("Child found");
                        parentHasChild = true;
                        registerUser(email, password, confPassword);
                        addChildData(firstName, lastName, userId);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void addChildData(String firstName, String lastName, String userId) {
        reference.child("parents").child(parentsUserId).child("children").child(childKey).child("firstName").setValue(firstName);
        reference.child("parents").child(parentsUserId).child("children").child(childKey).child("lastName").setValue(lastName);
        reference.child("parents").child(parentsUserId).child("children").child(childKey).child("userId").setValue(userId);
        toastMessage("Child data added");
    }

    /*public void SearchParentsEmail(String email) {
        Query query = reference.child("parents").orderByChild("emailAddress").equalTo(email);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Do something with the individual node here`enter code here`
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

