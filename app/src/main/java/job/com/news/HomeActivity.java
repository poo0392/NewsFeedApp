package job.com.news;


import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import job.com.news.adapter.ExpandListAdapter;
import job.com.news.adapter.HomeDashboardAdapter;
import job.com.news.adapter.ImageAdapter;
import job.com.news.changepassword.ChangePassword;
import job.com.news.interfaces.ItemClickListener;
import job.com.news.models.NewsFeedDetails;
import job.com.news.sharedpref.SessionManager;

import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Context mContext;
    private RecyclerView mRecyclerView;
    private ImageAdapter adapter;
    //private HashMap<String, ArrayList<String>> hashMap;
    private NewsFeedApplication newsFeedApplication;
    private LinearLayout mSmallClassiLayout, mCareerLayout;
    private TextView menuSmallClassi;
    private TextView menuCareer;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private SessionManager session, langSelection;
    Toolbar toolbar;
    ExpandListAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    ExpandableListView expListView;
    HomeDashboardAdapter mAdapter;
    List<NewsFeedDetails> mNewsFeedList;
    private FragmentPagerItemAdapter mFPIAdapter;
    private FragmentPagerItems pages;
    private ViewPager viewPager;
    private SmartTabLayout viewPagerTab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = this;
        newsFeedApplication = NewsFeedApplication.getApp();
        session = new SessionManager(getApplicationContext());
        langSelection = new SessionManager(getApplicationContext());

        if (!checkPermission()) {
            requestPermission();

        }
        setAppToolbar();
        setLocaleLang();
        initialializeComponents();
        setListeners();


        LinearLayout mMenuLayout = (LinearLayout) findViewById(R.id.main_menu_layout);

        mSmallClassiLayout = (LinearLayout) findViewById(R.id.small_classi_layout);
        mCareerLayout = (LinearLayout) findViewById(R.id.career_layout);

        TextView menuNationalInter = (TextView) findViewById(R.id.menu_nat_inter_text);
        menuNationalInter.setOnClickListener(this);

        TextView menuGov = (TextView) findViewById(R.id.menu_gov);
        menuGov.setOnClickListener(this);

        TextView menuSocRel = (TextView) findViewById(R.id.menu_soc_rel);
        menuSocRel.setOnClickListener(this);

        TextView menuSports = (TextView) findViewById(R.id.menu_sports);
        menuSports.setOnClickListener(this);

        TextView menuSciTech = (TextView) findViewById(R.id.menu_sci_tech);
        menuSciTech.setOnClickListener(this);

        TextView menuEco = (TextView) findViewById(R.id.menu_eco);
        menuEco.setOnClickListener(this);

        TextView menuHealth = (TextView) findViewById(R.id.menu_health);
        menuHealth.setOnClickListener(this);

        TextView menuBusinessNews = (TextView) findViewById(R.id.menu_business_news);
        menuBusinessNews.setOnClickListener(this);

        TextView menuAgri = (TextView) findViewById(R.id.menu_agri);
        menuAgri.setOnClickListener(this);

        TextView menuCinema = (TextView) findViewById(R.id.menu_cinema);
        menuCinema.setOnClickListener(this);

        menuSmallClassi = (TextView) findViewById(R.id.menu_small_classi);
        menuSmallClassi.setOnClickListener(this);

        TextView menuProperty = (TextView) findViewById(R.id.menu_property);
        menuProperty.setOnClickListener(this);

        TextView menuBirthday = (TextView) findViewById(R.id.menu_birthday);
        menuBirthday.setOnClickListener(this);

        TextView menuAppRel = (TextView) findViewById(R.id.menu_app_rel);
        menuAppRel.setOnClickListener(this);

        TextView menuBuySell = (TextView) findViewById(R.id.menu_buy_sell);
        menuBuySell.setOnClickListener(this);

        TextView menuServices = (TextView) findViewById(R.id.menu_services);
        menuServices.setOnClickListener(this);

        TextView menuLoanRel = (TextView) findViewById(R.id.menu_loan_rel);
        menuLoanRel.setOnClickListener(this);

        TextView menuMatRel = (TextView) findViewById(R.id.menu_mat_rel);
        menuMatRel.setOnClickListener(this);

        TextView menuBooksLit = (TextView) findViewById(R.id.menu_books_lit);
        menuBooksLit.setOnClickListener(this);

        TextView menuOther = (TextView) findViewById(R.id.menu_other);
        menuOther.setOnClickListener(this);

        TextView menuEnt = (TextView) findViewById(R.id.menu_ent);
        menuEnt.setOnClickListener(this);

        menuCareer = (TextView) findViewById(R.id.menu_career);
        menuCareer.setOnClickListener(this);

        TextView menuJob = (TextView) findViewById(R.id.menu_job);
        menuJob.setOnClickListener(this);

        TextView menuBusiness = (TextView) findViewById(R.id.menu_business);
        menuBusiness.setOnClickListener(this);

        TextView menuEdu = (TextView) findViewById(R.id.menu_edu);
        menuEdu.setOnClickListener(this);


    }
    //navigation Change

    private void setLocaleLang() {
        String[] lang_arr = getResources().getStringArray(R.array.language_arr);
        String getLang = langSelection.getLanguage();

        Log.v("HomeActivity ", "getLang " + getLang);
        Configuration config = new Configuration();
        Locale locale;
        if (getLang.equalsIgnoreCase(lang_arr[1])) {
            locale = new Locale("hi");
            Locale.setDefault(locale);
            config.setLocale(locale);
        } else if (getLang.equalsIgnoreCase(lang_arr[2])) {
            locale = new Locale("mr");
            Locale.setDefault(locale);
            config.setLocale(locale);
        }
        //getResources().getConfiguration().setTo(config);
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    private void setAppToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initialializeComponents() {



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        //   navigationView.addHeaderView(header);
        enableExpandableList();
        // navigationView.setNavigationItemSelectedListener(this);

       /* mRecyclerView = (RecyclerView) findViewById(R.id.news_feed_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        setData();*/


        /*addNewsFeedItems();
        mAdapter = new HomeDashboardAdapter(HomeActivity.this, mNewsFeedList);
        mRecyclerView.setAdapter(mAdapter);*/
       // adapter = new ImageAdapter(HomeActivity.this);
       // mRecyclerView.setAdapter(adapter);

        viewPager = (ViewPager)findViewById(R.id.home_viewpager);
        viewPagerTab = (SmartTabLayout)findViewById(R.id.viewpagertab);

        pages = new FragmentPagerItems(HomeActivity.this);
        FragmentPagerItems pages = new FragmentPagerItems(this);
        for (int titleResId : tabsValues()) {
            pages.add(FragmentPagerItem.of(getString(titleResId), NewsFeedFragment.class));
        };

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), pages);

        viewPager.setAdapter(adapter);
        viewPagerTab.setViewPager(viewPager);

        //
    }

    private int[] tabsValues() {
        return new int[] {
                R.string.recent_news,
                R.string.national_inter_menu,
                R.string.gov_news_menu,
                R.string.soc_rel_menu,
                R.string.sports_menu,
                R.string.sci_tech_menu,
                R.string.eco_news_menu,
                R.string.health_rel_menu,
                R.string.business_news_menu,
                R.string.agri_news_menu,
                R.string.cinema_menu
        };
    }

    private void addNewsFeedItems() {
        mNewsFeedList = new ArrayList<>();
        mNewsFeedList.add(new NewsFeedDetails("'We are here to stay', say Indian-Americans amid growing hate crime incidents in the US","10 mins ago",
                "1-01-2018",getResources().getDrawable(R.drawable.lebanon),"Mumbai","Maharashtra","John Williams"));
         mNewsFeedList.add(new NewsFeedDetails("Development of all, appeasement of none: Yogi's mantra for UP","1 day ago",
                "12-07-2017",getResources().getDrawable(R.drawable.yogi_adinath),"Mumbai","Maharashtra","Richie K"));
            mNewsFeedList.add(new NewsFeedDetails("India to seal border with Pakistan by 2018: Rajnath Singh","20 mins ago",
                "11-04-2017",getResources().getDrawable(R.drawable.india_pak_border),"Mumbai","Maharashtra","G K"));

    }

    private void enableExpandableList() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        expListView = (ExpandableListView) findViewById(R.id.left_drawer);
        expListView.setIndicatorBounds(expListView.getRight() - 40, expListView.getWidth());
        prepareListData(listDataHeader, listDataChild);
        listAdapter = new ExpandListAdapter(this, listDataHeader, listDataChild);
        // setting list adapter
        expListView.setAdapter(listAdapter);

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
             /*   Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();*/
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
              /*  Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();*/

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                // Temporary code:

                // till here
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });
    }

    private void prepareListData(List<String> listDataHeader, HashMap<String, List<String>> listDataChild) {
        listDataHeader.add("Home");
        listDataHeader.add("User Profile");
        listDataHeader.add("Requests");
        listDataHeader.add("News Categories");
        listDataHeader.add("About Us");

        List<String> requestsChild = new ArrayList<String>();
        requestsChild.add("Pending Request");
        requestsChild.add("Request Status");


        List<String> newsChild = new ArrayList<String>();
        newsChild.add("National and International");
        newsChild.add("Government News");
        newsChild.add("Social and Related News");
        newsChild.add("Sports");
        newsChild.add("Science and Technology");
        newsChild.add("Economical News");
        newsChild.add("Health Related");
        newsChild.add("Business News");
        newsChild.add("Agricultural News");
        newsChild.add("Cinema Related");

        listDataChild.put(listDataHeader.get(2), requestsChild); // Header, Child data
        listDataChild.put(listDataHeader.get(3), newsChild);


    }

    private void setListeners() {

    }

    private boolean checkPermission() {

        int readStoragePermissionResult = ContextCompat.checkSelfPermission(mContext, READ_EXTERNAL_STORAGE);
        int writeStoragePermissionResult = ContextCompat.checkSelfPermission(mContext, WRITE_EXTERNAL_STORAGE);

        return readStoragePermissionResult == PackageManager.PERMISSION_GRANTED &&
                writeStoragePermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{
                READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean readStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (readStorageAccepted && writeStorageAccepted) {
                        /*Toast.makeText(mContext, "All Permissions Granted",
                                Toast.LENGTH_SHORT).show();*/
                        //Snackbar.make(recyclerView, "Permission Granted, Now you can access Gallery and camera.", Snackbar.LENGTH_LONG).show();
                        /*if (userChoosenTask.equals("Take Photo"))
                            cameraIntent();
                        else if (userChoosenTask.equals("Choose from Library"))
                            pickImageFromGallery();*/
                    } else {

                        Toast.makeText(mContext, "Permission Denied, You cannot access Gallery",
                                Toast.LENGTH_SHORT).show();
                        //Snackbar.make(recyclerView, "Permission Denied, You cannot access Gallery and camera.", Snackbar.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow access to the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{
                                                                    READ_EXTERNAL_STORAGE,
                                                                    WRITE_EXTERNAL_STORAGE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }

                    break;
                }
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(mContext)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
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

    @Override
    public void onClick(View v) {
        if (v instanceof TextView) {
            String tag = v.getTag().toString().trim();
            if (tag.equalsIgnoreCase("1") || tag.equalsIgnoreCase("2")) {
                if (tag.equalsIgnoreCase("1")) {          //small Classified
                    mCareerLayout.setVisibility(View.GONE);
                    menuCareer.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_keyboard_arrow_down_black_24dp, 0);
                    if (mSmallClassiLayout.getVisibility() == View.VISIBLE) {
                        mSmallClassiLayout.setVisibility(View.GONE);
                        menuSmallClassi.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_keyboard_arrow_down_black_24dp, 0);
                    } else {
                        mSmallClassiLayout.setVisibility(View.VISIBLE);
                        /*Drawable d =
                        Drawable drawable = getResources().getDrawable(R.mipmap.ic_keyboard_arrow_down_black_24dp);
                        menuSmallClassi.setCompoundDrawables(null, null, R.mipmap.ic_keyboard_arrow_down_black_24dp, null);*/
                        menuSmallClassi.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_keyboard_arrow_up_black_24dp, 0);
                    }
                } else if (tag.equalsIgnoreCase("2")) {          //Career Layout
                    mSmallClassiLayout.setVisibility(View.GONE);
                    menuSmallClassi.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_keyboard_arrow_down_black_24dp, 0);
                    if (mCareerLayout.getVisibility() == View.VISIBLE) {
                        mCareerLayout.setVisibility(View.GONE);
                        menuCareer.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_keyboard_arrow_down_black_24dp, 0);
                    } else {
                        mCareerLayout.setVisibility(View.VISIBLE);
                        menuCareer.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_keyboard_arrow_up_black_24dp, 0);
                    }
                }
            } else {
                selectItem(tag);
            }
        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        setSearchViewMenu(menu);
        return true;
    }

    public void setSearchViewMenu(final Menu menu) {
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean newViewFocus) {
                if (!newViewFocus) {
                    //Collapse the action item.
                    searchItem.collapseActionView();
                    //Clear the filter/search query.
                    menu.findItem(R.id.action_search).setVisible(true);
                    menu.findItem(R.id.action_create_article).setVisible(true);
                    menu.findItem(R.id.action_change_pwd).setVisible(true);
                    menu.findItem(R.id.action_logout).setVisible(true);
                } else {
                    menu.findItem(R.id.action_logout).setVisible(false);
                    menu.findItem(R.id.action_change_pwd).setVisible(false);
                    menu.findItem(R.id.action_create_article).setVisible(false);
                }
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setItemsVisibility(menu, searchItem, false);
            }
        });
        // Detect SearchView close
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                setItemsVisibility(menu, searchItem, true);
                return false;
            }
        });
    }


    private void setItemsVisibility(Menu menu, MenuItem exception, boolean visible) {
        for (int i = 0; i < menu.size(); ++i) {
            MenuItem item = menu.getItem(i);
            if (item != exception) item.setVisible(visible);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_create_article) {
            Intent intent = new Intent(this, CreateArticle.class);
            startActivity(intent);
        } else if (id == R.id.action_change_pwd) {
            Intent intent = new Intent(this, ChangePassword.class);
            startActivity(intent);
        } else if (id == R.id.action_logout) {
            session.setLogin(false);
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        int order = item.getOrder();
        //selectItem(order);
        /*switch (id){
            case R.id.create_article:
                Intent intent = new Intent(this, CreateArticle.class);
                startActivity(intent);
                break;

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void selectItem(String order) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment newFragment = ContentFragment.newInstance(order);
        //transaction.replace(R.id.content_frame, newFragment);
        transaction.replace(R.id.content_frame, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
