package job.com.news.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import job.com.news.Constant;
import job.com.news.NewsDetailScreen;
import job.com.news.NewsFeedApplication;
import job.com.news.R;
import job.com.news.db.DBHelper;
import job.com.news.db.MemberTable;
import job.com.news.db.NewsImagesTable;
import job.com.news.interfaces.ItemClickListener;
import job.com.news.interfaces.OnLoadMoreListener;
import job.com.news.interfaces.onButtonClick;
import job.com.news.models.NewsFeedList;
import job.com.news.models.NewsImages;
import job.com.news.register.RegisterMember;

/**
 * Created by POOJA on 1/27/2018.
 */
//changes added on 3/9/2018.
public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 0;
    private final int VIEW_PROG = 1;
    Context mContext;
    private NewsFeedApplication newsFeedApplication;
    List<NewsFeedList> newsFeedList;
    List<RegisterMember> memberList;
    List<NewsImages> imagesList;
    DBHelper db;
    MemberTable memberTable;
    NewsImagesTable newsImagesTable;
    Bitmap decodedByte;
    ProgressDialog mProgressDialog;
    String value_status, from[];
    int pos;
    onButtonClick callback;
    int num = 1;
    String image;
    String load_image, comment;


    // The minimum amount of items to have below your current scroll position
