package job.com.news;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.sql.SQLException;

import job.com.news.db.DBHelper;
import job.com.news.sharedpref.SessionManager;

public class SplashScreen extends AppCompatActivity {
    private SessionManager session;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        session = new SessionManager(getApplicationContext());
        // DBHelper.getInstance(getApplicationContext());
        db = new DBHelper(getApplicationContext());
        try {
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.close();
        loadSplashScreen();

    }

    private void loadSplashScreen() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (session.isLoggedIn()) {
                    Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.v("SplashAct ", "First Time");
                    Intent intent = new Intent(SplashScreen.this, LanguageSelection.class);
                    intent.putExtra("from", "splash");
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);
    }
}
