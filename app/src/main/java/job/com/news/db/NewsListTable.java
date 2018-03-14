package job.com.news.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import job.com.news.models.NewsFeedList;

/**
 * Created by POOJA on 2/11/2018.
 */

public class NewsListTable {
    //chnages added on 3/9/2018.
    public static final String NEWS_LIST_TABLE_NAME = "NewsList";
    public static final String COLUMN_ID = "column_id";
    public static final String NEWS_ID = "id";
    public static final String NEWS_UUID = "news_uuid";
    public static final String CATEGORY = "category";
    public static final String COUNTRY = "country";
    public static final String STATE = "state";
    public static final String CITY = "city";
    public static final String NEWS_TITLE = "news_title";
    public static final String NEWS_DESCRIPTION = "news_description";
    public static final String NEWS_PIC = "news_pic";
    public static final String LIKE_COUNT = "like_count";
    public static final String MEMBER_ID = "member_id";
    public static final String CREATED_AT = "created_at";
    public static final String IS_UPDATED = "is_updated";
    public static final String STATUS = "status";


    DBHelper dbHelper;
    SQLiteDatabase db;
    Context context;

    public NewsListTable(Context context) {
        this.context = context;
        dbHelper = DBHelper.getInstance(context);
    }

    public void CRUD(ContentValues cv) {
        dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();

        db.insert(NEWS_LIST_TABLE_NAME, null, cv);
    }


    public void insertNewsList(NewsFeedList model) {
        db = dbHelper.getWritableDatabase();
        //SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NewsListTable.NEWS_ID, model.getId());
        cv.put(NewsListTable.NEWS_UUID, model.getNews_uuid());
        cv.put(NewsListTable.CATEGORY, model.getCategory());
        cv.put(NewsListTable.COUNTRY, model.getCountry());
        cv.put(NewsListTable.STATE, model.getState());
        cv.put(NewsListTable.CITY, model.getCity());
        cv.put(NewsListTable.NEWS_TITLE, model.getNews_title());
        cv.put(NewsListTable.NEWS_DESCRIPTION, model.getNews_description());
        //  cv.put(NewsListTable.NEWS_PIC, model.getNews_pic());
        cv.put(NewsListTable.LIKE_COUNT, model.getLike_count());
        cv.put(NewsListTable.MEMBER_ID, model.getMember_id());
        cv.put(NewsListTable.CREATED_AT, model.getCreated_at());
        cv.put(NewsListTable.IS_UPDATED, "Y");
        cv.put(NewsListTable.STATUS, "A");
        db.insert(NewsListTable.NEWS_LIST_TABLE_NAME, null, cv);
        // Toast.makeText(context, "Data inserted in NewsList Table", Toast.LENGTH_SHORT).show();
    }


