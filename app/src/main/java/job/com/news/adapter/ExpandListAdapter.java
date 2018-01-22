package job.com.news.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import job.com.news.R;

/**
 * Created by POOJA on 1/19/2018.
 */

public class ExpandListAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    public ExpandListAdapter(Context context, List<String> listDataHeader,
                             HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
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


        if (groupPosition == 2 || groupPosition == 3) {
            final String childText = (String) getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_child, null);
            }

            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.lblListItem);
            txtListChild.setText(childText);
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupPosition == 2 || groupPosition == 3) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
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
        } else if (headerTitle.equals("User Profile")) {
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
