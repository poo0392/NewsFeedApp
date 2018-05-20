package com.org.apnanews;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.org.apnanews.adapter.ImageAdapter;
import com.org.apnanews.globals.Globals;
import com.org.apnanews.helper.ConnectivityInterceptor;
import com.org.apnanews.helper.NoConnectivityException;
import com.org.apnanews.interfaces.ItemClickListener;
import com.org.apnanews.interfaces.WebService;
import com.org.apnanews.interfaces.onButtonClick;
import com.org.apnanews.models.NewsFeedList;
import com.org.apnanews.models.NewsFeedModelResponse;
import com.org.apnanews.sharedpref.MyPreferences;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Pooja.Patil on 19/03/2018.
 */

public class RequestsListFragment extends Fragment {
    private static final String TAG = "RequestsListFragment";
    Context mContext;
    public RecyclerView mRecyclerView;
    private ImageAdapter adapter;
    private NewsFeedApplication newsFeedApplication;
    LinearLayoutManager layoutManager;
    private MyPreferences myPreferences;
    String emailId, fullName, memberToken, news_status = "", comment;
    int memberId;
    String role;
    List<NewsFeedList> newsFeedList;
    List<NewsFeedList> newsFeedListResp=new ArrayList<>();
    ProgressDialog mProgressDialog;
    ItemClickListener clickListener;
    int newsId;
    SendMessage SM;
    boolean wrapInScrollView = true;
    LinearLayout ll_news_feed;
    String all_news;


    public static Fragment newInstance(int position) {
        RequestsListFragment fragment = new RequestsListFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_feed, container, false);
        setHasOptionsMenu(true);
        mContext = getActivity();
        //    SM = (SendMessage) mContext;
        Globals.back_press_screen=1;
        attachViews(view);
        getPrefData();
        getBundleData();

