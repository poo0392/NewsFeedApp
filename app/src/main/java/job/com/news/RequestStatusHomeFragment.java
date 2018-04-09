package job.com.news;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by POOJA on 3/9/2018.
 */

public class RequestStatusHomeFragment extends Fragment {
    private static final String TAG="RequestStatusHomeFragment";
    Toolbar toolbar;
    Context mContext;
    ViewPager mViewPager;
    TabLayout mTabLayout;
    MyFragmentPageAdapter mPageAdapter;
    ArrayList<String> categories;
    String fromStatus = "";


    @SuppressLint("LongLogTag")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_admin, container, false);
        Log.v(TAG," onCreateView called");
        setAdminHomeToolbar();

        mContext = getActivity();

        initializeComponents(view);
        getIntentData();
        return view;
    }

    private void getIntentData() {
        if (fromStatus.equals("")) {
           // Log.v("", "fromStatus is null");
        } else {
            try {
                if (fromStatus.equals("approved")) {
                    mViewPager.setCurrentItem(1);
                } else if (fromStatus.equals("rejected")) {
                    mViewPager.setCurrentItem(2);
                } else {
                    mViewPager.setCurrentItem(0);
                }
             //   Toast.makeText(mContext, "if not null", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setAdminHomeToolbar() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.request_status_toolbar_name));
        //toolbar.setTitle("Request Status");
        //setHasOptionsMenu(false);
    }

    private void initializeComponents(View view) {
        categories = new ArrayList<>();

        categories.add(mContext.getResources().getString(R.string.pending_news));
        categories.add(mContext.getResources().getString(R.string.approved_news));
        categories.add(mContext.getResources().getString(R.string.rejected_news));


        //  List<Fragment> fragments = buildFragments();
        mViewPager = (ViewPager) view.findViewById(R.id.admin_home_viewpager);

        setViewPager(mViewPager);

        mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);


    }

    private void setViewPager(ViewPager mViewPager) {
        mPageAdapter = new MyFragmentPageAdapter(mContext, getFragmentManager(), categories);
        mViewPager.setAdapter(mPageAdapter);
        mPageAdapter.notifyDataSetChanged();
    }

    private void setupViewPager(ViewPager mViewPager, List<Fragment> fragments) {

        mPageAdapter = new MyFragmentPageAdapter(mContext, getFragmentManager(), fragments, categories);
        mViewPager.setAdapter(mPageAdapter);
        mPageAdapter.notifyDataSetChanged();
    }

    private List<Fragment> buildFragments() {
        List<android.support.v4.app.Fragment> fragments = new ArrayList<Fragment>();
        for (int i = 0; i < categories.size(); i++) {
            Bundle b = new Bundle();
            b.putInt("position", i);
            // b.putParcelableArrayList("News",);

            fragments.add(Fragment.instantiate(mContext, RequestsListFragment.class.getName(), b));//AdminNewsListFragment
        }

        return fragments;
    }


    @SuppressLint("LongLogTag")
    public void sendData(String message) {
        Log.v(TAG, " sendData called");
        Log.v("sendData ", "NewsStatus: " + message);
        try {
            // fromStatus = getIntent().getExtras().getString("addFrom");

            if (message == null || message.equals("")) {
                Toast.makeText(mContext, "if null", Toast.LENGTH_SHORT).show();

            } else {
                fromStatus = message;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
