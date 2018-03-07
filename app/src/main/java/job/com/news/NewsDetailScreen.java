package job.com.news;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import job.com.news.db.MemberTable;
import job.com.news.db.NewsImagesTable;
import job.com.news.db.NewsListTable;
import job.com.news.models.NewsFeedList;
import job.com.news.models.NewsImages;
import job.com.news.register.RegisterMember;

public class NewsDetailScreen extends AppCompatActivity {
    //changes added on 08/03
    CollapsingToolbarLayout collapsingToolbar;
    Toolbar mToolbar;
    List<NewsFeedList> mNewsFeedList;
    List<RegisterMember> memberList;
    List<NewsImages> imagesList;
    NewsFeedApplication newsFeedApplication;
    TextView txtTitle, txtNewsdesc, date,txt_post_time,txt_post_person_name;
    ImageView ivBackground;
    int clickedPosition;
    NewsListTable newsListTable;
    MemberTable memberTable;
    NewsImagesTable newsImagesTable;
    Bitmap decodedByte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail_screen_new);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // db = new DBHelper(this);

        newsFeedApplication = NewsFeedApplication.getApp();
        mNewsFeedList = new ArrayList<>();
        imagesList = new ArrayList<>();
        newsListTable=new NewsListTable(this);
        memberTable=new MemberTable(this);
        newsImagesTable = new NewsImagesTable(this);
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
        memberList = memberTable.getMemberListByMemberId(Integer.parseInt(mNewsFeedList.get(clickedPosition).getMember_id()));


        //  String member_name=newsFeedList.get(position).getMember().getFirstName();
        String member_name = memberList.get(0).getFirstName();

        txtTitle.setText(mNewsFeedList.get(clickedPosition).getNews_title());
        txtNewsdesc.setText(mNewsFeedList.get(clickedPosition).getNews_description());
        txt_post_person_name.setText(member_name);
        collapsingToolbar.setTitle(mNewsFeedList.get(clickedPosition).getCategory());
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        imagesList=newsImagesTable.getNewsImagesList(mNewsFeedList.get(clickedPosition).getId());

        String pic = imagesList.get(7).getNews_pic().toString();
//        pic = pic.substring(0, pic.length() - 4);
       // Log.v("", "pic " + pic);
        byte[] decodedString = Base64.decode(pic, Base64.DEFAULT);
        decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        Log.v("", "bitmap get image:=>" + decodedByte);
        try {
            if (decodedByte == null || decodedByte.equals("")) {
                ivBackground.setImageResource(R.drawable.default_no_image);
            } else {
                ivBackground.setImageBitmap(decodedByte);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String dateTime = mNewsFeedList.get(clickedPosition).getCreated_at();
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


        /*if (clickedPosition == 0) {
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
        }*/
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
        txt_post_time = (TextView) findViewById(R.id.txt_post_time);
        txt_post_person_name = (TextView) findViewById(R.id.txt_post_person_name);
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
