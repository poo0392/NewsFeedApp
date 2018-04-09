package job.com.news.service;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import job.com.news.Constant;
import job.com.news.HomeActivity;
import job.com.news.R;
import job.com.news.db.MemberTable;
import job.com.news.db.NewsImagesTable;
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
 * Created by Pooja.Patil on 28/03/2018.
 */

public class ConnectivityChangeReciever extends BroadcastReceiver {

    static String TAG = "ConnectivityChangeReceiver";
    MyPreferences myPreferences;
    int memberId;
    String memberToken;
    NewsListTable newsListTable;
    private MemberTable memberTable;
    private NewsImagesTable newsImagesTable;
    String news_status = "";
    List<NewsFeedList> newsFeedList;

    @Override
    public void onReceive(Context context, Intent intent) {
        newsListTable = new NewsListTable(context);
        newsImagesTable = new NewsImagesTable(context);
        memberTable = new MemberTable(context);
        newsFeedList=new ArrayList<>();
        long last_id = newsListTable.getLastId();
        HomeActivity activity =new HomeActivity();
        if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
            if (isNetworkAvail(context)) {
                getPrefData(context);

               callNewsListAPI(context, memberToken, memberId, last_id, news_status);
               // activity.callNewsListAPI(memberToken,memberId);
               // disableReceiver(context);
            }
        }
    }

    private void callNewsListAPI(final Context context, String memberToken, int memberId, long last_id, String news_status) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WebService webService = retrofit.create(WebService.class);
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

        serverResponse.enqueue(new Callback<NewsFeedModelResponse>() {
            @Override
            public void onResponse(Call<NewsFeedModelResponse> call, Response<NewsFeedModelResponse> response) {
                if (response.isSuccessful()) {

                    NewsFeedModelResponse serverResponse = response.body();
                    //    newsList=serverResponse.toString();
                    if (serverResponse.getStatus() == 0) {
                        Log.v("BackSercallNewsListAPI ", "response " + new Gson().toJson(response.body()));
                        try {
                            newsFeedList = serverResponse.getNewsFeedList();
                            Log.v("", "newsFeedList " + newsFeedList.toString());

                            //   loadDatatoList(newsFeedList);


                            NewsFeedList model;
                            try {
                                RegisterMember member = new RegisterMember();
                                for (int i = 0; i < serverResponse.getNewsFeedList().size(); i++) {
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
                                        newsListTable.insertNewsList(model);

                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            //changes 06_03

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<NewsFeedModelResponse> call, Throwable t) {
                t.printStackTrace();

                if (t instanceof NoConnectivityException) {
                    // No internet connection
                    // Toast.makeText(mContext, "No Internet", Toast.LENGTH_SHORT).show();
                    setFailedAlertDialog(context, "Failed", "No Internet! Please Check Your internet connection");
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
    private void getPrefData(Context context) {
        myPreferences = MyPreferences.getMyAppPref(context);
        memberId = myPreferences.getMemberId();
        memberToken = myPreferences.getMemberToken().trim();
    }

    public boolean isNetworkAvail(Context mContext) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null)
                    for (int i = 0; i < info.length; i++)
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void enableReceiver(Context context)
    {

        Log.v(TAG, "In enableReceiver");
        ComponentName component = new ComponentName(context, ConnectivityChangeReciever.class);

        context.getPackageManager().setComponentEnabledSetting(component,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    /**
     * Disables ConnectivityReceiver
     *
     * @param context
     */
    public static void disableReceiver(Context context)
    {
        Log.v(TAG, "In disableReceiver");
        ComponentName component = new ComponentName(context, ConnectivityChangeReciever.class);

        context.getPackageManager().setComponentEnabledSetting(component,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }
}
