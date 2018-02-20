package job.com.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import job.com.news.db.DBHelper;
import job.com.news.db.NewsListTable;
import job.com.news.models.NewsFeedList;

public class NewsDetailScreen extends AppCompatActivity {
    //changes added on 09/02
    CollapsingToolbarLayout collapsingToolbar;
    Toolbar mToolbar;
    List<NewsFeedList> mNewsFeedList;
    NewsFeedApplication newsFeedApplication;
    TextView txtTitle, txtNewsdesc, date;
    ImageView ivBackground;
    int clickedPosition;
    DBHelper db;
    NewsListTable newsListTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail_screen_new);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // db = new DBHelper(this);

        newsFeedApplication = NewsFeedApplication.getApp();
        mNewsFeedList = new ArrayList<>();
        newsListTable=new NewsListTable(this);
        setAppToolbar();
        getIntentData();
        initializeCompo();
        setData();
        setListeners();

        // ArrayList<String> arrayList = newsFeedApplication.hashMap.get(position + "");


    }

    private void setData() {
        mNewsFeedList = newsListTable.getAllNewsRecords();
        Log.v("db ", "getNewsFeedList " + mNewsFeedList.toString());

        txtTitle.setText(mNewsFeedList.get(clickedPosition).getNews_title());
        txtNewsdesc.setText(mNewsFeedList.get(clickedPosition).getNews_description());
        collapsingToolbar.setTitle(mNewsFeedList.get(clickedPosition).getCategory());
        if (clickedPosition == 0) {
            //lebaon
            ivBackground.setBackgroundResource(R.drawable.lebanon);
        } else if (clickedPosition == 1) {
            //india us
            ivBackground.setBackgroundResource(R.drawable.india_us);
        } else if (clickedPosition == 2) {
            //yogi
            ivBackground.setBackgroundResource(R.drawable.yogi_adinath);
        } else if (clickedPosition == 3) {
            //india pak
            ivBackground.setBackgroundResource(R.drawable.india_pak_border);
        } else if (clickedPosition == 4) {
            //kuldeep
            ivBackground.setBackgroundResource(R.drawable.kuldeep_yadav);
        }
    }

    private void getIntentData() {
        Intent intent = getIntent();
        clickedPosition = Integer.parseInt(intent.getStringExtra("itemPosition"));
        Log.v("","clickedPosition "+clickedPosition);
    }

    private void setAppToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initializeCompo() {


        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
       // collapsingToolbar.setTitle("Sports");


        txtTitle = (TextView) findViewById(R.id.details_title);
        txtNewsdesc = (TextView) findViewById(R.id.details_desc);
        date = (TextView) findViewById(R.id.txt_post_time);
        ivBackground = (ImageView) findViewById(R.id.details_image);
    }

    private void setListeners() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
