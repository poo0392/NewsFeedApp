package job.com.news;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.google.gson.Gson;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import job.com.news.adapter.HomeDashboardAdapter;
import job.com.news.adapter.ImageAdapter;
import job.com.news.db.CategoryMasterTable;
import job.com.news.db.DBHelper;
import job.com.news.db.MemberTable;
import job.com.news.db.NewsImagesTable;
import job.com.news.db.NewsListTable;
import job.com.news.db.SubCategoryTable;
import job.com.news.helper.ConnectivityInterceptor;
import job.com.news.helper.NoConnectivityException;
import job.com.news.interfaces.WebService;
import job.com.news.models.NewsFeedDetails;
import job.com.news.models.NewsFeedList;
import job.com.news.models.NewsFeedModelResponse;
import job.com.news.models.NewsImages;
import job.com.news.register.RegisterMember;
import job.com.news.service.AlarmReceiver;
import job.com.news.sharedpref.MyPreferences;
import job.com.news.sharedpref.SessionManager;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Pooaj.Patil on 08/03/2018.
 */

public class HomeFragment extends Fragment {
    Context mContext;
    Toolbar toolbar;
    private ProgressDialog mProgressDialog;
    private List<NewsFeedList> newsFeedList = new ArrayList<>();
    private List<String> categoryList, catDupList;
    Gson gson;
    NewsListTable newsListTable;
    MemberTable memberTable;
    NewsImagesTable newsImagesTable;
    CategoryMasterTable categoryMasterTable;
    SubCategoryTable subCategoryTable;
    DBHelper db;

    HomeDashboardAdapter mAdapter;
    List<NewsFeedDetails> mNewsFeedList;
    private FragmentPagerItemAdapter mFPIAdapter;
    private FragmentPagerItems pages;
    private ViewPager viewPager;
    private SmartTabLayout viewPagerTab;
    private MyPreferences myPreferences;
    String emailId, fullName, memberToken;
    int memberId;
    private RecyclerView mRecyclerView;
    private ImageAdapter adapter;
    //private HashMap<String, ArrayList<String>> hashMap;
    private NewsFeedApplication newsFeedApplication;
    private SessionManager session, langSelection;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        //setTootlbarTitle("Home");
        toolbar.setTitle(getResources().getString(R.string.home_toolbar_title));

        //setHasOptionsMenu(false);
        mContext = getActivity();
        getPrefData();

