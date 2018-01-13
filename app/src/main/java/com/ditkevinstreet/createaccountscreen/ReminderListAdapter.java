package com.ditkevinstreet.createaccountscreen;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Admin on 02/01/2018.
 */

public class ReminderListAdapter extends ArrayAdapter<ReminderListItemModel> {
    private static final String TAG = "ReminderListAdapter";

    private final ArrayList<ReminderListItemModel> mItems;
    private final ArrayList<Reminder> reminderList;
    private final Context context;

    public ReminderListAdapter(Context context, ArrayList<ReminderListItemModel> resource, ArrayList<Reminder> reminderList){
        super(context, R.layout.reminder_list_item, resource);
        this.context = context;
        this.mItems = resource;
        this.reminderList = reminderList;//TODO why do we need this list of reminder objects?  SO we can view details of the reminder by clicking it

    }

    public View getView(int position, View convertView, ViewGroup parent){
        final ReminderListItemModel model = (ReminderListItemModel) getItem(position);
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.reminder_list_item, parent, false);

        TextView time = (TextView) convertView.findViewById(R.id.listReminderTimeField);
        TextView title = (TextView) convertView.findViewById(R.id.listReminderTitleField);
        time.setText(mItems.get(position).getTime());
        title.setText(mItems.get(position).getTitle());

        return convertView;
    }

    public Reminder getReminder(int position){
        return reminderList.get(position);
    }//TODO why do I have this? SO we can view details of the reminder by clicking it
}
