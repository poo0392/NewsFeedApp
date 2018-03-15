package job.com.news;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.Bundler;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import org.json.JSONObject;

import java.util.ArrayList;
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
import job.com.news.models.NewsFeedDetails;
import job.com.news.models.NewsFeedList;
import job.com.news.sharedpref.MyPreferences;
import job.com.news.sharedpref.SessionManager;

/**
 * Created by Pooaj.Patil on 08/03/2018.
 */
//changes added on 3/9/2018.
public class HomeFragment extends Fragment {
    Context mContext;
    Toolbar toolbar;
    private ProgressDialog mProgressDialog;
    private List<NewsFeedList> newsFeedList = new ArrayList<>();
    private List<NewsFeedList> newsFeedListNew = new ArrayList<>();
    private List<String> catListNew, catDupList;
    ArrayList<String> categoryList;
    ArrayList<String> catListNewEn;
    Gson gson;
    NewsListTable newsListTable;
    MemberTable memberTable;
    NewsImagesTable newsImagesTable;
    CategoryMasterTable categoryMasterTable;
    SubCategoryTable subCategoryTable;
    DBHelper db;
    FragmentPagerItemAdapter fpAdapter;
    HomeDashboardAdapter mAdapter;
    List<NewsFeedDetails> mNewsFeedList;
    private FragmentPagerItemAdapter mFPIAdapter;
    private FragmentPagerItems pages;
    private ViewPager viewPager;
    private TabLayout mTabLayout;
    private SmartTabLayout viewPagerTab;
    private MyPreferences myPreferences;
    String emailId, fullName, memberToken;
    int memberId;
    private RecyclerView mRecyclerView;
    private ImageAdapter adapter;
    //private HashMap<String, ArrayList<String>> hashMap;
    private NewsFeedApplication newsFeedApplication;
    private SessionManager session, langSelection;
    NewsFeedList model = new NewsFeedList();

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
        catListNew = new ArrayList<>();
        catListNewEn = new ArrayList<>();
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
        newsFeedList = newsListTable.getAllNewsRecords();
        viewPager = (ViewPager) view.findViewById(R.id.home_viewpager);
        viewPagerTab = (SmartTabLayout) view.findViewById(R.id.viewpagertab);
       /* mTabLayout = (TabLayout)view.findViewById(R.id.tab_layout);
        DynamicFragmentAdapter mAdapter = new DynamicFragmentAdapter(getFragmentManager(),newsFeedList);
        mTabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(mAdapter);*/
    }


    private void loadCategoryUI() {
        pages = new FragmentPagerItems(mContext);

        // FragmentPagerItems pages = new FragmentPagerItems(mContext);

        newsFeedList = newsListTable.getAllNewsRecords();
        // categoryList = newsListTable.getCategory();

        Log.v("", "newsFeedList.size() " + newsFeedList.size());
        for (int k = 0; k < newsFeedList.size(); k++) {
            catDupList.add(newsFeedList.get(k).getCategory());

        }

        categoryList.addAll(new HashSet<>(catDupList));
        for (int i = 1; i < categoryList.size(); i++) {
            if (categoryList.get(i).equals("National and International")) {
                catListNew.add(mContext.getResources().getString(R.string.national_inter_menu));
                catListNewEn.add("National and International");
            } else if (categoryList.get(i).equals("Government News")) {
                catListNew.add(mContext.getResources().getString(R.string.gov_news_menu));
                catListNewEn.add("Government News");
            } else if (categoryList.get(i).equals("Social and Related News")) {
                catListNew.add(mContext.getResources().getString(R.string.soc_rel_menu));
                catListNewEn.add("Social and Related News");
            } else if (categoryList.get(i).equals("Sports")) {
                catListNew.add(mContext.getResources().getString(R.string.sports_menu));
                catListNewEn.add("Sports");
            } else if (categoryList.get(i).equals("Science and Technology")) {
                catListNew.add(mContext.getResources().getString(R.string.sci_tech_menu));
                catListNewEn.add("Science and Technology");
            } else if (categoryList.get(i).equals("Economical News")) {
                catListNew.add(mContext.getResources().getString(R.string.eco_news_menu));
                catListNewEn.add("Economical News");
            } else if (categoryList.get(i).equals("Health Related")) {
                catListNew.add(mContext.getResources().getString(R.string.health_rel_menu));
                catListNewEn.add("National and International");
            } else if (categoryList.get(i).equals("Business News")) {
                catListNew.add(mContext.getResources().getString(R.string.business_news_menu));
                catListNewEn.add("Business News");
            } else if (categoryList.get(i).equals("Agricultural News")) {
                catListNew.add(mContext.getResources().getString(R.string.agri_news_menu));
                catListNewEn.add("Agricultural News");
            } else if (categoryList.get(i).equals("Cinema Related")) {
                catListNew.add(mContext.getResources().getString(R.string.cinema_menu));
                catListNewEn.add("Cinema Related");
            } else if (categoryList.get(i).equals("Small Classifieds")) {
                catListNew.add(mContext.getResources().getString(R.string.small_class_menu));
                catListNewEn.add("Small Classifieds");
            }/*else if (categoryList.get(i).equals("Property")) {
                catListNew.add(mContext.getResources().getString(R.string.cinema_menu));
            }else if (categoryList.get(i).equals("Birthday ads")) {
                catListNew.add(mContext.getResources().getString(R.string.birth_menu));
            }else if (categoryList.get(i).equals("App Related Ads")) {
                catListNew.add(mContext.getResources().getString(R.string.app_rel_menu));
            }else if (categoryList.get(i).equals("Buy and Sell")) {
                catListNew.add(mContext.getResources().getString(R.string.buy_sell_menu));
            }else if (categoryList.get(i).equals("Services")) {
                catListNew.add(mContext.getResources().getString(R.string.services_menu));
            }else if (categoryList.get(i).equals("Loan related")) {
                catListNew.add(mContext.getResources().getString(R.string.loan_rel_menu));
            }else if (categoryList.get(i).equals("Matrimony related")) {
                catListNew.add(mContext.getResources().getString(R.string.mat_rel_menu));
            }else if (categoryList.get(i).equals("Books and Literature")) {
                catListNew.add(mContext.getResources().getString(R.string.book_lit_menu));
            }*/ else if (categoryList.get(i).equals("Career Related")) {
                catListNew.add(mContext.getResources().getString(R.string.career_rel_menu));
                catListNewEn.add("Career Related");
            }/*else if (categoryList.get(i).equals("Job")) {
                catListNew.add(mContext.getResources().getString(R.string.job_menu));
            }else if (categoryList.get(i).equals("Business")) {
                catListNew.add(mContext.getResources().getString(R.string.business_menu));
            }else if (categoryList.get(i).equals("Educational")) {
                catListNew.add(mContext.getResources().getString(R.string.edu_menu));
            }*/
        }
        //   Log.v("", "catListNewEn.size() " + catListNewEn.size());
       /* for (int i = 0; i < catListNewEn.size(); i++) {

            newsFeedListNew = newsListTable.getNewsRecordsByCategory(catListNewEn.get(i));
            Log.v("HomeFragment ", " newsFeedListNew " + newsFeedListNew);
            Log.v("", " newsFeedListNew.size " + newsFeedListNew.size());
        }*/

         Bundle args = new Bundle();
      //    args.putParcelableArrayList("news", (ArrayList<? extends Parcelable>) newsFeedListNew);
       //  args.putSerializable("News", (Serializable) newsFeedListNew);
        // for (int i = 0; i < catListNewEn.size(); i++) {
        // args.putStringArrayList("category",categoryList);
        // }
        /* for (int i = 0; i < catListNewEn.size(); i++) {
         args.putString("id", String.valueOf(newsFeedListNew.get(i).getId()));
         args.putString("news_uuid", String.valueOf(newsFeedListNew.get(i).getNews_uuid()));
         args.putString("category", String.valueOf(newsFeedListNew.get(i).getCategory()));
         args.putString("country", String.valueOf(newsFeedListNew.get(i).getCity()));
         args.putString("state", String.valueOf(newsFeedListNew.get(i).getState()));
         args.putString("city", String.valueOf(newsFeedListNew.get(i).getCity()));
         args.putString("news_title", String.valueOf(newsFeedListNew.get(i).getNews_title()));
         args.putString("news_description", String.valueOf(newsFeedListNew.get(i).getNews_description()));
         args.putString("like_count", String.valueOf(newsFeedListNew.get(i).getLike_count()));
         args.putString("member_id", String.valueOf(newsFeedListNew.get(i).getMember_id()));
         args.putString("created_at", String.valueOf(newsFeedListNew.get(i).getCreated_at()));
       //  args.putString("news_images", String.valueOf(newsFeedListNew.get(i).getId()));
        }*/

        //bundle.putSerializable("lstContact", (Serializable) lstObject);
        //lstObj = (List<Contacts>) bundle.getSerializable("lstContact");
/**/
/*Log.v("", " newsFeedListNew.size " + newsFeedListNew.size());

            Log.v("HomeFragment ", " newsFeedListNew " + newsFeedListNew.toString());
            for (int j = 0; j < newsFeedListNew.size(); j++) {
                Log.v("HomeFragment ", "NewsId " + newsFeedListNew.get(j).getId());
            }*/



        for (int i = 0; i < catListNewEn.size(); i++) {
            newsFeedListNew = newsListTable.getNewsRecordsByCategory(catListNewEn.get(i));

            pages.add(FragmentPagerItem.of(catListNew.get(i), NewsFeedFragment.class, new Bundler().putString("News",newsFeedList.toString()).get()
                    /*,new Bundler().putString("NewsDetails", obj.getString())*/));
        }
        JSONObject obj = null;
       /* try {
          *//*  obj = new JSONObject(newsFeedList.toString());
            NewsFeedList newsListModel=new NewsFeedList();
            JSONArray jsonArray = obj.getJSONArray("news");
            for(int i=0;i<jsonArray.length();i++){
                Bundler b=new Bundler();
                b.putString("SubjectDetails", jsonArray.getString(i)).get();
                newsListModel=new Gson().fromJson(jsonArray.getString(i), NewsFeedList.class);
                pages.add(FragmentPagerItem.of(newsListModel.getCategory(),NewsFeedList.class,b));
            }*//*
        } catch (JSONException e) {
            e.printStackTrace();
        }
*/
       /* for (int titleResId : tabsValues()) {
            pages.add(FragmentPagerItem.of(getString(titleResId), NewsFeedFragment.class));
        }*/
      /*  for (int i = 0; i < catListNew.size(); i++) {
            fpAdapter = new FragmentPagerItemAdapter(getFragmentManager(),
                    FragmentPagerItems.with(mContext)
                    .add(catListNew.get(i), NewsFeedFragment.class, new Bundler().putString("News", newsFeedListNew.get(i).toString()).get())
                    .create());//getSupportFragmentManager
        }*/
        viewPager.setAdapter(fpAdapter);
        viewPagerTab.setViewPager(viewPager);

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

}

