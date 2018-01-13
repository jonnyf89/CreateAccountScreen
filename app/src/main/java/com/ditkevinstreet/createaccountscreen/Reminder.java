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
    private String Id;
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
        UUID realId = UUID.randomUUID();

        this.Id = UUID.randomUUID().toString();
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
    public Reminder(String Id, String creatorUserId, boolean creatorWantsNotification, int day, String description, int hour,
                    int minute, int month, String title, int year){
        this.Id = Id;
        this.creatorUserId = creatorUserId;
        this.creatorWantsNotification = creatorWantsNotification;
        this.day = day;
        this.description = description;
        this.hour = hour;
        this.minute = minute;
        this.month = month;
        this.title = title;
        this.year = year;
    }
    public Reminder(){

    }

    public boolean getCreatorWantsNotification() {
        return creatorWantsNotification;
    }

    public void setCreatorWantsNotification(boolean creatorWantsNotification) {
        this.creatorWantsNotification = creatorWantsNotification;
    }
    public String getId(){
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

    public void setId(String id) {
        Id = id;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setCreatorUserId(String creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
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
