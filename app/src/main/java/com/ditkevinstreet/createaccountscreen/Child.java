package com.ditkevinstreet.createaccountscreen;

/**
 * Created by Admin on 22/10/2017.
 */

public class Child {
    private String name;
    private String parent;
    private String email;
    private String password;
    private String gender;

    public Child(){

    }
    public Child(String name, String parent, String email, String password, String gender){
        this.name=name;
        this.parent=parent;
        this.email=email;
        this.password=password;
        this.gender = gender;
    }
    public Child(String name, String gender, String email){
        this.name=name;
        this.email=email;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
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
