package com.ditkevinstreet.createaccountscreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ditkevinstreet.createaccountscreen.Service.RingtonePlayingService;

/**
 * Created by Admin on 13/01/2018.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("In onReceive", "AlarmReceiver");

        String alarmState = intent.getExtras().getString("extra");
        Log.e("The alarm state is", alarmState);


        //grab data from intent
        String title = intent.getStringExtra("TITLE");
        String description = intent.getStringExtra("DESCRIPTION");
        int day = intent.getIntExtra("DAY", 0);
        int month = intent.getIntExtra("MONTH", 13);
        int year = intent.getIntExtra("YEAR", 0);
        int minute = intent.getIntExtra("MINUTE",61);
        int hour = intent.getIntExtra("HOUR",25);
        String creatorUserId = intent.getStringExtra("CREATORUSERID");
        boolean creatorWantsNotification = intent.getBooleanExtra("CREATORWANTSNOTIFICATION", false);



        //create intent to ringtone service
        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);
        serviceIntent.putExtra("TITLE", title);
        serviceIntent.putExtra("DESCRIPTION", description);
        serviceIntent.putExtra("DAY", day);
        serviceIntent.putExtra("MONTH", month);
        serviceIntent.putExtra("YEAR", year);
        serviceIntent.putExtra("MINUTE", minute);
        serviceIntent.putExtra("HOUR", hour);
        serviceIntent.putExtra("CREATORUSERID", creatorUserId);
        serviceIntent.putExtra("CREATORWANTSNOTIFICATION", creatorWantsNotification);
        serviceIntent.putExtra("ALARMSTATE", alarmState);
        context.startService(serviceIntent);
    }
}
