package com.ditkevinstreet.createaccountscreen;

/**
 * Created by Admin on 22/10/2017.
 */

public class Parent {
    private String firstName;
    private String lastName;
    private String nickname;
    private String email;
    private String password;
    private String[] children;

    public Parent(){

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

    public String[] getChildren() {
        return children;
    }

    public void setChildren(String[] children) {
        this.children = children;
    }
}

