package com.org.apnanews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.org.apnanews.R;
import com.org.apnanews.models.NewsFeedDetails;

/**
 * Created by POOJA on 1/25/2018.
 */

public class HomeDashboardAdapter extends RecyclerView.Adapter<HomeDashboardAdapter.ViewHolder> {
    Context mContext;
    List<NewsFeedDetails> mNewsFeedList;


    public HomeDashboardAdapter(Context mContext, List<NewsFeedDetails> mNewsFeedList) {
        this.mContext = mContext;
        this.mNewsFeedList = mNewsFeedList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.news_layout_item, parent, false);
        ViewHolder v = new ViewHolder(view);
        return v;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtPostPerson.setText(mNewsFeedList.get(position).getNews_post_person());
        holder.txtPostTime.setText(mNewsFeedList.get(position).getTime());
        holder.txtTitle.setText(mNewsFeedList.get(position).getTitle());
        holder.txtCity.setText(mNewsFeedList.get(position).getCity());
        holder.txtState.setText(mNewsFeedList.get(position).getState());
        holder.ivNewsItem.setImageDrawable(mNewsFeedList.get(position).getImage());
        //holder.txtNewsCategory.setText();
    }

    @Override
    public int getItemCount() {
        return mNewsFeedList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivNewsItem;
        TextView txtPostPerson, txtPostTime, txtTitle, txtCity, txtState, txtReadMore,txtNewsCategory;

        public ViewHolder(View itemView) {
            super(itemView);
            ivNewsItem = (ImageView) itemView.findViewById(R.id.iv_news_item_photo);
            txtPostPerson = (TextView) itemView.findViewById(R.id.txt_post_person_name);
            txtPostTime = (TextView) itemView.findViewById(R.id.txt_post_time);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_title);
            txtCity = (TextView) itemView.findViewById(R.id.txt_city);
            txtState = (TextView) itemView.findViewById(R.id.txt_state);
            txtReadMore = (TextView) itemView.findViewById(R.id.txt_read_more);
            txtNewsCategory = (TextView) itemView.findViewById(R.id.txt_news_category);
        }
    }
}
