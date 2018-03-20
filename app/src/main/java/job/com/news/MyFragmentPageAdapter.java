package job.com.news;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by POOJA on 3/9/2018.
 */

public class MyFragmentPageAdapter extends FragmentStatePagerAdapter {

    public static int pos = 0;

    private List<Fragment> myFragments;
    private ArrayList<String> categories;
    private Context context;

    public MyFragmentPageAdapter(Context c, FragmentManager fragmentManager, List<Fragment> myFrags, ArrayList<String> cats) {
        super(fragmentManager);
        myFragments = myFrags;
        this.categories = cats;
        this.context = c;
    }

    public MyFragmentPageAdapter(Context mContext, FragmentManager fragmentManager, ArrayList<String> categories) {
        super(fragmentManager);
        this.categories = categories;
        this.context = mContext;
    }

    @Override
    public Fragment getItem(int position) {

        //return myFragments.get(position);
        return AdminRequestsListFragment.newInstance(position);

    }

    @Override
    public int getCount() {

        return categories.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        setPos(position);
        return categories.get(position);
    }

    public static int getPos() {
        return pos;
    }

    public void add(Class<Fragment> c, String title, Bundle b) {
        myFragments.add(Fragment.instantiate(context,c.getName(),b));
        categories.add(title);
    }

    public static void setPos(int pos) {
        MyFragmentPageAdapter.pos = pos;
    }
}