// before loading more.
    private int visibleThreshold = 10;
    private int lastVisibleItem, totalItemCount, mLoadedItems;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;
    private boolean isLoadingAdded = false;


    public ImageAdapter(Context mContext, List<NewsFeedList> newsFeedList, RecyclerView mRecyclerView, String value_status, int mLoadedItems) {
        newsFeedApplication = NewsFeedApplication.getApp();
        this.mContext = mContext;
        this.newsFeedList = newsFeedList;
        this.value_status = value_status;
        this.mLoadedItems = mLoadedItems;
        memberTable = new MemberTable(mContext);
        newsImagesTable = new NewsImagesTable(mContext);
        this.memberList = new ArrayList<>();
        this.imagesList = new ArrayList<>();
        from = value_status.split(":");
        Log.v("", "" + Arrays.toString(from));

      /*  final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (loadMoreListener != null) {
                        loadMoreListener.onLoadMore();
                    }
                    isLoading = true;

                }
            }
        });*/
        //  StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        // StrictMode.setThreadPolicy(policy);
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return newsFeedList.get(position) == null ? VIEW_PROG : VIEW_ITEM;
    }

    public void setOnButtonClick(onButtonClick callback) {
        this.callback = callback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.news_feed_item, parent, false);
            return new ImageViewHolder(view);
        } else if (viewType == VIEW_PROG) {
            // View view = LayoutInflater.from(mContext).inflate(R.layout.layout_loading_item, parent, false);
            View view = LayoutInflater.from(mContext).inflate(R.layout.load_more_progressbar, parent, false);
            return new ProgressViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
            isLoading = true;
            loadMoreListener.onLoadMore();
        }
        if (holder instanceof ImageViewHolder) {
            ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
            pos = position;


            if (from[0].equals("admin") && !from[1].equals("")) {
                if (from[1].equals("pending")) {
                    imageViewHolder.ll_approve_or_dec.setVisibility(View.VISIBLE);
                    imageViewHolder.ll_reject_cmt.setVisibility(View.GONE);
                } else if (from[1].equals("rejected")) {

                    comment = newsFeedList.get(position).getComment();
                    Log.v("Adapter ", "comment " + comment);
                    if (!comment.equals("")) {
                        imageViewHolder.ll_reject_cmt.setVisibility(View.VISIBLE);
                        imageViewHolder.txt_reject_cmt.setText(comment);
                    } else {
                        imageViewHolder.ll_reject_cmt.setVisibility(View.GONE);
                    }

                    imageViewHolder.ll_approve_or_dec.setVisibility(View.GONE);
                } else {
                    imageViewHolder.ll_reject_cmt.setVisibility(View.GONE);
                    imageViewHolder.ll_approve_or_dec.setVisibility(View.GONE);
                }
               /* switch (from[1]) {
                    case "pending":
                        imageViewHolder.ll_approve_or_dec.setVisibility(View.VISIBLE);
                        break;
                    case "approved":
                        imageViewHolder.ll_approve_or_dec.setVisibility(View.GONE);
                        break;
                    case "rejected":
                        imageViewHolder.ll_approve_or_dec.setVisibility(View.VISIBLE);
                        break;

                }*/

            } else {
                imageViewHolder.ll_reject_cmt.setVisibility(View.GONE);
                imageViewHolder.ll_approve_or_dec.setVisibility(View.GONE);
            }
                /*ImageDetails user = app.mImageDetails.get(position);
                ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
                String imageUrl = user.getImg_url();
                Uri imageUri = Uri.parse(imageUrl);

                imageViewHolder.draweeView.setImageURI(imageUri);

                imageViewHolder.textViewTitle.setText(user.getTitle());
                imageViewHolder.textViewDistance.setText(user.getDistance());
                imageViewHolder.textViewUnit.setText(user.getUnit());*/


/*
            ArrayList<String> list = newsFeedApplication.hashMap.get("" + position);
            imageViewHolder.textViewSummary.setText(list.get(0));
            imageViewHolder.textViewDate.setText(list.get(2));*/
            // Log.v("", "clickedPosMemberID " + Integer.parseInt(newsFeedList.get(position).getMember_id()));

            String member_name;
            //  if(!from.equals("fromApi")) {
            memberList = memberTable.getMemberListByMemberId(Integer.parseInt(newsFeedList.get(position).getMember_id()));
            member_name = memberList.get(0).getFirstName();
            //}else {

            //  String member_name=newsFeedList.get(position).getMember().getFirstName();
            member_name = memberList.get(0).getFirstName();
            //  }


//get Member from member_id in news List i.e select member from member_table where member_id = NewsListTable.Member_id;
            // RegisterMember
            imageViewHolder.txt_post_person_name.setText(member_name);
            Log.v("Adapter ", "mLoadedItems " + mLoadedItems);
           /* if (from[0].equals("newsfeed_fragment")) {
                position = mLoadedItems;
            }*/ /*else {
                position = position;
            }*/

            imageViewHolder.textViewSummary.setText(newsFeedList.get(position).getNews_title());
            imageViewHolder.txt_city.setText(newsFeedList.get(position).getCity());
            imageViewHolder.txt_news_category.setText(newsFeedList.get(position).getCategory());
            imageViewHolder.txt_desc.setText(newsFeedList.get(position).getNews_description());
            imageViewHolder.txt_state.setText(newsFeedList.get(position).getState());
            String dateTime = newsFeedList.get(position).getCreated_at();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long time = 0;
            try {
                time = sdf.parse(dateTime).getTime();

            } catch (ParseException e) {
                e.printStackTrace();
            }
            long now = System.currentTimeMillis();
//2018-02-03 14:37:06
            CharSequence ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
            imageViewHolder.txt_post_time.setText(ago);

            imagesList = newsImagesTable.getNewsImagesList(newsFeedList.get(position).getId());
           /* if(from.equals("fromApi")) {
                String pic_name = imagesList.get(45).getNews_pic().toString();
            }*/
            //  for (int k = 0; k < imagesList.size(); k++)
            if (!imagesList.isEmpty() && imagesList.size() > 0) {
                image = imagesList.get(0).getNews_pic();
                load_image = Constant.IMAGE_URL + "/" + image;
                Log.v("Adapter ", "load_image" + load_image);
                //new DownloadImageTask(imageViewHolder.imageView).execute(load_image);

                //   progressBar.setVisibility(View.VISIBLE);
                // Hide progress bar on successful load
                Picasso.with(mContext).load(load_image)
                        .placeholder(R.drawable.default_no_image)
                        .into(imageViewHolder.imageView/*, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                if (progressBar != null) {
                                    progressBar.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onError() {

                            }
                        }*/);
            } else {
                imageViewHolder.imageView.setImageResource(R.drawable.default_no_image);
            }

           /* Picasso.with(mContext)
                    .load(load_image)
                    .placeholder(R.drawable.default_no_image) //this is optional the image to display while the url image is downloading
                    // .error(Your Drawable Resource)         //this is also optional if some error has occurred in downloading the image this image would be displayed
                    .into(imageViewHolder.imageView);*/
            //downloadImageFromURL(load_image);

            //  new DownloadImageFromInternet().execute(load_image);

            /*if(!pic_name.equals("") || (pic_name.substring(0,pic_name.length()-4).equals(".png"))) {
                try {

                    String load_image = IMAGE_URL + "/" + pic_name;
                    URL url = new URL(load_image);
                   // Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                   // Bitmap bmp = BitmapFactory.decodeStream((InputStream)new URL(load_image).getContent());
                    InputStream in = new java.net.URL(load_image).openStream();
                    Bitmap bmp = BitmapFactory.decodeStream(in);
                    imageViewHolder.imageView.setImageBitmap(bmp);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                imageViewHolder.imageView.setImageResource(R.drawable.default_no_image);
            }*/
            // Log.v("","pic "+pic);
        /*    if (pic != null) {
                //pic = pic.substring(1, pic.length() - 1);
                //Log.v("", "pic " + pic);
                //try {
                    byte[] decodedString = Base64.decode(pic, Base64.DEFAULT);
                    decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    Log.v("", "bitmap get image:=>" + decodedByte);
                    try {
                        if (decodedByte == null || decodedByte.equals("")) {
                            imageViewHolder.imageView.setImageResource(R.drawable.default_no_image);
                        } else {
                            imageViewHolder.imageView.setImageBitmap(decodedByte);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
               *//* }catch (Exception e){
                    e.printStackTrace();
                }*//*
            }else{*/

            //   }


            /*imageViewHolder.setClickListener(new ItemClickListener() {
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
            });*/
            imageViewHolder.ll_content_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadNewsDetails(position);
                }
            });

            imageViewHolder.txt_read_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadNewsDetails(position);

                }
            });


            //callback.onItemClicked(pos,imageViewHolder.ll_decline);


        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }

    }

    private void loadNewsDetails(int pos) {
        Log.v("Adapter ", "itemPosition " + pos);
        Intent intent = new Intent(mContext, NewsDetailScreen.class);
        intent.putExtra("itemPosition", pos + "");
        intent.putExtra("category", newsFeedList.get(pos).getCategory());
        intent.putExtra("newsId", newsFeedList.get(pos).getId() + "");
        mContext.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        // if (mLoadedItems == 0) {
        return newsFeedList.size();
        // } else {
        //return newsFeedList == null ? 0 : newsFeedList.size();
        //return newsFeedApplication.hashMap.size();
        //    return mLoadedItems;
        // }
    }

    public void setLoaded() {
        isLoading = false;
    }

    /*
        public void clearList(){
            app.mImageDetails.clear();
            notifyDataSetChanged();
        }

        public void addList(){
            //addData
            notifyDataSetChanged();
        }*/


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder/*
            implements View.OnClickListener, View.OnLongClickListener */ {
        TextView textViewDate, textViewSummary, txt_news_category, txt_approve_news, txt_decline_news, txt_read_more, txt_reject_cmt;
        ImageView imageView;
        TextView txt_post_person_name, txt_post_time, txt_desc, txt_city, txt_state;
        LinearLayout ll_approve_or_dec, ll_content_view, ll_decline, ll_aprove, ll_reject_cmt;
        private ItemClickListener clickListener;

        public ImageViewHolder(View itemView) {
            super(itemView);
            textViewSummary = (TextView) itemView.findViewById(R.id.row_summary);
            textViewDate = (TextView) itemView.findViewById(R.id.row_date);
            imageView = (ImageView) itemView.findViewById(R.id.row_image);

            txt_read_more = (TextView) itemView.findViewById(R.id.txt_read_more);
            txt_reject_cmt = (TextView) itemView.findViewById(R.id.txt_reject_cmt);
            txt_post_person_name = (TextView) itemView.findViewById(R.id.txt_post_person_name);
            txt_news_category = (TextView) itemView.findViewById(R.id.txt_news_category);
            txt_post_time = (TextView) itemView.findViewById(R.id.txt_post_time);
            txt_desc = (TextView) itemView.findViewById(R.id.txt_desc);
            txt_city = (TextView) itemView.findViewById(R.id.txt_city);
            txt_state = (TextView) itemView.findViewById(R.id.txt_state);
            txt_approve_news = (TextView) itemView.findViewById(R.id.txt_approve_news);
            txt_decline_news = (TextView) itemView.findViewById(R.id.txt_decline_news);
            ll_approve_or_dec = (LinearLayout) itemView.findViewById(R.id.ll_approve_or_dec);
            ll_content_view = (LinearLayout) itemView.findViewById(R.id.ll_content_view);
            ll_aprove = (LinearLayout) itemView.findViewById(R.id.ll_aprove);
            ll_decline = (LinearLayout) itemView.findViewById(R.id.ll_decline);
            ll_reject_cmt = (LinearLayout) itemView.findViewById(R.id.ll_reject_cmt);

            if (from[0].equals("admin_news_list")) {
                callback.onItemClicked(pos, itemView);
                //  int newsId = newsFeedList.get(pos).getId();
                //  Log.v(" Adapter ","newsId "+newsId);
            }
            // itemView.setOnClickListener(this);
            // itemView.setOnLongClickListener(this);
        }




       /* @Override
        public void onClick(View view) {
            clickListener.onClick(view, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onClick(view, getAdapterPosition(), true);
            return true;
        }*/
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        /* @Override
         protected void onPreExecute() {
             super.onPreExecute();
             mProgressDialog = new ProgressDialog(mContext);
             mProgressDialog.show();
         }
 */
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            Log.v("", "result " + result);
            //  mProgressDialog.dismiss();
            if (result != null) {
                bmImage.setImageBitmap(result);
            } else {
                bmImage.setImageResource(R.drawable.default_no_image);
            }
        }
    }

    public void setFilter(List<NewsFeedList> models) {
        newsFeedList = new ArrayList<>();
        newsFeedList.addAll(models);
        notifyDataSetChanged();
    }
}



