package job.com.news.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import job.com.news.Constant;
import job.com.news.db.MemberTable;
import job.com.news.db.NewsImagesTable;
import job.com.news.db.NewsListTable;
import job.com.news.helper.ConnectivityInterceptor;
import job.com.news.helper.NoConnectivityException;
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
    private NewsImagesTable newsImagesTable;

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
        newsListTable = new NewsListTable(this);
        newsImagesTable = new NewsImagesTable(this);
        Log.v("BackgroundService ", "Background Service Called");

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
        if (!this.isRunning) {
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

        //  Log.v("callNewsListAPI", " newsRequestList " + newsRequestList);

        Call<NewsFeedModelResponse> serverResponse = webService.getNewsListRequest(paramMemberToken, paramMemberId, status, last_id, paramAllNews);
        // Call<NewsFeedModelResponse> serverResponse = webService.getNewsListRequest(paramMemberToken, paramMemberId, id);
        serverResponse.enqueue(new Callback<NewsFeedModelResponse>() {
            @Override
            public void onResponse(Call<NewsFeedModelResponse> call, Response<NewsFeedModelResponse> response) {
                String newsList = "";
                if (response.isSuccessful()) {

                    NewsFeedModelResponse serverResponse = response.body();
                    if (serverResponse.getStatus() == 0) {
                        //    Log.v("BackSercallNewsListAPI ", "response " + new Gson().toJson(response.body()));

                        try {
                            newsFeedList = serverResponse.getNewsFeedList();
                            //   Log.v("", "newsFeedList " + newsFeedList.toString());

                            NewsFeedList model;
                            try {
                                RegisterMember member = new RegisterMember();
                                List<NewsImages> imagesList = new ArrayList<>();
                                NewsImages imagesModel = new NewsImages();
                                int n = serverResponse.getNewsFeedList().size();
                               // if (n != -1) {
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
                                      /*  model.setId(serverResponse.getNewsFeedList().get(i).getId());
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
                                        model.setMember(serverResponse.getNewsFeedList().get(i).getMember());*/

                                            //  for (int j = 0; j < serverResponse.getNewsFeedList().get(i).getMembersList().size(); j++) {
                                            if (!memberTable.checkUser(serverResponse.getNewsFeedList().get(i).getMember().getId())) {
                                                member.setMemberId(model.getMember().getId());
                                                //   member.setMemberToken(model.getMember().getMemberToken().trim());
                                                member.setFirstName(model.getMember().getFirstName().trim());
                                                member.setLastName(model.getMember().getLastName().trim());
                                                member.setEmailId(model.getMember().getEmailId().trim());
                                                member.setMobile(model.getMember().getMobile());

                                                memberTable.insertMembers(member);

                                            }
                                            // }
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

                               // }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


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