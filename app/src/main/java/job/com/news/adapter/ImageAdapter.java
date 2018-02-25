package job.com.news.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import job.com.news.NewsDetailScreen;
import job.com.news.NewsFeedApplication;
import job.com.news.R;
import job.com.news.db.DBHelper;
import job.com.news.db.MemberTable;
import job.com.news.interfaces.ItemClickListener;
import job.com.news.models.NewsFeedList;
import job.com.news.register.RegisterMember;

/**
 * Created by POOJA on 1/27/2018.
 */

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    //private OnLoadMoreListener mOnLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    Context mContext;
    private NewsFeedApplication newsFeedApplication;
    List<NewsFeedList> newsFeedList;
    List<RegisterMember> memberList;
    DBHelper db;
    MemberTable memberTable;
    //changes added

    public ImageAdapter(Context mContext, List<NewsFeedList> newsFeedList) {
        newsFeedApplication = NewsFeedApplication.getApp();
        this.mContext = mContext;
        this.newsFeedList = newsFeedList;
        memberTable = new MemberTable(mContext);
        memberList = new ArrayList<>();
            /*final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (mOnLoadMoreListener != null) {
                            mOnLoadMoreListener.onLoadMore();
                        }
                        isLoading = true;

                    }
                }
            });*/
    }

        /*public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
            this.mOnLoadMoreListener = mOnLoadMoreListener;
        }*/

       /* @Override
        public int getItemViewType(int position) {
            return app.mImageDetails.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
        }*/


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.news_feed_item, parent, false);
            return new ImageViewHolder(view);
        } /*else if (viewType == VIEW_TYPE_LOADING) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.layout_loading_item, parent, false);
                return new LoadingViewHolder(view);
            }*/
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ImageViewHolder) {
                /*ImageDetails user = app.mImageDetails.get(position);
                ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
                String imageUrl = user.getImg_url();
                Uri imageUri = Uri.parse(imageUrl);

                imageViewHolder.draweeView.setImageURI(imageUri);

                imageViewHolder.textViewTitle.setText(user.getTitle());
                imageViewHolder.textViewDistance.setText(user.getDistance());
                imageViewHolder.textViewUnit.setText(user.getUnit());*/

            ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
/*
            ArrayList<String> list = newsFeedApplication.hashMap.get("" + position);
            imageViewHolder.textViewSummary.setText(list.get(0));
            imageViewHolder.textViewDate.setText(list.get(2));*/
Log.v("","clickedPosMemberID "+Integer.parseInt(newsFeedList.get(position).getMember_id()));
            memberList = memberTable.getMemberListByMemberId(Integer.parseInt(newsFeedList.get(position).getMember_id()));


            //  String member_name=newsFeedList.get(position).getMember().getFirstName();
            String member_name = memberList.get(0).getFirstName();

//get Member from member_id in news List i.e select member from member_table where member_id = NewsListTable.Member_id;
            // RegisterMember
            imageViewHolder.txt_post_person_name.setText(member_name);
            imageViewHolder.textViewSummary.setText(newsFeedList.get(position).getNews_title());
            imageViewHolder.txt_city.setText(newsFeedList.get(position).getCity());
            imageViewHolder.txt_news_category.setText(newsFeedList.get(position).getCategory());
            imageViewHolder.txt_desc.setText(newsFeedList.get(position).getNews_description());
            imageViewHolder.txt_state.setText(newsFeedList.get(position).getState());

            if (position == 0) {
                //lebaon
                imageViewHolder.imageView.setBackgroundResource(R.drawable.lebanon);
            } else if (position == 1) {
                //india us
                imageViewHolder.imageView.setBackgroundResource(R.drawable.india_us);
            } else if (position == 2) {
                //yogi
                imageViewHolder.imageView.setBackgroundResource(R.drawable.yogi_adinath);
            } else if (position == 3) {
                //india pak
                imageViewHolder.imageView.setBackgroundResource(R.drawable.india_pak_border);
            } else if (position == 4) {
                //kuldeep
                imageViewHolder.imageView.setBackgroundResource(R.drawable.kuldeep_yadav);
            }


            imageViewHolder.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    if (isLongClick) {
                        // Toast.makeText(mContext, "#" + position + " (Long click)", Toast.LENGTH_SHORT).show();
                    } else {
                        //Toast.makeText(mContext, "#" + position, Toast.LENGTH_SHORT).show();
                        Log.v("Adapter ", "itemPosition " + position);
                        Intent intent = new Intent(mContext, NewsDetailScreen.class);
                        intent.putExtra("itemPosition", position + "");
                        mContext.startActivity(intent);
                    }
                }
            });

        }/* else if (holder instanceof LoadingViewHolder) {
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setIndeterminate(true);
            }*/
    }

    @Override
    public int getItemCount() {
        //return app.mImageDetails == null ? 0 : app.mImageDetails.size();
        //return newsFeedApplication.hashMap.size();
        return newsFeedList.size();
    }

        /*public void setLoaded() {
            isLoading = false;
        }

        public void clearList(){
            app.mImageDetails.clear();
            notifyDataSetChanged();
        }

        public void addList(){
            //addData
            notifyDataSetChanged();
        }*/

    public class ImageViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        TextView textViewSummary, txt_news_category;
        TextView textViewDate;
        ImageView imageView;
        TextView txt_post_person_name, txt_post_time, txt_desc, txt_city, txt_state;
        private ItemClickListener clickListener;

        public ImageViewHolder(View itemView) {
            super(itemView);
            textViewSummary = (TextView) itemView.findViewById(R.id.row_summary);
            textViewDate = (TextView) itemView.findViewById(R.id.row_date);
            imageView = (ImageView) itemView.findViewById(R.id.row_image);

            txt_post_person_name = (TextView) itemView.findViewById(R.id.txt_post_person_name);
            txt_news_category = (TextView) itemView.findViewById(R.id.txt_news_category);
            txt_post_time = (TextView) itemView.findViewById(R.id.txt_post_time);
            txt_desc = (TextView) itemView.findViewById(R.id.txt_desc);
            txt_city = (TextView) itemView.findViewById(R.id.txt_city);
            txt_state = (TextView) itemView.findViewById(R.id.txt_state);


            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }


        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onClick(view, getAdapterPosition(), true);
            return true;
        }
    }

}



