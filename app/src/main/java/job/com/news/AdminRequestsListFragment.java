package job.com.news;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
    String emailId, fullName, memberToken, news_status = "", comment;
    int memberId;
    String role;
    List<NewsFeedList> newsFeedList;
    ProgressDialog mProgressDialog;
    ItemClickListener clickListener;
    int pos, newsId;
    SendMessage SM;
    boolean wrapInScrollView = true;
    LinearLayout ll_news_feed;


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
        setHasOptionsMenu(true);
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
        ll_news_feed=(LinearLayout)view.findViewById(R.id.ll_news_feed);
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
        Toast.makeText(getActivity(), "Position is: " + position, Toast.LENGTH_SHORT).show();
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
                                loadDatatoList(newsFeedList,"response");

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

    private void loadDatatoList(List<NewsFeedList> newsList,String from) {
        Log.v("loadDatatoList "," from "+from);
        if (!role.equals("1")) {
            adapter = new ImageAdapter(getActivity(), newsList, mRecyclerView, "user" + ":" + news_status, 0);
            mRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            adapter = new ImageAdapter(getActivity(), newsList, mRecyclerView, "admin" + ":" + news_status, 0);
            mRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();


            adapter.setOnButtonClick(new onButtonClick() {
                @Override
                public void onItemClicked(int position, View view) {
                    pos = position;
                    Log.v(" ", "position 11 " + pos);

                    LinearLayout ll_approve_news = (LinearLayout) view.findViewById(R.id.ll_aprove);
                    LinearLayout ll_decline_news = (LinearLayout) view.findViewById(R.id.ll_decline);

                    ll_decline_news.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.v("ll_decline_news ", "position 22 " + pos);
                            newsId = AdminRequestsListFragment.this.newsFeedList.get(pos).getId();
                            Log.v("ll_decline_news ", "newsId " + newsId);

                            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View customView = inflater.inflate(R.layout.custom_reject_view, null);
                            TextView txt_news_title = (TextView) customView.findViewById(R.id.txt_news_title);
                            final EditText edt_comment = (EditText) customView.findViewById(R.id.edt_comment);

                            txt_news_title.setText(AdminRequestsListFragment.this.newsFeedList.get(pos).getNews_title());
                            comment = edt_comment.getText().toString();

                            new MaterialStyledDialog.Builder(mContext)
                                    //.setDescription()
                                    .setStyle(Style.HEADER_WITH_ICON)
                                    .setCustomView(customView)
                                    .setIcon(R.mipmap.ic_failed)
                                    .setPositiveText(R.string.button_ok)
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            news_status = "rejected";

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
                        @Override
                        public void onClick(View v) {
                            Log.v("ll_approve_news ", "position 22 " + pos);
                            newsId = AdminRequestsListFragment.this.newsFeedList.get(pos).getId();
                            Log.v("ll_approve_news ", "newsId " + newsId);

                            new MaterialStyledDialog.Builder(mContext)
                                    .setDescription("Do you want to approve this news?")
                                    .setStyle(Style.HEADER_WITH_ICON)
                                    .setIcon(R.mipmap.ic_dialog_approve)
                                    .setPositiveText(R.string.button_ok)
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            news_status = "approved";
                                            //   callNewsListAPI(memberToken, memberId, news_status);
                                            callUpdateNewsStatus(memberToken, memberId, newsId, news_status, "");
                                            Log.v("ll_approve_news ", "position 33 " + pos);

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

    private void callUpdateNewsStatus(final String memberToken, final int memberId, int newsId, final String news_status, String comment) {
        Log.v("callUpdateNewsStatus", "news_status " + news_status);
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
                            Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
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
                Log.v("onQueryTextChange ", "query " + query);


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
                    Snackbar.make(ll_news_feed, "Record Not Found..", Snackbar.LENGTH_LONG).setAction("Action", null).show();
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
        loadDatatoList(filteredList,"filter");
        return filteredList;
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
