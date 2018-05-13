package com.org.apnanews;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.org.apnanews.db.MemberTable;
import com.org.apnanews.db.NewsImagesTable;
import com.org.apnanews.db.NewsListTable;
import com.org.apnanews.models.NewsFeedList;
import com.org.apnanews.models.NewsImages;
import com.org.apnanews.register.RegisterMember;

public class NewsDetailScreen extends AppCompatActivity {
    //changes added on 08/03
    CollapsingToolbarLayout collapsingToolbar;
    Toolbar mToolbar;
    List<NewsFeedList> mNewsFeedList, newsListByNewsId;
    List<RegisterMember> memberList;
    List<NewsImages> imagesList;
    NewsFeedApplication newsFeedApplication;
    TextView txtTitle, txtNewsdesc, date, txt_post_time, txt_post_person_name, txt_city;
    ImageView ivBackground, iv_share;
    int clickedPosition, newsId;
    String category;
    NewsListTable newsListTable;
    MemberTable memberTable;
    NewsImagesTable newsImagesTable;
    String image, load_image;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail_screen_new);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // db = new DBHelper(this);

        newsFeedApplication = NewsFeedApplication.getApp();
        mNewsFeedList = new ArrayList<>();
        newsListByNewsId = new ArrayList<>();
        imagesList = new ArrayList<>();
        newsListTable = new NewsListTable(this);
        memberTable = new MemberTable(this);
        newsImagesTable = new NewsImagesTable(this);
        setAppToolbar();
        getIntentData();
        initializeCompo();
        setData();
        setListeners();

        // ArrayList<String> arrayList = newsFeedApplication.hashMap.get(position + "");


    }


    private void setData() {
        // mNewsFeedList = newsListTable.getAllNewsRecords();
        // mNewsFeedList = newsListTable.getNewsRecordsByCategory(category);

        newsListByNewsId = newsListTable.getRecordById(newsId);
        //Log.v("db ", "getNewsFeedList " + newsListByNewsId.toString());
        //  for(int k=0;k<newsListByNewsId.size();k++)
        memberList = memberTable.getMemberListByMemberId(Integer.parseInt(newsListByNewsId.get(0).getMember_id()));


        //  String member_name=newsFeedList.get(position).getMember().getFirstName();
        String member_name = memberList.get(0).getFirstName();

        txtTitle.setText(newsListByNewsId.get(0).getNews_title());
        txt_city.setText(newsListByNewsId.get(0).getCity());
        txtNewsdesc.setText(newsListByNewsId.get(0).getNews_description());
        txt_post_person_name.setText(member_name);
        if (newsListByNewsId.get(0).getSub_category() != null) {
            collapsingToolbar.setTitle(newsListByNewsId.get(0).getCategory() + " | " + newsListByNewsId.get(0).getSub_category());
        } else {
            collapsingToolbar.setTitle(newsListByNewsId.get(0).getCategory());
        }
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        imagesList = newsImagesTable.getNewsImagesList(newsListByNewsId.get(0).getId());

        if (!imagesList.isEmpty() && imagesList.size() > 0) {
            image = imagesList.get(0).getNews_pic();
            load_image = Constant.IMAGE_URL + "/" + image;
            //Log.v("Adapter ", "load_image" + load_image);
            //   new DownloadImageTask(ivBackground).execute(load_image);

            // Hide progress bar on successful load
            Picasso.with(this).load(load_image)
                    .placeholder(R.drawable.default_no_image)
                    .resize(1000, 1000)
                    .into(ivBackground/*, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError() {

                    }
                }*/);
        } else {
            ivBackground.setImageResource(R.drawable.default_no_image);
        }
       /* Picasso.with(this)
                .load(load_image)
                .placeholder(R.drawable.default_no_image) //this is optional the image to display while the url image is downloading
                // .error(Your Drawable Resource)         //this is also optional if some error has occurred in downloading the image this image would be displayed
                .into(ivBackground);*/

        String dateTime = newsListByNewsId.get(0).getCreated_at();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long time = 0;
        try {
            time = sdf.parse(dateTime).getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        long now = System.currentTimeMillis();
//2018-02-03 14:37:06
        CharSequence ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
        txt_post_time.setText(ago);


    }

    private void getIntentData() {
        Intent intent = getIntent();
        clickedPosition = Integer.parseInt(intent.getStringExtra("itemPosition"));
        newsId = Integer.parseInt(intent.getStringExtra("newsId"));
        category = intent.getStringExtra("category");

        //Log.v("NewsDetailScreen ", "clickedPosition=" + clickedPosition + ", newsId=" + newsId + ", category=" + category);
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

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtTitle = (TextView) findViewById(R.id.details_title);
        txt_city = (TextView) findViewById(R.id.txt_city);
        txtNewsdesc = (TextView) findViewById(R.id.details_desc);
        txt_post_time = (TextView) findViewById(R.id.txt_post_time);
        txt_post_person_name = (TextView) findViewById(R.id.txt_post_person_name);
        ivBackground = (ImageView) findViewById(R.id.details_image);
        iv_share = (ImageView)mToolbar.findViewById(R.id.iv_share);
        iv_share.setVisibility(View.GONE);
    }

    private void setListeners() {
      /*  iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    image = imagesList.get(0).getNews_pic();
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, "Check this news article. Download now Apna News App"+txtTitle.getText().toString());
                    String path = null;
                    path = MediaStore.Images.Media.insertImage(getContentResolver(), image, "title", null);
                    Uri screenshotUri = Uri.parse(path);
                    intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                    intent.setType("image*//*");
                    startActivity(Intent.createChooser(intent, "Share image"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });*/
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
