package com.ditkevinstreet.createaccountscreen.NotificationStuff;

import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Admin on 13/01/2018.
 */

public class MessageSender {

    public RemoteMessage remoteMessage;
    public String to;

    public MessageSender() {
    }

    public MessageSender(String to, RemoteMessage remoteMessage) {
        this.remoteMessage = remoteMessage;
        this.to = to;
    }

    public RemoteMessage getRemoteMessage() {
        return remoteMessage;
    }

    public void setRemoteMessage(RemoteMessage remoteMessage) {
        this.remoteMessage = remoteMessage;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    }
