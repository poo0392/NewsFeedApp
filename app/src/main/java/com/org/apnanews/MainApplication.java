package com.org.apnanews;



import android.app.Application;
import android.content.Context;
import com.org.apnanews.helper.LocaleHelper;


public class MainApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }
}