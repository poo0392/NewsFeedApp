package job.com.news;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import job.com.news.adapter.ImageAdapter;
import job.com.news.helper.ConnectivityInterceptor;
import job.com.news.helper.NoConnectivityException;
import job.com.news.interfaces.ItemClickListener;
import job.com.news.interfaces.WebService;
import job.com.news.interfaces.onButtonClick;
import job.com.news.models.NewsFeedList;
import job.com.news.models.NewsFeedModelResponse;
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
 * Created by Zafar.Hussain on 19/03/2018.
 */

public class AdminRequestsListFragment extends Fragment {
    Context mContext;
    public RecyclerView mRecyclerView;
    private ImageAdapter adapter;
    private NewsFeedApplication newsFeedApplication;
    LinearLayoutManager layoutManager;
    private MyPreferences myPreferences;
    String emailId, fullName, memberToken, news_status = "";
    int memberId;
    String role;
    List<NewsFeedList> newsFeedList;
    ProgressDialog mProgressDialog;
    ItemClickListener clickListener;
    int pos,newsId;
    SendMessage SM;

    public static Fragment newInstance(int position) {
        AdminRequestsListFragment fragment = new AdminRequestsListFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_feed, container, false);

        mContext = getActivity();
    //    SM = (SendMessage) mContext;
        attachViews(view);
        getPrefData();
        getBundleData();

