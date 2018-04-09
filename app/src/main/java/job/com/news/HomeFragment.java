package job.com.news;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import job.com.news.adapter.DynamicFragmentAdapter;
import job.com.news.adapter.HomeDashboardAdapter;
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
    private static final String TAG = "HomeFragment";
    Context mContext;
    Toolbar toolbar;
    private ProgressDialog mProgressDialog;
    private List<NewsFeedList> newsFeedList = new ArrayList<>();
    String catId;
    private List<NewsFeedList> newsFeedListNew;
    private List<NewsFeedList> listItems;
    private List<String> catDupList, subCatDupList;
    List<Integer> idListCat;
    int categoryId;
    ArrayList<String> categoryList, subCategoryList, catListNew;
    ArrayList<Integer> subCategoryIDList, categoryIDList;
    ArrayList<String> subCategoryListLang = new ArrayList<>();
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
    String emailId, fullName, memberToken, language, childName;
    int memberId, expandPosition;
    //private HashMap<String, ArrayList<String>> hashMap;
    private NewsFeedApplication newsFeedApplication;
    private SessionManager session, langSelection;
    private final List<Fragment> mFragmentList = new ArrayList<>();
    //Title List
    private final List<String> mFragmentTitleList = new ArrayList<>();
    DynamicFragmentAdapter mDynAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Log.v(TAG, " onCreateView called");
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        //setTootlbarTitle("Home");
        toolbar.setTitle(getResources().getString(R.string.home_toolbar_title));

        //setHasOptionsMenu(false);
        mContext = getActivity();
        initializeComponents();
        getPrefData();


        attachViews(view);
        setClickListeners();
        //  syncNewsList();


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
        subCatDupList = new ArrayList<>();
        subCategoryList = new ArrayList<>();
        subCategoryIDList = new ArrayList<>();
        categoryIDList = new ArrayList<>();
        newsFeedListNew = new ArrayList<>();
        listItems = new ArrayList<>();
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
        expandPosition = myPreferences.getExpandPosition();
        childName = myPreferences.getExpandChildName();
        Log.v("", "expandPosition " + expandPosition);
        Log.v("", "childName " + childName);
    }

    private void setClickListeners() {
        Collections.reverse(catListNewEn);
        for (int i = 0; i < catListNewEn.size(); i++) {
            if (/*childName != null || */!childName.equals("null")) {
                if (childName.equals(catListNewEn.get(i))) {
                    viewPager.setCurrentItem(i);
                } else {
                    Toast.makeText(mContext, "No data available", Toast.LENGTH_SHORT).show();
                }


                /*else if (fromStatus.equals("rejected")) {
                mViewPager.setCurrentItem(2);
            } else {
                mViewPager.setCurrentItem(0);
            }*/
            }
        }
    }

    private void attachViews(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.home_viewpager);
        //  viewPagerTab = (SmartTabLayout) view.findViewById(R.id.viewpagertab);

        mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        loadCategoryList();

        //catListNew == not required
        //subCategoryIDList == contains sub category ids by response data
        //

        // for(int k=0;k<newsFeedListNew.size();k++) {
        // mDynAdapter = new DynamicFragmentAdapter(getFragmentManager(), catListNew, catListNewEn,subCategoryIDList, "");

        mDynAdapter = new DynamicFragmentAdapter(getFragmentManager(), catListNew, categoryIDList, categoryId, subCategoryIDList, "");
        //     Log.v("11", " catListNew " + catListNew);
        Collections.reverse(catListNew);
        for (int i = 0; i < catListNew.size(); i++) {

            addTab(catListNew.get(i));

        }
        // Log.v("22", " catListNew " + catListNew);
        viewPager.setAdapter(mDynAdapter);
    }

    private void addTab(String title) {
        mTabLayout.addTab(mTabLayout.newTab().setText(title));
        addTabPage();
    }

    public void addTabPage() {
        mDynAdapter.notifyDataSetChanged();
    }

    @SuppressLint("LongLogTag")
    private void loadCategoryList() {


        newsFeedList = newsListTable.getAllNewsRecords();
        // categoryList = newsListTable.getCategory();

        Log.v(TAG + " loadCategoryList ", "newsFeedList.size() " + newsFeedList.size());
        Log.v(TAG + " loadCategoryList ", "newsFeedList " + newsFeedList.toString());
        if (newsFeedList != null) {
            for (int k = 0; k < newsFeedList.size(); k++) {
                catDupList.add(newsFeedList.get(k).getCategory());
                categoryIDList.add(Integer.valueOf(newsFeedList.get(k).getCategory_id()));
                if (/*newsFeedList.get(k).getSub_category()!=null || */newsFeedList.get(k).getSub_category_id() != null) {
                    subCatDupList.add(newsFeedList.get(k).getSub_category());

                }
                if (newsFeedList.get(k).getSub_category() == null) {
                    subCategoryIDList.add(0, 0);
                } else {
                    subCategoryIDList.add(Integer.valueOf(newsFeedList.get(k).getSub_category_id()));
                }
            }
            Log.v(TAG + " loadCategoryList ", "subCategoryIDList " + subCategoryIDList);
            //if(news.categoryId=categoryM.categoryId){//10
            // getCategoryNameFromDb(categoryList.get(i).toString)
//            String ids[]=catId.split(",");
            // idListCat
            categoryList.addAll(new HashSet<>(catDupList));
            subCategoryList.addAll(new HashSet<>(subCatDupList));
           /* for (int i=0;i<categoryIDList.size();i++) {
                categoryId=categoryIDList.get(i);

            }*/

            //   List<Integer> categoryList = categoryMasterTable.getCategoryId();

            //
            // subCategoryIDList.addAll(subCategoryTable.getSubCatIdByCatId(categoryId));

          /*  List<Integer> catIdList=new ArrayList<>(categoryMasterTable.getCategoryId());

            for(int i=0;i<catIdList.size();i++){ //4 & catIsList == 10

                if(ids[i])
            }
*/
            for (int i = 0; i < categoryList.size(); i++) {
                //   for (int l = 0; l < subCategoryList.size(); l++) {
                categoryId = categoryMasterTable.getCategoryIdByName(categoryList.get(i).toString());

                if (categoryList.get(i).equals("National and International")) {
                    catListNew.add(mContext.getResources().getString(R.string.national_inter_menu));
                    catListNewEn.add("National and International");
                } else if (categoryList.get(i).equals("Government News")) {
                    catListNew.add(mContext.getResources().getString(R.string.gov_news_menu));
                    catListNewEn.add("Government News");
                } else if (categoryList.get(i).equals("Social and Related News") || categoryList.get(i).equals("Social News")) {
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
                } else if (categoryList.get(i).equals("Health Related") || categoryList.get(i).equals("Health")) {
                    catListNew.add(mContext.getResources().getString(R.string.health_rel_menu));
                    catListNewEn.add("Health Related");
                } else if (categoryList.get(i).equals("Business News") || categoryList.get(i).equals("Business")) {
                    catListNew.add(mContext.getResources().getString(R.string.business_news_menu));
                    catListNewEn.add("Business News");
                } else if (categoryList.get(i).equals("Agricultural News")) {
                    catListNew.add(mContext.getResources().getString(R.string.agri_news_menu));
                    catListNewEn.add("Agricultural News");
                } else if (categoryList.get(i).equals("Cinema Related") || categoryList.get(i).equals("Cinema")) {
                    catListNew.add(mContext.getResources().getString(R.string.cinema_menu));
                    catListNewEn.add("Cinema Related");
                } else if (categoryList.get(i).equals("Small Classifieds")) {
                    for (int m = 0; m < subCategoryList.size(); m++) {
                        //catListNew.add(mContext.getResources().getString(R.string.small_class_menu));
                        //catListNewEn.add("Small Classifieds");
                        if (subCategoryList.get(m).equals("Property")) {
                            catListNew.add(mContext.getResources().getString(R.string.property_menu));
                            catListNewEn.add("Property");
                        } else if (subCategoryList.get(m).equals("Birthday ads")) {
                            catListNew.add(mContext.getResources().getString(R.string.birth_menu));
                            catListNewEn.add("Birthday ads");
                        } else if (subCategoryList.get(m).equals("App Related Ads")) {
                            catListNew.add(mContext.getResources().getString(R.string.app_rel_menu));
                            catListNewEn.add("App Related Ads");
                        } else if (subCategoryList.get(m).equals("Buy and Sell")) {
                            catListNew.add(mContext.getResources().getString(R.string.buy_sell_menu));
                            catListNewEn.add("Buy and Sell");
                        } else if (subCategoryList.get(m).equals("Services")) {
                            catListNew.add(mContext.getResources().getString(R.string.services_menu));
                            catListNewEn.add("Services");
                        } else if (subCategoryList.get(m).equals("Loan related")) {
                            catListNew.add(mContext.getResources().getString(R.string.loan_rel_menu));
                            catListNewEn.add("Loan related");
                        } else if (subCategoryList.get(m).equals("Matrimony related") ||
                                subCategoryList.get(m).equals("Matrimony")) {
                            catListNew.add(mContext.getResources().getString(R.string.mat_rel_menu));
                            catListNewEn.add("Matrimony related");
                        } else if (subCategoryList.get(m).equals("Books and Literature")) {
                            catListNew.add(mContext.getResources().getString(R.string.book_lit_menu));
                            catListNewEn.add("Books and Literature");
                        }
                    }
                } else if (categoryList.get(i).equals("Other and Uncategorized News")) {
                    catListNew.add(mContext.getResources().getString(R.string.other_uncat_menu));
                    catListNewEn.add("Other and Uncategorized News");
                } else if (categoryList.get(i).equals("Entertainment News") || categoryList.get(i).equals("Entertainment")) {
                    catListNew.add(mContext.getResources().getString(R.string.ent_news_menu));
                    catListNewEn.add("Entertainment News");
                } else if (categoryList.get(i).equals("Career Related") || categoryList.get(i).equals("Career")) {
                    for (int m = 0; m < subCategoryList.size(); m++) {
                        // catListNew.add(mContext.getResources().getString(R.string.career_rel_menu));
                        //  catListNewEn.add("Career Related");
                        if (subCategoryList.get(m).equals("Job")) {
                            catListNew.add(mContext.getResources().getString(R.string.job_menu));
                            catListNewEn.add("Job");
                        } else if (subCategoryList.get(m).equals("Business")) {
                            catListNew.add(mContext.getResources().getString(R.string.business_menu));
                            catListNewEn.add("Business");
                        } else if (subCategoryList.get(m).equals("Educational")) {
                            catListNew.add(mContext.getResources().getString(R.string.edu_menu));
                            catListNewEn.add("Educational");
                        }
                    }
                }


/*
                    if (subCategoryList.get(l).equals("Property")) {
                        catListNew.add(mContext.getResources().getString(R.string.cinema_menu));
                        catListNewEn.add("Property");
                    } else if (subCategoryList.get(l).equals("Birthday ads")) {
                        catListNew.add(mContext.getResources().getString(R.string.birth_menu));
                        catListNewEn.add("Birthday ads");
                    } else if (subCategoryList.get(l).equals("App Related Ads")) {
                        catListNew.add(mContext.getResources().getString(R.string.app_rel_menu));
                        catListNewEn.add("App Related Ads");
                    } else if (subCategoryList.get(l).equals("Buy and Sell")) {
                        catListNew.add(mContext.getResources().getString(R.string.buy_sell_menu));
                        catListNewEn.add("Buy and Sell");
                    } else if (subCategoryList.get(l).equals("Services")) {
                        catListNew.add(mContext.getResources().getString(R.string.services_menu));
                        catListNewEn.add("Services");
                    } else if (subCategoryList.get(l).equals("Loan related")) {
                        catListNew.add(mContext.getResources().getString(R.string.loan_rel_menu));
                        catListNewEn.add("Loan related");
                    } else if (subCategoryList.get(l).equals("Matrimony related")) {
                        catListNew.add(mContext.getResources().getString(R.string.mat_rel_menu));
                        catListNewEn.add("Matrimony related");
                    } else if (subCategoryList.get(l).equals("Books and Literature")) {
                        catListNew.add(mContext.getResources().getString(R.string.book_lit_menu));
                        catListNewEn.add("Books and Literature");
                    } else if (subCategoryList.get(l).equals("Job")) {
                        catListNew.add(mContext.getResources().getString(R.string.job_menu));
                        catListNewEn.add("Job");
                    } else if (subCategoryList.get(l).equals("Business")) {
                        catListNew.add(mContext.getResources().getString(R.string.business_menu));
                        catListNewEn.add("Business");
                    } else if (subCategoryList.get(l).equals("Educational")) {
                        catListNew.add(mContext.getResources().getString(R.string.edu_menu));
                        catListNewEn.add("Educational");
                    }*/
                //  }
            }
        }
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

