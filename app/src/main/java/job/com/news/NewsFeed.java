package job.com.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import job.com.news.interfaces.ItemClickListener;

public class NewsFeed extends AppCompatActivity {

    private Context mContext;
    private RecyclerView mRecyclerView;
    private ImageAdapter adapter;
    //private HashMap<String, ArrayList<String>> hashMap;
    private NewsFeedApplication newsFeedApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        mContext = this;
        newsFeedApplication = NewsFeedApplication.getApp();

        mRecyclerView = (RecyclerView) findViewById(R.id.news_feed_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);

        //hashMap = new HashMap<>();

        setData();

        adapter = new ImageAdapter();
        mRecyclerView.setAdapter(adapter);
        /*adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.e("haint", "Load More");
                mCurrentPage = mCurrentPage + 1;
                if(mCurrentPage > 0 && mCurrentPage<mTotalPageCount) {
                    // bottom progress bar
                    app.mImageDetails.add(null);
                    adapter.notifyDataSetChanged();
                    // adapter.notifyItemInserted(app.mImageDetails.size() - 1);
                }
                //Load more data for reyclerview
                if(mCurrentPage < mTotalPageCount){
                    getData(String.valueOf(mCurrentPage));
                }
            }
        });*/

    }

    private void setData() {

        ArrayList list  = new ArrayList();
        list.add("White man shouts 'go back to Lebanon' to Sikh-American girl");
        list.add("NEW YORK: A Sikh-American girl was harassed on a subway train here when a white man, mistaking her to be from the Middle East, allegedly shouted \"go back to Lebanon\" and \"you don't belong in this country,\" the latest in a series of hate crimes against people of South-Asian origin.\n" +
                "Rajpreet Heir was taking the subway train to a friend's birthday party in Manhattan this month when the white man began shouting at her, according to a report in the New York Times.\n" +
                "Heir recounted the ordeal in a video for a Times section called 'This Week in Hate', which highlights hate crimes and harassment around the country since the election of President Donald Trump.\n" +
                "Heir said she was looking at her phone when the white man shouted at her saying, \"Do you even know what a Marine looks like? Do you know what they have to see? What they do for this country? Because of people like you.\"");
        list.add("20 March 2017");


        newsFeedApplication.hashMap.put(0+"" , list);

        list  = new ArrayList();
        list.add("'We are here to stay', say Indian-Americans amid growing hate crime incidents in the US");
        list.add("WASHINGTON: \"We are here to stay\", Indian-Americans have vowed while holding a series of meetings to express their concern over growing hate crime incidents against ethnic and religious minorities in the US.\n" +
                "\"No matter what gunmen or the President (Donald Trump) say, this is our country, we are here to stay, and we will keep demanding our rightful and equal place in this quintessential nation of immigrants,\" said Suman Raghunathan from the South Asian Americans leading Together (SAALT) at a town hall discussion here on Friday.\n" +
                "Initiated by SAALT, South Asian groups are planning to organise a number of similar town halls across the country.\n" +
                "Prominent community leaders who addressed the town hall were Arjun Sethi of the Georgetown University Law Center, Dr Revathi Vikram of ASHA for Women, Shabab Ahmed Mirza of KhushDC, Darakshan Raja of Washington Peace Center and Kathy Doan of the Capital Area Immigrants' Rights Coalition.");
        list.add("21 March 2017");

        newsFeedApplication.hashMap.put(1+"" , list);

        list  = new ArrayList();
        list.add("Development of all, appeasement of none: Yogi's mantra for UP");
        list.add("NEW DELHI: In his first public speech as the Uttar Pradesh chief minister, Yogi Adityanath said on Saturday that his government will work for the development of all+ and there will be no discrimination on the grounds of religion, caste or gender in the state.\n" +
                "\"We will follow \"sabka saath, sabka vikas\" mantra of PM Modi,\" Yogi Adityanath said, adding that there will be no attempt to \"appease\" any section+ of the population.\n" +
                "\"We will show UP how a government should be run. How it should treat a common man,\" the UP CM said while addressing a huge gathering at Gorakhpur's Maharana Pratap Inter College.\n" +
                "Yogi Adityanath is on his maiden two-day tour to Gorakhpur after becoming the chief minister.\n" +
                "He also claimed that the newly formed BJP government will fulfill all promises made in the election manifesto.");
        list.add("22 March 2017");

        newsFeedApplication.hashMap.put(2+"" , list);

        list  = new ArrayList();
        list.add("India to seal border with Pakistan by 2018: Rajnath Singh");
        list.add("NEW DELHI: Union home minister Rajnath Singh on Saturday said India plans to seal international boundaries+ with neighbouring countries Pakistan and Bangladesh soon.\n" +
                "\"India is planning to seal the international boundaries with Pakistan and Bangladesh+ as soon as possible. This could be India's major step against terrorism and the problem of refugees,\" Singh said while addressing the passing out parade of the Border Security Force Assistant Commandants at the BSF Academy in Madhya Pradesh's Tekanpur area.");
        list.add("23 March 2017");

        newsFeedApplication.hashMap.put(3+"" , list);

        list  = new ArrayList();
        list.add("India v Australia: Shane Warne tips come in handy for Kuldeep Yadav");
        list.add("DHARAMSALA: Newcomer Kuldeep Yadav on Saturday revealed he had learnt some tricks of the trade from none other than Aussie spin legend Shane Warne, after scripting a memorable debut against Australia in Dharamsala.\n" +
                "The chinaman bowler had his rivals in a spin, taking four wickets for 68 runs to help India bowl out the Aussies for 300 on the opening day of the decisive fourth Test in Dharamsala.");
        list.add("24 March 2017");

        newsFeedApplication.hashMap.put(4+"" , list);

    }

    class ImageAdapter extends RecyclerView.Adapter < RecyclerView.ViewHolder > {
        private final int VIEW_TYPE_ITEM = 0;
        private final int VIEW_TYPE_LOADING = 1;
        //private OnLoadMoreListener mOnLoadMoreListener;
        private boolean isLoading;
        private int visibleThreshold = 1;
        private int lastVisibleItem, totalItemCount;

        public ImageAdapter() {
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

                ArrayList<String> list = newsFeedApplication.hashMap.get(""+position);
                imageViewHolder.textViewSummary.setText(list.get(0));
                imageViewHolder.textViewDate.setText(list.get(2));
                if(position == 0){
                    //lebaon
                    imageViewHolder.imageView.setBackgroundResource(R.drawable.lebanon);
                } else if(position == 1){
                    //india us
                    imageViewHolder.imageView.setBackgroundResource(R.drawable.india_us);
                } else if(position == 2){
                    //yogi
                    imageViewHolder.imageView.setBackgroundResource(R.drawable.yogi_adinath);
                } else if(position == 3){
                    //india pak
                    imageViewHolder.imageView.setBackgroundResource(R.drawable.india_pak_border);
                } else if(position == 4){
                    //kuldeep
                    imageViewHolder.imageView.setBackgroundResource(R.drawable.kuldeep_yadav);
                }


                imageViewHolder.setClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        if(isLongClick){
                            Toast.makeText(mContext, "#" + position + " (Long click)", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "#" + position , Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(mContext, NewsDetailScreen.class);
                            intent.putExtra("itemPosition", position+"");
                            startActivity(intent);
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
            return newsFeedApplication.hashMap.size();
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


    }

    static class ImageViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        TextView textViewSummary;
        TextView textViewDate;
        ImageView imageView;
        private ItemClickListener clickListener;

        public ImageViewHolder(View itemView) {
            super(itemView);
            this.textViewSummary = (TextView) itemView.findViewById(R.id.row_summary);
            this.textViewDate = (TextView) itemView.findViewById(R.id.row_date);
            this.imageView = (ImageView) itemView.findViewById(R.id.row_image);
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
