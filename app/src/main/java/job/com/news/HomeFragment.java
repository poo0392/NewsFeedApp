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
//changes added on 3/9/2018.
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
        initializeComponents();
        attachViews(view);
       // setClickListeners();
      //  syncNewsList();
        loadCategoryUI();

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



}

