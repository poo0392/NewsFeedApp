package job.com.news;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.sql.SQLException;

import job.com.news.db.DBHelper;
import job.com.news.sharedpref.SessionManager;

public class SplashScreen extends AppCompatActivity {
    private SessionManager session;
    DBHelper db;
    String DB_PATH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        session = new SessionManager(getApplicationContext());
        DBHelper.getInstance(getApplicationContext());
        try {
           /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                DB_PATH = getApplicationContext().getFilesDir().getAbsolutePath().replace("files", "databases") + File.separator;
            }
            else {
                DB_PATH = getApplicationContext().getFilesDir().getPath() + getApplicationContext().getPackageName() + "/databases/";
            }*/

          writeToSD();
        }  catch (IOException e) {
            e.printStackTrace();
        }
        loadSplashScreen();

    }
    private void writeToSD() throws IOException {
        File sd = Environment.getExternalStorageDirectory();

        if (sd.canWrite()) {
            String currentDBPath = DBHelper.DATABASE_NAME;
            String backupDBPath = "backupname.db";
            File currentDB = new File(DB_PATH, currentDBPath);
            File backupDB = new File(sd, backupDBPath);

            if (currentDB.exists()) {
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            }
        }
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
