package com.ditkevinstreet.createaccountscreen.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ditkevinstreet.createaccountscreen.R;
import com.ditkevinstreet.createaccountscreen.ReminderDetail;

import java.security.Provider;

/**
 * Created by Admin on 13/01/2018.
 */

public class RingtonePlayingService extends Service {
    private static final String TAG = "RingtonePlayingService";

    MediaPlayer media_song;
    int startId;
    private boolean isRunning;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.e(TAG, "onStartCommand");



//        String reminderId = intent.getStringExtra("REMINDERID");
        String title = intent.getStringExtra("TITLE");
        String description = intent.getStringExtra("DESCRIPTION");
        int day = intent.getIntExtra("DAY", 0);
        int month = intent.getIntExtra("MONTH", 13);
        int year = intent.getIntExtra("YEAR", 0);
        int minute = intent.getIntExtra("MINUTE", 61);
        int hour = intent.getIntExtra("HOUR", 25);
        String creatorUserId = intent.getStringExtra("CREATORUSERID");
        boolean creatorWantsNotification = intent.getBooleanExtra("CREATORWANTSNOTIFICATION", false);
        String alarmState = intent.getStringExtra("ALARMSTATE");

        Intent intentDetail = new Intent(this, ReminderDetail.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentDetail.putExtra("TITLE", title);
        intentDetail.putExtra("DESCRIPTION", description);
        intentDetail.putExtra("DAY", day);
        intentDetail.putExtra("MONTH", month);
        intentDetail.putExtra("YEAR", year);
        intentDetail.putExtra("MINUTE", minute);
        intentDetail.putExtra("HOUR", hour);
        intentDetail.putExtra("CREATORUSERID", creatorUserId);
        intentDetail.putExtra("CREATORWANTSNOTIFICATION", creatorWantsNotification);

        PendingIntent pendingIntentDetail = PendingIntent.getActivity(this, 0, intentDetail, PendingIntent.FLAG_ONE_SHOT);
        if(alarmState.equals("alarm on")) {
            Notification notificationPopup = new Notification.Builder(this)
                    .setContentTitle("Alarm is ringing")
                    .setContentText("Click me to silence")
                    .setContentIntent(pendingIntentDetail)
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .build();
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationPopup);
        }
        //convert alarmState from String to start ids, 0 or 1
        assert alarmState != null;
        switch (alarmState) {
            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                break;
            default:
                startId = 0;
                break;
        }

        //if alarm is not sounding, and alarm gets set
        if(!this.isRunning && startId == 1) {
            Log.d(TAG, "logMessage: alarm set");

            //create media player
            media_song = MediaPlayer.create(this, R.raw.twilight_piano);
            media_song.start();

            this.isRunning = true;
            this.startId = 0;
        }else if (this.isRunning && startId == 0) {
            Log.d(TAG, "logMessage : alarm silenced");
            //stop the ringtone
            media_song.stop();
            media_song.reset();

            this.isRunning = false;
            this.startId = 0;

        }
        //if user presses stop alarm but alarm isnt sounding
        else if (!this.isRunning && startId ==0) {
            Log.d(TAG, "logMessage : alarm silenced but isnt sounding");

            this.isRunning = false;
            this.startId = 0;
        }
        //if alarm is sounding while an alarm is set
        else if (this.isRunning && startId == 1) {
            Log.d(TAG, "logMessage: alarm set(while another sounds)");
            //create media player
            media_song = MediaPlayer.create(this, R.raw.twilight_piano);
            media_song.start();

            this.isRunning = true;
            this.startId = 0;
        }
            //just in case
        else{
            Log.d(TAG, "logMessage : reached last else somehow");
        }


        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestory: called");
    }
}