        return view;
    }

    private void attachViews(View view) {

        newsFeedList = new ArrayList<>();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.news_feed_recycler_view);
        ll_news_feed = (LinearLayout) view.findViewById(R.id.ll_news_feed);
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
      //  //Log.v("AdminReqLstFragment ", "position " + position);
      //  Toast.makeText(getActivity(), "Position is: " + position, Toast.LENGTH_SHORT).show();
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

    @SuppressLint("LongLogTag")
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

        if(role.equals("1")){
            all_news = "1";
        }else{
            all_news = "0";
        }
        long last_id = 0;
        RequestBody paramMemberToken = RequestBody.create(MediaType.parse("text/plain"), memberToken);
        RequestBody paramMemberId = RequestBody.create(MediaType.parse("text/plain"), "" + memberId);
        RequestBody paramAllNews = RequestBody.create(MediaType.parse("text/plain"), all_news);
        RequestBody status = RequestBody.create(MediaType.parse("text/plain"), news_status);

        HashMap<String, String> newsRequestList = new HashMap<>();
        newsRequestList.put("member_token", memberToken);
        newsRequestList.put("member_id", String.valueOf(memberId));
        newsRequestList.put("last_id", String.valueOf(last_id));
        newsRequestList.put("all_news", all_news);
        newsRequestList.put("news_status", news_status);

        //Log.v(TAG + " callNewsListAPI", " newsRequestList " + newsRequestList);

        Call<NewsFeedModelResponse> serverResponse = webService.getNewsListRequest(paramMemberToken, paramMemberId, status, last_id, paramAllNews);
        //  Call<NewsFeedModelResponse> serverResponse = webService.getNewsListRequest(paramMemberToken, paramMemberId, 0);

        serverResponse.enqueue(new Callback<NewsFeedModelResponse>() {
            @Override
            public void onResponse(Call<NewsFeedModelResponse> call, Response<NewsFeedModelResponse> response) {
                mProgressDialog.dismiss();
                String newsList = "";
                if (response.isSuccessful()) {
                    NewsFeedModelResponse serverResponse = response.body();
                    String jsonResponse = new Gson().toJson(response.body());
                    //Log.v(TAG + "callNewsListAPI ", "response " + jsonResponse);
                    if (serverResponse.getStatus() == 0) {

                        try {

                            try {

                                newsFeedList =serverResponse.getNewsFeedList() ;

                              // Collections.sort(newsFeedList, Collections.reverseOrder());
                                Collections.reverse(newsFeedList);

                                if(newsFeedList!=null || !newsFeedList.isEmpty()) {
                                    loadDatatoList(newsFeedList, "response");
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    setFailedAlertDialog(mContext, "Failed", "Something went Wrong");
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

    @SuppressLint("LongLogTag")
    private void loadDatatoList(List<NewsFeedList> newsList, String from) {
        //Log.v(TAG + "loadDatatoList ", " from " + from);
        //Log.v(TAG + "loadDatatoList ", " role " + role);
        if (role.equals("0")) {
            adapter = new ImageAdapter(getActivity(), newsList, mRecyclerView, "user" + ":" + news_status, 0);
            mRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else if (role.equals("1")) {
            adapter = new ImageAdapter(getActivity(), newsList, mRecyclerView, "admin" + ":" + news_status, 0);
            mRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();


            adapter.setOnButtonClick(new onButtonClick() {
                @Override
                public void onItemClicked(final int position, View view) {


                    LinearLayout ll_approve_news = (LinearLayout) view.findViewById(R.id.ll_aprove);
                    LinearLayout ll_decline_news = (LinearLayout) view.findViewById(R.id.ll_decline);

                    ll_decline_news.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onClick(View v) {

                            newsId = RequestsListFragment.this.newsFeedList.get(position).getId();
                            //Log.v(TAG + " ll_decline_news ", "newsId " + newsId);

                            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View customView = inflater.inflate(R.layout.custom_reject_view, null);
                            TextView txt_news_title = (TextView) customView.findViewById(R.id.txt_news_title);
                            final EditText edt_comment = (EditText) customView.findViewById(R.id.edt_comment);

                            txt_news_title.setText(RequestsListFragment.this.newsFeedList.get(position).getNews_title());

                            new MaterialStyledDialog.Builder(mContext)
                                    //.setDescription()
                                    .setStyle(Style.HEADER_WITH_ICON)
                                    .setCustomView(customView)
                                    .setIcon(R.mipmap.ic_failed)
                                    .setCancelable(false)
                                    .setPositiveText(R.string.button_ok)
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            news_status = "rejected";
                                            comment = edt_comment.getText().toString();
                                            //Log.v("ll_decline_news", "comment " + comment);
                                            callUpdateNewsStatus(memberToken, memberId, newsId, news_status, comment);
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
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onClick(View v) {
                            newsId = RequestsListFragment.this.newsFeedList.get(position).getId();
                            //Log.v(TAG + "ll_approve_news ", "newsId " + newsId);

                            new MaterialStyledDialog.Builder(mContext)
                                    .setDescription("Do you want to approve this news?")
                                    .setStyle(Style.HEADER_WITH_ICON)
                                    .setCancelable(false)
                                    .setIcon(R.mipmap.ic_dialog_approve)
                                    .setPositiveText(R.string.button_ok)
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            news_status = "approved";
                                            //   callNewsListAPI(memberToken, memberId, news_status);
                                            callUpdateNewsStatus(memberToken, memberId, newsId, news_status, "");
                                            //Log.v("ll_approve_news ", "position 33 " + position);

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
    }

    @SuppressLint("LongLogTag")
    private void callUpdateNewsStatus(final String memberToken, final int memberId, int newsId, final String news_status, String comment) {
        //Log.v(TAG + "callUpdateNewsStatus", "news_status " + news_status);
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


        HashMap<String, String> updateNewsRequestList = new HashMap<>();
        updateNewsRequestList.put("member_token", memberToken);
        updateNewsRequestList.put("member_id", String.valueOf(memberId));
        updateNewsRequestList.put("news_id", String.valueOf(newsId));
        updateNewsRequestList.put("news_status", news_status);
        updateNewsRequestList.put("comment", comment);

        //Log.v("callUpdateNewsStatus", " updateNewsRequestList " + updateNewsRequestList);

        Call<NewsFeedModelResponse> serverResponse = webService.addAdminNewsStatus(paramMemberToken, paramMemberId, paramNewsId, status, comment);

        serverResponse.enqueue(new Callback<NewsFeedModelResponse>() {
            @Override
            public void onResponse(Call<NewsFeedModelResponse> call, Response<NewsFeedModelResponse> response) {
                mProgressDialog.dismiss();
                String newsList = "";
                if (response.isSuccessful()) {
                    NewsFeedModelResponse serverResponse = response.body();

                    if (serverResponse.getStatus() == 0) {

                        try {
                          //  Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
                            // callNewsListAPI(memberToken,memberId,news_status);
                           /* RequestsListFragment frg = new RequestsListFragment();
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            Bundle b = new Bundle();
                            b.putInt("position", 0);
                            frg.sendData();
                            ft.detach(frg);
                           // ft.add(0,frg);
                           ft.attach(frg);
                            ft.commit();
                            adapter.notifyDataSetChanged();*/
                            RequestStatusHomeFragment af = new RequestStatusHomeFragment();
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
                .setCancelable(false)
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        setSearchViewMenu(menu);
        menu.findItem(R.id.action_logout).setVisible(false);
        menu.findItem(R.id.action_change_pwd).setVisible(false);
        menu.findItem(R.id.action_create_article).setVisible(false);
        menu.findItem(R.id.action_change_language).setVisible(false);
    }

    public void setSearchViewMenu(final Menu menu) {
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("Search by state and city");
        SearchManager searchManager = (SearchManager) mContext.getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //Log.v("onQueryTextChange ", "query " + query);


                //if (!newsFeedList.isEmpty()) {
                List<NewsFeedList> filteredModelList = filter(newsFeedList, query);

                if (filteredModelList.size() > 0) {
                    adapter.setFilter(filteredModelList);
                    // hideKeyboard();
                    return true;
                } else {
                    // If not matching search filter data
                    hideKeyboard();
                    // Constants.snackbar(sp_av_layout, "Record Not Found..");
                  //  Snackbar.make(ll_news_feed, "Record Not Found..", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    noDataFoundPopUp();

                    return false;
                }

                //    }
                //  return true;
            }
        });
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean newViewFocus) {
                if (!newViewFocus) {
                    //Collapse the action item.
                    searchItem.collapseActionView();
                    //Clear the filter/search query.
                    menu.findItem(R.id.action_search).setVisible(true);
                    menu.findItem(R.id.action_create_article).setVisible(false);
                    menu.findItem(R.id.action_change_language).setVisible(false);
                    menu.findItem(R.id.action_change_pwd).setVisible(false);
                    menu.findItem(R.id.action_logout).setVisible(false);
                } else {
                    menu.findItem(R.id.action_logout).setVisible(false);
                    menu.findItem(R.id.action_change_pwd).setVisible(false);
                    menu.findItem(R.id.action_create_article).setVisible(false);
                    menu.findItem(R.id.action_change_language).setVisible(false);
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

    private List<NewsFeedList> filter(List<NewsFeedList> newsFeedList, String query) {
        query = query.toLowerCase();
        final List<NewsFeedList> filteredList = new ArrayList<>();
        for (NewsFeedList list : newsFeedList) {

            String cityFilter = list.getCity().toLowerCase();
            String stateFilter = list.getState().toLowerCase();
            if ((cityFilter.contains(query)) || (stateFilter.contains(query))) {
                filteredList.add(list);
            }

        }

        // NewsFeedFragment nf = new NewsFeedFragment();
        //nf.loadDataFilter(filteredList, categoryFilter);
        //  loadDataNew(filteredList);
        loadDatatoList(filteredList, "filter");
        return filteredList;
    }
    private void noDataFoundPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        // builder.setTitle("Confirm Please...");
        builder.setMessage("No Data Found !!");
        builder.setCancelable(true);
        builder.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();
                        dialog.cancel();

                    }
                });

        AlertDialog alert1 = builder.create();
        alert1.show();

    }
    public void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void setItemsVisibility(Menu menu, MenuItem exception, boolean visible) {
        for (int i = 0; i < menu.size(); ++i) {
            MenuItem item = menu.getItem(i);
            if (item != exception) item.setVisible(visible);
        }
    }
}
