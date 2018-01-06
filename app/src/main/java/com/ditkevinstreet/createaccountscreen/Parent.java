package com.ditkevinstreet.createaccountscreen;

import android.content.Intent;

import java.util.ArrayList;

/**
 * Created by Admin on 22/10/2017.
 */

public class Parent implements IUser {

    public final static String FIRSTNAME = "firstName";
    public final static String LASTNAME = "lastName";
    public final static String NICKNAME = "";
    public final static String EMAIL = "email";

    private String firstName;
    private String lastName;
    private String nickname;
    private String email;
    private String password;
    //private ArrayList<Child> children;
    private Object[] children;
    //private Child[] children;

    public Parent(){
        firstName = "firstName";
        lastName = "lastName";
        //children = new ArrayList<Child>();
        children = new Object[]{};
    }
    public Parent(String firstName, String lastName, String nickname, String email, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickname=nickname;
        this.email = email;
        this.password = password;
    }
    public Parent(String firstName, String lastName, String nickname){
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickname=nickname;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    /*public ArrayList<Child> getChildren() {
        return children;
    }
     public void setChildren(ArrayList<Child> children) {
        this.children = children;
    }

    */

    public Object[] getChildren() {
        return children;
    }
    public void setChildren(Object[] children) {
        this.children = children;
    }




    public static void packageIntent(Intent intent, String firstName, String lastName, String nickname, String email){
        intent.putExtra(Parent.FIRSTNAME, firstName);
        intent.putExtra(Parent.LASTNAME, lastName);
        intent.putExtra(Parent.NICKNAME, nickname);
        intent.putExtra(Parent.EMAIL, email);

    }
}

