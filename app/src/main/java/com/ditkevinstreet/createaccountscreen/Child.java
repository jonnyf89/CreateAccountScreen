package com.ditkevinstreet.createaccountscreen;

/**
 * Created by Admin on 22/10/2017.
 */

public class Child implements IUser {
    private String firstName;
    private String lastName;
    //private String parent;
    private Parent parent;
    private String email;
    private String password;
    //private String gender;
    private String userID;//needed?

    public Child(){

    }

    public Child(String email, Parent parent){
        this.email=email;
        this.parent=parent;
    }
    public Child(String firstName, String lastName, Parent parent, String email, String password /*,String gender*/){
        this.firstName=firstName;
        this.lastName=lastName;
        this.parent=parent;
        this.email=email;
        this.password=password;
        //this.gender = gender;
    }
    public Child(String firstName, String lastName /*,String gender*/, String email){
        this.firstName=firstName;
        this.lastName=lastName;
        this.email=email;
       //this.gender = gender;
    }
    public Child(String email, String firstName, String lastName, Parent parent){
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.parent = parent;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String name) {
        this.lastName = name;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
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
}
