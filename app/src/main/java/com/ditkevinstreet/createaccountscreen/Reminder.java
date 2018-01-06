package com.ditkevinstreet.createaccountscreen;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Admin on 21/12/2017.
 * Firebase will not accept a calendar object, to deal with this I will convert the date into seperate ints for
 * minute, hour, day, month and year
 */

public class Reminder {
    private UUID Id;
    private int minute;
    private int hour;
    private int day;
    private int month;
    private int year;
    private String creatorUserId;
    private String title;
    private String description;
    private boolean creatorWantsNotification;

    public Reminder(Calendar reminderTime, String creatorUserId, /*ArrayList<Child> recipientsArrayList,*/ String title, String description ){
        this.Id = UUID.randomUUID();
        this.minute = reminderTime.get(Calendar.MINUTE);
        this.hour = reminderTime.get(Calendar.HOUR_OF_DAY);
        this.day = reminderTime.get(Calendar.DAY_OF_MONTH);
        this.month = reminderTime.get(Calendar.MONTH);
        this.year = reminderTime.get(Calendar.YEAR);
        this.creatorUserId = creatorUserId;
        this.title = title;
        this.description = description;

        //setting up recipients, needs to be an array rather than an arrayList
//        this.recipients = new Child[recipientsArrayList.size()];
//        for(int i = 0; i < recipientsArrayList.size(); i++){
//            recipients[i] = recipientsArrayList.get(i);
//        }
    }
    public Reminder(){

    }

    public boolean getCreatorWantsNotification() {
        return creatorWantsNotification;
    }

    public void setCreatorWantsNotification(boolean creatorWantsNotification) {
        this.creatorWantsNotification = creatorWantsNotification;
    }
    public UUID getId(){
        return Id;
    }

    public int getMinute() {
        return minute;
    }

    public int getHour() {
        return hour;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public String getCreatorUserId() {
        return creatorUserId;
    }

//    public Child[] getRecipients() {
//        return recipients;
//    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
