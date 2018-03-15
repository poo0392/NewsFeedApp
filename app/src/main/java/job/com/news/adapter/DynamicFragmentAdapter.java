package job.com.news.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import job.com.news.NewsFeedFragment;
import job.com.news.models.NewsFeedList;

/**
 * Created by Zafar.Hussain on 14/03/2018.
 */


public class DynamicFragmentAdapter  extends FragmentStatePagerAdapter {
    List<NewsFeedList> newsFeedList=new ArrayList<>();
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private List<String> catListNew;
    List<NewsFeedList> newsFeedListNew;
    Fragment fragment = null;
    Bundle b;

    public DynamicFragmentAdapter(FragmentManager fm, List<String> catListNew, List<NewsFeedList> newsFeedListNew, Bundle b) {
        super(fm);
        this.catListNew= catListNew;
        this.newsFeedListNew= newsFeedListNew;
        this. b= b;

    }

    @Override
    public Fragment getItem(int position) {
       // for (int i = 0; i < catListNew.size(); i++) {
          //  return mFragmentList.get(position);
         ////   fragment = NewsFeedFragment.newInstance();
         //   break;
       // }
       // return mFragmentList.get(position);
        return NewsFeedFragment.newInstance(position,b);
    }

    @Override
    public int getCount() {
        return catListNew.size();
    }

    public void addFragment(Fragment fragment,String title){
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return catListNew.get(position);
    }
    /*@Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }*/

}
