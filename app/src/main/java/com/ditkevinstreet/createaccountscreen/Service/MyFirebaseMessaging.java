package com.ditkevinstreet.createaccountscreen.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.ditkevinstreet.createaccountscreen.AddReminder;
import com.ditkevinstreet.createaccountscreen.LogInScreen;
import com.ditkevinstreet.createaccountscreen.R;
import com.ditkevinstreet.createaccountscreen.Reminder;
import com.ditkevinstreet.createaccountscreen.ReminderDetail;
import com.ditkevinstreet.createaccountscreen.WelcomeScreen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.lang.reflect.Array;
import java.util.Map;
import java.util.Set;

/**
 * Created by Admin on 11/01/2018.
 */

public class MyFirebaseMessaging extends FirebaseMessagingService {

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

@Override
public void handleIntent(Intent intent){
    final Intent theIntent = intent;

    mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    Bundle extras = theIntent.getExtras();
    String reminderId = extras.getString("gcm.notification.body");
    if (user == null) {
        Intent notLoggedInIntent = new Intent(getApplicationContext(), LogInScreen.class);
        notLoggedInIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notLoggedInIntent.putExtra("LOGGEDINSTATE", "notLoggedIn");
        notLoggedInIntent.putExtra("REMINDERID", reminderId);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notLoggedInIntent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Calendar Update Received")
                .setContentText("Warning, you are not logged in")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }
    else{
        showNotification(reminderId);
    }



}

    private void showNotification(String reminderId) {

        Intent intent = new Intent(this, ReminderDetail.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("REMINDERID", reminderId);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Calendar Notification")
                .setContentText("A new reminder has been added to your calendar")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

//
//    private void showNotification(RemoteMessage.Notification notification) {
//
//        Intent intent = new Intent(this, ReminderDetail.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("REMINDERID", notification.getBody());
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher_round)
//                .setContentTitle("Calendar Notification")
//                .setContentText("A new reminder has been added to your calendar")
//                .setAutoCancel(true)
//                .setContentIntent(pendingIntent);
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        notificationManager.notify(0, builder.build());
//    }
//    private void showNotification(String reminderId) {
//        Intent intent = new Intent(this, ReminderDetail.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("REMINDERID", reminderId);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher_round)
//                .setContentTitle("Calendar Notification")
//                .setContentText("A new reminder has been added to your calendar")
//                .setAutoCancel(true)
//                .setContentIntent(pendingIntent);
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        notificationManager.notify(0, builder.build());
//
//    }
}
