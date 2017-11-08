package com.ditkevinstreet.createaccountscreen;

import java.util.ArrayList;

/**
 * Created by Admin on 22/10/2017.
 */

public class UserInformation {

    private String firstName;
    private String lastName;
    private String nickname;
    private ArrayList<Object> children;

    public UserInformation(){

    }
    public UserInformation(String firstName, String lastName, String nickname){
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickname = nickname;

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
}
