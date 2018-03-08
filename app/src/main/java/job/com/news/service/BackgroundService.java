package job.com.news.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import job.com.news.Constant;
import job.com.news.adapter.ImageAdapter;
import job.com.news.db.MemberTable;
import job.com.news.db.NewsListTable;
import job.com.news.helper.ConnectivityInterceptor;
import job.com.news.helper.NoConnectivityException;
import job.com.news.interfaces.WebService;
import job.com.news.models.NewsFeedList;
import job.com.news.models.NewsFeedModelResponse;
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
 * Created by Zafar.Hussain on 13/02/2018.
 */

public class BackgroundService extends Service {

    private boolean isRunning;
    private Context context;
    private Thread backgroundThread;
    private MyPreferences myPreferences;
    private String emailId, fullName, memberToken;
    private int memberId;
    private List<NewsFeedList> newsFeedList = new ArrayList<>();
    private Gson gson;
    private List<NewsFeedList> newsFeedListTable = new ArrayList<>();
    private MemberTable memberTable;
    private NewsListTable newsListTable;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        this.context = this;
        this.isRunning = false;
        getPrefData();
        this.backgroundThread = new Thread(myTask);
        newsListTable=new NewsListTable(this);
        Log.v("BackgroundService ","Background Service Called");

    }
    private void getPrefData() {
        myPreferences = MyPreferences.getMyAppPref(this);
        memberId = myPreferences.getMemberId();
        memberToken = myPreferences.getMemberToken().trim();
        emailId = myPreferences.getEmailId().trim();
        fullName = myPreferences.getFirstName().trim() + " " + myPreferences.getLastName().trim();
    }
    private Runnable myTask = new Runnable() {
        public void run() {
            // Do something here
            callNewsListAPI(memberToken, memberId);
           // stopSelf();
        }
    };

    @Override
    public void onDestroy() {
        this.isRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!this.isRunning) {
            this.isRunning = true;
            this.backgroundThread.start();
        }
        return START_STICKY;
    }
    private void callNewsListAPI(String memberToken, int memberId) {
      //  mProgressDialog = new ProgressDialog(HomeActivity.this);
      //  mProgressDialog.setMessage("Loading...");
      //  mProgressDialog.show();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(getApplicationContext()))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WebService webService = retrofit.create(WebService.class);

        RequestBody paramMemberToken = RequestBody.create(MediaType.parse("text/plain"), memberToken);
        RequestBody paramMemberId = RequestBody.create(MediaType.parse("text/plain"), "" + memberId);

        Call<NewsFeedModelResponse> serverResponse = webService.getNewsListRequest(paramMemberToken, paramMemberId);
        serverResponse.enqueue(new Callback<NewsFeedModelResponse>() {
            @Override
            public void onResponse(Call<NewsFeedModelResponse> call, Response<NewsFeedModelResponse> response) {
               // mProgressDialog.dismiss();
                String newsList = "";
                if (response.isSuccessful()) {

                  /*  Type collectionType = new TypeToken<List<NewsFeedModelResponse>>() {
                    }.getType();
                    List<NewsFeedModelResponse> lcs = (List<NewsFeedModelResponse>) new Gson()
                            .fromJson(String.valueOf(response.body()), collectionType);*/

                    NewsFeedModelResponse serverResponse = response.body();
                    //    newsList=serverResponse.toString();
                    if (serverResponse.getStatus() == 0) {
                        Log.v("callNewsListAPI ", "response " + new Gson().toJson(response.body()));
                        //   Log.v("", "Response " + serverResponse.getNewsFeedList().toString());
                      /*  Log.v("", "News Category " + serverResponse.getNewsFeedList().getCategory());
                        Log.v("", "News Desc " + serverResponse.getNewsFeedList().getNews_description());*/

//                        serverResponse = gson.fromJson(newsList, NewsFeedModelResponse.class);
                        //   newsFeedList = serverResponse.getNewsFeedList();
                        //  gson.fromJson(serverResponse);
                        //  Log.v("callNewsListAPI ", "response " + newsFeedList.toString());
                        try {
                            newsFeedList = serverResponse.getNewsFeedList();
                            Log.v("", "newsFeedList " + newsFeedList.toString());

                            //   loadDatatoList(newsFeedList);


                            NewsFeedList model = new NewsFeedList();
                            try {
                                RegisterMember member = new RegisterMember();
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
                                     //   model.setNews_pic(serverResponse.getNewsFeedList().get(i).getNews_pic());
                                        model.setLike_count(serverResponse.getNewsFeedList().get(i).getLike_count());
                                        model.setMember_id(serverResponse.getNewsFeedList().get(i).getMember_id());
                                        model.setCreated_at(serverResponse.getNewsFeedList().get(i).getCreated_at());
                                        model.setMember(serverResponse.getNewsFeedList().get(i).getMember());

                                        if (!memberTable.checkUser(serverResponse.getNewsFeedList().get(i).getMember().getId())) {
                                            member.setMemberId(model.getMember().getId());
                                            //   member.setMemberToken(model.getMember().getMemberToken().trim());
                                            member.setFirstName(model.getMember().getFirstName().trim());
                                            member.setLastName(model.getMember().getLastName().trim());
                                            member.setEmailId(model.getMember().getEmailId().trim());
                                            member.setMobile(model.getMember().getMobile());

                                            memberTable.insertMembers(member);

                                        }
                                        newsListTable.insertNewsList(model);

                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            newsFeedListTable = newsListTable.getAllNewsRecords();
                            Log.v("db ","getNewsFeedList "+newsFeedList.toString());
                            ImageAdapter adapter = new ImageAdapter(context, newsFeedList);
                            /*NewsFeedFragment frag =new NewsFeedFragment();
                            frag.mRecyclerView.setAdapter(adapter);*/
                            //recyclerView.setAdapter(new RecyclerViewAdapter(newList));
                            adapter.notifyDataSetChanged();
                            //changes 06_03

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<NewsFeedModelResponse> call, Throwable t) {
               // mProgressDialog.dismiss();
                t.printStackTrace();

                if (t instanceof NoConnectivityException) {
                    // No internet connection
                    // Toast.makeText(mContext, "No Internet", Toast.LENGTH_SHORT).show();
                  //  setFailedAlertDialog(HomeActivity.this, "Failed", "No Internet! Please Check Your internet connection");
                }
            }
        });
    }
}