package job.com.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import job.com.news.R;
import job.com.news.utils.SecondLevelExpandableListView;

/**
 * Created by Zafar.Hussain on 02/02/2018.
 */

public class ThreeLevelListAdapter extends BaseExpandableListAdapter {

    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private Context context;
    List<HashMap<String, List<String>>> data;

    public ThreeLevelListAdapter(Context context,  List<String> listDataHeader, HashMap<String, List<String>> listDataChild, List<HashMap<String, List<String>>>  data) {
        this.context = context;

        this.listDataHeader = listDataHeader;

        this.listDataChild = listDataChild;

        this.data = data;
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {


        // no idea why this code is working

        return 1;

    }

    @Override
    public Object getGroup(int groupPosition) {

        return groupPosition;
    }

    @Override
    public Object getChild(int group, int child) {


        return child;


    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
       // String headerTitle = (String) getGroup(groupPosition);
        String headerTitle = listDataHeader.get(groupPosition);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_group, null);
        TextView text = (TextView) convertView.findViewById(R.id.lblListHeader);
        text.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final SecondLevelExpandableListView secondLevelELV = new SecondLevelExpandableListView(context);

        List<String> headers = listDataChild.get(groupPosition);


        List<List<String>> childData = new ArrayList<>();
        HashMap<String, List<String>> secondLevelData=data.get(groupPosition);

        for(String key : secondLevelData.keySet())
        {


            childData.add(secondLevelData.get(key));

        }



        secondLevelELV.setAdapter(new SecondLevelAdapter(context, headers,childData));

        secondLevelELV.setGroupIndicator(null);


        secondLevelELV.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition != previousGroup)
                    secondLevelELV.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });


        return secondLevelELV;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}