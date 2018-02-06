package job.com.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsDetailScreen extends AppCompatActivity {
    CollapsingToolbarLayout collapsingToolbar;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail_screen_new);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setAppToolbar();
        initializeCompo();
        setListeners();

        NewsFeedApplication newsFeedApplication = NewsFeedApplication.getApp();

        Intent intent = getIntent();
        int position = Integer.parseInt(intent.getStringExtra("itemPosition"));

        ArrayList<String> arrayList = newsFeedApplication.hashMap.get(position + "");

        TextView summary = (TextView) findViewById(R.id.details_summary);
        summary.setText(arrayList.get(0));

        TextView news = (TextView) findViewById(R.id.details_news);
        news.setText(arrayList.get(1));

        TextView date = (TextView) findViewById(R.id.txt_post_time);
        date.setText(arrayList.get(2));

        ImageView imageView = (ImageView) findViewById(R.id.details_image);

        if (position == 0) {
            //lebaon
            imageView.setBackgroundResource(R.drawable.lebanon);
        } else if (position == 1) {
            //india us
            imageView.setBackgroundResource(R.drawable.india_us);
        } else if (position == 2) {
            //yogi
            imageView.setBackgroundResource(R.drawable.yogi_adinath);
        } else if (position == 3) {
            //india pak
            imageView.setBackgroundResource(R.drawable.india_pak_border);
        } else if (position == 4) {
            //kuldeep
            imageView.setBackgroundResource(R.drawable.kuldeep_yadav);
        }

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
        collapsingToolbar.setTitle("Sports");
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
