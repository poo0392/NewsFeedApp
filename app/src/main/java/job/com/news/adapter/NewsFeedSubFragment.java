package job.com.news.adapter;


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

import job.com.news.R;
import job.com.news.db.NewsListTable;
import job.com.news.models.NewsFeedList;
import job.com.news.service.ConnectivityChangeReciever;

/**
 * Created by Zafar.Hussain on 03/04/2018.
 */

public class NewsFeedSubFragment extends Fragment {
    public static final String TAG = "NewsFeedSubFragment";
    Context mContext;
    ArrayList<String> subCatList = new ArrayList<>();
    ArrayList<String> subCatListEn = new ArrayList<>();
    List<NewsFeedList>   newsFeedList = new ArrayList<>();
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
    int pos;
    private boolean isDataLoading = true;

    public static Fragment newInstance(int position, ArrayList<String> subCategory, String category, ArrayList<String> subCatList) {
        NewsFeedSubFragment fragment = new NewsFeedSubFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putStringArrayList("subcategories", subCategory);
        args.putString("category", category);
        args.putStringArrayList("subcatEn", subCatList);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      //
        View view = inflater.inflate(R.layout.fragment_news_feed_sub, container, false);

       // ConnectivityChangeReciever.enableReceiver(mContext);
        Log.v(TAG + " ", "onCreateView ");
        return view;
    }

    private void setData(String cat, String subCat) {
        Log.v("setData ", " cat " + cat);
        Log.v("setData ", " subCat " + subCat);

        if (newsFeedList != null || !newsFeedList.isEmpty()) {
            newsFeedList.clear();
        }

        newsFeedList.addAll(newsListTable.getNewsRecordsByCategoryAndSubCat(cat, subCat));
        Log.v("", "newsFeedList " + newsFeedList.size());
       // isDataLoading = false;

        Log.v(TAG, " isDataLoading "+isDataLoading);
        if(!isDataLoading){
            setDataToAdapter(newsFeedList);
        }
    }


    private void setDataToAdapter(List<NewsFeedList> List) {
        Log.v(TAG + " ", "setDataToAdapter ");

        if (!List.isEmpty() || List.size() > 0) {
            Log.v("", "newsFeedList " + List.size());

            adapter = new ImageAdapter(getActivity(), List, mRecyclerView, "newsfeedSubfragment", 0);
            mRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
   /* @SuppressLint("LongLogTag")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG + " onActivityCreated", " called ");
        if(isDataLoading){
            fetchData();
        }else{
            //get saved instance variable listData()
        }
    }*/

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        attachViews(view);
       // setDataToAdapter(newsFeedList);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.v(TAG, " setUserVisibleHint ");

        //  Log.v(TAG, ":: isVisibleToUser " + isVisibleToUser);
        if (isVisibleToUser && isResumed()) {   // only at fragment screen is resumed
            fragmentResume = true;
            fragmentVisible = false;
            fragmentOnCreated = true;
            getBundleData();
        } else if (isVisibleToUser) {        // only at fragment onCreated
            fragmentResume = false;
            fragmentVisible = true;
            fragmentOnCreated = true;

        } else if (!isVisibleToUser && fragmentOnCreated) {// only when you go out of fragment screen
            fragmentVisible = false;
            fragmentResume = false;
        }
    }

    @SuppressLint("LongLogTag")
    private void getBundleData() {
        Log.v(TAG + " ", "getBundleData ");
        pos = getArguments().getInt("position");
        Log.v(TAG + " getBundleData ", "Fragpos " + pos);
        //  Toast.makeText(getActivity(), "Position is: " + pos, Toast.LENGTH_SHORT).show();
        subCatList = getArguments().getStringArrayList("subcategories");
        Log.v("getBundleData ", " subCatList" + subCatList.toString());

        subCatListEn = getArguments().getStringArrayList("subcatEn");
        Log.v("getBundleData ", " subCatListEn" + subCatListEn.toString());

        category = getArguments().getString("category");
        Log.v(TAG + " getBundleData ", " category" + category);

        subCategory = subCatListEn.get(pos).toString();
        Log.v(TAG + " getBundleData ", " category" + category);

     //  isDataLoading=false;

        setData(category, subCategory);
    }

    private void attachViews(View view) {
        mContext = getActivity();
        newsListTable = new NewsListTable(mContext);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.news_feed_sub_recycler_view);
        layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        isDataLoading = false;
    }
}