    public boolean checkNewsPresent(int news_id) {
        db = dbHelper.getWritableDatabase();
        String query = "select * from NewsList where " + NewsListTable.NEWS_ID + " = " + news_id;

        Cursor cursor = db.rawQuery(query, null);
        int cursorCount = cursor.getCount();
        //   if (cursor.getCount() > 0) {
        cursor.close();
        // mDb.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }


    public List<NewsFeedList> getAllNewsRecords() {
        db = dbHelper.getWritableDatabase();
        // Cursor cursor = mDb.query(NewsListTable.NEWS_LIST_TABLE_NAME, null, null, null, null, null, null);
        ArrayList<NewsFeedList> listAll = new ArrayList<NewsFeedList>();
        NewsFeedList model;
        String query1 = "SELECT * FROM " + NewsListTable.NEWS_LIST_TABLE_NAME;
        Cursor cursor = db.rawQuery(query1, null);

        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                model = new NewsFeedList();
               // Log.v("db getAllNewsRecords ", "NewsID " + cursor.getInt(cursor.getColumnIndex(NewsListTable.NEWS_ID)));
                model.setId(cursor.getInt(cursor.getColumnIndex(NewsListTable.NEWS_ID)));
                model.setNews_uuid(cursor.getString(cursor.getColumnIndex(NewsListTable.NEWS_UUID)));
                model.setCategory(cursor.getString(cursor.getColumnIndex(NewsListTable.CATEGORY)));
                model.setCountry(cursor.getString(cursor.getColumnIndex(NewsListTable.COUNTRY)));
                model.setState(cursor.getString(cursor.getColumnIndex(NewsListTable.STATE)));
                model.setCity(cursor.getString(cursor.getColumnIndex(NewsListTable.CITY)));
                model.setNews_title(cursor.getString(cursor.getColumnIndex(NewsListTable.NEWS_TITLE)));
                model.setNews_description(cursor.getString(cursor.getColumnIndex(NewsListTable.NEWS_DESCRIPTION)));
                //  model.setNews_pic(cursor.getString(cursor.getColumnIndex(NewsListTable.NEWS_PIC)));
                model.setLike_count(cursor.getString(cursor.getColumnIndex(NewsListTable.LIKE_COUNT)));
                model.setMember_id(cursor.getString(cursor.getColumnIndex(NewsListTable.MEMBER_ID)));
                model.setCreated_at(cursor.getString(cursor.getColumnIndex(NewsListTable.CREATED_AT)));
                // model.setMember(c.getString(c.getColumnIndex(MemberTable.FIRST_NAME)));
                listAll.add(model);

            }
        }
        //  Log.v("DbHelper ", " list from db " + listAll.toString());
        cursor.close();
        db.close();
        return listAll;
    }

    public List<NewsFeedList> getNewsRecordsByCategory(String category) {
        db = dbHelper.getWritableDatabase();
        // Cursor cursor = mDb.query(NewsListTable.NEWS_LIST_TABLE_NAME, null, null, null, null, null, null);
        ArrayList<NewsFeedList> listAll = new ArrayList<NewsFeedList>();
        NewsFeedList model;
        String query1 = "SELECT * FROM " + NewsListTable.NEWS_LIST_TABLE_NAME + " where " + NewsListTable.CATEGORY + " = '" + category + "'";
        Log.v("", "query1 " + query1);
        Cursor cursor = db.rawQuery(query1, null);
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                model = new NewsFeedList();
                Log.v("db ", "NewsID " + cursor.getInt(cursor.getColumnIndex(NewsListTable.NEWS_ID)));
                model.setId(cursor.getInt(cursor.getColumnIndex(NewsListTable.NEWS_ID)));
                model.setNews_uuid(cursor.getString(cursor.getColumnIndex(NewsListTable.NEWS_UUID)));
                model.setCategory(cursor.getString(cursor.getColumnIndex(NewsListTable.CATEGORY)));
                model.setCountry(cursor.getString(cursor.getColumnIndex(NewsListTable.COUNTRY)));
                model.setState(cursor.getString(cursor.getColumnIndex(NewsListTable.STATE)));
                model.setCity(cursor.getString(cursor.getColumnIndex(NewsListTable.CITY)));
                model.setNews_title(cursor.getString(cursor.getColumnIndex(NewsListTable.NEWS_TITLE)));
                model.setNews_description(cursor.getString(cursor.getColumnIndex(NewsListTable.NEWS_DESCRIPTION)));
                //  model.setNews_pic(cursor.getString(cursor.getColumnIndex(NewsListTable.NEWS_PIC)));
                model.setLike_count(cursor.getString(cursor.getColumnIndex(NewsListTable.LIKE_COUNT)));
                model.setMember_id(cursor.getString(cursor.getColumnIndex(NewsListTable.MEMBER_ID)));
                model.setCreated_at(cursor.getString(cursor.getColumnIndex(NewsListTable.CREATED_AT)));
                // model.setMember(c.getString(c.getColumnIndex(MemberTable.FIRST_NAME)));
                listAll.add(model);
            }
        }
        return listAll;
    }

    public long getLastId() {//SELECT last_insert_rowid();
        db = dbHelper.getWritableDatabase();
        String query1 = "select seq from sqlite_sequence where name = '" + NewsListTable.NEWS_LIST_TABLE_NAME + "'";
        Cursor cursor = db.rawQuery(query1, null);
        long id = 0;
        if (cursor.moveToFirst()) {
            id = cursor.getLong(cursor.getColumnIndex("seq"));
        }
  /*  if(cursor.moveToFirst()){
        id = cursor.getString(cursor.getColumnIndex(NewsListTable.NEWS_ID));
    }*/
        Log.v("getLastId ", "id " + id);
        cursor.close();
        db.close();
        return id;
    }

    public List<String> getCategory() {
        db = dbHelper.getWritableDatabase();
        ArrayList<String> listAll = new ArrayList<String>();

        String query1 = "SELECT category FROM " + NewsListTable.NEWS_LIST_TABLE_NAME;
        Cursor cursor = db.rawQuery(query1, null);

        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                // model = new NewsFeedList();
                cursor.getString(cursor.getColumnIndex(NewsListTable.CATEGORY));
            }
        }
        //  Log.v("DbHelper getCategory", " list from db " + listAll.toString());
        cursor.close();
        db.close();
        return listAll;
    }
}
