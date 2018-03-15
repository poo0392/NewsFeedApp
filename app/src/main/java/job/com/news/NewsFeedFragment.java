package job.com.news;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.google.gson.Gson;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import job.com.news.adapter.ImageAdapter;
import job.com.news.db.NewsListTable;
import job.com.news.helper.ConnectivityInterceptor;
import job.com.news.helper.NoConnectivityException;
import job.com.news.interfaces.OnLoadMoreListener;
import job.com.news.interfaces.WebService;
import job.com.news.models.NewsFeedList;
import job.com.news.models.NewsFeedModelResponse;
import job.com.news.models.NewsImages;
import job.com.news.register.RegisterMember;
import job.com.news.sharedpref.MyPreferences;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by POOJA on 1/28/2018.
 */

public class NewsFeedFragment extends Fragment {
    //changes reflect to be 05/03
    public RecyclerView mRecyclerView;
    private ImageAdapter adapter;
    private NewsFeedApplication newsFeedApplication;
    LinearLayoutManager layoutManager;

    //For Recyclerview scroll
    private boolean userScrolled = true;
    int pastVisiblesItems, visibleItemCount;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    RelativeLayout bottomLayout;
    TextView tvEmptyView;
    Context mContext;
    private MyPreferences myPreferences;
    String emailId, fullName, memberToken;
    String category;
    private List<NewsFeedList> newsFeedListAll = new ArrayList<>();
    private List<NewsFeedList> newsFeedList = new ArrayList<>();
    private List<NewsFeedList> newsFeedListResp = new ArrayList<>();
    private ArrayList<? extends NewsFeedList> newsFeedListNew = new ArrayList<>();
    private List<RegisterMember> memberList = new ArrayList<>();
    private List<NewsImages> imagesList = new ArrayList<>();
    ArrayList<String> categoryResp = new ArrayList<>();
    int memberId;
    int pos;
    ProgressDialog mProgressDialog;
    NewsListTable newsListTable;
    protected Handler handler;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    private List<String> categoryList, catListNew, catDupList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_feed, container, false);
        newsFeedApplication = NewsFeedApplication.getApp();
        mContext = getActivity();
        newsListTable = new NewsListTable(mContext);
        handler = new Handler();
        categoryList = new ArrayList<>();
        catListNew = new ArrayList<>();
        catDupList = new ArrayList<>();
        getPrefData();

        attachViews(view);
        getBundleData();
        //loadDatatoList();
        return view;
    }

    private void getBundleData() {

       // = getArguments().getParcelableArrayList("News");
        loadDatatoList();
        //if(null!=getArguments().getSerializable("News")){
            //newsFeedListNew = getArguments().getString("news");
         // getArguments().getSerializable("News");
          //  Log.v("getBundleData ","newsFeedListNew "+getArguments().getString("News"));

      //  }
     /*  if(null!=getArguments().getString("category")){
           category=getArguments().getString("category");
           Log.v("getBundleData ","category "+category);
       }
       */
      // if(null!=getArguments().getString("News")){
          // category=getArguments().getString("news");
           //Log.v("getBundleData ","news "+category);
      //     String news=getArguments().getString("News");
       //    Log.v(""," getArguments "+news);
          // newsFeedListNew = new Gson().toJson(news);
         /*  NewsFeedModelResponse model = new NewsFeedModelResponse();
           model  = new Gson().fromJson(news, NewsFeedModelResponse.class);
           newsFeedListNew= model.getNewsFeedList();
           Log.v("","newsFeedListNew "+newsFeedListNew);*/



      // }
     /* if(null!=getArguments().getStringArrayList("category")){
          categoryListBundle =getArguments().getStringArrayList("category");

          Log.v("getBundleData ","category "+categoryListBundle.toString());
      }*/
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pos = FragmentPagerItem.getPosition(getArguments());
        //getArguments();
        Log.v("", "pos " + pos);
        //   attachViews(view);
        //   getPrefData();
        //
        long lastId = 0;
        //callNewsListAPI(memberToken, memberId,lastId);
        // setData();
        /*addNewsFeedItems();
        mAdapter = new HomeDashboardAdapter(HomeActivity.this, mNewsFeedList);
        mRecyclerView.setAdapter(mAdapter);*/
        setListeners();

    }

    private void setListeners() {

        //implementScrollListener();

    }

    private void getPrefData() {
        myPreferences = MyPreferences.getMyAppPref(mContext);
        memberId = myPreferences.getMemberId();
        memberToken = myPreferences.getMemberToken().trim();
        emailId = myPreferences.getEmailId().trim();
        fullName = myPreferences.getFirstName().trim() + " " + myPreferences.getLastName().trim();
    }

    private void attachViews(View view) {
        int position = FragmentPagerItem.getPosition(getArguments());
        bottomLayout = (RelativeLayout) view.findViewById(R.id.loadItemsLayout_recyclerView);
        tvEmptyView = (TextView) view.findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.news_feed_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void callNewsListAPI(String memberToken, int memberId, long last_id) {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(mContext))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WebService webService = retrofit.create(WebService.class);

        RequestBody paramMemberToken = RequestBody.create(MediaType.parse("text/plain"), memberToken);
        RequestBody paramMemberId = RequestBody.create(MediaType.parse("text/plain"), "" + memberId);
        // RequestBody paramLastId = RequestBody.create(MediaType.parse("text/plain"), "" + last_id);
        String news_status = "";

        Log.v("", " memberToken " + memberToken);
        Log.v("", " memberId " + memberId);
        Log.v("", " last_id " + "" + last_id);

        //  Call<NewsFeedModelResponse> serverResponse = webService.getNewsListRequest(paramMemberToken, paramMemberId,news_status, paramLastId);
        Call<NewsFeedModelResponse> serverResponse = webService.getNewsListRequest(paramMemberToken, paramMemberId, last_id);
        serverResponse.enqueue(new Callback<NewsFeedModelResponse>() {
            @Override
            public void onResponse(Call<NewsFeedModelResponse> call, Response<NewsFeedModelResponse> response) {
                mProgressDialog.dismiss();
                String newsList = "";
                if (response.isSuccessful()) {

                    NewsFeedModelResponse serverResponse = response.body();
                    FragmentPagerItems pages = new FragmentPagerItems(mContext);
                    if (serverResponse.getStatus() == 0) {
                        Log.v("callNewsListAPI ", "response " + new Gson().toJson(response.body()));


                        newsFeedListResp = serverResponse.getNewsFeedList();
                        Log.v("", "newsFeedList " + newsFeedListResp.toString());
                        for (int i = 0; i < newsFeedListResp.size(); i++) {
                            // memberList = newsFeedList.get(i).getMember().getMemberId();
                            categoryResp.add(newsFeedListResp.get(i).getCategory());
                            imagesList = newsFeedListResp.get(i).getNews_images();
                        }
                        // adapter.notifyItemInserted(newsFeedList.size());

                        // loadDatatoListFromAPI(newsFeedList, memberList, imagesList);

                        loadDatatoList();

                    }
                }
            }

            @Override
            public void onFailure(Call<NewsFeedModelResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                t.printStackTrace();

                if (t instanceof NoConnectivityException) {
                    // No internet connection
                    // Toast.makeText(mContext, "No Internet", Toast.LENGTH_SHORT).show();
                    setFailedAlertDialog(mContext, "Failed", "No Internet! Please Check Your internet connection");
                }
            }
        });
    }

    private void loadDatatoListFromAPI(List<NewsFeedList> newsFeedList, List<RegisterMember> memberList, List<NewsImages> imagesList) {
        adapter = new ImageAdapter(getActivity(), newsFeedList, mRecyclerView, memberList, imagesList, "fromAPi");
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void setFailedAlertDialog(Context context, String title, String desc) {
        new MaterialStyledDialog.Builder(context)
                .setTitle(title)
                .setDescription(desc)
                .setStyle(Style.HEADER_WITH_ICON)
                .setIcon(R.mipmap.ic_failed)
                .setPositiveText(R.string.button_ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void loadDatatoList() {

        //     newsFeedListAll = newsListTable.getAllNewsRecords();

       // getCategory();

       /* for (int titleResId : tabsValues()) {
            newsFeedList = newsListTable.getNewsRecordsByCategory(getString(titleResId));
        }*/


     /*   for (int i = 0; i < categoryList.size(); i++) {//.get(pos).
            newsFeedList = newsListTable.getNewsRecordsByCategory(categoryList.get(i));
            //  newsFeedList = newsListTable.getNewsRecordsByCategory(category);
        }*/
        // newsFeedList = newsListTable.getAllNewsRecords();
        Log.v("", "getNewsFeedList " + newsFeedList.toString());
        adapter = new ImageAdapter(getActivity(), newsFeedList, mRecyclerView, "fromAPi");

        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        // }
       /* if (newsFeedList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.VISIBLE);

        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
        }*/

       /* setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                NewsFeedFragment.this.newsFeedList.add(null);
                adapter.notifyItemInserted(NewsFeedFragment.this.newsFeedList.size() - 1);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //   remove progress item
                        NewsFeedFragment.this.newsFeedList.remove(NewsFeedFragment.this.newsFeedList.size() - 1);
                        adapter.notifyItemRemoved(NewsFeedFragment.this.newsFeedList.size());
                        //add items one by one
                        int start = NewsFeedFragment.this.newsFeedList.size();
                        int end = start + 10;

                        for (int i = start + 1; i <= end; i++) {
                            // newsFeedList.add(new Student("Student " + i, "AndroidStudent" + i + "@gmail.com"));
                            callNewsListAPI(memberToken,memberId,end);

                        }
                        setLoaded();
                        //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                    }
                }, 2000);

            }
        });*/

    }

    private int[] tabsValues() {


        return new int[]{
                // R.string.recent_news,
                R.string.national_inter_menu,
                R.string.gov_news_menu,
                R.string.soc_rel_menu,
                R.string.sports_menu,
                R.string.sci_tech_menu,
                R.string.eco_news_menu,
                R.string.health_rel_menu,
                R.string.business_news_menu,
                R.string.agri_news_menu,
                R.string.cinema_menu,
                R.string.small_class_menu,
                R.string.other_uncat_menu,
                R.string.ent_news_menu,
                R.string.career_rel_menu

        };
    }

    private void getCategory() {
        //  newsFeedListAll = newsListTable.getAllNewsRecords();
        // categoryList = newsListTable.getCategory();

        //  Log.v("", "newsFeedList.size() " + newsFeedListAll.size());
        /*for (int k = 0; k < newsFeedListAll.size(); k++) {
            catDupList.add(newsFeedListAll.get(k).getCategory());

        }*/
        categoryList.addAll(new HashSet<>(categoryResp));
        Log.v("", "categoryList " + categoryList);


    }

    public void setLoaded() {
        isLoading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    // Implement scroll listener
    private void implementScrollListener() {
        //mRecyclerView.addOnScrollListener(onLoadMoreListener);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {

                super.onScrollStateChanged(recyclerView, newState);

                // If scroll state is touch scroll then set userScrolled
                // true
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    userScrolled = true;

                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx,
                                   int dy) {

                super.onScrolled(recyclerView, dx, dy);
                // Here get the child count, item count and visibleitems
                // from layout manager

                /*visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                // Now check if userScrolled is true and also check if
                // the item is end then update recycler view and set
                // userScrolled to false
                if (userScrolled && (visibleItemCount + pastVisiblesItems) == totalItemCount) {
                    userScrolled = false;

                    updateRecyclerView();
                }*/


                totalItemCount = layoutManager.getItemCount();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                Log.v("", "totalItemCount " + totalItemCount + " lastVisibleItem " + lastVisibleItem);
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;

                }


            }

        });

    }

    // Method for repopulating recycler view
    private void updateRecyclerView() {

        // Show Progress Layout
        bottomLayout.setVisibility(View.VISIBLE);

        // Handler to show refresh for a period of time you can use async task
        // while commnunicating serve

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                // Loop for 3 items
                for (int i = 0; i < 3; i++) {
                    //  int value = new RandomNumberGenerator().RandomGenerator();// Random
                    // value

                    // add random data to arraylist
                    //   listArrayList.add(new Data_Model(getTitle[value],getLocation[value], getYear[value], images[value]));
                    setData();
                }
                adapter.notifyDataSetChanged();// notify adapter

                // Toast for task completion
                Toast.makeText(mContext, "Items Updated.",
                        Toast.LENGTH_SHORT).show();

                // After adding new data hide the view.
                bottomLayout.setVisibility(View.GONE);

            }
        }, 5000);
    }

    private void setData() {

        ArrayList list = new ArrayList();
        list.add("White man shouts 'go back to Lebanon' to Sikh-American girl");
        list.add("NEW YORK: A Sikh-American girl was harassed on a subway train here when a white man, mistaking her to be from the Middle East, allegedly shouted \"go back to Lebanon\" and \"you don't belong in this country,\" the latest in a series of hate crimes against people of South-Asian origin.\n" +
                "Rajpreet Heir was taking the subway train to a friend's birthday party in Manhattan this month when the white man began shouting at her, according to a report in the New York Times.\n" +
                "Heir recounted the ordeal in a video for a Times section called 'This Week in Hate', which highlights hate crimes and harassment around the country since the election of President Donald Trump.\n" +
                "Heir said she was looking at her phone when the white man shouted at her saying, \"Do you even know what a Marine looks like? Do you know what they have to see? What they do for this country? Because of people like you.\"");
        list.add("20 March 2017");


        newsFeedApplication.hashMap.put(0 + "", list);

        list = new ArrayList();
        list.add("'We are here to stay', say Indian-Americans amid growing hate crime incidents in the US");
        list.add("WASHINGTON: \"We are here to stay\", Indian-Americans have vowed while holding a series of meetings to express their concern over growing hate crime incidents against ethnic and religious minorities in the US.\n" +
                "\"No matter what gunmen or the President (Donald Trump) say, this is our country, we are here to stay, and we will keep demanding our rightful and equal place in this quintessential nation of immigrants,\" said Suman Raghunathan from the South Asian Americans leading Together (SAALT) at a town hall discussion here on Friday.\n" +
                "Initiated by SAALT, South Asian groups are planning to organise a number of similar town halls across the country.\n" +
                "Prominent community leaders who addressed the town hall were Arjun Sethi of the Georgetown University Law Center, Dr Revathi Vikram of ASHA for Women, Shabab Ahmed Mirza of KhushDC, Darakshan Raja of Washington Peace Center and Kathy Doan of the Capital Area Immigrants' Rights Coalition.");
        list.add("21 March 2017");

        newsFeedApplication.hashMap.put(1 + "", list);

        list = new ArrayList();
        list.add("Development of all, appeasement of none: Yogi's mantra for UP");
        list.add("NEW DELHI: In his first public speech as the Uttar Pradesh chief minister, Yogi Adityanath said on Saturday that his government will work for the development of all+ and there will be no discrimination on the grounds of religion, caste or gender in the state.\n" +
                "\"We will follow \"sabka saath, sabka vikas\" mantra of PM Modi,\" Yogi Adityanath said, adding that there will be no attempt to \"appease\" any section+ of the population.\n" +
                "\"We will show UP how a government should be run. How it should treat a common man,\" the UP CM said while addressing a huge gathering at Gorakhpur's Maharana Pratap Inter College.\n" +
                "Yogi Adityanath is on his maiden two-day tour to Gorakhpur after becoming the chief minister.\n" +
                "He also claimed that the newly formed BJP government will fulfill all promises made in the election manifesto.");
        list.add("22 March 2017");

        newsFeedApplication.hashMap.put(2 + "", list);

        list = new ArrayList();
        list.add("India to seal border with Pakistan by 2018: Rajnath Singh");
        list.add("NEW DELHI: Union home minister Rajnath Singh on Saturday said India plans to seal international boundaries+ with neighbouring countries Pakistan and Bangladesh soon.\n" +
                "\"India is planning to seal the international boundaries with Pakistan and Bangladesh+ as soon as possible. This could be India's major step against terrorism and the problem of refugees,\" Singh said while addressing the passing out parade of the Border Security Force Assistant Commandants at the BSF Academy in Madhya Pradesh's Tekanpur area.");
        list.add("23 March 2017");

        newsFeedApplication.hashMap.put(3 + "", list);

        list = new ArrayList();
        list.add("India v Australia: Shane Warne tips come in handy for Kuldeep Yadav");
        list.add("DHARAMSALA: Newcomer Kuldeep Yadav on Saturday revealed he had learnt some tricks of the trade from none other than Aussie spin legend Shane Warne, after scripting a memorable debut against Australia in Dharamsala.\n" +
                "The chinaman bowler had his rivals in a spin, taking four wickets for 68 runs to help India bowl out the Aussies for 300 on the opening day of the decisive fourth Test in Dharamsala.");
        list.add("24 March 2017");

        newsFeedApplication.hashMap.put(4 + "", list);

    }

  /*  public static Fragment newInstance(int position, List<NewsFeedList> newsFeedListNew) {
    }*/

    public static Fragment newInstance(int position, Bundle newsFeedList) {
        NewsFeedFragment fragment = new NewsFeedFragment();
        Bundle args = new Bundle();
       // args.putParcelableArrayList("News", (ArrayList<? extends Parcelable>) newsFeedList);
        //args.putParcelableArrayList("News", (ArrayList<? extends Parcelable>) newsFeedList);
        args.getBundle("News");
        fragment.setArguments(args);
        return fragment;
    }

   /* @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        newsFeedListNew = args.getParcelableArrayList("News");
    }*/
}
