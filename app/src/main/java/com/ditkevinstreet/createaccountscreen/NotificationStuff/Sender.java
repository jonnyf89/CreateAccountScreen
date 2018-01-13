package com.ditkevinstreet.createaccountscreen.NotificationStuff;


import com.ditkevinstreet.createaccountscreen.Reminder;

/**
 * Created by Admin on 11/01/2018.
 */

public class Sender {
    public Notification notification;
    public String to;

    public Sender() {
    }

    public Sender(String to, Notification notification, Reminder reminder) {
        this.notification = notification;
        this.to = to;
    }

    public Sender(String to, Notification notification) {
        this.notification = notification;
        this.to = to;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