        attachViews(view);
        setClickListeners();
        callNewsListAPI(memberToken, memberId);
        return view;
    }

    private void initializeComponents() {
        gson = new Gson();
        newsListTable = new NewsListTable(mContext);
        memberTable = new MemberTable(mContext);
        categoryMasterTable = new CategoryMasterTable(mContext);
        subCategoryTable = new SubCategoryTable(mContext);
        newsImagesTable = new NewsImagesTable(mContext);
        categoryList = new ArrayList<>();
        catDupList = new ArrayList<>();
        newsFeedApplication = NewsFeedApplication.getApp();
        session = new SessionManager(mContext);
        langSelection = new SessionManager(mContext);
    }

    private void getPrefData() {
        myPreferences = MyPreferences.getMyAppPref(mContext);
        memberId = myPreferences.getMemberId();
        memberToken = myPreferences.getMemberToken().trim();
        emailId = myPreferences.getEmailId().trim();
        fullName = myPreferences.getFirstName().trim() + " " + myPreferences.getLastName().trim();
    }

    private void setClickListeners() {

    }

    private void attachViews(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.home_viewpager);
        viewPagerTab = (SmartTabLayout) view.findViewById(R.id.viewpagertab);
    }

    private void callNewsListAPI(String memberToken, int memberId) {
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

        Log.v("", " memberToken " + memberToken);
        Call<NewsFeedModelResponse> serverResponse = webService.getNewsListRequest(paramMemberToken, paramMemberId);

        serverResponse.enqueue(new Callback<NewsFeedModelResponse>() {
            @Override
            public void onResponse(Call<NewsFeedModelResponse> call, Response<NewsFeedModelResponse> response) {
                mProgressDialog.dismiss();
                String newsList = "";
                if (response.isSuccessful()) {

                  /*  Type collectionType = new TypeToken<List<NewsFeedModelResponse>>() {
                    }.getType();
                    List<NewsFeedModelResponse> lcs = (List<NewsFeedModelResponse>) new Gson()
                            .fromJson(String.valueOf(response.body()), collectionType);*/

                    NewsFeedModelResponse serverResponse = response.body();
                    String serverResponse2 = new Gson().toJson(response.body());
                    Log.v("callNewsListAPI ", "response " + serverResponse2);
                    //    newsList=serverResponse.toString();
                    if (serverResponse.getStatus() == 0) {
                        //   Log.v("callNewsListAPI ", "response " + new Gson().toJson(response.body()));
                        try {
                            newsFeedList = serverResponse.getNewsFeedList();
                            // Log.v("", "newsFeedList " + newsFeedList.toString());

                            //   loadDatatoList(newsFeedList);
                            NewsFeedList model = new NewsFeedList();
                            try {
                                RegisterMember member = new RegisterMember();
                                List<NewsImages> imagesList = new ArrayList<>();
                                NewsImages imagesModel = new NewsImages();
                                for (int i = 0; i < serverResponse.getNewsFeedList().size(); i++) {
                                    if (!newsListTable.checkNewsPresent(serverResponse.getNewsFeedList().get(i).getId())) {
                                        model.setId(serverResponse.getNewsFeedList().get(i).getId());
                                        model.setNews_uuid(serverResponse.getNewsFeedList().get(i).getNews_uuid());
                                        model.setCategory(serverResponse.getNewsFeedList().get(i).getCategory());
                                        model.setCountry(serverResponse.getNewsFeedList().get(i).getCountry());
                                        model.setState(serverResponse.getNewsFeedList().get(i).getState());
                                        model.setCity(serverResponse.getNewsFeedList().get(i).getCity());
                                        model.setNews_title(serverResponse.getNewsFeedList().get(i).getNews_title());
                                        model.setNews_description(serverResponse.getNewsFeedList().get(i).getNews_description());
                                        //  model.setNews_pic(serverResponse.getNewsFeedList().get(i).getNews_pic());
                                        model.setNews_images(serverResponse.getNewsFeedList().get(i).getNews_images());
                                        model.setLike_count(serverResponse.getNewsFeedList().get(i).getLike_count());
                                        model.setMember_id(serverResponse.getNewsFeedList().get(i).getMember_id());
                                        model.setCreated_at(serverResponse.getNewsFeedList().get(i).getCreated_at());
                                        model.setMember(serverResponse.getNewsFeedList().get(i).getMember());

                                        Log.v("", "Log" + Arrays.asList(serverResponse.getNewsFeedList().get(i).getNews_images()));

                                        if (!memberTable.checkUser(serverResponse.getNewsFeedList().get(i).getMember().getId())) {
                                            member.setMemberId(model.getMember().getId());
                                            //member.setMemberToken(model.getMember().getMemberToken().trim());
                                            member.setFirstName(model.getMember().getFirstName().trim());
                                            member.setLastName(model.getMember().getLastName().trim());
                                            member.setEmailId(model.getMember().getEmailId().trim());
                                            member.setMobile(model.getMember().getMobile());

                                            memberTable.insertMembers(member);

                                        }
                                        //Log.v("", "getNews_images().size() " + serverResponse.getNewsFeedList().get(i).getNews_images().size());
                                        if (serverResponse.getNewsFeedList().get(i).getNews_images() != null && serverResponse.getNewsFeedList().get(i).getNews_images().size() > 0) {
                                            for (int j = 0; j < serverResponse.getNewsFeedList().get(i).getNews_images().size(); j++) {
                                                imagesModel.setId(model.getNews_images().get(j).getId());
                                                imagesModel.setNews_id(model.getNews_images().get(j).getNews_id());
                                                imagesModel.setNews_pic(model.getNews_images().get(j).getNews_pic());
                                                imagesModel.setCreated_at(model.getNews_images().get(j).getCreated_at());
                                                imagesModel.setUpdated_at(model.getNews_images().get(j).getUpdated_at());

                                                imagesList.add(imagesModel);
//changes 06_03
                                                newsImagesTable.insertNewsImages(imagesModel);
                                            }
                                        }

                                        newsListTable.insertNewsList(model);

                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        initializeComponents();
                        //setListeners();
                        syncNewsList();
                        loadCategoryUI();

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

    private void syncNewsList() {
        Intent alarm = new Intent(getActivity(), AlarmReceiver.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(getActivity(), 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);
        if (alarmRunning == false) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, alarm, 0);
            AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 1000000, pendingIntent);
        }
    }

    private void loadCategoryUI() {
        pages = new FragmentPagerItems(mContext);
        FragmentPagerItems pages = new FragmentPagerItems(mContext);
        /*for (int titleResId : tabsValues()) {

        }*/
        newsFeedList = newsListTable.getAllNewsRecords();
        // categoryList = newsListTable.getCategory();

        Log.v("", "newsFeedList.size() " + newsFeedList.size());
        for (int k = 0; k < newsFeedList.size(); k++) {
            catDupList.add(newsFeedList.get(k).getCategory());

        }
        categoryList.addAll(new HashSet<>(catDupList));
        for (int i = 0; i < categoryList.size(); i++) {
            pages.add(FragmentPagerItem.of(categoryList.get(i), NewsFeedFragment.class));
        }

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getFragmentManager(), pages);//getSupportFragmentManager

        viewPager.setAdapter(adapter);
        viewPagerTab.setViewPager(viewPager);
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
}

