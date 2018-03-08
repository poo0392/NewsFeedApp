package job.com.news.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import job.com.news.NewsDetailScreen;
import job.com.news.NewsFeedApplication;
import job.com.news.R;
import job.com.news.db.DBHelper;
import job.com.news.db.MemberTable;
import job.com.news.db.NewsImagesTable;
import job.com.news.interfaces.ItemClickListener;
import job.com.news.models.NewsFeedList;
import job.com.news.models.NewsImages;
import job.com.news.register.RegisterMember;

/**
 * Created by POOJA on 1/27/2018.
 */
//changes added on 3/9/2018.
public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String IMAGE_URL="http://thanehousingfederation.com/newsapp/storage/app/public/uploads/news";
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
    List<NewsImages> imagesList;
    DBHelper db;
    MemberTable memberTable;
    NewsImagesTable newsImagesTable;
    Bitmap decodedByte;
    ProgressDialog mProgressDialog;

    public ImageAdapter(Context mContext, List<NewsFeedList> newsFeedList) {
        newsFeedApplication = NewsFeedApplication.getApp();
        this.mContext = mContext;
        this.newsFeedList = newsFeedList;
        memberTable = new MemberTable(mContext);
        newsImagesTable = new NewsImagesTable(mContext);
        memberList = new ArrayList<>();
        imagesList = new ArrayList<>();
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
           // Log.v("", "clickedPosMemberID " + Integer.parseInt(newsFeedList.get(position).getMember_id()));
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
            imagesList=newsImagesTable.getNewsImagesList(newsFeedList.get(position).getId());

            String pic_name = imagesList.get(45).getNews_pic().toString();

            String load_image = IMAGE_URL + "/" + "42IEt1OACW9x.png";

            Picasso.with(mContext)
                    .load(load_image)
                    .placeholder(R.drawable.default_no_image) //this is optional the image to display while the url image is downloading
                   // .error(Your Drawable Resource)         //this is also optional if some error has occurred in downloading the image this image would be displayed
                    .into(imageViewHolder.imageView);

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



