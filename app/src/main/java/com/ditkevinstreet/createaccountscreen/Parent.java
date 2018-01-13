package com.ditkevinstreet.createaccountscreen;

import android.content.Intent;

/**
 * Created by Admin on 22/10/2017.
 */

public class Parent {

    public final static String FIRSTNAME = "firstName";
    public final static String LASTNAME = "lastName";
    public final static String EMAIL = "email";

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Object[] children;
    private String deviceToken;

    public Parent(){
        firstName = "firstName";
        lastName = "lastName";
        children = new Object[]{};
    }
    public Parent(String firstName, String lastName, String email, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
    public Parent(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Object[] getChildren() {
        return children;
    }
    public void setChildren(Object[] children) {
        this.children = children;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public static void packageIntent(Intent intent, String firstName, String lastName, String email){
        intent.putExtra(Parent.FIRSTNAME, firstName);
        intent.putExtra(Parent.LASTNAME, lastName);
        intent.putExtra(Parent.EMAIL, email);

    }
}

