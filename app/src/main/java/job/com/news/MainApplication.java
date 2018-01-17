package job.com.news;



import android.app.Application;
import android.content.Context;
import job.com.news.helper.LocaleHelper;


public class MainApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }
}