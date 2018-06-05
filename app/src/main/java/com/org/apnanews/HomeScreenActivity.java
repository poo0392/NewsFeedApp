package com.org.apnanews;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.org.apnanews.changepassword.ChangePassword;
import com.org.apnanews.db.MemberTable;
import com.org.apnanews.db.NewsImagesTable;
import com.org.apnanews.db.NewsListTable;
import com.org.apnanews.helper.ConnectivityInterceptor;
import com.org.apnanews.helper.NoConnectivityException;
import com.org.apnanews.interfaces.WebService;
import com.org.apnanews.models.NewsFeedList;
import com.org.apnanews.models.NewsFeedModelResponse;
import com.org.apnanews.models.NewsImages;
import com.org.apnanews.register.RegisterMember;
import com.org.apnanews.sharedpref.MyPreferences;
import com.org.apnanews.sharedpref.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

public class HomeScreenActivity extends AppCompatActivity {
    static final int PERMISSION_REQUEST_CODE = 200;

    Toolbar toolbar;
    TextView txt_header_username, txt_header_email;

    String emailId, fullName, memberToken, role;
    int memberId;
    Context mContext;
    MyPreferences myPreferences;
    ProgressDialog mProgressDialog;
    NewsListTable newsListTable;
    MemberTable memberTable;
    NewsImagesTable newsImagesTable;

    List<NewsFeedList> newsFeedList;
    Call<NewsFeedModelResponse> serverResponse;
    SessionManager session, langSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initialize();
        if (!checkPermission()) {
            requestPermission();

        }

        getPrefData();
        setLocaleLang();
        attachViews();
        callNewsListAPI(memberToken, memberId, "");
    }

    private void attachViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.home_toolbar_title));

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (serverResponse.isExecuted()) {
            callHomeFragment();
        }
       /* if (backTask.getStatus() == Status.FINISHED) {
            doAnimation();
        }*/
    }
    private void initialize() {
        mContext = this;
        session = new SessionManager(mContext);
        newsListTable = new NewsListTable(mContext);
        memberTable = new MemberTable(mContext);
        newsImagesTable = new NewsImagesTable(mContext);
        langSelection = new SessionManager(mContext);
        newsFeedList=new ArrayList<>();
    }

    private void getPrefData() {
        myPreferences = MyPreferences.getMyAppPref(mContext);
        memberId = myPreferences.getMemberId();
        role = myPreferences.getRole();
        memberToken = myPreferences.getMemberToken().trim();
        emailId = myPreferences.getEmailId().trim();
        fullName = myPreferences.getFirstName().trim() + " " + myPreferences.getLastName().trim();
        String childName = myPreferences.getExpandChildName();
        if (childName == null || childName != null) {
            myPreferences.setExpandChildName("null");
        }
    }
    public void callHomeFragment() {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.content_frame, new HomeFragment());
        // tx.addToBackStack(null);
        tx.commitAllowingStateLoss();
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
    private boolean checkPermission() {

        int readStoragePermissionResult = ContextCompat.checkSelfPermission(mContext, READ_EXTERNAL_STORAGE);
        int writeStoragePermissionResult = ContextCompat.checkSelfPermission(mContext, WRITE_EXTERNAL_STORAGE);

        return readStoragePermissionResult == PackageManager.PERMISSION_GRANTED &&
                writeStoragePermissionResult == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(HomeScreenActivity.this, new String[]{
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
    public void callNewsListAPI(String memberToken, int memberId, final String from) {
        if (!from.equals("background")) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.show();
        }
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(getApplicationContext()))
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

         serverResponse = webService.getNewsListRequest(paramMemberToken, paramMemberId, status, last_id, paramAllNews);
        //   Call<NewsFeedModelResponse> serverResponse = webService.getNewsListRequest(paramMemberToken, paramMemberId,id);

        serverResponse.enqueue(new Callback<NewsFeedModelResponse>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<NewsFeedModelResponse> call, Response<NewsFeedModelResponse> response) {

                if (response.isSuccessful()) {
                    NewsFeedModelResponse serverResponse = response.body();
                    String jsonResponse = new Gson().toJson(response.body());
                    Log.v("" + " callNewsListAPI ", "response " + jsonResponse);
                    String code = serverResponse.getCode();
                    String desc = serverResponse.getDescription();
                    if (serverResponse.getStatus() == 0) {
                        if (serverResponse.getNewsFeedList() == null) {
                            //   Log.v("callNewsListAPI ", "newsFeedList " + "null");
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
                                    int n = serverResponse.getNewsFeedList().size();
                                    if (n > 0) {
                                        for (int i = n - 1; i < n; i--) {
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
                                                    int p = serverResponse.getNewsFeedList().get(i).getNews_images().size();
                                                    for (int j = p - 1; j < p; j++) {
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
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (!from.equals("background")) {
                                if (mProgressDialog.isShowing()) {
                                    mProgressDialog.dismiss();
                                }
                            }

                        }
                    } else {
                        //   Log.v("Failure ", "status " + serverResponse.getStatus() + " Desc " + serverResponse.getDescription());
                        //setFailedAlertDialog(HomeActivity.this, serverResponse.getStatus().toString(), "Failure");
                        Toast.makeText(mContext, "Error in Response ", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<NewsFeedModelResponse> call, Throwable t) {
                if (!from.equals("background")) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                }
                t.printStackTrace();

                if (t instanceof NoConnectivityException) {
                    // No internet connection
                     Toast.makeText(mContext, "No Internet! Please Check Your internet connection", Toast.LENGTH_SHORT).show();
                   // setFailedAlertDialog(mContext, "Failed", "No Internet! Please Check Your internet connection");
                }
            }
        });
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(mContext)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
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
            SharedPreferences sharedpreferences = getSharedPreferences("NewsFeed", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(this, LoginActivity.class);
            // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mContext.startActivity(intent);
            finish();
        } else if (id == R.id.action_change_language) {
            Intent intent = new Intent(this, LanguageSelection.class);
            intent.putExtra("from", "home");
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
