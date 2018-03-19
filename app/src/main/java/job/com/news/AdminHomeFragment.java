package job.com.news;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import job.com.news.models.NewsFeedList;

/**
 * Created by POOJA on 3/9/2018.
 */

public class AdminHomeFragment extends Fragment {
    Toolbar toolbar;
    Context mContext;
    ViewPager mViewPager;
    TabLayout mTabLayout;
    MyFragmentPageAdapter mPageAdapter;
    ArrayList<String> categories;
    ArrayList<NewsFeedList> newsStatusList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_admin, container, false);
        setAdminHomeToolbar();

        mContext = getActivity();

        initializeComponents(view);

        return view;
    }

    private void setAdminHomeToolbar() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        //toolbar.setTitle(getResources().getString(R.string.home_toolbar_title));
        toolbar.setTitle("Admin News Feed");
        //setHasOptionsMenu(false);
    }

    private void initializeComponents(View view) {
        categories=new ArrayList<>();
        categories.add("Pending News");
        categories.add("Approved News");
        categories.add("Rejected News");


        List<Fragment> fragments = buildFragments();
        mViewPager = (ViewPager) view.findViewById(R.id.admin_home_viewpager);

        mPageAdapter = new MyFragmentPageAdapter(mContext,getFragmentManager(), fragments, categories);
        setupViewPager(mViewPager,fragments);

        mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);

/*//Add a new Fragment to the list with bundle
        Bundle b = new Bundle();
        b.putInt("position", i);
        String title = "asd";
        mPageAdapter.add(MyFragment.class, title, b);
        mPageAdapter.notifyDataSetChanged();*/
    }
    private void setupViewPager(ViewPager mViewPager, List<Fragment> fragments) {

        mPageAdapter = new MyFragmentPageAdapter(mContext,getFragmentManager(), fragments, categories);
        mViewPager.setAdapter(mPageAdapter);
        mPageAdapter.notifyDataSetChanged();
    }
    private List<Fragment> buildFragments() {
        List<android.support.v4.app.Fragment> fragments = new ArrayList<Fragment>();
        for(int i = 0; i<categories.size(); i++) {
            Bundle b = new Bundle();
            b.putInt("position", i);
          // b.putParcelableArrayList("News",);

            fragments.add(Fragment.instantiate(mContext,AdminRequestsListFragment.class.getName(),b));//AdminNewsListFragment
        }

        return fragments;
    }



}
