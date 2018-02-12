package job.com.news.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import job.com.news.models.NewsFeedList;
import job.com.news.register.RegisterMember;

import static job.com.news.db.MemberTable.MEMBER_ID;

/**
 * Created by POOJA on 2/10/2018.
 */

public class DBHelper {
    private static DBHelper mInstance = null;
    private DatabaseHelper mDbHelper;
    public static SQLiteDatabase mDb;
    private Context context;
    private static final String DATABASE_NAME = "NewsApp";
    private static final int DATABASE_VERSION = 1;


    String query_member = " CREATE TABLE IF NOT EXISTS " + MemberTable.MEMBER_TABLE_NAME +
            "(" + MemberTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + MEMBER_ID + " INTEGER," + MemberTable.MEMBER_TOKEN + " TEXT,"
            + MemberTable.FIRST_NAME + " TEXT," + MemberTable.LAST_NAME + " TEXT,"
            + MemberTable.EMAIL_ID + " TEXT," + MemberTable.MOBILE + " TEXT," + MemberTable.STATUS + " TEXT)";

    String query_news_list = " CREATE TABLE IF NOT EXISTS " + NewsListTable.NEWS_LIST_TABLE_NAME +
            "(" + NewsListTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            NewsListTable.NEWS_ID + " INTEGER," + NewsListTable.NEWS_UUID + " TEXT," + NewsListTable.CATEGORY + " TEXT," +
            NewsListTable.COUNTRY + " TEXT," + NewsListTable.STATE + " TEXT," + NewsListTable.CITY + " TEXT," +
            NewsListTable.NEWS_TITLE + " TEXT," + NewsListTable.NEWS_DESCRIPTION + " TEXT," + NewsListTable.NEWS_PIC + " TEXT," +
            NewsListTable.LIKE_COUNT + " TEXT," + NewsListTable.MEMBER_ID + " INTEGER," + NewsListTable.CREATED_AT + " TEXT,"
            + NewsListTable.IS_UPDATED + " TEXT," + NewsListTable.STATUS + " TEXT)";

