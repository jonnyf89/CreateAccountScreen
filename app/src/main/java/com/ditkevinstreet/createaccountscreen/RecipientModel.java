package com.ditkevinstreet.createaccountscreen;

/**
 * Created by Admin on 20/12/2017.
 * TODO this works, not sure why my adapter and model equivelant class dont
 */

public class RecipientModel {
    String name;
    int value; /* 0 -&gt; checkbox disable, 1 -&gt; checkbox enable */

    public RecipientModel(String name, int value){
        this.name = name;
        this.value = value;
    }
    public String getName(){
        return this.name;
    }
    public int getValue(){
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
