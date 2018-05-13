package com.org.apnanews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.org.apnanews.R;

/**
 * Created by Zafar.Hussain on 02/02/2018.
 */

public class SecondLevelAdapter extends BaseExpandableListAdapter {

    private Context context;


    List<List<String>> data;

    List<String> headers;


    public SecondLevelAdapter(Context context, List<String> headers, List<List<String>> data) {
        this.context = context;
        this.data = data;
        this.headers = headers;
    }

    @Override
    public Object getGroup(int groupPosition) {

        return headers.get(groupPosition);
    }

    @Override
    public int getGroupCount() {

        return headers.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_child_child, null);
        ImageView ivGroupIndicator = (ImageView) convertView.findViewById(R.id.ivGroupIndicator);
        TextView text = (TextView) convertView.findViewById(R.id.lbl_ChildHeader);
        String groupText = getGroup(groupPosition).toString();
        text.setText(groupText);
        if (groupText.equals("Small Classified")) {
            ivGroupIndicator.setVisibility(View.VISIBLE);
        } else if (groupText.equals("Career Related")) {
            ivGroupIndicator.setVisibility(View.VISIBLE);
        }else{
            ivGroupIndicator.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        List<String> childData;

        childData = data.get(groupPosition);


        return childData.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.third_row_child, null);

        TextView textView = (TextView) convertView.findViewById(R.id.rowThirdText);

        List<String> childArray = data.get(groupPosition);

        String text = childArray.get(childPosition);

        textView.setText(text);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<String> children = data.get(groupPosition);


        return children.size();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}