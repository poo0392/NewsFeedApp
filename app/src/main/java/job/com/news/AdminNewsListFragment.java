package job.com.news;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;

import java.util.ArrayList;
import java.util.List;

import job.com.news.adapter.ImageAdapter;
import job.com.news.db.NewsListTable;
import job.com.news.models.NewsFeedList;
import job.com.news.sharedpref.MyPreferences;

/**
 * Created by POOJA on 3/9/2018.
 */

public class AdminNewsListFragment extends Fragment {
    LinearLayoutManager layoutManager;

    //For Recyclerview scroll
    private boolean userScrolled = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    RelativeLayout bottomLayout;
    Context mContext;
    private MyPreferences myPreferences;
    String emailId, fullName, memberToken;
    private List<NewsFeedList> newsFeedList = new ArrayList<>();
    int memberId;
    ProgressDialog mProgressDialog;
    NewsListTable newsListTable;
    public RecyclerView mRecyclerView;
    private ImageAdapter adapter;
    private NewsFeedApplication newsFeedApplication;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_news_feed, container, false);
        newsFeedApplication = NewsFeedApplication.getApp();
        mContext = getActivity();
        newsListTable=new NewsListTable(mContext);
        attachViews(view);
        loadNewsListAPI();
        loadDatatoList();
        return view;
    }

    private void loadNewsListAPI() {

    }

    private void attachViews(View view) {
        int position = FragmentPagerItem.getPosition(getArguments());
        bottomLayout = (RelativeLayout) view.findViewById(R.id.loadItemsLayout_recyclerView);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.news_feed_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void loadDatatoList() {

        newsFeedList = newsListTable.getAllNewsRecords();
        Log.v("db ","getNewsFeedList "+newsFeedList.toString());
        adapter = new ImageAdapter(getActivity(), newsFeedList, mRecyclerView, "");
        mRecyclerView.setAdapter(adapter);
    }
}
