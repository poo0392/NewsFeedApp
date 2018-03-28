package job.com.news.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import job.com.news.HomeActivity;
import job.com.news.R;
import job.com.news.utils.SecondLevelExpandableListView;

/**
 * Created by POOJA on 1/19/2018.
 */

public class ExpandListAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private HashMap<String, List<String>> listThirdLevelChild;

    public ExpandListAdapter(Context context, List<String> listDataHeader,
                             HashMap<String, List<String>> listChildData, HashMap<String, List<String>> listThirdLevelChild) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.listThirdLevelChild = listThirdLevelChild;
        Log.v("ExpandListAdapter ", "Size" + listThirdLevelChild.size());
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);
        if (groupPosition == 2 /*|| groupPosition == 3*/) {

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_child, null);
            }

            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.lblListItem);
            txtListChild.setText(childText);

            return convertView;
        } else if ((childText.equals("Small Classified")) || (childText.equals("Career Related"))) {
            // if (convertView != null) {
            Log.v("", "childPosition " + childPosition);
            Log.v("", "groupPosition " + _listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosition));
            final SecondLevelExpandableListView secondLevelELV = new SecondLevelExpandableListView(_context);
            String header = _listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosition);

            header = header.replace("[", "").replace("]", "");
            String str[] = header.split(",");
            Log.v("", "header " + header);
            List<String> headers = new ArrayList<>();
            headers = Arrays.asList(str);


            Log.v("", "headers " + headers.toString());


            List<List<String>> childData = new ArrayList<>();
            HashMap<String, List<String>> secondLevelData = listThirdLevelChild;

          //  for (String key : listThirdLevelChild.keySet()) {
                childData.add(listThirdLevelChild.get(childText));
                Log.v("", "childData " + childData);

           // }
            secondLevelELV.setIndicatorBounds(secondLevelELV.getRight() - 40, secondLevelELV.getWidth());
                secondLevelELV.setGroupIndicator(null);
           // setGroupIndicatorToRight(secondLevelELV);

            secondLevelELV.setAdapter(new SecondLevelAdapter(_context, headers, childData));


            secondLevelELV.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                int previousGroup = -1;

                @Override
                public void onGroupExpand(int groupPosition) {
                    if (groupPosition != previousGroup)
                        secondLevelELV.collapseGroup(previousGroup);
                    previousGroup = groupPosition;
                }
            });


            return secondLevelELV;
            //} else {

        } else {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_child, null);

            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.lblListItem);
            txtListChild.setText(childText);

            return convertView;

        }
    }

    private void setGroupIndicatorToRight(SecondLevelExpandableListView secondLevelELV) {
    /* Get the screen width */
        DisplayMetrics dm = new DisplayMetrics();
        ((HomeActivity) _context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;

        secondLevelELV.setIndicatorBounds(width - getDipsFromPixel(35), width - getDipsFromPixel(5));
    }

    // Convert pixel to dip
    public int getDipsFromPixel(float pixels) {
        // Get the screen's density scale
        final float scale = _context.getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupPosition == 2 || groupPosition == 3) {
            return _listDataChild.get(_listDataHeader.get(groupPosition)).size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return _listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return _listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        ImageView ivListHeader = (ImageView) convertView.findViewById(R.id.iv_group_item);
        ImageView ivGroupIndicator = (ImageView) convertView.findViewById(R.id.ivGroupIndicator);
        ivListHeader.setColorFilter(_context.getResources().getColor(R.color.DarkGray));
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        if (headerTitle.equals("Home")) {
            ivListHeader.setImageDrawable(_context.getResources().getDrawable(R.mipmap.ic_home));
            // ivGroupIndicator.setSelected(false);
            ivGroupIndicator.setVisibility(View.GONE);
        } else if (headerTitle.equals("Profile")) {
            ivListHeader.setImageDrawable(_context.getResources().getDrawable(R.mipmap.ic_profile));
            //  ivGroupIndicator.setSelected(false);
            ivGroupIndicator.setVisibility(View.GONE);
        } else if (headerTitle.equals("Requests")) {
            ivListHeader.setImageDrawable(_context.getResources().getDrawable(R.mipmap.ic_request));
            ivGroupIndicator.setVisibility(View.VISIBLE);
            ivGroupIndicator.setSelected(isExpanded);
        } else if (headerTitle.equals("News Categories")) {
            ivListHeader.setImageDrawable(_context.getResources().getDrawable(R.mipmap.ic_news));
            ivGroupIndicator.setVisibility(View.VISIBLE);
            ivGroupIndicator.setSelected(isExpanded);
        } else {
            ivListHeader.setImageDrawable(_context.getResources().getDrawable(R.mipmap.ic_about_us));
            ivGroupIndicator.setSelected(false);
            ivGroupIndicator.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
