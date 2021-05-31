package com.kozdemir.encryptednotes.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.kozdemir.encryptednotes.R;
import com.kozdemir.encryptednotes.pojo.Note;
import com.kozdemir.encryptednotes.pojo.Crypt;
import com.kozdemir.encryptednotes.pojo.Sabitler;

import java.util.ArrayList;
import java.util.HashMap;

public class ExpListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<String> listGroup;
    private HashMap<String, ArrayList<Note>> listItem;
    //LayoutInflater inflater;

    public ExpListAdapter(Context context, ArrayList<String> listGroup, HashMap<String, ArrayList<Note>> listItem) {
        super();
        this.context = context;
        this.listGroup = listGroup;
        this.listItem = listItem;

    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listItem.get(this.listGroup.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
           return childPosition;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {

         Note note = (Note) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.list_child);
        Crypt crypt = new Crypt();
        try {
            textView.setText(crypt.decrypt(note.getTitle(), Sabitler.loginPassword));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listItem.get(this.listGroup.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listGroup.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return listGroup.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String group = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_grup, null);
        }
        Crypt crypt = new Crypt();
        TextView textView= (TextView) convertView.findViewById(R.id.list_parent);

        try {
            textView.setText(crypt.decrypt(group, Sabitler.loginPassword));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return true;
    }

}