    /*public static synchronized DBHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DBHelper(context);
        }
        return mInstance;
    }
*/
    private class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(query_member);
            db.execSQL(query_news_list);
            Toast.makeText(context, "Table Created in db", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + query_member);
            db.execSQL("DROP TABLE IF EXISTS " + query_news_list);
            onCreate(db);
        }
    }

    public void Reset() {
        mDbHelper.onUpgrade(mDb, 1, 1);
    }

    public DBHelper(Context ctx) {
        context = ctx;
        mDbHelper = new DatabaseHelper(context);
    }

    public DBHelper open() throws SQLException {
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public void insertMembers(RegisterMember model) {
        Log.v("DB ", "MEMBER_ID " + model.getMemberId());
        ContentValues cv = new ContentValues();
        cv.put(MemberTable.MEMBER_ID, model.getMemberId());
        cv.put(MemberTable.MEMBER_TOKEN, model.getMemberToken());
        cv.put(MemberTable.FIRST_NAME, model.getFirstName());
        cv.put(MemberTable.LAST_NAME, model.getLastName());
        cv.put(MemberTable.EMAIL_ID, model.getEmailId());
        cv.put(MemberTable.MOBILE, model.getMobile());
        cv.put(MemberTable.STATUS, "A");

        mDb.insert(MemberTable.MEMBER_TABLE_NAME, null, cv);
        Toast.makeText(context, "Data inserted in db", Toast.LENGTH_SHORT).show();
    }

    public void insertNewsList(NewsFeedList model) {
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
        cv.put(NewsListTable.NEWS_PIC, model.getNews_pic());
        cv.put(NewsListTable.LIKE_COUNT, model.getLike_count());
        cv.put(NewsListTable.MEMBER_ID, model.getMember_id());
        cv.put(NewsListTable.CREATED_AT, model.getCreated_at());
        cv.put(NewsListTable.IS_UPDATED, "N");
        cv.put(NewsListTable.STATUS, "A");
        mDb.insert(NewsListTable.NEWS_LIST_TABLE_NAME, null, cv);
        Toast.makeText(context, "Data inserted in NewsList Table", Toast.LENGTH_SHORT).show();
    }

    public boolean checkUser(int member_id) {
        String query = "select * from Member where " + MemberTable.MEMBER_ID + " = " + member_id;

        Cursor cursor = mDb.rawQuery(query, null);
        int cursorCount = cursor.getCount();
        //   if (cursor.getCount() > 0) {
        cursor.close();
        // mDb.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    public boolean checkNewsPresent(int news_id) {
        String query = "select * from NewsList where " + NewsListTable.NEWS_ID + " = " + news_id;

        Cursor cursor = mDb.rawQuery(query, null);
        int cursorCount = cursor.getCount();
        //   if (cursor.getCount() > 0) {
        cursor.close();
        // mDb.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    public List<RegisterMember> getMember() {
        ArrayList<RegisterMember> listAll = new ArrayList<RegisterMember>();
        RegisterMember model;
        String query1 = "SELECT * FROM " + MemberTable.MEMBER_TABLE_NAME;
        Cursor cursor = mDb.rawQuery(query1, null);
        while (cursor != null && cursor.moveToNext()) {
            model = new RegisterMember();
            model.setMemberId(cursor.getColumnIndex(MemberTable.MEMBER_ID));
            model.setMemberToken(cursor.getString(cursor.getColumnIndex(MemberTable.MEMBER_TOKEN)));
            model.setFirstName(cursor.getString(cursor.getColumnIndex(MemberTable.FIRST_NAME)));
            model.setLastName(cursor.getString(cursor.getColumnIndex(MemberTable.LAST_NAME)));
            model.setEmailId(cursor.getString(cursor.getColumnIndex(MemberTable.EMAIL_ID)));
            model.setMobile(cursor.getString(cursor.getColumnIndex(MemberTable.MOBILE)));
            listAll.add(model);
        }
        Log.v("DbHelper ", " list from db " + listAll.toString());
        cursor.close();
        mDb.close();
        return listAll;
    }

    public List<NewsFeedList> getAllNewsRecords() {
        // Cursor cursor = mDb.query(NewsListTable.NEWS_LIST_TABLE_NAME, null, null, null, null, null, null);
        ArrayList<NewsFeedList> listAll = new ArrayList<NewsFeedList>();
        NewsFeedList model;
        String query1 = "SELECT * FROM " + NewsListTable.NEWS_LIST_TABLE_NAME;
        Cursor cursor = mDb.rawQuery(query1, null);

        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                model = new NewsFeedList();
                model.setId(cursor.getColumnIndex(NewsListTable.NEWS_ID));
                model.setNews_uuid(cursor.getString(cursor.getColumnIndex(NewsListTable.NEWS_ID)));
                model.setCategory(cursor.getString(cursor.getColumnIndex(NewsListTable.CATEGORY)));
                model.setCountry(cursor.getString(cursor.getColumnIndex(NewsListTable.COUNTRY)));
                model.setState(cursor.getString(cursor.getColumnIndex(NewsListTable.STATE)));
                model.setCity(cursor.getString(cursor.getColumnIndex(NewsListTable.CITY)));
                model.setNews_title(cursor.getString(cursor.getColumnIndex(NewsListTable.NEWS_TITLE)));
                model.setNews_description(cursor.getString(cursor.getColumnIndex(NewsListTable.NEWS_DESCRIPTION)));
                model.setNews_pic(cursor.getString(cursor.getColumnIndex(NewsListTable.NEWS_PIC)));
                model.setLike_count(cursor.getString(cursor.getColumnIndex(NewsListTable.LIKE_COUNT)));
                model.setMember_id(cursor.getString(cursor.getColumnIndex(NewsListTable.MEMBER_ID)));
                model.setCreated_at(cursor.getString(cursor.getColumnIndex(NewsListTable.CREATED_AT)));
               // model.setMember(c.getString(c.getColumnIndex(MemberTable.FIRST_NAME)));
                listAll.add(model);

            }
        }
        Log.v("DbHelper ", " list from db " + listAll.toString());
        cursor.close();
        mDb.close();
        return listAll;
    }

}
