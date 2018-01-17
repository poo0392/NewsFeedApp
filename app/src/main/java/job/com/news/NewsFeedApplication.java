package job.com.news;

import android.app.Application;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by CrazyInnoTech on 25-03-2017.
 */

public class NewsFeedApplication extends Application {

    private static NewsFeedApplication mApp;
    public ArrayList<Object> mImageDetails;
    public HashMap<String, ArrayList<String>> hashMap;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        mImageDetails = new ArrayList<>();
        hashMap = new HashMap<>();
    }

    public static NewsFeedApplication getApp() {
        return mApp;
    }

}
