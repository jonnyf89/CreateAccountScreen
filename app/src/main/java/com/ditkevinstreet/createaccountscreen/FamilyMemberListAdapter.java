//package com.ditkevinstreet.createaccountscreen;
//
//import android.content.Context;
//import android.text.Layout;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.CheckBox;
//import android.widget.TextView;
//
//import org.w3c.dom.Text;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by Admin on 20/12/2017.

// */
//
//public class FamilyMemberListAdapter extends BaseAdapter {
//    private static final String TAG = "FamilyMemberListAdapter";
//
//    private final List<FamilyMemberListItem> mItems;
//    final Context mContext;
//
//    public FamilyMemberListAdapter(List<FamilyMemberListItem> mItems, Context context){
//        this.mItems = mItems;
//        this.mContext = context;
//
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        final Child child = (Child) getItem(position);
//
//        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View itemLayout = inflater.inflate(R.layout.family_member_list_item, parent, false);
//
//        final TextView listItemTextView = (TextView) itemLayout.findViewById(R.id.nameField);
//        listItemTextView.setText(child.getFirstName());
//        final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.listItemCheckBox);
//
//        return itemLayout;
//    }
//
//
//        @Override
//    public int getCount() {
//        return 0;
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//}
