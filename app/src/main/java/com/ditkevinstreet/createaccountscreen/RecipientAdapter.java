package com.ditkevinstreet.createaccountscreen;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Admin on 20/12/2017.
 * TODO this works, not sure why my adapter and model equivelant class dont
 */

//public class RecipientAdapter extends ArrayAdapter<FamilyMemberListItem>{
public class RecipientAdapter extends ArrayAdapter<RecipientModel>{
    private static final String TAG = "RecipientAdapter";

    ArrayList<RecipientModel> mItems;
    Context context;


    public RecipientAdapter(Context context, ArrayList<RecipientModel> resource) {
        super(context,R.layout.family_member_list_item,resource);
        this.context = context;
        this.mItems = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final RecipientModel recipientModel = (RecipientModel) getItem(position);
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.family_member_list_item, parent, false);

        TextView name = (TextView) convertView.findViewById(R.id.nameField);
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.listItemCheckBox);
        name.setText(mItems.get(position).getName());


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "onCheckedChanged: ");

                if(isChecked){
                    recipientModel.setValue(1);
                }else{
                    recipientModel.setValue(0);
                }
            }
        });



        return convertView;
    }
}