package com.ditkevinstreet.createaccountscreen.NotificationStuff;

import android.app.PendingIntent;
import android.content.Intent;

import com.ditkevinstreet.createaccountscreen.Reminder;

/**
 * Created by Admin on 11/01/2018.
 */

public class Notification {
    public String body;


    public Notification(String body) {

        this.body = body;
    }
    public void setBody(String body) {
        this.body = body;
    }
}
