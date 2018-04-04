package job.com.news.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import job.com.news.NewsFeedFragment;
import job.com.news.SubCategoryNewsFragment;
import job.com.news.db.NewsListTable;
import job.com.news.models.NewsFeedList;

/**
 * Created by Zafar.Hussain on 14/03/2018.
 */


public class DynamicFragmentAdapter extends FragmentStatePagerAdapter {
    List<NewsFeedList> newsFeedList = new ArrayList<>();
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private ArrayList<String> catListNew;
    ArrayList<String> catListNewEn,subCatList;
    List<NewsFeedList> newsFeedListNew;
    Map<String,List<NewsFeedList>> mapItems;
    Fragment fragment = null;
    NewsListTable newsListTable;
    Context mContext;
    Bundle b;
    String from;
    int category;

    public DynamicFragmentAdapter(FragmentManager fm, ArrayList<String> catListNew, ArrayList<String> catListNewEn, String from) {
        super(fm);
        this.catListNew = catListNew;
        this.catListNewEn = catListNewEn;
        this.from = from;
    }

    public DynamicFragmentAdapter(FragmentManager fm, ArrayList<String> catListNew, ArrayList<String> subCatList, int category, String from) {
        super(fm);
        this.catListNew = catListNew;
        this.category = category;
        this.from = from;
        this.subCatList = subCatList;
    }

    @Override
    public Fragment getItem(int position) {
       //catListNew == language
        //catListNewEn == category list in english
        //category == category value from list
        //subCatList = subcategory list
        if (from.equals("sub_cat")) {
         //   return NewsFeedSubFragment.newInstance(position, catListNew,category,subCatList);
            return SubCategoryNewsFragment.newInstance(position, catListNew,category,subCatList);
        } else
            return NewsFeedFragment.newInstance(position, catListNewEn);

    }

    @Override
    public int getCount() {
        return catListNew.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return catListNew.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
