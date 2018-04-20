package job.com.news;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import job.com.news.adapter.ExpandListAdapter;
import job.com.news.changepassword.ChangePassword;
import job.com.news.db.CategoryMasterTable;
import job.com.news.db.MemberTable;
import job.com.news.db.NewsImagesTable;
import job.com.news.db.NewsListTable;
import job.com.news.db.SubCategoryTable;
import job.com.news.helper.ConnectivityInterceptor;
import job.com.news.helper.NoConnectivityException;
import job.com.news.interfaces.FragmentFromAdapter;
import job.com.news.interfaces.WebService;
import job.com.news.models.NewsFeedList;
import job.com.news.models.NewsFeedListParcable;
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

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static job.com.news.globals.Globals.back_press_screen;

//changes added on //changes 16_03.
public class HomeActivity extends AppCompatActivity
        implements View.OnClickListener, FragmentFromAdapter {
    //changes reflect to be 05/03
    private static final String TAG = "HomeActivity";
    private Context mContext;
    private LinearLayout mSmallClassiLayout, mCareerLayout;
    private TextView menuSmallClassi;
    private TextView menuCareer, txt_header_username, txt_header_email;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private SessionManager session, langSelection;
    Toolbar toolbar;
    private MyPreferences myPreferences;
    String emailId, fullName, memberToken, cityFilter, stateFilter, categoryFilter;
    int memberId;
    String role;
    String jsonResponse;
    LinearLayout ll_linBase;
    private NewsFeedApplication newsFeedApplication;
    ExpandListAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild, listThirdLevelChild;
    List<HashMap<String, List<String>>> data;
    ExpandableListView expListView;
    ProgressDialog mProgressDialog;
    NewsListTable newsListTable;
    MemberTable memberTable;
    NewsImagesTable newsImagesTable;
    CategoryMasterTable categoryMasterTable;
    SubCategoryTable subCategoryTable;
    private List<NewsFeedList> newsFeedList;
    private List<NewsFeedList> newsFeedListNew;
    private List<NewsFeedList> ListNew, catList;
    private List<NewsFeedListParcable> newsFeedListParc = new ArrayList<>();
    private List<String> categoryList, catDupList;
    Gson gson;
    Fragment fragment;
    JSONArray jsonArray;
    private int lastExpandedPosition = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = this;
        newsFeedApplication = NewsFeedApplication.getApp();
        session = new SessionManager(mContext);
        langSelection = new SessionManager(mContext);
        newsListTable = new NewsListTable(mContext);
        ll_linBase = (LinearLayout) findViewById(R.id.ll_linBase);
        back_press_screen=2;
        //DBHelper.getInstance(getApplicationContext());
        scheduleAlarm();
        getPrefData();


        /*if (role.equals("1")) {
            callRequestStatusHomeFragment();
        } else {
            callHomeFragment();
        }*/


        if (!checkPermission()) {
            requestPermission();

        }
        setLocaleLang();

        setAppToolbar();
        initialializeComponents();
        callHomeFragment();
        callNewsListAPI(memberToken, memberId);

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

    private void callRequestStatusHomeFragment() {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.content_frame, new RequestStatusHomeFragment());
        // tx.addToBackStack(null);
        tx.commit();
    }

    public void callHomeFragment() {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.content_frame, new HomeFragment());
        // tx.addToBackStack(null);
        tx.commit();
    }

    private void scheduleAlarm() {
        // startService(new Intent(this, AlarmReceiver.class));
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(this, AlarmReceiver.class);
        //PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, AlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every every half hour from this point onwards
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                AlarmManager.INTERVAL_HALF_HOUR, pIntent); //

        // AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        // Start service every 15 min
        // alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 900000, pintent);

       /* Intent alarm = new Intent(HomeActivity.this, AlarmReceiver.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(HomeActivity.this, 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);
        if (alarmRunning == false) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(HomeActivity.this, 0, alarm, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 900000, pendingIntent);
        }*/
    }

    public void callNewsListAPI(String memberToken, int memberId) {
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

        String news_status = "";
        String all_news = "";
        WebService webService = retrofit.create(WebService.class);
        long last_id = newsListTable.getLastId();
        RequestBody paramMemberToken = RequestBody.create(MediaType.parse("text/plain"), memberToken);
        RequestBody paramMemberId = RequestBody.create(MediaType.parse("text/plain"), "" + memberId);
        RequestBody paramAllNews = RequestBody.create(MediaType.parse("text/plain"), "1");
        RequestBody status = RequestBody.create(MediaType.parse("text/plain"), news_status);

        HashMap<String, String> newsRequestList = new HashMap<>();
        newsRequestList.put("member_token", memberToken);
        newsRequestList.put("member_id", String.valueOf(memberId));
        newsRequestList.put("last_id", String.valueOf(last_id));
        newsRequestList.put("all_news", "1");
        newsRequestList.put("news_status", news_status);

        Log.v("callNewsListAPI", " newsRequestList " + newsRequestList);


        Call<NewsFeedModelResponse> serverResponse = webService.getNewsListRequest(paramMemberToken, paramMemberId, status, last_id, paramAllNews);
        //   Call<NewsFeedModelResponse> serverResponse = webService.getNewsListRequest(paramMemberToken, paramMemberId,id);

        serverResponse.enqueue(new Callback<NewsFeedModelResponse>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<NewsFeedModelResponse> call, Response<NewsFeedModelResponse> response) {
                mProgressDialog.dismiss();
                String newsList = "";
                if (response.isSuccessful()) {

                  /*  Type collectionType = new TypeToken<List<NewsFeedModelResponse>>() {
                    }.getType();
                    List<NewsFeedModelResponse> lcs = (List<NewsFeedModelResponse>) new Gson()
                            .fromJson(String.valueOf(response.body()), collectionType);*/

                    //  NewsFeedModelResponse serverResponse = new Gson().fromJson(response.body().toString(), NewsFeedModelResponse.class);
                    NewsFeedModelResponse serverResponse = response.body();
                    jsonResponse = new Gson().toJson(response.body());
                    Log.v(TAG + " callNewsListAPI ", "response " + jsonResponse);
                    String code = serverResponse.getCode();
                    String desc = serverResponse.getDescription();
                    if (serverResponse.getStatus() == 0) {
                        if (serverResponse.getNewsFeedList() == null) {
                            Log.v("callNewsListAPI ", "newsFeedList " + "null");
                            Toast.makeText(mContext, "Error in Response. Please check after some time.", Toast.LENGTH_SHORT).show();
                        } else {

                            try {
                                newsFeedList = serverResponse.getNewsFeedList();
                                //    Log.v("", "newsFeedList " + newsFeedList.toString());

                                NewsFeedList model;
                                try {
                                    RegisterMember member = new RegisterMember();
                                    List<NewsImages> imagesList = new ArrayList<>();
                                    NewsImages imagesModel = new NewsImages();
                                    int n=serverResponse.getNewsFeedList().size();
                                    for (int i = n-1; i < n; i--) {
                                        if (!newsListTable.checkNewsPresent(serverResponse.getNewsFeedList().get(i).getId())) {


                                            model = new NewsFeedList(serverResponse.getNewsFeedList().get(i).getId(),
                                                    serverResponse.getNewsFeedList().get(i).getNews_uuid(),
                                                    serverResponse.getNewsFeedList().get(i).getCategory(),
                                                    serverResponse.getNewsFeedList().get(i).getCategory_id(),
                                                    serverResponse.getNewsFeedList().get(i).getSub_category(),
                                                    serverResponse.getNewsFeedList().get(i).getSub_category_id(),
                                                    serverResponse.getNewsFeedList().get(i).getCountry(),
                                                    serverResponse.getNewsFeedList().get(i).getState(),
                                                    serverResponse.getNewsFeedList().get(i).getCity(),
                                                    serverResponse.getNewsFeedList().get(i).getNews_title(),
                                                    serverResponse.getNewsFeedList().get(i).getNews_description(),
                                                    serverResponse.getNewsFeedList().get(i).getLanguage(),
                                                    serverResponse.getNewsFeedList().get(i).getComment(),
                                                    serverResponse.getNewsFeedList().get(i).getLike_count(),
                                                    serverResponse.getNewsFeedList().get(i).getMember_id(),
                                                    serverResponse.getNewsFeedList().get(i).getCreated_at(),
                                                    serverResponse.getNewsFeedList().get(i).getNews_images(),
                                                    serverResponse.getNewsFeedList().get(i).getMember()
                                            );

                                            // for (int k = 0; k < serverResponse.getNewsFeedList().get(i).getMember(); k++) {
                                            if (!memberTable.checkUser(serverResponse.getNewsFeedList().get(i).getMember().getId())) {
                                                member.setMemberId(model.getMember().getId());
                                                //   member.setMemberToken(model.getMembersList().get(j).getMemberToken().trim());
                                                member.setFirstName(model.getMember().getFirstName().trim());
                                                member.setLastName(model.getMember().getLastName().trim());
                                                member.setEmailId(model.getMember().getEmailId().trim());
                                                member.setMobile(model.getMember().getMobile());

                                                memberTable.insertMembers(member);

                                            }
                                            // }
                                            //Log.v("", "getNews_images().size() " + serverResponse.getNewsFeedList().get(i).getNews_images().size());
                                            if (serverResponse.getNewsFeedList().get(i).getNews_images() != null && serverResponse.getNewsFeedList().get(i).getNews_images().size() > 0) {
                                              int p=serverResponse.getNewsFeedList().get(i).getNews_images().size();
                                                for (int j = p-1; j < p; j++) {
                                                    imagesModel.setId(model.getNews_images().get(j).getId());
                                                    imagesModel.setNews_id(model.getNews_images().get(j).getNews_id());
                                                    imagesModel.setNews_pic(model.getNews_images().get(j).getNews_pic());
                                                    imagesModel.setCreated_at(model.getNews_images().get(j).getCreated_at());
                                                    imagesModel.setUpdated_at(model.getNews_images().get(j).getUpdated_at());

                                                    imagesList.add(imagesModel);

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

                            //setListeners();
                            // scheduleAlarm();
                            // loadCategoryUI();
                        }
                    } else {
                        Log.v("Failure ", "status " + serverResponse.getStatus() + " Desc " + serverResponse.getDescription());
                        //setFailedAlertDialog(HomeActivity.this, serverResponse.getStatus().toString(), "Failure");
                        Toast.makeText(mContext, "Error in Response ", Toast.LENGTH_SHORT).show();

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
    //navigation Change

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        // Intent refresh = new Intent(this, HomeActivity.class);
        //   startActivity(refresh);
        // finish();
    }

    private void setLocaleLang() {
        String[] lang_arr = getResources().getStringArray(R.array.language_arr);
        String getLang = langSelection.getLanguage();

        //  Log.v("HomeActivity ", "getLang " + getLang);
        Configuration config = new Configuration();
        Locale locale;
        if (getLang.equalsIgnoreCase(lang_arr[1])) {
            locale = new Locale("hi");
            Locale.setDefault(locale);
            config.setLocale(locale);

            // setLocale("hi");
        } else if (getLang.equalsIgnoreCase(lang_arr[2])) {
            locale = new Locale("mr");
            Locale.setDefault(locale);
            config.setLocale(locale);

            //setLocale("mr");
        }
        getResources().getConfiguration().setTo(config);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    private void setAppToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.home_toolbar_title));
    }

    private void initialializeComponents() {
        gson = new Gson();

        memberTable = new MemberTable(mContext);
        categoryMasterTable = new CategoryMasterTable(mContext);
        subCategoryTable = new SubCategoryTable(mContext);
        newsImagesTable = new NewsImagesTable(mContext);
        newsFeedList = new ArrayList<>();
        categoryList = new ArrayList<>();
        catDupList = new ArrayList<>();
        newsFeedApplication = NewsFeedApplication.getApp();
        session = new SessionManager(mContext);
        langSelection = new SessionManager(mContext);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        txt_header_username = (TextView) header.findViewById(R.id.txt_header_username);
        txt_header_email = (TextView) header.findViewById(R.id.txt_header_email);

        txt_header_username.setText(fullName);
        txt_header_email.setText(emailId);

        //   navigationView.addHeaderView(header);
        enableExpandableList();
        // navigationView.setNavigationItemSelectedListener(this);


    }

    private void getPrefData() {
        myPreferences = MyPreferences.getMyAppPref(mContext);
        memberId = myPreferences.getMemberId();
        role = myPreferences.getRole();
        memberToken = myPreferences.getMemberToken().trim();
        emailId = myPreferences.getEmailId().trim();
        fullName = myPreferences.getFirstName().trim() + " " + myPreferences.getLastName().trim();
        String childName = myPreferences.getExpandChildName();
        if (childName != null) {
            myPreferences.setExpandChildName("null");
        }
    }

    private void enableExpandableList() {

        newsFeedList = newsListTable.getAllNewsRecords();
        if (newsFeedList != null) {
            for (int k = 0; k < newsFeedList.size(); k++) {
                catDupList.add(newsFeedList.get(k).getCategory());
              /*  categoryIDList.add(Integer.valueOf(newsFeedList.get(k).getCategory_id()));
                if (*//*newsFeedList.get(k).getSub_category()!=null || *//*newsFeedList.get(k).getSub_category_id() != null) {
                    subCatDupList.add(newsFeedList.get(k).getSub_category());

                }
                if (newsFeedList.get(k).getSub_category() == null) {
                    subCategoryIDList.add(0, 0);
                } else {
                    subCategoryIDList.add(Integer.valueOf(newsFeedList.get(k).getSub_category_id()));
                }*/
            }
        }
        categoryList.addAll(new HashSet<>(catDupList));

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        listThirdLevelChild = new HashMap<>();
        data = new ArrayList<>();
        HomeActivity activity = new HomeActivity();
        expListView = (ExpandableListView) findViewById(R.id.left_drawer);
        expListView.setIndicatorBounds(expListView.getRight() - 40, expListView.getWidth());
        prepareListData(listDataHeader, listDataChild, listThirdLevelChild);
      //  Log.v("enableExpandableList ", "" + listThirdLevelChild);
        //   ThreeLevelListAdapter threeLevelListAdapterAdapter = new ThreeLevelListAdapter(this, listDataHeader, listDataChild, data);
        // set adapter
        // expListView.setAdapter( threeLevelListAdapterAdapter );

        listAdapter = new ExpandListAdapter(mContext, listDataHeader, listDataChild, listThirdLevelChild, this);
        expListView.setAdapter(listAdapter);

        setGroupIndicatorToRight();

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                //Toast.makeText(mContext, "Group Clicked " + listDataHeader.get(groupPosition), Toast.LENGTH_SHORT).show();
                String group = listDataHeader.get(groupPosition);
                //Log.v(""," group "+group);
                if (group.equals("Home")) {
                    fragment = new HomeFragment();
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (group.equals("Profile")) {
                    fragment = new ProfileFragment();
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                }else if(group.equals("About Us")){
                    fragment = new AboutUsFragment();
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                }
                //replacing the fragment
                if (fragment != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                return false;
            }
        });
        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
               /* Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();*/
                // String group = listDataHeader.get(groupPosition);
                //Log.v(""," group "+group);
                /*if(group.equals("Home")){

                }*/
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
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
                String child_name = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                Log.v("onChildClick ", "child_name " + child_name);


                if (child_name.equals("Pending Request")) {
                    callRequestStatusHomeFragment();
                } /*else if (child_name.equals("Request Status")) {

                }*/ else {
                    setFragment(child_name, childPosition);

                }

                // till here
               /* Toast.makeText(
                        mContext,
                        listDataHeader.get(groupPosition) + " : " + listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT)
                        .show();*/
               /* if (fragment != null) {
                    openFragment(fragment);
                }*/
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    public void openFragment(Fragment fragment) {
        //HomeActivity activity=new HomeActivity();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack(null);
        ft.commit();

    }

    private void prepareListData(List<String> listDataHeader, HashMap<String, List<String>> listDataChild, HashMap<String, List<String>> listThirdLevelChild) {
        listDataHeader.add("Home");
        listDataHeader.add("Profile");
        listDataHeader.add("Requests");
        listDataHeader.add("News Categories");
        listDataHeader.add("About Us");

        List<String> requestsChild = new ArrayList<String>();
        requestsChild.add("Pending Request");
       // requestsChild.add("Request Status");


        List<String> newsChild = new ArrayList<String>();
       // newsChild.add("Latest News");
        newsChild.add("National and International");
        newsChild.add("Government News");
        newsChild.add("Social and Related News");
        newsChild.add("Sports");
        newsChild.add("Science and Technology");
        newsChild.add("Health Related");
        newsChild.add("Business News");
        newsChild.add("Agricultural News");
        newsChild.add("Cinema Related");
        newsChild.add("Small Classified");
        newsChild.add("Other and Uncategorized News");
        newsChild.add("Entertainment");
        newsChild.add("Career Related");
        newsChild.add("Economical News");

        List<String> newsCareerChild = new ArrayList<String>();
        newsCareerChild.add("Job");
        newsCareerChild.add("Business");
        newsCareerChild.add("Educational");

        List<String> newsSmallChild = new ArrayList<String>();
        newsSmallChild.add("Property");
        newsSmallChild.add("Birthday ads");
        newsSmallChild.add("App Related Ads");
        newsSmallChild.add("Buy and Sell");
        newsSmallChild.add("Services");
        newsSmallChild.add("Loan related");
        newsSmallChild.add("Matrimony related");
        newsSmallChild.add("Books and Literature");


        listDataChild.put(listDataHeader.get(2), requestsChild); // Header, Child data
        listDataChild.put(listDataHeader.get(3), newsChild);
        listThirdLevelChild.put(newsChild.get(9), newsSmallChild);
        listThirdLevelChild.put(newsChild.get(12), newsCareerChild);

        data.add(listThirdLevelChild);
    }

    private void setGroupIndicatorToRight() {
    /* Get the screen width */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;

        expListView.setIndicatorBounds(width - getDipsFromPixel(35), width
                - getDipsFromPixel(5));
    }

    // Convert pixel to dip
    public int getDipsFromPixel(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }




    /*private void addNewsFeedItems() {
        mNewsFeedList = new ArrayList<>();
        mNewsFeedList.add(new NewsFeedDetails("'We are here to stay', say Indian-Americans amid growing hate crime incidents in the US", "10 mins ago",
                "1-01-2018", getResources().getDrawable(R.drawable.lebanon), "Mumbai", "Maharashtra", "John Williams"));
        mNewsFeedList.add(new NewsFeedDetails("Development of all, appeasement of none: Yogi's mantra for UP", "1 day ago",
                "12-07-2017", getResources().getDrawable(R.drawable.yogi_adinath), "Mumbai", "Maharashtra", "Richie K"));
        mNewsFeedList.add(new NewsFeedDetails("India to seal border with Pakistan by 2018: Rajnath Singh", "20 mins ago",
                "11-04-2017", getResources().getDrawable(R.drawable.india_pak_border), "Mumbai", "Maharashtra", "G K"));

    }*/


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
                startActivity(new Intent(this, HomeActivity.class));
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
          //  closeApplication();
            // super.onBackPressed();
            if (back_press_screen == 1) {
                callHomeFragment();
            } else if (back_press_screen == 2) {
               /* FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                NewFeaturesFragment fragmentB = NewFeaturesFragment.newInstance("Services");
                tx.replace(R.id.content_frame, fragmentB);
                //  tx.addToBackStack(null);
                tx.commit();
            } else {*/
                //  this.finish();
                closeApplication();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    private void closeApplication() {
        new MaterialStyledDialog.Builder(HomeActivity.this)
                .setTitle("Confirm Please...")
                .setDescription("Do you want to close the app ?")
                .setStyle(Style.HEADER_WITH_TITLE)
                .setPositiveText(R.string.button_ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //finishAffinity();
                        // finish();

                        //for lower api version than 16
                        ActivityCompat.finishAffinity(HomeActivity.this);
                    }
                })
                .setNegativeText(R.string.cancel)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();


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
        } else if (id == R.id.action_change_language) {
            Intent intent = new Intent(this, LanguageSelection.class);
            intent.putExtra("from", "home");
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
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

    @Override
    public void setFragment(String childName, int childPosition) {
        Log.v("setFragment ","childName "+childName);
        myPreferences = MyPreferences.getMyAppPref(mContext);
        myPreferences.setExpandPosition(childPosition);
        myPreferences.setExpandChildName(childName);
        HomeFragment fragment = new HomeFragment();
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }
}