        return view;
    }

    private void attachViews(View view) {

        newsFeedList = new ArrayList<>();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.news_feed_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void getPrefData() {
        myPreferences = MyPreferences.getMyAppPref(mContext);
        memberId = myPreferences.getMemberId();
        role = myPreferences.getRole();
        memberToken = myPreferences.getMemberToken().trim();
        emailId = myPreferences.getEmailId().trim();
        fullName = myPreferences.getFirstName().trim() + " " + myPreferences.getLastName().trim();
    }

    private void getBundleData() {
        int position = getArguments().getInt("position");
        Log.v("AdminReqLstFragment ", "position " + position);
        Toast.makeText(getActivity(),"Position is: "+position,Toast.LENGTH_SHORT).show();
        if (position == 0) {
            news_status = "pending";
            callNewsListAPI(memberToken, memberId, news_status);
        } else if (position == 1) {
            news_status = "approved";
            callNewsListAPI(memberToken, memberId, news_status);
        } else {
            news_status = "rejected";
            callNewsListAPI(memberToken, memberId, news_status);
        }

    }

    private void callNewsListAPI(String memberToken, int memberId, String news_status) {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(600000, TimeUnit.MILLISECONDS)
                .readTimeout(600000, TimeUnit.MILLISECONDS)
                .addInterceptor(new ConnectivityInterceptor(mContext))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WebService webService = retrofit.create(WebService.class);
        //long id= newsListTable.getLastId();
        RequestBody paramMemberToken = RequestBody.create(MediaType.parse("text/plain"), memberToken);
        RequestBody paramMemberId = RequestBody.create(MediaType.parse("text/plain"), "" + memberId);
        RequestBody status = RequestBody.create(MediaType.parse("text/plain"), news_status);

        Log.v("", " memberToken " + memberToken);
        Log.v("", " memberId " + memberId);
        Log.v("", " last_id " + 0);
        Log.v("", " news_status " + this.news_status);

        Call<NewsFeedModelResponse> serverResponse = webService.getNewsListRequest(paramMemberToken, paramMemberId, status, 0);
        //  Call<NewsFeedModelResponse> serverResponse = webService.getNewsListRequest(paramMemberToken, paramMemberId, 0);

        serverResponse.enqueue(new Callback<NewsFeedModelResponse>() {
            @Override
            public void onResponse(Call<NewsFeedModelResponse> call, Response<NewsFeedModelResponse> response) {
                mProgressDialog.dismiss();
                String newsList = "";
                if (response.isSuccessful()) {
                    NewsFeedModelResponse serverResponse = response.body();
                    String jsonResponse = new Gson().toJson(response.body());
                    Log.v("callNewsListAPI ", "response " + jsonResponse);
                    if (serverResponse.getStatus() == 0) {

                        try {

                            try {
                                newsFeedList = serverResponse.getNewsFeedList();
                                loadDatatoList();

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

    private void loadDatatoList() {
        adapter = new ImageAdapter(getActivity(), newsFeedList, mRecyclerView, "admin_news_list" + ":" + news_status);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        adapter.setOnButtonClick(new onButtonClick() {
            @Override
            public void onItemClicked(int position, View view) {
                pos=position;
                Log.v(" ","position 11 "+pos);

                LinearLayout ll_approve_news = (LinearLayout) view.findViewById(R.id.ll_aprove);
                LinearLayout ll_decline_news = (LinearLayout) view.findViewById(R.id.ll_decline);

                ll_decline_news.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.v("ll_decline_news ","position 22 "+pos);
                        newsId = newsFeedList.get(pos).getId();
                        Log.v("ll_decline_news ","newsId "+newsId);

                        new MaterialStyledDialog.Builder(mContext)
                                .setDescription("Do you want to reject this news?")
                                .setStyle(Style.HEADER_WITH_ICON)
                                .setIcon(R.mipmap.ic_failed)
                                .setPositiveText(R.string.button_ok)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        news_status="rejected";
                                        callUpdateNewsStatus(memberToken,memberId,newsId,news_status);
                                    }
                                })
                                .setNegative("Cancel", new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }
                });

                ll_approve_news.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.v("ll_approve_news ","position 22 "+pos);
                        newsId = newsFeedList.get(pos).getId();
                        Log.v("ll_approve_news ","newsId "+newsId);

                        new MaterialStyledDialog.Builder(mContext)
                                .setDescription("Do you want to approve this news?")
                                .setStyle(Style.HEADER_WITH_ICON)
                                .setIcon(R.mipmap.ic_dialog_approve)
                                .setPositiveText(R.string.button_ok)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        news_status="approved";
                                     //   callNewsListAPI(memberToken, memberId, news_status);
                                       callUpdateNewsStatus(memberToken,memberId,newsId,news_status);
                                        Log.v("ll_approve_news ","position 33 "+pos);

                                    }
                                })
                                .setNegative("Cancel", new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();
                                    }
                                })

                                .show();
                    }
                });
            }
        });
    }

    private void callUpdateNewsStatus(final String memberToken, final int memberId, int newsId, final String news_status) {
        Log.v("callUpdateNewsStatus","news_status "+news_status);
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
        RequestBody paramNewsId = RequestBody.create(MediaType.parse("text/plain"), "" + newsId);
        RequestBody status = RequestBody.create(MediaType.parse("text/plain"), news_status);

        Call<NewsFeedModelResponse> serverResponse = webService.addAdminNewsStatus(paramMemberToken, paramMemberId,paramNewsId, status);

        serverResponse.enqueue(new Callback<NewsFeedModelResponse>() {
            @Override
            public void onResponse(Call<NewsFeedModelResponse> call, Response<NewsFeedModelResponse> response) {
                mProgressDialog.dismiss();
                String newsList = "";
                if (response.isSuccessful()) {
                    NewsFeedModelResponse serverResponse = response.body();

                    if (serverResponse.getStatus() == 0) {

                        try {
                            Toast.makeText(mContext,"Success",Toast.LENGTH_SHORT).show();
                           // callNewsListAPI(memberToken,memberId,news_status);
                           /* AdminRequestsListFragment frg = new AdminRequestsListFragment();
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            Bundle b = new Bundle();
                            b.putInt("position", 0);
                            frg.sendData();
                            ft.detach(frg);
                           // ft.add(0,frg);
                           ft.attach(frg);
                            ft.commit();
                            adapter.notifyDataSetChanged();*/
                            AdminHomeFragment af=new  AdminHomeFragment();
                            FragmentTransaction tx = getFragmentManager().beginTransaction();

                            tx.replace(R.id.content_frame, af);
                            af.sendData(news_status);
                            // tx.addToBackStack(null);
                            tx.commit();

                           // SM.sendData(news_status);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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


   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            SM = (SendMessage) mContext;
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }*/

    interface SendMessage {
        void sendData(String message);
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
