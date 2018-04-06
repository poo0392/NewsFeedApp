package job.com.news;

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

import java.util.ArrayList;
import java.util.List;

import job.com.news.adapter.ImageAdapter;
import job.com.news.db.NewsListTable;
import job.com.news.models.NewsFeedList;

/**
 * Created by Zafar.Hussain on 04/04/2018.
 */

public class SubCategoryNewsFragment extends Fragment {
    public static final String TAG = "SubCategoryNewsFragment";
    Context mContext;
    public RecyclerView mRecyclerView;
    private ImageAdapter adapter;
    LinearLayoutManager layoutManager;
    NewsListTable newsListTable;
    int pos, categoryId;
    ArrayList<String> subCatList = new ArrayList<>();
    ArrayList<String> subCatIdList = new ArrayList<>();
    List<NewsFeedList> newsFeedList = new ArrayList<>();
    String category, subCategory;
    private View rootView;
    private boolean fragmentResume = false;
    private boolean fragmentVisible = false;
    private boolean fragmentOnCreated = false;

    private boolean isVisible;
    private boolean isStarted;

    public SubCategoryNewsFragment() {

    }

    public static Fragment newInstance(int position, ArrayList<String> subCategory, int categoryId, ArrayList<String> subCatList) {
        SubCategoryNewsFragment fragment = new SubCategoryNewsFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putStringArrayList("subcategories", subCategory);
        args.putInt("categoryId", categoryId);
        args.putStringArrayList("subcatIdList", subCatList);
        fragment.setArguments(args);
        return fragment;
    }

   /* @Override
    public void onStart() {
        super.onStart();
        isStarted = true;
        if (isVisible)
           readBundle(getArguments()); //your request method
    }
    @Override
    public void onStop() {
        super.onStop();
        isStarted = false;
    }*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(TAG," onCreateView "+" called");
        if(rootView==null){
            rootView = inflater.inflate(R.layout.fragment_news_feed_sub, container, false);
            mContext = getActivity();
            // setUserVisibleHint(false);
            findViews(rootView);
        }else{
            Log.v(TAG," rootview "+" null");
        }


        return rootView;
    }

   /* @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.v(TAG, " setUserVisibleHint ");
        isVisible = isVisibleToUser;
        if (isVisible && isStarted)
            readBundle(getArguments());//your request method


        //  Log.v(TAG, ":: isVisibleToUser " + isVisibleToUser);
       *//* if (isVisibleToUser && isResumed()) {   // only at fragment screen is resumed
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
        }*//*
    }*/

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
         super.onViewCreated(view, savedInstanceState);
        Log.v(TAG," ViewCr "+" called");

        newsListTable = new NewsListTable(getActivity());
        findViews(view);


        readBundle(getArguments());
    }

    private void findViews(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.news_feed_sub_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);


    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG," onResume "+" called");
        readBundle(getArguments());
    }

    @SuppressLint("LongLogTag")
    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            Log.v(TAG + " getBundleData ", "called");

            pos = getArguments().getInt("position");
            Log.v(TAG + " getBundleData ", "Fragpos " + pos);

            subCatList = getArguments().getStringArrayList("subcategories");
            Log.v("getBundleData ", " subCatList" + subCatList);

            subCatIdList = getArguments().getStringArrayList("subcatIdList");
            Log.v("getBundleData ", " subCatIdList" + subCatIdList);

            categoryId = getArguments().getInt("categoryId");
            Log.v(TAG + " getBundleData ", " category" + categoryId);

            subCategory = subCatIdList.get(pos);
            Log.v(TAG + " getBundleData ", " subCategory" + subCategory);

            setData(categoryId, subCategory);
        } else {
            System.out.println(TAG + "11 getBundleData  " + "null");
        }
    }

    private void setData(int cat, String subCat) {

        if (newsFeedList != null || !newsFeedList.isEmpty()) {
            newsFeedList.clear();
        }
        //newsFeedList.addAll(newsListTable.getNewsRecordsByCategoryAndSubCat(cat, subCat));
        newsFeedList=newsListTable.getNewsRecordsByCategoryAndSubCat(cat, subCat);
        Log.v("setData ", "newsFeedList " + newsFeedList.size());

        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.news_feed_sub_recycler_view);
       // layoutManager = ;
       // layoutManager.setOrientation();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        adapter = new ImageAdapter(getActivity(), newsFeedList, mRecyclerView, "newsfeedSubfragment", 0);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
