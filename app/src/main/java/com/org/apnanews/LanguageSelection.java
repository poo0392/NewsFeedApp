package com.org.apnanews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.org.apnanews.sharedpref.SessionManager;

import java.util.Locale;

public class LanguageSelection extends AppCompatActivity {

    private Spinner mSpinItem;
    private Context mContext;
    TextView mNextView;
    ArrayAdapter<String> adapter;
    String lang_arr[];
    SessionManager langSelection;
    boolean isFirstTime;
    SharedPreferences reader;
    String fromActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_selection);

        mContext = LanguageSelection.this;
        langSelection = new SessionManager(getApplicationContext());
        //  isFirstTime = MyPreferences.isFirst(mContext);
        reader = getSharedPreferences("NewsFeed", Context.MODE_PRIVATE);
        isFirstTime = reader.getBoolean("is_first", true);
    //    Log.v("LanguageSelection ", "isFirstTime " + isFirstTime);
        fromActivity = getIntent().getExtras().getString("from");

        if (isFirstTime) {
            initializeComponents();
            setListeners();
        } else if (!isFirstTime && (fromActivity.equals("home"))) {
            initializeComponents();
            setListeners();
        } else {
            setLocaleLang();
            moveToNextActivity();

        }

    }

    private void setLocaleLang() {

        String[] lang_arr = getResources().getStringArray(R.array.language_arr);
        String getLang = langSelection.getLanguage();
      //  Log.v("LanguageSelection ", "getLang " + getLang);
        Configuration config = new Configuration();
        Locale locale;
        if (getLang.equalsIgnoreCase(lang_arr[1])) {
            locale = new Locale("hi");
            Locale.setDefault(locale);
            config.setLocale(locale);

        } else if (getLang.equalsIgnoreCase(lang_arr[2])) {
            locale = new Locale("mr");
            Locale.setDefault(locale);
            config.setLocale(locale);
        }
        //getResources().getConfiguration().setTo(config);
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

    }

    private void setListeners() {
        mNextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedItem = mSpinItem.getSelectedItem().toString().trim();

                if (selectedItem.equalsIgnoreCase(lang_arr[0])) {
                    Toast.makeText(LanguageSelection.this, getString(R.string.error_language_selection), Toast.LENGTH_SHORT).show();
                } else {
                    Configuration config = new Configuration();
                    Locale locale;
                    if (selectedItem.equalsIgnoreCase(lang_arr[1])) {
                        locale = new Locale("hi");
                        Locale.setDefault(locale);
                        config.setLocale(locale);
                        // Globals.selectedLanguage = lang_arr[1];
                        langSelection.setLanguage(lang_arr[1]);
                    } else if (selectedItem.equalsIgnoreCase(lang_arr[2])) {
                        locale = new Locale("mr");
                        Locale.setDefault(locale);
                        config.setLocale(locale);
                        //Globals.selectedLanguage = lang_arr[2];
                        langSelection.setLanguage(lang_arr[2]);
                    }
                    //getResources().getConfiguration().setTo(config);
                    getBaseContext().getResources().updateConfiguration(config,
                            getBaseContext().getResources().getDisplayMetrics());


                    moveToNextActivity();
                    if(!fromActivity.equals("home")) {
                        isFirstTime = false;
                        final SharedPreferences.Editor editor = reader.edit();
                        editor.putBoolean("is_first", false);
                        editor.commit();
                    }
                }
            }
        });
    }

    private void initializeComponents() {
        lang_arr = getResources().getStringArray(R.array.language_arr);

        adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, lang_arr);

        RelativeLayout relativeLayout_Spinner = (RelativeLayout) findViewById(R.id.lang_spinner_layout);
        mSpinItem = (Spinner) relativeLayout_Spinner.findViewById(R.id.spinner);
        mSpinItem.setAdapter(adapter);

        mNextView = (TextView) findViewById(R.id.lang_next);
    }

    private void moveToNextActivity() {
        try {
            fromActivity = getIntent().getExtras().getString("from");
            if (fromActivity != null) {
                if (fromActivity.equals("home")) {
                    Intent intent = new Intent(LanguageSelection.this, HomeActivity.class);
                  //  Intent intent = new Intent(LanguageSelection.this, HomeScreenActivity.class);
                    startActivity(intent);
                    finish();
                } else if (fromActivity.equals("splash")) {
                    Intent intent = new Intent(LanguageSelection.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
