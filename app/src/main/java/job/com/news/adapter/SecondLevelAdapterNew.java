package job.com.news.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by POOJA on 3/4/2018.
 */

public class SecondLevelAdapterNew extends BaseExpandableListAdapter {
    private Context context;
    private HashMap<String, List<String>> listThirdLevelChild;

    public SecondLevelAdapterNew(Context context, HashMap<String, List<String>> listThirdLevelChild) {
        this.context = context;
        this.listThirdLevelChild = listThirdLevelChild;
    }

    @Override

    public Object getChild(int groupPosition, int childPosition) {

        return childPosition;

    }


    @Override

    public long getChildId(int groupPosition, int childPosition) {

        return childPosition;

    }


    @Override

    public View getChildView(int groupPosition, int childPosition,

                             boolean isLastChild, View convertView, ViewGroup parent) {
        TextView tv = new TextView(context);
        if (groupPosition == 11) {

            tv.setText(listThirdLevelChild.get("Small Classifieds").get(childPosition));


        } else {

            tv.setText(listThirdLevelChild.get("Career Related").get(childPosition));
        }
        tv.setTextColor(Color.BLACK);

        tv.setTextSize(20);

        tv.setPadding(15, 5, 5, 5);

        tv.setBackgroundColor(Color.WHITE);

        tv.setLayoutParams(new ListView.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));

        return tv;
    }

    @Override

    public int getChildrenCount(int groupPosition) {

        return listThirdLevelChild.get(groupPosition).size();

    }


    @Override

    public Object getGroup(int groupPosition) {

        return groupPosition;

    }


    @Override

    public int getGroupCount() {

        return 2;

    }


    @Override

    public long getGroupId(int groupPosition) {

        return groupPosition;

    }


    @Override

    public View getGroupView(int groupPosition, boolean isExpanded,

                             View convertView, ViewGroup parent) {

        TextView tv = new TextView(context);

        tv.setText("-->Second Level");

        tv.setTextColor(Color.BLACK);

        tv.setTextSize(20);

        tv.setPadding(12, 7, 7, 7);

        tv.setBackgroundColor(Color.RED);


        return tv;

    }


    @Override

    public boolean hasStableIds() {

        // TODO Auto-generated method stub

        return true;

    }


    @Override

    public boolean isChildSelectable(int groupPosition, int childPosition) {

        // TODO Auto-generated method stub

        return true;

    }


}