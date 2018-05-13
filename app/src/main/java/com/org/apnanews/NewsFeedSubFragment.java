package com.org.apnanews;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.org.apnanews.adapter.ImageAdapter;
import com.org.apnanews.db.NewsListTable;
import com.org.apnanews.interfaces.IFragmentToActivity;
import com.org.apnanews.models.NewsFeedList;

/**
 * Created by Pooja.Patil on 03/04/2018.
 */

public class NewsFeedSubFragment extends Fragment implements IFragmentToActivity{
    public static final String TAG = "NewsFeedSubFragment";
    Context mContext;
    ArrayList<String> subCatList = new ArrayList<>();
    ArrayList<String> subCatIdList = new ArrayList<>();
    List<NewsFeedList> newsFeedList = new ArrayList<>();
    List<NewsFeedList> newsFeedListNew;
    String category, subCategory;
    public RecyclerView mRecyclerView;
    private ImageAdapter adapter;
    LinearLayoutManager layoutManager;
    RelativeLayout bottomLayout;
    TextView tvEmptyView;
    LinearLayout ll_news_feed, ll_frag_nestedtabs;
    ProgressBar itemProgressBar;
    NewsListTable newsListTable;
    private boolean fragmentResume = false;
    private boolean fragmentVisible = false;
    private boolean fragmentOnCreated = false;
    int pos, categoryId;
    private boolean isDataLoading = true;
    String status = "1";
    View view;

    public static Fragment newInstance(int position, ArrayList<String> subCategory, int categoryId, ArrayList<String> subCatList) {
        NewsFeedSubFragment fragment = new NewsFeedSubFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putStringArrayList("subcategories", subCategory);
        args.putInt("categoryId", categoryId);
        args.putStringArrayList("subcatIdList", subCatList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        // findViews(view);
        readBundle(getArguments());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news_feed_sub, container, false);
        //Log.v(TAG + " " + pos + " ", "onCreateView ");
        //Log.v("onCreateV ", " isDataLoading " + isDataLoading);
        mContext = getActivity();
        attachViews(view);
        return view;
    }

    private void attachViews(View view) {

        newsListTable = new NewsListTable(mContext);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.news_feed_sub_recycler_view);
        layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
    }


    private void setData(int cat, String subCat) {
        // Log.v("setData ", " cat " + cat);
        //  Log.v("setData ", " subCat " + subCat);

        if (newsFeedList != null || !newsFeedList.isEmpty()) {
            newsFeedList.clear();
        }
      //  newsFeedList.addAll(newsListTable.getNewsRecordsByCategoryAndSubCat(cat, subCat));
        //Log.v("setData ", "newsFeedList " + newsFeedList.size());

       // isDataLoading = false;
        //Log.v("setData ", " isDataLoading " + isDataLoading);

        mRecyclerView.setAdapter(new ImageAdapter(getActivity(), newsFeedList, mRecyclerView, "newsfeedSubfragment", 0));
      //  adapter.notifyDataSetChanged();

    }

    @Override
    public void Fragmentbecamevisible() {
        readBundle(getArguments());
    }

    @SuppressLint("LongLogTag")
    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            //Log.v(TAG + " getBundleData ", "called");

            pos = getArguments().getInt("position");
            //Log.v(TAG + " getBundleData ", "Fragpos " + pos);

            subCatList = getArguments().getStringArrayList("subcategories");
            //Log.v("getBundleData ", " subCatList" + subCatList);

            subCatIdList = getArguments().getStringArrayList("subcatIdList");
            //Log.v("getBundleData ", " subCatIdList" + subCatIdList);

            categoryId = getArguments().getInt("categoryId");
            //Log.v(TAG + " getBundleData ", " category" + categoryId);

            subCategory = subCatIdList.get(pos);
           //Log.v(TAG + " getBundleData ", " subCategory" + subCategory);

            setData(categoryId, subCategory);
        } else {
           // System.out.println(TAG + "11 getBundleData  " + "null");
        }
    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //Log.v(TAG, " setUserVisibleHint ");

        //  Log.v(TAG, ":: isVisibleToUser " + isVisibleToUser);
        if (isVisibleToUser && isResumed()) {   // only at fragment screen is resumed
            fragmentResume = true;
            fragmentVisible = false;
            fragmentOnCreated = true;
            readBundle(getArguments());
        } else if (isVisibleToUser) {        // only at fragment onCreated
            fragmentResume = false;
            fragmentVisible = true;
            fragmentOnCreated = true;

        } else if (!isVisibleToUser && fragmentOnCreated) {// only when you go out of fragment screen
            fragmentVisible = false;
            fragmentResume = false;
        }
    }

    //@SuppressLint("LongLogTag")
   /* private void getBundleData() {

    }*/





    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        attachViews(view);
        readBundle(getArguments());
    }
}
