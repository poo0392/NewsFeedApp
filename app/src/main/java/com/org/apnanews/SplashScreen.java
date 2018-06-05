package com.org.apnanews;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.org.apnanews.db.DBHelper;
import com.org.apnanews.sharedpref.SessionManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class SplashScreen extends AppCompatActivity {
    private SessionManager session;
    String DB_PATH;
    DBHelper db;
    //changes reflect to be 05/03
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        session = new SessionManager(getApplicationContext());
        DBHelper.getInstance(getApplicationContext());

        try {
           /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                DB_PATH = getApplicationContext().getFilesDir().getAbsolutePath().replace("files", "databases") + File.separator;
            } else {
                DB_PATH = getApplicationContext().getFilesDir().getPath() + getApplicationContext().getPackageName() + "/databases/";
            }*/

            // writeToSD();
            exportDB();
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadSplashScreen();

    }

    private void exportDB() throws IOException {
        try {
            File sd = Environment.getExternalStorageDirectory();

            if (sd.canWrite()) {
                String currentDBPath = DBHelper.DATABASE_NAME;
                String backupDBPath = "backupname.db";
                File currentDB = new File(currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeToSD() throws IOException {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        if (sd.canWrite()) {
            // String currentDBPath = "\\data\\"+ "job.com.news" +"\\databases\\"+"NewsApp.db";
            String currentDBPath = DBHelper.DATABASE_NAME;

            String backupDBPath = "backupdbname.db";
            File currentDB = new File(DB_PATH, currentDBPath);
            File backupDB = new File(sd, backupDBPath);

            if (currentDB.exists()) {
                // if (Environment.MEDIA_MOUNTED.equals(sd)) {

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(getApplicationContext(), backupDB.toString(), Toast.LENGTH_LONG).show();
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
                  //  Intent intent = new Intent(SplashScreen.this, HomeScreenActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                   // Log.v("SplashAct ", "First Time");
                    Intent intent = new Intent(SplashScreen.this, LanguageSelection.class);
                    intent.putExtra("from", "splash");
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);
    }
}
